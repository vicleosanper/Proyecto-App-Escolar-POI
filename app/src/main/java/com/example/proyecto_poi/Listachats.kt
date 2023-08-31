

package com.example.proyecto_poi
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_poi.R
import com.example.proyecto_poi.modelos.chat
import com.example.proyecto_poi.modelos.usuarios
import com.fcfm.poi.chat.adaptadores.ChatAdapter
import com.fcfm.poi.chat.adaptadores.ListaChatAdapter
import com.fcfm.poi.chat.modelos.Mensaje
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Listachats : AppCompatActivity(),ListaChatAdapter.OnChatClickListener {

    private lateinit var encriptacion:String
    private lateinit var tipochat: String
    private lateinit var nombreUsuario: String
    private lateinit var PadreChat:String
    private lateinit var carrera:String
    private var lista = mutableListOf<chat>() //Lista de chats
    private var listaU = mutableListOf<usuarios>() //Lista de usuarios
    /*private lateinit var adaptador : ListaChatAdapter*/

    private val database = FirebaseDatabase.getInstance()
    private val chatRef = database.getReference("chats")
    public lateinit var adaptador : ListaChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lista_chats_o_grupos)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        encriptacion = intent.getStringExtra("Encriptacion") ?: "Activada"
        tipochat = intent.getStringExtra("TipoChat") ?: "0" //ID para saber el tipo de chat
        nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "sin_nombre"
        PadreChat = intent.getStringExtra("Padrechat") ?: "sin_Padre"
        carrera = intent.getStringExtra("carrera") ?: "sin_carrera"

        Toast.makeText(this, nombreUsuario, Toast.LENGTH_SHORT).show()

        adaptador = ListaChatAdapter(lista,tipochat,this)
        findViewById<RecyclerView>(R.id.lista_chats).adapter = adaptador

        imprimirchats()

    }

    private fun imprimirchats(){

        chatRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                lista.clear()

                for (snap in snapshot.children) {

                    val chhat: chat = snap.getValue(chat::class.java) as chat

                        if(chhat.tipo_chat == tipochat && chhat.nombre!= nombreUsuario
                            && chhat.Padre == PadreChat){

                            lista.add(chhat)
                        }

                }

                if (lista.size > 0) {

                    adaptador.notifyDataSetChanged()
                    findViewById<RecyclerView>(R.id.lista_chats).smoothScrollToPosition(lista.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@Listachats, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onChatClick(nombre: String) {

        if(tipochat == "Subgrupos" || tipochat == "Individual"){

            val intentChat = Intent(this, ChatActivity::class.java)

            intentChat.putExtra("nombreUsuario", nombreUsuario)
            intentChat.putExtra("TipoChat",tipochat)
            intentChat.putExtra("NombreDestino", nombre)
            intentChat.putExtra("Encriptacion",encriptacion)
            startActivity(intentChat)

        }

        if(tipochat == "Grupos"){

            if(nombre != carrera){

                Toast.makeText(this@Listachats, "No perteneces a este grupo", Toast.LENGTH_SHORT).show()

            }
            else{

                val intentSub = Intent(this, SeleccionActivity::class.java)

                intentSub.putExtra("nombreUsuario", nombreUsuario)
                intentSub.putExtra("TipoChat","Subgrupos")
                intentSub.putExtra("Padrechat",nombre)
                intentSub.putExtra("Encriptacion",encriptacion)
                startActivity(intentSub)

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
