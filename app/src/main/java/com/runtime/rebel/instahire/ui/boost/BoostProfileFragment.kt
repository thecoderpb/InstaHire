package com.runtime.rebel.instahire.ui.boost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.runtime.rebel.instahire.App
import com.runtime.rebel.instahire.databinding.FragmentAboutBinding
import timber.log.Timber
import javax.inject.Inject

class BoostProfileFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: BoostViewModel
    private lateinit var binding: FragmentAboutBinding
    private var jobUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[BoostViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            jobUrl =it.getString("jobUrl")
        }

        jobUrl?.let {
            Timber.d("BoostProfileFragment", "Job URL: $jobUrl")
        }
    }


}