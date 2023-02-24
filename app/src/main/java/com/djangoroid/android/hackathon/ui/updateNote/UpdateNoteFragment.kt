package com.djangoroid.android.hackathon.ui.updateNote

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Color.parseColor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.djangoroid.android.hackathon.MainActivity
import com.djangoroid.android.hackathon.R
import com.djangoroid.android.hackathon.databinding.FragmentUpdateNoteBinding
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.slider.RangeSlider
import java.io.OutputStream


class UpdateNoteFragment : Fragment() {

    // 1. Context를 할당할 변수를 프로퍼티로 선언(어디서든 사용할 수 있게)
    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 2. Context를 액티비티로 형변환해서 할당
        mainActivity = context as MainActivity
    }

    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val paint = binding.drawView
        val rangeSlider = binding.rangebar
        val undo = binding.btnUndo
        val save = binding.btnSave
        val color = binding.btnColor
        val stroke = binding.btnStroke

        // creating a OnClickListener for each button,
        // to perform certain actions

        // the undo button will remove the most
        // recent stroke from the canvas
        undo.setOnClickListener { paint.undo() }

        // the save button will save the current
        // canvas which is actually a bitmap
        // in form of PNG, in the storage
        save.setOnClickListener {
            // getting the bitmap from DrawView class
            val bmp: Bitmap = paint.save()!!

            // opening a OutputStream to write into the file
            var imageOutStream: OutputStream? = null
            val cv = ContentValues()

            // name of the file
            cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png")

            // type of the file
            cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png")

            // location of the file to be saved
            cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)

            // get the Uri of the file which is to be created in the storage
            val uri: Uri =
                mainActivity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)!!
            try {
                // open the output stream with the above uri
                imageOutStream = mainActivity.contentResolver.openOutputStream(uri)

                // this method writes the files in storage
                bmp.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream)

                // close the output stream after use
                imageOutStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // the color button will allow the user
        // to select the color of his brush
        color.setOnClickListener {
            ColorPickerDialog
                .Builder(mainActivity)        				// Pass Activity Instance
                .setTitle("색 설정")
                .setDefaultColor(R.color.black)          	// Default "Choose Color"
                .setColorShape(ColorShape.CIRCLE)   // Default ColorShape.CIRCLE
                .setColorListener { color, colorHex ->
                    paint.setColor(color)
                }
                .show()
        }

        // the button will toggle the visibility of the RangeBar/RangeSlider
        stroke.setOnClickListener {
            if (rangeSlider.visibility == View.VISIBLE) rangeSlider.visibility =
                View.GONE else rangeSlider.visibility =
                View.VISIBLE
        }

        // set the range of the RangeSlider
        rangeSlider.valueFrom = 0.0f
        rangeSlider.valueTo = 100.0f

        // adding a OnChangeListener which will
        // change the stroke width
        // as soon as the user slides the slider
        rangeSlider.addOnChangeListener(RangeSlider.OnChangeListener { slider, value, fromUser ->
            paint.setStrokeWidth(
                value.toInt()
            )
        })

        // pass the height and width of the custom view
        // to the init method of the DrawView object
        val vto = paint.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                paint.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = paint.measuredWidth
                val height = paint.measuredHeight
                paint.init(height, width)
            }
        })
    }

}