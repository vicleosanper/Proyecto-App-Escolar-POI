package com.example.proyecto_poi


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_poi.databinding.ActivityMensajeBinding
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.File

class ImagenActivity : AppCompatActivity() {

    lateinit var binding: ActivityMensajeBinding
    val storageRef = FirebaseStorage.getInstance().reference

    private lateinit var imagenURL:String

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri = data?.data

            val filePath = ImagePicker.getFile(data)!!
            subirImagen(filePath)


            binding.ivPerfil.setImageURI(uri)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityMensajeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFoto.setOnClickListener {

            val cameraIntenr = ImagePicker.with(this)
                .provider(ImageProvider.CAMERA)
                .createIntent()

            startActivityForResult(cameraIntenr, 1)
        }

        binding.btnGaleria.setOnClickListener {

            val cameraIntenr = ImagePicker.with(this)
                .provider(ImageProvider.GALLERY)
                .createIntent()

            startActivityForResult(cameraIntenr, 1)
        }


        // RTraemos la imagen que previamente se guardo en sharedprefs
        val urlPrevPic = getSharedPreferences("MiArchiv", MODE_PRIVATE).getString("URL_FOTO", "")!!

        if (urlPrevPic.isNotEmpty()){

            Picasso.get().load(urlPrevPic).placeholder(R.drawable.ic_launcher_background).into(binding.ivPerfil)

        }

        binding.btnGuardar.setOnClickListener{

            val intent = Intent(this, RegistroActivity::class.java)
            intent.putExtra("URLImagen", imagenURL)
            startActivity(intent)
            finish()

        }


    }

    // ERROR
    /*
    java.io.FileNotFoundException:  (No such file or directory)
    An unknown error occurred, please check the HTTP result code and inner exception for server response.
     */
    fun subirImagen(file: File) {

        var uriFile = Uri.fromFile(file)

        val perfilRef = storageRef.child("perfil/${uriFile.lastPathSegment}")

        imagenURL = uriFile.toString()

        perfilRef.putFile(uriFile)
            .addOnSuccessListener { snap ->

                perfilRef.downloadUrl.addOnSuccessListener {

                    // Guardo en shared la url de la imagen
                    getSharedPreferences("MiArchiv", MODE_PRIVATE)
                        .edit().putString("URL_FOTO", it.toString())
                        .apply()
                }


            }
            .addOnFailureListener {

                Toast.makeText(this, "No se pudo subir tu imagen", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when(item.itemId){

            android.R.id.home->{
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)

    }

}