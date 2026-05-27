package com.example.studymate

data class TugasData(
    val judul: String,
    val deskripsi: String,
    val deadline: String,
    val ikon: String,
    val status: String,              // "Belum Selesai" atau "Selesai"
    val warnaBackground: Int         // Resource drawable: R.drawable.bg_time_blue, dll
)