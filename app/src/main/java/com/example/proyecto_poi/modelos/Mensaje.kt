package com.fcfm.poi.chat.modelos

import com.google.firebase.database.Exclude

class Mensaje(
    var id_chat: String = "",
    var contenido: String = "",
    var de: String = "",
    var para: String = "",
    val timeStamp: Any? = null,
    var encriptado:String = ""
) {
    @Exclude
    var esMio: Boolean = false
}




