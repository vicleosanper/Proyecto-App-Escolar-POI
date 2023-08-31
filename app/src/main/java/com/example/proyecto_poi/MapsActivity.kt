package com.example.proyecto_poi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.proyecto_poi.databinding.ActivityMapsBinding
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var btnUSAR:Button
    private lateinit var coordenadaSeleccionada:LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)

        btnUSAR = findViewById(R.id.btnUSAR)
        btnUSAR.setOnClickListener{

            traducirCoordenadas()

        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        activarMiUbicacion()

        mMap.setOnMapClickListener { cordenada ->

            coordenadaSeleccionada = cordenada

            mMap.clear()
            mMap.addMarker(MarkerOptions().position(cordenada))
            btnUSAR.isEnabled = true


        }

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(25.67, -100.31)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,10F))
    }

    override fun onBackPressed() {

        super.onBackPressed()
        setResult(RESULT_CANCELED)

    }

    private fun traducirCoordenadas(){

        val geocoder = Geocoder(this, Locale.getDefault())

        Thread{

            val direcciones = geocoder.getFromLocation(
                coordenadaSeleccionada.latitude,
                coordenadaSeleccionada.longitude,
                1)

            if(direcciones.size>0){

                val direccion = direcciones[0].getAddressLine(0)

                val intentDeRegreso = Intent()
                intentDeRegreso.putExtra("ubicacion",direccion)

                setResult(RESULT_OK,intentDeRegreso)
                finish()

            }

        }.start()

    }

    private fun activarMiUbicacion(){

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap.isMyLocationEnabled = true

    }
}