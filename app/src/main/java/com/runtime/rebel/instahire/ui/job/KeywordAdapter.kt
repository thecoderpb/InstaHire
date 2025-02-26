package com.runtime.rebel.instahire.ui.job

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.runtime.rebel.instahire.R

class KeywordAdapter(private val keywords: List<String>) : RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder>() {

    class KeywordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chip: Chip = itemView.findViewById(R.id.chip_keyword)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chip, parent, false)
        return KeywordViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        holder.chip.text = keywords[position]
    }

    override fun getItemCount(): Int = keywords.size
}