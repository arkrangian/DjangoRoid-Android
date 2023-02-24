package com.djangoroid.android.hackathon.ui.noteDetailedPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.djangoroid.android.hackathon.databinding.FragmentNotedetailedBinding

data class TempData(
    val testString: String
)

class NoteDetailedFragment: Fragment() {
    private lateinit var binding: FragmentNotedetailedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotedetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 임시 테스트
        val adapter = NoteDetailedListAdapter()
        adapter.submitList(
            listOf(
                TempData("hi\n\n\n"),
                TempData("hi\n\n\n"),
                TempData("hi\n\n\n"),
                TempData("hi\n\n\n"),
                TempData("hi\n\n\n"),
                TempData("hi\n\n\n"),
                TempData("hi\n\n\n"),
                TempData("hi\n\n\n"),
                TempData("hi\n\n\n"),
                TempData("hi\n\n\n"),
                TempData("hi\n\n\n"),
                TempData("hi\n\n\n"),
                TempData("hi\n\n\n"),
            )
        )

        binding.apply {
            description.text = "sdfdsl\nk\nf\nj\nsdlf\nksjdl\nfksdj\nflsdk\njfls\ndkf\njsdlk\nfjs\ndlfkjsdlfkasdj\nlfksdj\nflksdj\nflksdjflksdjflsdkjflsdkjflsdkfjlskfjlsdkfjlsdkfjlsdkfjsldfkjlsdkfj"
            //description.text = "여기는 설명들\n\n~~~~~\n\n설명 끝"
            files.adapter = adapter
        }
    }
}