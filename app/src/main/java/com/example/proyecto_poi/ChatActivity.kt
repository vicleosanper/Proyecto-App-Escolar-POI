package com.example.proyecto_poi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_poi.databinding.ActivityChatBinding
import com.example.proyecto_poi.modelos.chat
import com.example.proyecto_poi.modelos.usuarios
import com.fcfm.poi.chat.adaptadores.ChatAdapter
import com.fcfm.poi.chat.modelos.Mensaje
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import org.w3c.dom.Text


class ChatActivity : AppCompatActivity() {

    //lateinit var binding: ActivityChatBinding

    private lateinit var encriptacion:String
    private lateinit var tipo_chat: String
    private val listaMensajes = mutableListOf<Mensaje>()
    private lateinit var adaptador : ChatAdapter
    private lateinit var nombreUsuario: String
    private lateinit var nombreChat: String
    private lateinit var direccion:String
    private lateinit var imagenurl:String
    var vuelta:Boolean = false

    private val database = FirebaseDatabase.getInstance()
    private val msjRef = database.getReference("mensajes")
    //private val chatRef = database.getReference("chats")
    private val UsuaRef = database.getReference("Usuarios")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imagenurl = intent.getStringExtra("imagenURL") ?: "sin_Url"
        direccion = intent.getStringExtra("Ubicacion") ?: "sin_Direccion"
        encriptacion = intent.getStringExtra("Encriptacion") ?: "Activada"
        tipo_chat = intent.getStringExtra("TipoChat") ?: "0" //ID para saber en que chat estamos
        nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "sin_nombre"
        nombreChat = intent.getStringExtra("NombreDestino") ?: "sin_nombre"

        if(tipo_chat == "Individual"){

            findViewById<TextView>(R.id.text_nomChat).text = nombreChat

            Estado()

        }
        if(tipo_chat == "Subgrupos"){

            findViewById<TextView>(R.id.text_nomChat).text = nombreChat
            findViewById<TextView>(R.id.text_Estado).text = " Chat Grupal"

        }
        if(direccion != "sin_Direccion"){

            findViewById<TextView>(R.id.txtMensaje).text = direccion

        }



        adaptador = ChatAdapter(listaMensajes,tipo_chat)

        Toast.makeText(this, nombreUsuario, Toast.LENGTH_SHORT).show()

        findViewById<RecyclerView>(R.id.rvMensajes).adapter = adaptador

        findViewById<ImageView>(R.id.btn_MAPS).setOnClickListener{

            val intentG = Intent(this, Geolocalizacion::class.java)
            intentG.putExtra("nombreUsuario", nombreUsuario)
            intentG.putExtra("TipoChat",tipo_chat)
            intentG.putExtra("NombreDestino", nombreChat)
            intentG.putExtra("Encriptacion",encriptacion)
            startActivity(intentG)
            finish()


        }


        findViewById<ImageView>(R.id.btnEnviar).setOnClickListener {

            val mensaje = findViewById<EditText>(R.id.txtMensaje).text.toString()
            if (mensaje.isNotEmpty()) {

                findViewById<EditText>(R.id.txtMensaje).setText("")
                enviarMensaje(Mensaje(tipo_chat, mensaje, nombreUsuario,nombreChat,ServerValue.TIMESTAMP))
            }
        }

        recibirMensajes()




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

    private fun enviarMensaje(mensaje: Mensaje) {

        val mensajeFirebase = msjRef.push()
        mensaje.id_chat = tipo_chat ?: ""

        if(encriptacion == "Activada"){

            mensaje.contenido = CifradoTools.cifrar(mensaje.contenido,"POI03")
            mensaje.encriptado = "Activada"

        }
        if(encriptacion == "Desactivada"){

            mensaje.encriptado = "Desactivada"

        }

       // val chatFirebase = chatRef.push()
        //Chat.tipo_chat = tipo_chat?:""

        mensajeFirebase.setValue(mensaje)
        //chatFirebase.setValue(Chat)
    }

    private fun recibirMensajes() {

        msjRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                listaMensajes.clear()

                for (snap in snapshot.children) {

                    val mensaje: Mensaje = snap.getValue(Mensaje::class.java) as Mensaje

                    if(mensaje.id_chat == "Individual"){

                        if(mensaje.de == nombreChat){ //Caso de que el mensaje no es mio

                            if(mensaje.id_chat == tipo_chat){

                                if(mensaje.para == nombreUsuario){


                                    mensaje.esMio = false
                                    listaMensajes.add(mensaje)

                                }
                            }
                        }

                        if(mensaje.de == nombreUsuario){ // En caso de que el mensaje sea mio

                            if(mensaje.id_chat == tipo_chat){

                                if(mensaje.para == nombreChat){

                                    mensaje.esMio = true
                                    listaMensajes.add(mensaje)

                                }

                            }

                        }

                    }


                    if(mensaje.id_chat == "Subgrupos"){

                        if(mensaje.para == nombreChat){

                            if(mensaje.de == nombreUsuario){

                                mensaje.esMio = true
                                listaMensajes.add(mensaje)

                            }
                            if(mensaje.de != nombreUsuario){

                                mensaje.esMio = false
                                listaMensajes.add(mensaje)

                            }

                        }

                    }



                }



                if (listaMensajes.size > 0) {
                    adaptador.notifyDataSetChanged()
                    findViewById<RecyclerView>(R.id.rvMensajes).smoothScrollToPosition(listaMensajes.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@ChatActivity, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun Estado(){

        UsuaRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {

                    val usuario: usuarios = snap.getValue(usuarios::class.java) as usuarios

                        if(usuario.Usuario == nombreChat && tipo_chat == "Individual"){

                            if(!vuelta){

                                imagenurl = usuario.ImagenURL
                                var cargarImagen:ImageView = findViewById(R.id.IV_Perfilchat)
                                Picasso.get().load(imagenurl).into(cargarImagen)

                                if(usuario.activo){

                                    findViewById<TextView>(R.id.text_Estado).text = " Estado:Activo"

                                }
                                else
                                {
                                    findViewById<TextView>(R.id.text_Estado).text = " Estado:Ausente"
                                }

                                vuelta = true

                            }


                        }

                }

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@ChatActivity, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })


    }

}