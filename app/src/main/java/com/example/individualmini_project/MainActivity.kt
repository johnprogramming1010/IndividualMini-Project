package com.example.individualmini_project

import android.app.Activity
    import android.content.Intent
    import android.content.pm.PackageManager
    import android.graphics.Bitmap
    import android.graphics.BitmapFactory
    import android.net.Uri
    import android.os.Bundle
    import android.provider.MediaStore
    import android.widget.Button
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.app.ActivityCompat
    import com.example.individualmini_project.R

    class MainActivity : AppCompatActivity() {

        private val PICK_IMAGE_REQUEST = 1
        private lateinit var imageView: ImageView
        private lateinit var textView1: TextView
        private lateinit var textView2: TextView

        private val strings = listOf("Dog", "Cat", "Horse", "Lion", "Tiger", "Wolf", "Bear", "Deer", "plant", "Shark")
        private val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            // Initialize views
            imageView = findViewById(R.id.imageView)
            textView1 = findViewById(R.id.textView1)
            textView2 = findViewById(R.id.textView2)

            // Button click listener
            findViewById<Button>(R.id.loadImageButton).setOnClickListener {
                // Check if permission to read external storage is granted
                if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // If not, request the permission
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        PICK_IMAGE_REQUEST
                    )
                } else {
                    // If permission is already granted, open file picker
                    openFilePicker()
                }

                // Display random string and number
                displayRandomStringAndNumber()
            }
        }

        // Function to display random string and number
        private fun displayRandomStringAndNumber() {
            // Randomly select a string from the list
            val randomString = strings.random()
            textView1.text = randomString

            // Randomly select a number from the list
            val randomNumber = numbers.random()
            textView2.text = randomNumber.toString()
        }

        // Function to open file picker
        private fun openFilePicker() {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Handle result from file picker
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
                // Get selected image URI
                val imageUri = data.data
                // Resize and set selected image to ImageView
                imageUri?.let { uri ->
                    val bitmap = decodeUriToBitmap(uri, 400) // Resize bitmap to a maximum width of 400px
                    imageView.setImageBitmap(bitmap)
                }
            }
        }

        // Function to decode URI to Bitmap and resize it
        private fun decodeUriToBitmap(uri: Uri, requiredWidth: Int): Bitmap? {
            val options = BitmapFactory.Options().apply {
                // Decode bitmap dimensions only
                inJustDecodeBounds = true
            }
            // Decode bitmap dimensions
            BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, options)
            val width = options.outWidth
            val height = options.outHeight

            // Calculate sample size to resize bitmap
            var sampleSize = 1
            if (width > requiredWidth) {
                sampleSize = width / requiredWidth
            }

            // Decode bitmap with calculated sample size
            options.apply {
                inJustDecodeBounds = false
                inSampleSize = sampleSize
            }
            return BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, options)
        }
    }
