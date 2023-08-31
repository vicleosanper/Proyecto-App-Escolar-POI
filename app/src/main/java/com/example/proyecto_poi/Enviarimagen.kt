package com.example.proyecto_poi


import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.cast.framework.media.ImagePicker


class Enviarimagen:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.enviar_imagen)

        val btnPicker = findViewById<Button>(R.id.btnPicker)

        btnPicker.setOnClickListener{


            /*
            val intentGaleria = ImagePicker.with(this)
                .crop()        //Crop image(Optional), Check Customization for more option
                .compress(1024)   //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080) //Final image resolution will be less than 1080 x 1080(Optional)

             */




        }





    }


}