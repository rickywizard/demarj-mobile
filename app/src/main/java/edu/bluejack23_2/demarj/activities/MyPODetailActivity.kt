package edu.bluejack23_2.demarj.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import edu.bluejack23_2.demarj.adapter.BuyerAdapter
import edu.bluejack23_2.demarj.adapter.HistoryAdapter
import edu.bluejack23_2.demarj.databinding.ActivityMyPodetailBinding
import edu.bluejack23_2.demarj.model.History
import edu.bluejack23_2.demarj.model.PreOrder
import edu.bluejack23_2.demarj.viewmodel.TransactionViewModel
import java.text.NumberFormat
import java.util.*

class MyPODetailActivity : AppCompatActivity() {

    private lateinit var viewModel: TransactionViewModel
    private lateinit var binding: ActivityMyPodetailBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: BuyerAdapter

    private fun formatToRupiah(amount: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return format.format(amount).replace("Rp", "Rp ").replace(",00", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyPodetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("DATA", PreOrder::class.java)
        } else {
            intent.getParcelableExtra<PreOrder>("DATA")
        }

        if (data != null) {
            consumeData(data)

            buttonListener(data)

            viewModel.fetchTransactionsWithUserByProductId(data.poId!!)

            viewModel.transactionsWithUser.observe(this) { buyers ->
                adapter = BuyerAdapter(buyers)
                binding.rvBuyer.adapter = adapter
                binding.rvBuyer.layoutManager = LinearLayoutManager(this)
                binding.rvBuyer.setHasFixedSize(true)
            }
        }
    }

    private fun consumeData(data: PreOrder) {
        Glide.with(this).load(data.po_img).into(binding.detailPoImage)
        binding.detailPoName.text = data.po_name
        binding.detailPoPrice.text = formatToRupiah(data.po_price!!)
        binding.detailPoDesc.text = data.po_desc
        binding.detailPoEndDate.text = data.po_end_date
        binding.detailPoReadyDate.text = data.po_ready_date

        Glide.with(this).load(sharedPreferences.getString("profile_picture", null)).into(binding.photoProfileDetailPo)
        binding.detailStoreNameText.text = sharedPreferences.getString("store_name", null)
    }

    private fun buttonListener(data: PreOrder) {
        binding.btnEdit.setOnClickListener {
            val intent= Intent(this, MyPODetailActivity::class.java)
            intent.putExtra("DATA", data)
            startActivity(intent)
        }

        binding.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete Confirmation")
            builder.setMessage("\n" +
                    "Are you sure you want to delete?")

            builder.setPositiveButton("Ok") { dialog, _ ->
                onConfirmedDelete(data)
                dialog.dismiss()
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun onConfirmedDelete(data: PreOrder) {

    }
}