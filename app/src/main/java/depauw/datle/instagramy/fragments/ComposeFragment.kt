package depauw.datle.instagramy.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.parse.ParseFile
import com.parse.ParseUser
import depauw.datle.instagramy.MainActivity
import depauw.datle.instagramy.Post
import depauw.datle.instagramy.R
import depauw.datle.instagramy.databinding.FragmentComposeBinding
import java.io.File

class ComposeFragment : Fragment() {
    private lateinit var binding: FragmentComposeBinding

    private val btn_take_picture_clickListener = View.OnClickListener {
        onLaunchCamera()
    }
    private val btn_post_clickListener = View.OnClickListener {
        val description = binding.etDescription.text.toString()
        val user = ParseUser.getCurrentUser()
        if(photoFile != null) {
            submitPost(description, user, photoFile!!)
        } else {
            Toast.makeText(requireContext(), "Take picture", Toast.LENGTH_SHORT).show()
        }
    }
    private val btn_log_out_clickListener = View.OnClickListener {
        ParseUser.logOut()
        requireActivity().finish()
    }
    private fun submitPost(description: String, user: ParseUser, image: File) {
        var post = Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(image))
        post.saveInBackground {
            if(it != null) {
                Log.e(TAG, "Save post failed")
                it.printStackTrace()
            } else {
                Log.i(TAG, "Save post successfully")
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentComposeBinding.bind(view)

        binding.btnTakePicture.setOnClickListener(btn_take_picture_clickListener)
        binding.btnPost.setOnClickListener(btn_post_clickListener)
        binding.btnLogOut.setOnClickListener(btn_log_out_clickListener)
    }
    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        val fileProvider: Uri =
            FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        }
    }
    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(ComposeFragment.photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                val ivPreview: ImageView = binding.ivTakenPicture
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object {
        val TAG = "ComposeFragment"
        val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
        val photoFileName = "photo.jpg"
        var photoFile: File? = null
    }
}