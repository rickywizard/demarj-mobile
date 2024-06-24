package edu.bluejack23_2.demarj.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack23_2.demarj.databinding.NotificationCardBinding
import edu.bluejack23_2.demarj.databinding.PreOrderCardBinding
import edu.bluejack23_2.demarj.model.NotifWithPOWithStore
import edu.bluejack23_2.demarj.model.PreOrderWithStore
import java.text.NumberFormat
import java.util.*

class NotificationAdapter(private val listNotifWithPO: List<NotifWithPOWithStore>): RecyclerView.Adapter<NotificationAdapter.ListViewHolder>() {

    fun formatToRupiah(amount: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return format.format(amount).replace("Rp", "Rp ").replace(",00", "")
    }

    interface IOnPreOrderClickCallback {
        fun onPreOrderClicked(data: NotifWithPOWithStore)
    }

    private lateinit var onItemClickCallback: IOnPreOrderClickCallback

    fun setOnItemClickCallback(onItemClickCallback: IOnPreOrderClickCallback) {
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
                    .load(this.po_with_store.store.profile_picture)
                    .into(binding.photoStoreNotif)
                binding.storeNameNotif.text = this.po_with_store.store.store_name
                binding.poNameNotif.text = this.po_with_store.preOrder.po_name
                binding.poPriceNotif.text = formatToRupiah(this.po_with_store.preOrder.po_price!!)
                binding.notifTime.text = this.notif.notifTime
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onPreOrderClicked(listNotifWithPO[holder.adapterPosition])
        }

//        Log.d("AdapterBinding", "Binding data at position $position: $this")
    }

}