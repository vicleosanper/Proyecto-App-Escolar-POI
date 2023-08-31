package com.example.proyecto_poi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.proyecto_poi.modelos.usuarios
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val UsuaRef = database.getReference("Usuarios")
    private lateinit var nombreUsuario : String
    private lateinit var contrasena : String
    private lateinit var correo : String
    private var autenticado:Boolean = false

    //Variables de cuando cierra Sesion
    /*
    private lateinit var nombreusuarioCS:String
    private lateinit var idUsuario:String
    private lateinit var contraCS:String
    private lateinit var correoCS:String
    private lateinit var carreraCS:String

     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /*
        nombreusuarioCS = intent.getStringExtra("nombreUsuario") ?: "sin_nombre"
        idUsuario = intent.getStringExtra("id_Usuario") ?: "sin_ID"
        contraCS = intent.getStringExtra("contraseña_CS") ?: "sin"
        correoCS = intent.getStringExtra("correo") ?: "sin"
        carreraCS = intent.getStringExtra("carrera") ?: "sin"

        if(idUsuario != "sin_ID"){

            UsuaRef.child(idUsuario).setValue(
                usuarios(nombreusuarioCS,contraCS,correoCS,carreraCS
                    ,false,idUsuario)
            )

        }

         */

        findViewById<Button>(R.id.id_inicio).setOnClickListener {

            nombreUsuario = findViewById<TextView>(R.id.id_Usuario).text.toString()
            contrasena = findViewById<TextView>(R.id.id_Contraseña2).text.toString()
            correo = findViewById<TextView>(R.id.id_correo).text.toString()

            autenticar()

            if (nombreUsuario.isEmpty()) {

                Toast.makeText(this, "Falta usuario", Toast.LENGTH_SHORT).show()
            }
            else{

                autenticar()

            }


        }

        findViewById<Button>(R.id.id_registrar).setOnClickListener {

            val intentR = Intent(this, ImagenActivity::class.java)
            startActivity(intentR)

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

    private fun autenticar(){

        val intent = Intent(this, PaginaInicioActivity::class.java)

        UsuaRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {

                    val usuario:usuarios = snap.getValue(usuarios::class.java) as usuarios

                    if(usuario.Correo == correo){

                        if(usuario.Usuario == nombreUsuario && usuario.Contraseña == contrasena){

                            if(!autenticado){

                                autenticado = true

                                UsuaRef.child(usuario.id_Usuario).setValue(usuarios(
                                    usuario.Usuario,usuario.Contraseña,usuario.Correo,usuario.carrera
                                    ,true,usuario.id_Usuario,usuario.tareas,usuario.ImagenURL))

                                intent.putExtra("nombreUsuario", nombreUsuario)
                                intent.putExtra("carrera",usuario.carrera)
                                intent.putExtra("correo",usuario.Correo)
                                startActivity(intent)

                            }


                        }

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@MainActivity, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })



    }


}