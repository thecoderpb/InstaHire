package com.runtime.rebel.instahire.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.runtime.rebel.instahire.databinding.ItemJobBinding
import com.runtime.rebel.instahire.model.JobItem

class JobListAdapter(private val jobClickListener: (JobItem) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var jobList = mutableListOf<JobItem>()

    fun setData(newJobs: List<JobItem>) {
        jobList.addAll(newJobs)
        notifyItemRangeChanged(0, jobList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val job = jobList[position]
        (holder as JobViewHolder).bind(job)

        // Pagination trigger: When reaching the last item in the list
        if (position == jobList.size - 1) {
            // Trigger load more
            holder.itemView.findFragment<DashboardFragment>().loadMoreJobs()
        }
    }

    override fun getItemCount(): Int = jobList.size

    inner class JobViewHolder(private val binding: ItemJobBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(job: JobItem) {
            binding.job = job
            binding.root.setOnClickListener { jobClickListener(job) }
            binding.executePendingBindings()
        }
    }
}