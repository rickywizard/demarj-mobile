package edu.bluejack23_2.demarj.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack23_2.demarj.R
import edu.bluejack23_2.demarj.databinding.PreOrderCardBinding
import edu.bluejack23_2.demarj.model.PreOrder

class ListPreOrderAdapter(private val listPreOrder: ArrayList<PreOrder>): RecyclerView.Adapter<ListPreOrderAdapter.ListViewHolder>() {
    private var storeNames: Map<String, String> = emptyMap()

    inner class ListViewHolder(val binding: PreOrderCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = PreOrderCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listPreOrder.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder) {
            with(listPreOrder[position]) {
                Glide.with(holder.itemView.context)
                    .load(this.po_img)
                    .into(binding.imgPreOrder)
                binding.tvStoreName.text = storeNames[this.po_ownerId] ?: "Unknown Store"
                binding.tvPOName.text = this.po_name
                binding.tvPOPrice.text = this.po_price.toString()
                binding.tvPOEndDate.text = this.po_end_date
            }
        }

        Log.d("AdapterBinding", "Binding data at position $position: $this")
    }

    fun updateData(newListPreOrder: List<PreOrder>) {
        Log.d("AdapterUpdate", "Updating data: $newListPreOrder")
        listPreOrder.clear()
        listPreOrder.addAll(newListPreOrder)
        notifyDataSetChanged()
    }

    fun updateStoreNames(newStoreNames: Map<String, String>) {
        storeNames = newStoreNames
        notifyDataSetChanged()
    }
}