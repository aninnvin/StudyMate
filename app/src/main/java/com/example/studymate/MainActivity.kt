package com.example.studymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.text.TextWatcher
import android.text.Editable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Cek apakah user sudah login
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Belum login, balik ke LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Ambil data user dari Firebase
        loadUserData()

        animateEntrance()
        setupClickListeners()
        setupSearchBar()
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return

        database.child("users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").getValue(String::class.java) ?: "User"

                // Update TextView sapaan di header
                val tvUserName = findViewById<TextView>(R.id.tvUserName)
                tvUserName.text = "$username ✨"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Gagal load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupClickListeners() {
        // Tombol Jadwal
        findViewById<CardView>(R.id.btnJadwal)?.setOnClickListener {
            startActivity(Intent(this, JadwalActivity::class.java))
            overridePendingTransition(R.anim.slide_left, R.anim.fade_in)
        }

        // Tombol Tugas
        findViewById<CardView>(R.id.btnTugas)?.setOnClickListener {
            startActivity(Intent(this, TugasActivity::class.java))
            overridePendingTransition(R.anim.slide_left, R.anim.fade_in)
        }

        // Tombol Profil
        findViewById<ImageView>(R.id.btnProfileIcon)?.setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
            overridePendingTransition(R.anim.slide_left, R.anim.fade_in)
        }
    }

    private fun setupSearchBar() {
        val etSearch = findViewById<EditText>(R.id.etSearch)
        val layoutSearch = findViewById<CardView>(R.id.layoutSearch)

        layoutSearch?.setOnClickListener {
            etSearch?.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT)
        }

        etSearch?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun animateEntrance() {
        val views = listOf(
            findViewById<View>(R.id.layoutHeader),
            findViewById<View>(R.id.layoutSearch),
            findViewById<View>(R.id.layoutMenu),
            findViewById<View>(R.id.layoutComingUp)
        )

        views.forEachIndexed { index, view ->
            view?.apply {
                alpha = 0f
                translationY = 40f
                animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(700)
                    .setStartDelay((index * 120).toLong())
                    .setInterpolator(OvershootInterpolator(0.6f))
                    .start()
            }
        }
    }
}