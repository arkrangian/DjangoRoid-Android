package com.djangoroid.android.hackathon.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.djangoroid.android.hackathon.databinding.FragmentSignupBinding


class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = binding.id
        val password = binding.password
        val signupButton = binding.signup
        val radioButtonStudying = binding.radioButtonStudying
        val radioButtonPlaying = binding.radioButtonPlaying
        val radioButtonDrawing = binding.radioButtonDrawing
        val radioButtonEtc = binding.radioButtonEtc

        if (radioButtonStudying.isChecked) {

        }

        signupButton.setOnClickListener {
            val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
            this.findNavController().navigate(action)
        }
    }


}