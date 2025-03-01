package com.runtime.rebel.instahire.ui.job

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.runtime.rebel.instahire.App
import com.runtime.rebel.instahire.databinding.FragmentJobPostingBinding
import javax.inject.Inject

class JobPostingFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: JobPostingViewModel
    private lateinit var binding: FragmentJobPostingBinding
    private val args: JobPostingFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[JobPostingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobPostingBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jobItem = args.jobItem

        jobItem?.let { jobInfo ->
            binding.tvJobTitle.text = jobInfo.role
            binding.tvCompanyName.text = jobInfo.companyName
            binding.tvLocationDate.text =
                "${jobInfo.location ?: "Remote"} | Posted: ${jobInfo.datePosted}"
            binding.tvJobDescription.text = jobInfo.text ?: "No description available"
            binding.tvSource.text = "Source: ${jobInfo.source ?: ""}"

            // Set Keywords

            binding.rvKeywords.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvKeywords.adapter = KeywordAdapter(jobInfo.keywords)

            // Tailor Button
            binding.btnTailor.setOnClickListener { _ ->

                jobInfo.url?.let { safeUrl ->
                    findNavController().navigate(
                        JobPostingFragmentDirections.actionJobPostingFragmentToBoostFragment(
                            safeUrl,
                            jobInfo.text
                        )
                    )
                }

            }

            binding.fabApplyJob.setOnClickListener {
                jobInfo.url?.let { safeUrl ->
                    // Open Url in browser with intent

                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(safeUrl))
                    startActivity(browserIntent)

                }
            }

            binding.tvSource.setOnClickListener {
                jobInfo.source?.let { safeSource ->
                    // Open Url in browser with intent

                    val url = "https://www.google.com/search?q=" + Uri.encode(safeSource)
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                }
            }
        }

    }
}