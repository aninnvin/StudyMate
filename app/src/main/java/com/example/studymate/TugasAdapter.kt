package com.example.studymate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TugasAdapter(private var list: List<TugasData>) : RecyclerView.Adapter<TugasAdapter.ViewHolder>() {

    // ViewHolder: Reference ke semua view di item_tugas.xml
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layoutIcon: LinearLayout = view.findViewById(R.id.layoutIconContainer)
        val tvIkon: TextView = view.findViewById(R.id.tvIkonTugas)
        val tvJudul: TextView = view.findViewById(R.id.tvJudulTugas)
        val tvDeskripsi: TextView = view.findViewById(R.id.tvDeskripsiTugas)
        val tvDeadline: TextView = view.findViewById(R.id.tvDeadline)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tugas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        // Set teks
        holder.tvJudul.text = data.judul
        holder.tvDeskripsi.text = data.deskripsi
        holder.tvDeadline.text = "📅 ${data.deadline}"

        // Set icon (kalau kosong, pakai default)
        holder.tvIkon.text = if (data.ikon.isEmpty()) "📄" else data.ikon

        // ✅ Set background warna sesuai mapel (dari data)
        holder.layoutIcon.setBackgroundResource(data.warnaBackground)

        // Set Status & styling
        if (data.status == "Selesai") {
            holder.tvStatus.text = "Selesai"
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.status_selesai_text))
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_selesai)
            holder.tvJudul.alpha = 0.5f // Samarkan judul kalau udah selesai
        } else {
            holder.tvStatus.text = "Belum Selesai"
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.status_belum_text))
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_belum)
            holder.tvJudul.alpha = 1f
        }
    }

    override fun getItemCount(): Int = list.size

    // Fungsi untuk update data (dipanggil saat filter berubah)
    fun updateData(newData: List<TugasData>) {
        list = newData
        notifyDataSetChanged()
    }
}