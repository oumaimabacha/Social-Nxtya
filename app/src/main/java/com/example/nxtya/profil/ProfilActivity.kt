package com.example.nxtya.profil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.nxtya.MainActivity
import com.example.nxtya.R

class ProfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        //button_back

        val Back :ImageView = findViewById(R.id.button_back)
        Back.setOnClickListener {

            finish()
        }
    }
}