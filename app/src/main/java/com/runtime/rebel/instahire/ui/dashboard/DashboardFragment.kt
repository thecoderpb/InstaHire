package com.runtime.rebel.instahire.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.runtime.rebel.instahire.App
import com.runtime.rebel.instahire.databinding.FragmentDashboardBinding
import com.runtime.rebel.instahire.model.JobItem
import javax.inject.Inject

class DashboardFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: DashboardViewModel
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var jobListAdapter: JobListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[DashboardViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jobListAdapter = JobListAdapter(::onJobClicked)
        binding.recyclerView.apply {
            adapter = jobListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastVisibleItemPosition == jobListAdapter.itemCount - 1) {
                        // Load more jobs when scrolled to the bottom
                        loadMoreJobs()
                    }
                }
            })
        }

        // Observe job listings and update UI
        viewModel.jobListings.observe(viewLifecycleOwner) { jobs ->
            binding.tvError.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            jobListAdapter.setData(jobs)
        }

        // Observe errors
        viewModel.error.observe(viewLifecycleOwner) { error ->
            binding.tvError.apply {
                text = error
                visibility = if (error.isNotEmpty()) View.VISIBLE else View.GONE
            }
            binding.recyclerView.visibility = View.GONE
        }

        // Observe loading status to show a loading indicator
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Initial data load
        viewModel.fetchJobListings()

    }

    private fun onJobClicked(jobItem: JobItem) {
        val action = DashboardFragmentDirections.actionDashboardFragmentToJobPostingFragment(jobItem)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerView.visibility = View.GONE
    }

    // Method to load more jobs
    fun loadMoreJobs() {
        viewModel.fetchJobListings()
    }


}