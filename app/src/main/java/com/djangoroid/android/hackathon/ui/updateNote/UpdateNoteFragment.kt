package com.djangoroid.android.hackathon.ui.updateNote

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.djangoroid.android.hackathon.MainActivity
import com.djangoroid.android.hackathon.R
import com.djangoroid.android.hackathon.databinding.FragmentUpdateNoteBinding
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.slider.RangeSlider
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File
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
    private val viewModel: CanvasViewModel by viewModel()

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

        val title = binding.title
        val paint = binding.drawView
        val rangeSlider = binding.rangebar
        val undo = binding.btnUndo
        val save = binding.btnSave
        val color = binding.btnColor
        val stroke = binding.btnStroke
        val upload = binding.btnUpload

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
            Log.d("UpdateNoteFragment.kt", "Start store drawView")

            val bmp: Bitmap = paint.save()

            // opening a OutputStream to write into the file
            var imageOutStream: OutputStream? = null


            val cv = ContentValues()
            // name of the file
            cv.put(MediaStore.Images.Media.DISPLAY_NAME, "${title.text}.png")
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

                val path = getRealPathFromURI(uri)
                val file = File("$path")
                val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("images", file.name, requestFile)

                lifecycleScope.launch {
                    viewModel.createNoteCanvas(images = body)
                }

                Log.d("UpdateNoteFragment.kt", "End store drawView ")
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

        upload.setOnClickListener {

            // opening a OutputStream to write into the file
            var imageOutStream: OutputStream? = null

            val cv = ContentValues()
            // name of the file
            cv.put(MediaStore.Images.Media.DISPLAY_NAME, "${title.text}.png")
            // type of the file
            cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            // location of the file to be saved
            cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)

            // get the Uri of the file which is to be created in the storage
            val uri: Uri =
                mainActivity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)!!
            val path = getRealPathFromURI(uri)
            val file = File("$path")
            val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("images", file.name, requestFile)

            lifecycleScope.launch {
                viewModel.createNoteCanvas(images = body)
            }
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

    private fun getImageUri(context: Context, bmp: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, bytes)
        Log.d("UpdateNoteFragment.kt", "${bytes.toByteArray()}")
        val path: String = MediaStore.Images.Media.insertImage(context.contentResolver, bmp, "Title1", null)
        return Uri.parse(path)
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? = mainActivity.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
}
