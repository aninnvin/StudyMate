package com.example.studymate

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class TugasActivity : AppCompatActivity() {

    private var filterAktif = "Semua"
    private lateinit var adapter: TugasAdapter

    // Variables untuk form
    private var iconDipilih = "📝"
    private var warnaBackgroundDipilih = R.drawable.bg_time_purple

    private val allTugas = mutableListOf(
        TugasData("Tugas Bahasa Inggris", "Essay tentang descriptive text", "Jumat, 24 Mei 2024", "📝", "Belum Selesai", R.drawable.bg_time_purple),
        TugasData("Latihan Matematika", "Kerjakan soal halaman 45-47", "Rabu, 22 Mei 2024", "➗", "Selesai", R.drawable.bg_time_blue),
        TugasData("Tugas PAI", "Rangkuman materi bab 2", "Kamis, 23 Mei 2024", "🕌", "Belum Selesai", R.drawable.bg_time_green),
        TugasData("Laporan IPA", "Praktikum zat dan wujudnya", "Senin, 27 Mei 2024", "🧪", "Belum Selesai", R.drawable.bg_time_orange),
        TugasData("Presentasi Sejarah", "Buat slide PPT bab Revolusi", "Selasa, 28 Mei 2024", "🏛️", "Belum Selesai", R.drawable.bg_time_pink)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tugas)

        val btnBack = findViewById<ImageView>(R.id.btnBackTugas)
        val btnAdd = findViewById<ImageView>(R.id.btnAddTugas)
        val rvTugas = findViewById<RecyclerView>(R.id.rvTugas)

        val tabSemua = findViewById<TextView>(R.id.tabSemua)
        val tabBelum = findViewById<TextView>(R.id.tabBelum)
        val tabSelesai = findViewById<TextView>(R.id.tabSelesai)

        btnBack.setOnClickListener { finish() }

        // ✅ Tombol Plus - Tampilkan Dialog
        btnAdd.setOnClickListener { showAddTugasDialog() }

        // Setup RecyclerView
        adapter = TugasAdapter(getFilteredData())
        rvTugas.layoutManager = LinearLayoutManager(this)
        rvTugas.adapter = adapter

        // Tab Click Listeners
        tabSemua.setOnClickListener { switchTab(tabSemua, tabBelum, tabSelesai, "Semua") }
        tabBelum.setOnClickListener { switchTab(tabBelum, tabSemua, tabSelesai, "Belum") }
        tabSelesai.setOnClickListener { switchTab(tabSelesai, tabSemua, tabBelum, "Selesai") }
    }

    // 🎨 Fungsi Show Dialog Tambah Tugas
    private fun showAddTugasDialog() {
        val dialog = android.app.Dialog(this)
        dialog.setContentView(R.layout.dialog_add_tugas)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Reference views
        val etJudul = dialog.findViewById<EditText>(R.id.etJudulTugas)
        val etDeskripsi = dialog.findViewById<EditText>(R.id.etDeskripsi)
        val etDeadline = dialog.findViewById<EditText>(R.id.etDeadline)
        val btnBatal = dialog.findViewById<Button>(R.id.btnBatalTugas)
        val btnSimpan = dialog.findViewById<Button>(R.id.btnSimpanTugas)

        // Icon pickers
        val iconBook = dialog.findViewById<ImageView>(R.id.iconBook)
        val iconMath = dialog.findViewById<ImageView>(R.id.iconMath)
        val iconMosque = dialog.findViewById<ImageView>(R.id.iconMosque)
        val iconScience = dialog.findViewById<ImageView>(R.id.iconScience)
        val iconHistory = dialog.findViewById<ImageView>(R.id.iconHistory)

        // Color pickers
        val colorBlue = dialog.findViewById<ImageView>(R.id.colorBlue)
        val colorPurple = dialog.findViewById<ImageView>(R.id.colorPurple)
        val colorGreen = dialog.findViewById<ImageView>(R.id.colorGreen)
        val colorOrange = dialog.findViewById<ImageView>(R.id.colorOrange)
        val colorPink = dialog.findViewById<ImageView>(R.id.colorPink)

        // Default selection
        iconDipilih = "📝"
        warnaBackgroundDipilih = R.drawable.bg_time_purple
        highlightSelectedIcon(iconBook, iconMath, iconMosque, iconScience, iconHistory, iconBook)
        highlightSelectedColor(colorBlue, colorPurple, colorGreen, colorOrange, colorPink, colorPurple)

        // Date Picker untuk Deadline
        etDeadline.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    val bulan = arrayOf("Januari", "Februari", "Maret", "April", "Mei", "Juni",
                        "Juli", "Agustus", "September", "Oktober", "November", "Desember")
                    val hari = arrayOf("Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")
                    val cal = Calendar.getInstance()
                    cal.set(year, month, day)
                    val namaHari = hari[cal.get(Calendar.DAY_OF_WEEK) - 1]
                    etDeadline.setText("$namaHari, $day ${bulan[month]} $year")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Icon click listeners
        iconBook.setOnClickListener {
            iconDipilih = "📝"
            highlightSelectedIcon(iconBook, iconMath, iconMosque, iconScience, iconHistory, iconBook)
        }
        iconMath.setOnClickListener {
            iconDipilih = "➗"
            highlightSelectedIcon(iconBook, iconMath, iconMosque, iconScience, iconHistory, iconMath)
        }
        iconMosque.setOnClickListener {
            iconDipilih = "🕌"
            highlightSelectedIcon(iconBook, iconMath, iconMosque, iconScience, iconHistory, iconMosque)
        }
        iconScience.setOnClickListener {
            iconDipilih = "🧪"
            highlightSelectedIcon(iconBook, iconMath, iconMosque, iconScience, iconHistory, iconScience)
        }
        iconHistory.setOnClickListener {
            iconDipilih = "🏛️"
            highlightSelectedIcon(iconBook, iconMath, iconMosque, iconScience, iconHistory, iconHistory)
        }

        // Color click listeners
        colorBlue.setOnClickListener {
            warnaBackgroundDipilih = R.drawable.bg_time_blue
            highlightSelectedColor(colorBlue, colorPurple, colorGreen, colorOrange, colorPink, colorBlue)
        }
        colorPurple.setOnClickListener {
            warnaBackgroundDipilih = R.drawable.bg_time_purple
            highlightSelectedColor(colorBlue, colorPurple, colorGreen, colorOrange, colorPink, colorPurple)
        }
        colorGreen.setOnClickListener {
            warnaBackgroundDipilih = R.drawable.bg_time_green
            highlightSelectedColor(colorBlue, colorPurple, colorGreen, colorOrange, colorPink, colorGreen)
        }
        colorOrange.setOnClickListener {
            warnaBackgroundDipilih = R.drawable.bg_time_orange
            highlightSelectedColor(colorBlue, colorPurple, colorGreen, colorOrange, colorPink, colorOrange)
        }
        colorPink.setOnClickListener {
            warnaBackgroundDipilih = R.drawable.bg_time_pink
            highlightSelectedColor(colorBlue, colorPurple, colorGreen, colorOrange, colorPink, colorPink)
        }

        btnBatal.setOnClickListener { dialog.dismiss() }

        btnSimpan.setOnClickListener {
            val judul = etJudul.text.toString().trim()
            val deskripsi = etDeskripsi.text.toString().trim()
            val deadline = etDeadline.text.toString().trim()

            if (judul.isEmpty() || deadline.isEmpty()) {
                Toast.makeText(this, "Judul dan deadline harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ UI DONE - Tambah data baru ke list (nanti temanmu yang sambungkan ke database)
            val tugasBaru = TugasData(judul, deskripsi, deadline, iconDipilih, "Belum Selesai", warnaBackgroundDipilih)
            allTugas.add(0, tugasBaru) // Tambah di posisi pertama

            // Refresh adapter
            adapter.updateData(getFilteredData())

            Toast.makeText(this, "✅ Tugas '$judul' berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun highlightSelectedIcon(
        book: ImageView, math: ImageView, mosque: ImageView,
        science: ImageView, history: ImageView, selected: ImageView
    ) {
        val allIcons = listOf(book, math, mosque, science, history)
        allIcons.forEach { view ->
            view.setBackgroundResource(R.drawable.bg_icon_circle_blue)
            view.setColorFilter(Color.parseColor("#1E40AF"))
        }
        selected.setBackgroundResource(R.drawable.bg_time_purple)
        selected.setColorFilter(Color.parseColor("#FFFFFF"))
    }

    private fun highlightSelectedColor(
        blue: ImageView, purple: ImageView, green: ImageView,
        orange: ImageView, pink: ImageView, selected: ImageView
    ) {
        val allColors = listOf(blue, purple, green, orange, pink)
        allColors.forEach { view ->
            view.foreground = ContextCompat.getDrawable(this, R.drawable.bg_color_circle)
        }
        selected.foreground = ContextCompat.getDrawable(this, R.drawable.bg_color_circle_selected)
    }

    private fun switchTab(active: TextView, inactive1: TextView, inactive2: TextView, filter: String) {
        filterAktif = filter

        listOf(inactive1, inactive2, active).forEach {
            it.setBackgroundResource(R.drawable.bg_tab_inactive)
            it.setTextColor(Color.parseColor("#64748B"))
            it.setTypeface(null, android.graphics.Typeface.NORMAL)
        }

        active.setBackgroundResource(R.drawable.bg_tab_active_purple)
        active.setTextColor(Color.parseColor("#FFFFFF"))
        active.setTypeface(null, android.graphics.Typeface.BOLD)

        adapter.updateData(getFilteredData())
    }

    private fun getFilteredData(): List<TugasData> {
        return when (filterAktif) {
            "Belum" -> allTugas.filter { it.status == "Belum Selesai" }
            "Selesai" -> allTugas.filter { it.status == "Selesai" }
            else -> allTugas
        }
    }
}