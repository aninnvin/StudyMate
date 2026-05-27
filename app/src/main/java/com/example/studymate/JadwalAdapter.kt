package com.example.studymate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JadwalAdapter(private var list: List<JadwalData>) : RecyclerView.Adapter<JadwalAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvWaktu: TextView = view.findViewById(R.id.tvWaktu)
        val tvMapel: TextView = view.findViewById(R.id.tvMapel)
        val tvRuang: TextView = view.findViewById(R.id.tvRuang)
        val tvIkon: TextView = view.findViewById(R.id.tvIkon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jadwal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.tvWaktu.text = data.waktu
        holder.tvMapel.text = data.mapel
        holder.tvRuang.text = data.ruang
        holder.tvIkon.text = data.ikon

        // Set background warna jam
        holder.tvWaktu.setBackgroundResource(data.bgTimeRes)
    }

    override fun getItemCount(): Int = list.size

    // Fungsi untuk update data
    fun updateData(newData: List<JadwalData>) {
        list = newData
        notifyDataSetChanged()
    }
}