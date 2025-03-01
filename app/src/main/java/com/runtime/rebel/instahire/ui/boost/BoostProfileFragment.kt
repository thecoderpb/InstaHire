package com.runtime.rebel.instahire.ui.boost

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.runtime.rebel.instahire.App
import com.runtime.rebel.instahire.R
import com.runtime.rebel.instahire.databinding.FragmentBoostBinding
import com.runtime.rebel.instahire.model.FileData
import com.runtime.rebel.instahire.model.Result
import com.runtime.rebel.instahire.ui.PdfViewerActivity
import com.runtime.rebel.instahire.utils.PDFGenerator
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class BoostProfileFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: BoostViewModel
    private lateinit var binding: FragmentBoostBinding
    private var navJobUrl: String? = null
    private var navJobDescription: String? = null
    private var selectedPdfUri: Uri? = null
    private var isBoostClicked = false

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
            navJobDescription = it.getString("jobDescription")
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
            viewModel.uploadUserFile(
                selectedPdfUri!!,
                getFileNameFromUri(requireContext(), selectedPdfUri)
            )
        }

    }

    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedPdfUri = uri
                binding.tvUploadedFile.text = getFileNameFromUri(requireContext(), uri)

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
        val adapter = UploadedFilesAdapter(::onCardClick) { file -> viewModel.deleteFile(file) }
        binding.rvUploadedFiles.adapter = adapter

        viewModel.uploadedFiles.observe(viewLifecycleOwner) { files ->
            adapter.submitList(files)
            binding.rvUploadedFiles.visibility = if (files.isEmpty()) View.GONE else View.VISIBLE
            binding.tvRecent.visibility = if (files.isEmpty()) View.GONE else View.VISIBLE
            binding.divider.visibility = if (files.isEmpty()) View.GONE else View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun onCardClick(fileData: FileData) {

        val intent = Intent(requireContext(), PdfViewerActivity::class.java)
        intent.putExtra("pdfUrl", fileData.url)
        startActivity(intent)
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

        viewModel.uploadUserFile(
            selectedPdfUri!!,
            getFileNameFromUri(requireContext(), selectedPdfUri)
        )
        isBoostClicked = true

    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun observeViewModel() {

        viewModel.boostStatus.distinctUntilChanged().observe(viewLifecycleOwner) { status ->
            when (status) {
                Result.Success -> {
                    showUploadUI()
                    viewModel.getUploadedFiles()
                    Snackbar.make(
                        requireView(),
                        "AI overlord cooked your resume!!!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                Result.Loading -> {

                    showLoadingUI("Boosting Resume ...")
                    Snackbar.make(
                        requireView(),
                        "Let the AI overlord cook...",
                        Snackbar.LENGTH_SHORT
                    ).show()

                }

                is Result.Error -> {
                    Timber.d(status.message)
                    binding.tvUploadText.visibility = View.VISIBLE
                    binding.tvUploadText.text = "Oops, something went wrong. Please try again"
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }

        viewModel.uploadingStatus.distinctUntilChanged().observe(viewLifecycleOwner) { status ->
            when (status) {
                Result.Success -> {
                    viewModel.getUploadedFiles()
                    if(viewModel.boostStatus.value !is Result.Loading)
                    showUploadUI()
                }

                Result.Loading -> {
                    showLoadingUI("Uploading Beep Boop ...")
                }

                is Result.Error -> {
                    Timber.d(status.message)
                    binding.tvUploadText.visibility = View.VISIBLE
                    binding.tvUploadText.text = "Oops, something went wrong. Please try again"
                    binding.progressCircular.visibility = View.GONE
                    Snackbar.make(
                        requireView(),
                        "Oops, something went wrong. Please try again",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

        }

        viewModel.uploadedFileUrl.distinctUntilChanged().observe(viewLifecycleOwner) { url ->
            if (url != null && navJobUrl != null && navJobDescription != null && isBoostClicked) {
                showLoadingUI("Generating Resume ...")
                isBoostClicked = false
                viewModel.processResumeEnhancement(
                    requireContext(),
                    url,
                    navJobUrl!!,
                    navJobDescription!!,
                )
            }

            if (url !=null && isBoostClicked && binding.etJobUrl.text.toString().trim().isNotEmpty()) {
                showLoadingUI("Generating Resume ...")
                isBoostClicked = false
                viewModel.processResumeEnhancement(
                    requireContext(),
                    url,
                    binding.etJobUrl.text.toString().trim(),
                    "Job description not provided",
                )
            }
        }

        viewModel.generatedText.distinctUntilChanged().observe(viewLifecycleOwner) { text ->
            showLoadingUI("Generating Resume ...")
            val pdfDirectory = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                "InstaHire"
            )
            if (!pdfDirectory.exists()) {
                pdfDirectory.mkdirs()  // Create directory if it doesn't exist
            }
            val pdfFile = File(
                pdfDirectory,
                getFileNameFromUri(requireContext(), selectedPdfUri) ?: "InstaHire.pdf"
            )
            PDFGenerator.createPDF(pdfFile, text)

            viewModel.uploadGeneratedFile(pdfFile.absolutePath, "Enhanced - ${pdfFile.name}")

        }

        viewModel.uploadingGeneratedFileStatus.distinctUntilChanged().observe(viewLifecycleOwner) {
            when (it) {
                Result.Success -> {
                    viewModel.getUploadedFiles()

                    showUploadUI()
                }

                is Result.Error -> {

                    binding.tvUploadText.text = "Error: ${it.message}"
                    binding.progressCircular.visibility = View.GONE

                }

                Result.Loading -> {
                    showLoadingUI("Uploading your enhanced resume...")
                }
            }
        }

    }

    private fun showUploadUI() {
        binding.btnUpload.visibility = View.GONE
        binding.imgUpload.visibility = View.VISIBLE
        binding.tvUploadedFile.visibility = View.VISIBLE
        binding.tvUploadText.visibility = View.VISIBLE
        binding.tvUploadedFile.visibility = View.GONE
        binding.progressCircular.visibility = View.GONE

        binding.imgUpload.setImageDrawable(requireContext().getDrawable(R.drawable.ic_upload))
        binding.tvUploadText.text = getString(R.string.upload_your_resume_pdf_only)
    }

    private fun showLoadingUI(spinnerText: String) {
        binding.btnUpload.visibility = View.GONE
        binding.imgUpload.visibility = View.INVISIBLE
        binding.tvUploadedFile.visibility = View.GONE
        binding.tvUploadText.visibility = View.VISIBLE
        binding.progressCircular.visibility = View.VISIBLE
        binding.tvUploadedFile.visibility = View.GONE

        binding.tvUploadText.text = spinnerText
    }

    private fun getFileNameFromUri(context: Context, uri: Uri?): String? {
        var fileName: String? = null

        // Query the content provider for the file name
        uri?.let {
            val cursor = context.contentResolver.query(it, null, null, null, null)
            cursor?.use { safeCursor ->
                val nameIndex = safeCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (safeCursor.moveToFirst()) {
                    fileName = safeCursor.getString(nameIndex)
                }
            }
        }

        return fileName

    }

}