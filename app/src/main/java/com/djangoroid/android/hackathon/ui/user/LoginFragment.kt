package com.djangoroid.android.hackathon.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.djangoroid.android.hackathon.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = binding.id
        val password = binding.password
        val loginButton = binding.login
        val loadingProgressBar = binding.loading
        val upButton = binding.upButton

        loginButton.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToMyNoteFragment()
            this.findNavController().navigate(action)
        }
    }
}