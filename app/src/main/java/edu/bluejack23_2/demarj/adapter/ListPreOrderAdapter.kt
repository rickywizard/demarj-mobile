package edu.bluejack23_2.demarj.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack23_2.demarj.databinding.PreOrderCardBinding
import edu.bluejack23_2.demarj.model.PreOrderWithStore

class ListPreOrderAdapter(private val listPreOrderWithStore: List<PreOrderWithStore>): RecyclerView.Adapter<ListPreOrderAdapter.ListViewHolder>() {

    interface IOnPreOrderClickCallback {
        fun onPreOrderClicked(data: PreOrderWithStore)
    }

    private lateinit var onItemClickCallback: IOnPreOrderClickCallback

    fun setOnItemClickCallback(onItemClickCallback: IOnPreOrderClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(val binding: PreOrderCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = PreOrderCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listPreOrderWithStore.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder) {
            with(listPreOrderWithStore[position]) {
                Glide.with(holder.itemView.context)
                    .load(this.preOrder.po_img)
                    .into(binding.imgPreOrder)
                binding.tvStoreName.text = this.store.store_name
                binding.tvPOName.text = this.preOrder.po_name
                binding.tvPOPrice.text = this.preOrder.po_price.toString()
                binding.tvPOEndDate.text = this.preOrder.po_end_date
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onPreOrderClicked(listPreOrderWithStore[holder.adapterPosition])
        }

//        Log.d("AdapterBinding", "Binding data at position $position: $this")
    }

}