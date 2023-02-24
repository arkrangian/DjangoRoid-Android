package com.djangoroid.android.hackathon.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.djangoroid.android.hackathon.databinding.FragmentSignupBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignupFragment : Fragment() {

    private val viewModel: UserViewModel by viewModel()
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
        val nickname = binding.nickname
        val signupButton = binding.signup
        val checkboxStudying = binding.checkboxStudying
        val checkboxPlaying = binding.checkboxPlaying
        val checkboxDrawing = binding.checkboxDrawing
        val checkboxEtc = binding.checkboxEtc
        val loadingProgressBar = binding.loading



        signupButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE

            if (checkboxStudying.isChecked) {
                viewModel.addTags(checkboxStudying.text.toString())
            }
            if (checkboxPlaying.isChecked) {
                viewModel.addTags(checkboxPlaying.text.toString())
            }
            if (checkboxDrawing.isChecked) {
                viewModel.addTags(checkboxDrawing.text.toString())
            }
            if (checkboxEtc.isChecked) {
                viewModel.addTags(checkboxEtc.text.toString())
            }

            lifecycleScope.launch{
                viewModel.userTags.collect {
                    val response = async(Dispatchers.IO) {
                        viewModel.signup(id.text.toString(), password.text.toString(), nickname.text.toString(), tags = it)
                    }
                    response.await()
                    loadingProgressBar.visibility = View.INVISIBLE
                    val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
                    findNavController().navigate(action)
                }
            }
        }
    }


}