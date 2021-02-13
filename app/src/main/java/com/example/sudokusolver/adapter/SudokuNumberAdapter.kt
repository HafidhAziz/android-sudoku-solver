package com.example.sudokusolver.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sudokusolver.R
import com.example.sudokusolver.data.SudokuNumber
import com.example.sudokusolver.databinding.ItemNumberBinding

/**
 * Created by M Hafidh Abdul Aziz on 13/02/21.
 */

class SudokuNumberAdapter(private var list: MutableList<SudokuNumber>) :
    RecyclerView.Adapter<SudokuNumberAdapter.NumberViewHolder>() {

    var listener: ClickItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_number, parent, false)
        return NumberViewHolder(view)
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        holder.listener = listener
        holder.bindItem(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class NumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var binding: ItemNumberBinding = DataBindingUtil.bind(itemView)!!
        var listener: ClickItemListener? = null

        fun bindItem(items: SudokuNumber) {
            binding.tvNumber.text = items.name
            itemView.setOnClickListener {
                listener?.onClickItemListener(items.name)
            }
        }
    }

    fun setClickItemListener(listener: ClickItemListener) {
        this.listener = listener
    }

    interface ClickItemListener {
        fun onClickItemListener(number: String)
    }
}