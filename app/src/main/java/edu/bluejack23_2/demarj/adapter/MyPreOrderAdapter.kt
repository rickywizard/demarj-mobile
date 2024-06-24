package edu.bluejack23_2.demarj.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack23_2.demarj.databinding.MyPoCardBinding
import edu.bluejack23_2.demarj.model.PreOrder

class MyPreOrderAdapter(private val listMyPO: List<PreOrder>): RecyclerView.Adapter<MyPreOrderAdapter.ListViewHolder>() {

    inner class ListViewHolder(val binding: MyPoCardBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = MyPoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listMyPO.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder) {
            with(listMyPO[position]) {
                Glide.with(holder.itemView.context)
                    .load(this.po_img)
                    .into(binding.imgMyPO)
                binding.tvPOName.text = this.po_name
                binding.tvPOEndDate.text = this.po_end_date
            }
        }
    }
}