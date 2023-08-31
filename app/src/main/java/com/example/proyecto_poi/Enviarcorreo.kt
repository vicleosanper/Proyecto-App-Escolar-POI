package com.example.proyecto_poi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Enviarcorreo:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.enviar_correo)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.btn_enviarcorreo).setOnClickListener{

            val para = findViewById<TextView>(R.id.txt_correo).text.toString()
            val asunto = findViewById<TextView>(R.id.txt_Asunto).text.toString()
            val mensaje = findViewById<TextView>(R.id.txt_Mensaje_Correo).text.toString()

            if(para.isNotEmpty() && asunto.isNotEmpty() && mensaje.isNotEmpty()){

                val intentCorreo = Intent(Intent.ACTION_SENDTO)
                intentCorreo.putExtra(Intent.EXTRA_EMAIL, arrayOf(para))
                intentCorreo.putExtra(Intent.EXTRA_SUBJECT,asunto)
                intentCorreo.putExtra(Intent.EXTRA_TEXT,mensaje)
                intentCorreo.data = Uri.parse("mailto:")

                val intencorreo2 = Intent.createChooser(intentCorreo,null)
                startActivity(intencorreo2)

            }


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