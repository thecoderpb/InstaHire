package com.runtime.rebel.instahire.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.runtime.rebel.instahire.App
import com.runtime.rebel.instahire.R
import com.runtime.rebel.instahire.databinding.FragmentLoginBinding
import com.runtime.rebel.instahire.ui.AboutActivity
import com.runtime.rebel.instahire.ui.HomeActivity
import timber.log.Timber
import javax.inject.Inject

class LoginFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        if (viewModel.isUserLoggedIn()) {
            navigateToHome()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            if(binding.etEmail.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.signInUser(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                ::signInResponse
            )
        }

        binding.tvAboutUs.setOnClickListener {
            startActivity(Intent(requireContext(), AboutActivity::class.java))
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun signInResponse(isSuccess: Boolean, response: String?) {
        if (isSuccess) {
            // navigate to home
            navigateToHome()
        } else {
            // show error
            Toast.makeText(
                requireContext(),
                "Error login in. Please try again. ",
                Toast.LENGTH_SHORT
            ).show()
            Timber.d(response)
        }
    }

    private fun navigateToHome() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}