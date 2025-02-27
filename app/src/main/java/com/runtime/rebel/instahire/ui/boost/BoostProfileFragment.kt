package com.runtime.rebel.instahire.ui.boost

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.runtime.rebel.instahire.App
import com.runtime.rebel.instahire.R
import com.runtime.rebel.instahire.databinding.FragmentBoostBinding
import com.runtime.rebel.instahire.model.Result
import timber.log.Timber
import javax.inject.Inject

class BoostProfileFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: BoostViewModel
    private lateinit var binding: FragmentBoostBinding
    private var navJobUrl: String? = null
    private var selectedPdfUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[BoostViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBoostBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            navJobUrl = it.getString("jobUrl")
        }

        navJobUrl?.let {
            Timber.d("BoostProfileFragment", "Job URL: $navJobUrl")
            // User navigated from dashboard to this fragment. tailor the resume/CV
            binding.etJobUrl.setText(navJobUrl)
        }

        setupRecyclerView()
        observeViewModel()


        binding.cardUpload.setOnClickListener { filePickerLauncher.launch("application/pdf") }
        binding.btnBoostResume.setOnClickListener { boostResume() }
        binding.btnUpload.setOnClickListener {
            viewModel.uploadFile(selectedPdfUri!!)
        }

    }

    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedPdfUri = uri
                binding.tvUploadedFile.text = "Selected: ${uri.lastPathSegment}"
            }
            binding.tvUploadedFile.visibility = if (uri != null) View.VISIBLE else View.GONE
            binding.tvUploadText.visibility = if (uri != null) View.GONE else View.VISIBLE
            binding.btnUpload.visibility = if (uri != null) View.VISIBLE else View.GONE
            binding.imgUpload.setImageDrawable(
                if (uri != null)
                    requireContext().getDrawable(R.drawable.ic_pdf)
                else
                    requireContext().getDrawable(R.drawable.ic_upload)
            )
        }

    private fun setupRecyclerView() {
        binding.rvUploadedFiles.layoutManager = LinearLayoutManager(requireContext())
        val adapter = UploadedFilesAdapter { file -> viewModel.deleteFile(file) }
        binding.rvUploadedFiles.adapter = adapter

        viewModel.uploadedFiles.observe(viewLifecycleOwner) { files ->
            adapter.submitList(files)
            binding.rvUploadedFiles.visibility = if (files.isEmpty()) View.GONE else View.VISIBLE
            binding.tvRecent.visibility = if (files.isEmpty()) View.GONE else View.VISIBLE
            binding.divider.visibility = if (files.isEmpty()) View.GONE else View.VISIBLE
        }
    }


    private fun boostResume() {
        val jobUrl = binding.etJobUrl.text.toString().trim()
        if (jobUrl.isEmpty() || selectedPdfUri == null) {
            Snackbar.make(
                requireContext(),
                binding.root,
                "Please enter a job URL and upload a PDF",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }
        // Call prompt API from viewmodel
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeViewModel() {

        viewModel.boostStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                Result.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "Resume boosted!",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.getUploadedFiles()

                }

                Result.Loading -> Toast.makeText(
                    requireContext(),
                    "Boosting resume...",
                    Toast.LENGTH_SHORT
                ).show()

                is Result.Error -> Toast.makeText(
                    requireContext(),
                    "Error: ${status.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.uploadingStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                Result.Success -> {
                    viewModel.getUploadedFiles()
                    resetUploadUI()
                }

                Result.Loading -> {
                    Snackbar.make(
                        requireView(),
                        "Uploading Beep Boop ...",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Result.Error -> {
                    Snackbar.make(
                        requireView(),
                        "Oops, something went wrong. Please try again",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    resetUploadUI()
                }
            }

        }
    }

    private fun resetUploadUI() {
        binding.btnUpload.visibility = View.GONE
        binding.imgUpload.setImageDrawable(requireContext().getDrawable(R.drawable.ic_upload))
        binding.tvUploadedFile.visibility = View.VISIBLE
        binding.tvUploadText.visibility = View.GONE
    }

}