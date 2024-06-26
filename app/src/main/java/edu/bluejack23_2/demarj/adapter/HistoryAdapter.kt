package edu.bluejack23_2.demarj.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack23_2.demarj.databinding.HistoryCardBinding
import edu.bluejack23_2.demarj.databinding.PreOrderCardBinding
import edu.bluejack23_2.demarj.model.History
import edu.bluejack23_2.demarj.model.PreOrderWithStore
import java.text.NumberFormat
import java.util.*

class HistoryAdapter(private val listHistory: List<History>): RecyclerView.Adapter<HistoryAdapter.ListViewHolder>() {

    interface IOnHistoryClickCallback {
        fun onHistoryClicked(data: History)
    }

    private lateinit var onItemClickCallback: IOnHistoryClickCallback

    fun setOnItemClickCallback(onItemClickCallback: IOnHistoryClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(val binding: HistoryCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = HistoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listHistory.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder) {
            with(listHistory[position]) {
                binding.tvStoreName.text = this.store.store_name
                binding.tvPOName.text = this.preOrder.po_name
                binding.tvPaymentStatus.text = if (this.transaction.paid == true) {
                    "Paid"
                } else {
                    "Unpaid"
                }
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onHistoryClicked(listHistory[holder.adapterPosition])
        }

//        Log.d("AdapterBinding", "Binding data at position $position: $this")
    }

}