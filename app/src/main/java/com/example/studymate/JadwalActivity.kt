package com.example.studymate

import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class JadwalActivity : AppCompatActivity() {

    private var hariAktif = "Senin"
    private var warnaDipilih = R.drawable.bg_time_blue
    private lateinit var adapter: JadwalAdapter

    // Data dummy (contoh pelajar, tapi UI general)
    private val dataPerHari = mapOf(
        "Senin" to listOf(
            JadwalData("07:00", "Matematika", "Ruang 1", "➗", R.drawable.bg_time_blue),
            JadwalData("08:30", "Bahasa Inggris", "Ruang 2", "", R.drawable.bg_time_purple),
            JadwalData("10:00", "Pendidikan Agama", "Ruang 3", "🕌", R.drawable.bg_time_green),
            JadwalData("11:30", "IPA", "Ruang 4", "🧪", R.drawable.bg_time_orange)
        ),
        "Selasa" to listOf(
            JadwalData("07:30", "Bahasa Indonesia", "Ruang 2", "📖", R.drawable.bg_time_pink),
            JadwalData("09:00", "IPS", "Ruang 5", "", R.drawable.bg_time_blue),
            JadwalData("10:30", "PJOK", "Lapangan", "⚽", R.drawable.bg_time_green)
        ),
        "Rabu" to listOf(
            JadwalData("07:00", "Matematika", "Ruang 1", "➗", R.drawable.bg_time_blue),
            JadwalData("08:30", "Seni Budaya", "Ruang 6", "🎨", R.drawable.bg_time_pink),
            JadwalData("10:00", "Bahasa Inggris", "Ruang 2", "🔤", R.drawable.bg_time_purple)
        ),
        "Kamis" to listOf(
            JadwalData("07:30", "IPA", "Ruang 4", "🧪", R.drawable.bg_time_orange),
            JadwalData("09:00", "Pendidikan Agama", "Ruang 3", "🕌", R.drawable.bg_time_green),
            JadwalData("10:30", "Bahasa Indonesia", "Ruang 2", "", R.drawable.bg_time_pink)
        ),
        "Jumat" to listOf(
            JadwalData("07:00", "Upacara Bendera", "Lapangan", "🚩", R.drawable.bg_time_red),
            JadwalData("08:00", "Bahasa Inggris", "Ruang 2", "🔤", R.drawable.bg_time_purple),
            JadwalData("09:30", "Praktikum IPA", "Lab", "🔬", R.drawable.bg_time_orange)
        ),
        "Sabtu" to listOf(
            JadwalData("07:00", "Ekstrakurikuler", "Berbagai Tempat", "", R.drawable.bg_time_blue)
        ),
        "Minggu" to listOf(
            JadwalData("-", "Libur", "-", "😴", R.drawable.bg_time_purple)
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnAdd = findViewById<ImageView>(R.id.btnAdd)
        val rvJadwal = findViewById<RecyclerView>(R.id.rvJadwal)

        btnBack.setOnClickListener { finish() }
        btnAdd.setOnClickListener { showAddJadwalDialog() }

        adapter = JadwalAdapter(dataPerHari[hariAktif] ?: emptyList())
        rvJadwal.layoutManager = LinearLayoutManager(this)
        rvJadwal.adapter = adapter

        setupTabClicks()
    }

    private fun setupTabClicks() {
        val tabs = mapOf(
            findViewById<TextView>(R.id.tabSenin) to "Senin",
            findViewById<TextView>(R.id.tabSelasa) to "Selasa",
            findViewById<TextView>(R.id.tabRabu) to "Rabu",
            findViewById<TextView>(R.id.tabKamis) to "Kamis",
            findViewById<TextView>(R.id.tabJumat) to "Jumat",
            findViewById<TextView>(R.id.tabSabtu) to "Sabtu",
            findViewById<TextView>(R.id.tabMinggu) to "Minggu"
        )

        tabs.forEach { (tabView, hari) ->
            tabView.setOnClickListener {
                updateTabSelection(tabView, hari)
            }
        }
    }

    private fun updateTabSelection(selectedTab: TextView, hari: String) {
        hariAktif = hari

        val allTabs = listOf(
            findViewById<TextView>(R.id.tabSenin),
            findViewById<TextView>(R.id.tabSelasa),
            findViewById<TextView>(R.id.tabRabu),
            findViewById<TextView>(R.id.tabKamis),
            findViewById<TextView>(R.id.tabJumat),
            findViewById<TextView>(R.id.tabSabtu),
            findViewById<TextView>(R.id.tabMinggu)
        )

        allTabs.forEach { tab ->
            tab.setBackgroundResource(R.drawable.bg_tab_inactive)
            tab.setTextColor(Color.parseColor("#64748B"))
            tab.typeface = null
        }

        selectedTab.setBackgroundResource(R.drawable.bg_tab_active)
        selectedTab.setTextColor(Color.parseColor("#FFFFFF"))
        selectedTab.setTypeface(null, android.graphics.Typeface.BOLD)

        val newData = dataPerHari[hari] ?: emptyList()
        adapter.updateData(newData)

        Toast.makeText(this, "Menampilkan jadwal $hari", Toast.LENGTH_SHORT).show()
    }

    private fun showAddJadwalDialog() {
        val dialog = android.app.Dialog(this)
        dialog.setContentView(R.layout.dialog_add_jadwal)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val etMapel = dialog.findViewById<EditText>(R.id.etMapel)
        val etWaktu = dialog.findViewById<EditText>(R.id.etWaktu)
        val etRuang = dialog.findViewById<EditText>(R.id.etRuang)
        val btnBatal = dialog.findViewById<Button>(R.id.btnBatal)
        val btnSimpan = dialog.findViewById<Button>(R.id.btnSimpan)
        val spinnerHari = dialog.findViewById<Spinner>(R.id.spinnerHari)

        // Setup Spinner Hari
        val daftarHari = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, daftarHari)
        spinnerHari.adapter = adapterSpinner
        spinnerHari.setSelection(daftarHari.indexOf(hariAktif))

        val colorBlue = dialog.findViewById<ImageView>(R.id.colorBlue)
        val colorPurple = dialog.findViewById<ImageView>(R.id.colorPurple)
        val colorGreen = dialog.findViewById<ImageView>(R.id.colorGreen)
        val colorOrange = dialog.findViewById<ImageView>(R.id.colorOrange)
        val colorPink = dialog.findViewById<ImageView>(R.id.colorPink)

        // Default: Biru terpilih
        warnaDipilih = R.drawable.bg_time_blue
        highlightSelectedColor(colorBlue, colorPurple, colorGreen, colorOrange, colorPink, colorBlue)

        // Color Picker Clicks
        colorBlue.setOnClickListener {
            warnaDipilih = R.drawable.bg_time_blue
            highlightSelectedColor(colorBlue, colorPurple, colorGreen, colorOrange, colorPink, colorBlue)
        }
        colorPurple.setOnClickListener {
            warnaDipilih = R.drawable.bg_time_purple
            highlightSelectedColor(colorBlue, colorPurple, colorGreen, colorOrange, colorPink, colorPurple)
        }
        colorGreen.setOnClickListener {
            warnaDipilih = R.drawable.bg_time_green
            highlightSelectedColor(colorBlue, colorPurple, colorGreen, colorOrange, colorPink, colorGreen)
        }
        colorOrange.setOnClickListener {
            warnaDipilih = R.drawable.bg_time_orange
            highlightSelectedColor(colorBlue, colorPurple, colorGreen, colorOrange, colorPink, colorOrange)
        }
        colorPink.setOnClickListener {
            warnaDipilih = R.drawable.bg_time_pink
            highlightSelectedColor(colorBlue, colorPurple, colorGreen, colorOrange, colorPink, colorPink)
        }

        btnBatal.setOnClickListener { dialog.dismiss() }

        btnSimpan.setOnClickListener {
            val aktivitas = etMapel.text.toString().trim()
            val waktu = etWaktu.text.toString().trim()
            val lokasi = etRuang.text.toString().trim()
            val hariTerpilih = spinnerHari.selectedItem.toString()

            if (aktivitas.isEmpty() || waktu.isEmpty() || lokasi.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua field!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ UI DONE - Data siap dikirim ke logic/database temanmu
            Toast.makeText(this, "✅ '$aktivitas' untuk hari $hariTerpilih berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
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
}