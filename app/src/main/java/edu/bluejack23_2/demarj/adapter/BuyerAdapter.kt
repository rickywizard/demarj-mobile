package edu.bluejack23_2.demarj.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack23_2.demarj.databinding.BuyerCardBinding
import edu.bluejack23_2.demarj.databinding.PreOrderCardBinding
import edu.bluejack23_2.demarj.model.PreOrderWithStore
import edu.bluejack23_2.demarj.model.TransactionWithUser

class BuyerAdapter(private val listTransactionWithUser: List<TransactionWithUser>): RecyclerView.Adapter<BuyerAdapter.ListViewHolder>() {

    inner class ListViewHolder(val binding: BuyerCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = BuyerCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listTransactionWithUser.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder) {
            with(listTransactionWithUser[position]) {
                Glide.with(holder.itemView.context)
                    .load(this.user.profile_picture)
                    .into(binding.imgProfile)
                binding.tvUsername.text = this.user.fullname
                binding.tvPhone.text = this.user.phone_number
                binding.tvEmail.text = this.user.email

                binding.cbTaken.isChecked = this.transaction.taken == true

                binding.cbPaid.visibility = if (this.transaction.img_proof == "") {
                    View.GONE
                } else {
                    View.VISIBLE
                }

                binding.tvQuantity.text = if (this.transaction.large == true) {
                    this.transaction.quantity.toString() + " (LARGE)"
                } else {
                    this.transaction.quantity.toString()
                }

                binding.tvNotes.text = this.transaction.notes

                binding.btnUploadProof.visibility = if (this.transaction.img_proof == "") {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        }

//        Log.d("AdapterBinding", "Binding data at position $position: $this")
    }

}