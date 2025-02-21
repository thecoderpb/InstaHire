package com.runtime.rebel.instahire.ui.registration

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.runtime.rebel.instahire.App
import com.runtime.rebel.instahire.databinding.FragmentRegistrationBinding
import com.runtime.rebel.instahire.ui.HomeActivity
import javax.inject.Inject

class RegistrationFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegistrationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener {
            viewModel.signUpUser(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                ::signUpResponse
            )
        }
    }

    private fun signUpResponse(isSuccess: Boolean, response: String?) {
        if (isSuccess) {
            // navigate to home
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            activity?.finish()
        } else {
            // show error
            Toast.makeText(
                requireContext(),
                "Error creating account. Please try again. ",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}