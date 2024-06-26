package edu.bluejack23_2.demarj.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack23_2.demarj.databinding.NotificationCardBinding
import edu.bluejack23_2.demarj.model.PreOrderWithStore
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class NotificationAdapter(private val listNotifWithPO: List<PreOrderWithStore>): RecyclerView.Adapter<NotificationAdapter.ListViewHolder>() {

    private fun formatToRupiah(amount: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return format.format(amount).replace("Rp", "Rp ").replace(",00", "")
    }

    fun formatDate(date: String): String {
        val dateTime = LocalDateTime.parse(date)
        val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh.mm a")
        return dateTime.format(outputFormatter)
    }

    interface IOnNotifClickCallback {
        fun onPreOrderClicked(data: PreOrderWithStore)
    }

    private lateinit var onItemClickCallback: IOnNotifClickCallback

    fun setOnItemClickCallback(onItemClickCallback: IOnNotifClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(val binding: NotificationCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = NotificationCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listNotifWithPO.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder) {
            with(listNotifWithPO[position]) {
                com.bumptech.glide.Glide.with(holder.itemView.context)
                    .load(this.store.profile_picture)
                    .into(binding.photoStoreNotif)
                binding.storeNameNotif.text = this.store.store_name
                binding.poNameNotif.text = this.preOrder.po_name
                binding.poPriceNotif.text = formatToRupiah(this.preOrder.po_price!!)
                binding.notifTime.text = formatDate(this.preOrder.po_created_at!!)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onPreOrderClicked(listNotifWithPO[holder.adapterPosition])
        }

//        Log.d("AdapterBinding", "Binding data at position $position: $this")
    }

}