package edu.bluejack23_2.demarj.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import edu.bluejack23_2.demarj.R
import edu.bluejack23_2.demarj.databinding.ActivityDetailPoactivityBinding
import edu.bluejack23_2.demarj.model.PreOrderWithStore
import edu.bluejack23_2.demarj.model.Transaction
import edu.bluejack23_2.demarj.viewmodel.TransactionViewModel
import java.text.NumberFormat
import java.util.*

class DetailPOActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPoactivityBinding
    private lateinit var viewModel: TransactionViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private var quantity: Int = 1
    private var totalPrice: Int = 0
    private var isLarge: Boolean = false

    private fun formatToRupiah(amount: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return format.format(amount).replace("Rp", "Rp ").replace(",00", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailPoactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.detailBackBttn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val data = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("DATA", PreOrderWithStore::class.java)
        } else {
            intent.getParcelableExtra<PreOrderWithStore>("DATA")
        }

        if (data != null) {
            consumeData(data)

            quantityPriceListener(data)

            orderListener(data)
        }

    }

    private fun orderListener(data: PreOrderWithStore) {
        binding.btnOrder.setOnClickListener {
            val poId = data.preOrder.poId.toString()
            val userId = sharedPreferences.getString("user_id", null)
            val quantity = binding.tvQuantity.text.toString().toIntOrNull() ?: 0
            val notes = binding.detailNotes.text.toString()
            val total = totalPrice

            val transaction = Transaction(
                poId = poId,
                userId = userId,
                quantity = quantity,
                notes = notes,
                total_price = total,
                large = isLarge,
                paid = false
            )

            viewModel.addTransaction(transaction, data.preOrder.po_stock!!) { isSuccess, errorMessage ->
                if (isSuccess) {
                    Toast.makeText(this, "Order success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, errorMessage ?: "Order failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateViews(price: Int?, largePrice: Int?) {
        binding.tvQuantity.text = quantity.toString()
        totalPrice = if (isLarge) {
            quantity * price!! + largePrice!!
        } else {
            quantity * price!!
        }
        binding.tvTotalPrice.text = formatToRupiah(totalPrice)
    }

    private fun quantityPriceListener(data: PreOrderWithStore) {
        binding.minusBttn.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateViews(data.preOrder.po_price, data.preOrder.po_large_price)
            }
        }

        val stock = binding.detailPoStock.text.toString().toIntOrNull() ?: 0
        binding.plusBttn.setOnClickListener {
            if (quantity < stock) {
                quantity++
                updateViews(data.preOrder.po_price, data.preOrder.po_large_price)
            }
        }

        binding.cbLargeSize.setOnCheckedChangeListener { _, isChecked ->
            isLarge = isChecked
            updateViews(data.preOrder.po_price, data.preOrder.po_large_price)
        }
    }

    private fun consumeData(data: PreOrderWithStore) {
        Glide.with(this).load(data.preOrder.po_img).into(binding.detailPoImage)
        binding.detailPoName.text = data.preOrder.po_name
        binding.detailPoPrice.text = formatToRupiah(data.preOrder.po_price!!)
        binding.detailPoDesc.text = data.preOrder.po_desc
        binding.detailPoEndDate.text = data.preOrder.po_end_date
        binding.detailPoReadyDate.text = data.preOrder.po_ready_date
        binding.detailLargeSize.text = data.preOrder.po_large_price.toString()
        binding.detailPoStock.text = data.preOrder.po_stock.toString()

        totalPrice = data.preOrder.po_price!!
        binding.tvTotalPrice.text = formatToRupiah(totalPrice)

        if (!data.store.profile_picture.isNullOrEmpty()) {
            Glide.with(this).load(data.store.profile_picture).into(binding.photoProfileDetailPo)
        } else {
            binding.detailPoImage.setImageResource(R.drawable.dummy_profile)
        }

        binding.detailStoreNameText.text = data.store.store_name
    }
}