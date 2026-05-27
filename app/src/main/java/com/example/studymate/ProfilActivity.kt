package com.example.studymate

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfilActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var tvNamaProfil: TextView
    private lateinit var tvEmailProfil: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Inisialisasi views
        val btnBack = findViewById<ImageView>(R.id.btnBackProfil)
        val cardLogout = findViewById<CardView>(R.id.cardLogout)
        tvNamaProfil = findViewById(R.id.tvNamaProfil)
        tvEmailProfil = findViewById(R.id.tvEmailProfil)

        btnBack.setOnClickListener { finish() }

        // Load data user dari Firebase
        loadUserProfile()

        cardLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Berhasil logout", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadUserProfile() {
        val userId = auth.currentUser?.uid ?: return

        database.child("users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").getValue(String::class.java) ?: "User"
                val email = snapshot.child("email").getValue(String::class.java) ?: "-"

                tvNamaProfil.text = username
                tvEmailProfil.text = email
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfilActivity, "Gagal load data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}