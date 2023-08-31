package com.example.proyecto_poi

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_poi.modelos.usuarios
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Configuraciones:AppCompatActivity() {

    private lateinit var encriptacion:String

    private lateinit var nombreUsuario: String
    private lateinit var carrera:String
    private lateinit var correo:String
    private lateinit var contraseña:String
    private lateinit var idUsuario:String

    private val database = FirebaseDatabase.getInstance()
    private val usuaRef = database.getReference("Usuarios")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.opciones_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "sin_nombre"
        carrera = intent.getStringExtra("carrera") ?: "sin_nombre"
        correo = intent.getStringExtra("correo") ?: "sin_nombre"


        usuaRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {

                    val usuario: usuarios = snap.getValue(usuarios::class.java) as usuarios

                    if(usuario.Usuario == nombreUsuario){

                        idUsuario = usuario.id_Usuario
                        contraseña = usuario.Contraseña

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@Configuraciones, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }

        })

        findViewById<Button>(R.id.id_encriptacion).setOnClickListener{

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Encriptación")
            builder.setMessage("¿Deseas activar la encriptación de tus mensajes?")
            builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(this,"La encriptación ha sido activada",Toast.LENGTH_LONG).show()

                encriptacion = "Activada"

                val intent = Intent(this, PaginaInicioActivity::class.java)
                intent.putExtra("nombreUsuario", nombreUsuario)
                intent.putExtra("carrera",carrera)
                intent.putExtra("correo",correo)
                intent.putExtra("Encriptacion",encriptacion)
                startActivity(intent)

            })
            builder.setNegativeButton("Denegar", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(this,"La encriptación ha sido desactivada",Toast.LENGTH_LONG).show()

                encriptacion = "Desactivada"

                val intent = Intent(this, PaginaInicioActivity::class.java)
                intent.putExtra("nombreUsuario", nombreUsuario)
                intent.putExtra("carrera",carrera)
                intent.putExtra("correo",correo)
                intent.putExtra("Encriptacion",encriptacion)
                startActivity(intent)


            })

            builder.show()

        }

        findViewById<Button>(R.id.id_Enviarcorreo).setOnClickListener{

            val intentCorreo = Intent(this, Enviarcorreo::class.java)
            startActivity(intentCorreo)

        }

        findViewById<Button>(R.id.id_CERRAR_SESION).setOnClickListener{

/*
            usuaRef.child(idUsuario).setValue(
                usuarios(nombreUsuario,contraseña,correo,carrera
                    ,false,idUsuario)
            )

 */

            val intentC = Intent(this, MainActivity::class.java)
            intentC.putExtra("id_Usuario", idUsuario)
            intentC.putExtra("nombreUsuario", nombreUsuario)
            intentC.putExtra("contraseña_CS",contraseña)
            intentC.putExtra("correo",correo)
            intentC.putExtra("carrera",carrera)

            startActivity(intentC)
            finish()


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