package com.runtime.rebel.instahire.ui.files

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.runtime.rebel.instahire.App
import com.runtime.rebel.instahire.databinding.FragmentFilesBinding
import com.runtime.rebel.instahire.model.FileData
import com.runtime.rebel.instahire.model.Result
import com.runtime.rebel.instahire.ui.PdfViewerActivity
import com.runtime.rebel.instahire.ui.boost.UploadedFilesAdapter
import javax.inject.Inject

class FilesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: FilesViewModel
    private lateinit var binding: FragmentFilesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[FilesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecyclerView()
        setupObservers()
    }

    private fun setupObservers() {

        viewModel.uploadedStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }

                Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvUploadedFiles.layoutManager = LinearLayoutManager(requireContext())
        val userAdapter = UploadedFilesAdapter(::onCardClick) { file -> viewModel.deleteFile(file) }
        binding.rvUploadedFiles.adapter = userAdapter

        binding.rvGeneratedFiles.layoutManager = LinearLayoutManager(requireContext())
        val generatedAdapter =
            UploadedFilesAdapter(::onCardClick) { file -> viewModel.deleteFile(file) }
        binding.rvGeneratedFiles.adapter = generatedAdapter

        viewModel.uploadedFiles.observe(viewLifecycleOwner) { files ->
            val userFiles = files.filter { fileData -> fileData.isUserUploaded }
            val generatedFiles = files.filter { fileData -> !fileData.isUserUploaded }

            userAdapter.submitList(userFiles)
            generatedAdapter.submitList(generatedFiles)

            binding.rvUploadedFiles.visibility =
                if (userFiles.isEmpty()) View.GONE else View.VISIBLE
            binding.tvRecent.visibility = if (userFiles.isEmpty()) View.GONE else View.VISIBLE
            binding.divider.visibility = if (userFiles.isEmpty()) View.GONE else View.VISIBLE

            binding.rvGeneratedFiles.visibility =
                if (generatedFiles.isEmpty()) View.GONE else View.VISIBLE
            binding.tvRecentGenerated.visibility =
                if (generatedFiles.isEmpty()) View.GONE else View.VISIBLE
            binding.dividerTwo.visibility =
                if (generatedFiles.isEmpty()) View.GONE else View.VISIBLE

            binding.tvEmptyFiles.visibility = if (files.isEmpty()) View.VISIBLE else View.GONE
            binding.ivBolt.visibility = if (files.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun onCardClick(fileData: FileData) {

        val intent = Intent(requireContext(), PdfViewerActivity::class.java)
        intent.putExtra("pdfUrl", fileData.url)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUploadedFiles()
    }
}