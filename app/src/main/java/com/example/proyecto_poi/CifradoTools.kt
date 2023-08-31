package com.example.proyecto_poi

import android.util.Base64
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CifradoTools {

    private const val CIPHER_TRANSFORM = "AES/CBC/PKCS5PADDING"

    // AES

    fun cifrar(textoPlano: String, llave: String): String {

        val cipher = Cipher.getInstance(CIPHER_TRANSFORM)


        val llaveBytesFinal = ByteArray(16)
        val llaveBytesOriginal = llave.toByteArray(charset("UTF-8"))

        System.arraycopy(
            llaveBytesOriginal,
            0,
            llaveBytesFinal,
            0,
            Math.min(
                llaveBytesOriginal.size,
                llaveBytesFinal.size
            )
        )


        val secretKeySpec: SecretKeySpec = SecretKeySpec(
            llaveBytesFinal,
            "AES"
        )


        val vectorInicializacion = IvParameterSpec(llaveBytesFinal)

        cipher.init(
            Cipher.ENCRYPT_MODE,
            secretKeySpec,
            vectorInicializacion
        )

        val textoCifrado = cipher.doFinal(
            textoPlano.toByteArray(
                charset("UTF-8")
            )
        )

        var resultadoString = String(textoCifrado)

        val resultadoEnBase = String(Base64.encode(textoCifrado, Base64.NO_PADDING))

        return resultadoEnBase
    }

    fun descifrar( textoCifrado: String, llave: String): String {

        val textoCifradoBytes = Base64.decode(textoCifrado, Base64.NO_PADDING)

        val cipher = Cipher.getInstance(CIPHER_TRANSFORM)


        val llaveBytesFinal = ByteArray(16)
        val llaveBytesOriginal = llave.toByteArray(charset("UTF-8"))

        System.arraycopy(
            llaveBytesOriginal,
            0,
            llaveBytesFinal,
            0,
            Math.min(
                llaveBytesOriginal.size,
                llaveBytesFinal.size
            )
        )


        val secretKeySpec: SecretKeySpec = SecretKeySpec(
            llaveBytesFinal,
            "AES"
        )


        val vectorInicializacion = IvParameterSpec(llaveBytesFinal)

        cipher.init(
            Cipher.DECRYPT_MODE,
            secretKeySpec,
            vectorInicializacion
        )

        val textoPlano = String(cipher.doFinal(textoCifradoBytes))

        return textoPlano

    }

}