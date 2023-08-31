package com.example.proyecto_poi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat

class Geolocalizacion : AppCompatActivity() {

    private lateinit var encriptacion:String
    private lateinit var tipo_chat: String
    private lateinit var nombreUsuario: String
    private lateinit var nombreChat: String

    private lateinit var direccion:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.geolocalizacion)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        encriptacion = intent.getStringExtra("Encriptacion") ?: "Activada"
        tipo_chat = intent.getStringExtra("TipoChat") ?: "0" //ID para saber en que chat estamos
        nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "sin_nombre"
        nombreChat = intent.getStringExtra("NombreDestino") ?: "sin_nombre"

        direccion = "sin_Direccion"

        findViewById<Button>(R.id.btnSeleccionar).setOnClickListener {

            revisarPermisos()
        }

        findViewById<Button>(R.id.btnCONFIRMAR).setOnClickListener{

            val intentChat = Intent(this, ChatActivity::class.java)
            intentChat.putExtra("nombreUsuario", nombreUsuario)
            intentChat.putExtra("TipoChat",tipo_chat)
            intentChat.putExtra("NombreDestino", nombreChat)
            intentChat.putExtra("Encriptacion",encriptacion)
            intentChat.putExtra("Ubicacion",direccion)

            startActivity(intentChat)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {

            findViewById<TextView>(R.id.tvUbicacion).text = data?.getStringExtra("ubicacion") ?: ""
            direccion = data?.getStringExtra("ubicacion") ?: ""

        } else {

            findViewById<TextView>(R.id.tvUbicacion).text = "Error o no seleccionaste una direccion"
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Se requiere aceptar el permiso", Toast.LENGTH_SHORT).show()
                revisarPermisos()
            } else {
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
                abrirMapa()
            }
        }
    }

    private fun abrirMapa() {

        startActivityForResult(Intent(this, MapsActivity::class.java), 1)
    }

    private fun revisarPermisos() {
        // Apartir de Android 6.0+ necesitamos pedir el permiso de ubicacion
        // directamente en tiempo de ejecucion de la app
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Si no tenemos permiso para la ubicacion
            // Solicitamos permiso
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        } else {
            // Ya se han concedido los permisos anteriormente
            abrirMapa()
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