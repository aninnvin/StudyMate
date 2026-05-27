package com.example.studymate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etEmail = findViewById<EditText>(R.id.etEmail) // Pastikan ID ini ada di XML
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPass = findViewById<EditText>(R.id.etConfirmPassword)

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLogin = findViewById<TextView>(R.id.tvLogin)

        tvLogin.setOnClickListener {
            finish()
        }

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPass = etConfirmPass.text.toString().trim()

            // Validasi
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!email.contains("@") || !email.contains(".")) {
                Toast.makeText(this, "Format email tidak valid!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPass) {
                Toast.makeText(this, "Password tidak cocok!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Register ke Firebase Auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid

                        // Simpan data username ke database
                        if (userId != null) {
                            val userMap = mapOf(
                                "uid" to userId,
                                "username" to username,
                                "email" to email
                            )

                            database.child("users").child(userId).setValue(userMap)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Akun berhasil dibuat!", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Gagal simpan data: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }

                    } else {
                        val errorMsg = task.exception?.message
                        if (errorMsg != null && errorMsg.contains("already")) {
                            Toast.makeText(this, "Email sudah terdaftar!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Gagal: $errorMsg", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }
}