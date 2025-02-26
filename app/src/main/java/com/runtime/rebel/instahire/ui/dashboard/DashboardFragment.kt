package com.runtime.rebel.instahire.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.runtime.rebel.instahire.App
import com.runtime.rebel.instahire.databinding.FragmentDashboardBinding
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

        jobListAdapter = JobListAdapter()
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
            jobListAdapter.setData(jobs)
        }

        // Observe errors
        viewModel.error.observe(viewLifecycleOwner) { error ->
            binding.tvError.apply {
                text = error
                visibility = if (error.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }

        // Observe loading status to show a loading indicator
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Initial data load
        viewModel.fetchJobListings()

    }


    // Method to load more jobs
    fun loadMoreJobs() {
        viewModel.fetchJobListings()
    }
}