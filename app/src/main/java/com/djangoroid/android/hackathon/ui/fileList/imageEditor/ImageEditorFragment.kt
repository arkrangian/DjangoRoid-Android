package com.djangoroid.android.hackathon.ui.fileList.imageEditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.djangoroid.android.hackathon.databinding.FragmentImageEditorBinding

class ImageEditorFragment: Fragment() {
    private lateinit var binding: FragmentImageEditorBinding
    private val navigationArgs: ImageEditorFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Glide.with(this)
            .asBitmap()
            .load(navigationArgs.url)
            .into(binding.image)
    }
}