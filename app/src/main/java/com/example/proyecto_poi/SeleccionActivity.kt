package com.example.proyecto_poi

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SeleccionActivity:AppCompatActivity() {

    private lateinit var encriptacion:String
    private lateinit var tipochat: String
    private lateinit var nombreUsuario: String
    private lateinit var PadreChat:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seleccion_grupo_subgrupos)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        encriptacion = intent.getStringExtra("Encriptacion") ?: "Activada"
        tipochat = intent.getStringExtra("TipoChat") ?: "0" //ID para saber el tipo de chat
        nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "sin_nombre"
        PadreChat = intent.getStringExtra("Padrechat") ?: "sin_Padre"

        findViewById<Button>(R.id.btn_MURO).setOnClickListener {

            val intentChat = Intent(this, ChatActivity::class.java)

            intentChat.putExtra("nombreUsuario", nombreUsuario)
            intentChat.putExtra("TipoChat",tipochat)
            intentChat.putExtra("NombreDestino", PadreChat)
            intentChat.putExtra("Encriptacion",encriptacion)
            startActivity(intentChat)


        }

        findViewById<Button>(R.id.btn_Subgrupos).setOnClickListener {

            val intentSub = Intent(this, Listachats::class.java)

            intentSub.putExtra("nombreUsuario", nombreUsuario)
            intentSub.putExtra("TipoChat","Subgrupos")
            intentSub.putExtra("Padrechat",PadreChat)
            intentSub.putExtra("Encriptacion",encriptacion)
            startActivity(intentSub)


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