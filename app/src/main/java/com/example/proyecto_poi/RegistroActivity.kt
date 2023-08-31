package com.example.proyecto_poi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_poi.databinding.ActivityChatBinding
import com.example.proyecto_poi.databinding.ActivityMensajeBinding
import com.example.proyecto_poi.databinding.RegistroUsuarioBinding
import com.example.proyecto_poi.modelos.chat
import com.example.proyecto_poi.modelos.usuarios
import com.github.drjacky.imagepicker.ImagePicker
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.File

class RegistroActivity : AppCompatActivity() {

    //lateinit var binding: RegistroUsuarioBinding

    private val database = FirebaseDatabase.getInstance()
    private val RegRef = database.getReference("Usuarios")
    private val chatsRef = database.getReference("chats")

    private lateinit var nombreUsuario : String
    private lateinit var contrasena2 : String
    private lateinit var contrasena : String
    private lateinit var correo : String
    private lateinit var carrera:String

    private lateinit var urlImagen:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_usuario)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlImagen = intent.getStringExtra("URLImagen") ?: "sin_foto"
        //Picasso.get().load(urlImagen).into(binding.IVPerfilF)

        findViewById<Button>(R.id.id_inicio).setOnClickListener {

            nombreUsuario = findViewById<TextView>(R.id.id_Usuario).text.toString()
            contrasena = findViewById<TextView>(R.id.id_Contraseña).text.toString()
            contrasena2 = findViewById<TextView>(R.id.id_Contraseña2).text.toString()
            correo = findViewById<TextView>(R.id.id_Correo).text.toString()
            carrera = findViewById<TextView>(R.id.txt_Carrera_Registro).text.toString()

            if(nombreUsuario.isEmpty() || contrasena.isEmpty() || contrasena2.isEmpty()
                || correo.isEmpty() || carrera.isEmpty()){

                Toast.makeText(this, "Faltan campos por llenar", Toast.LENGTH_SHORT).show()


            }
            else{

                if(contrasena != contrasena2){

                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()

                }
                else{

                    Registrar(usuarios(nombreUsuario,contrasena,correo,carrera,false,"",0)
                    , chat("Individual",nombreUsuario,"sin_Padre"))
                    Toast.makeText(this, "El usuario ha sido registrado", Toast.LENGTH_SHORT).show()

                    val intentI = Intent(this, MainActivity::class.java)
                    startActivity(intentI)

                }

            }

        }

    }


    private fun Registrar(usuario:usuarios,chhat:chat) {

        val mensajeFirebase = RegRef.push()
        usuario.Usuario = nombreUsuario ?: ""
        usuario.Contraseña = contrasena ?:""
        usuario.Correo = correo ?:""
        usuario.carrera = carrera ?: ""
        usuario.id_Usuario = mensajeFirebase.key ?: ""
        usuario.ImagenURL = urlImagen

        mensajeFirebase.setValue(usuario)

        val mensajeFirebaseChat = chatsRef.push()
        chhat.Padre = "sin_Padre"
        chhat.nombre = nombreUsuario
        chhat.tipo_chat = "Individual"

        mensajeFirebaseChat.setValue(chhat)

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