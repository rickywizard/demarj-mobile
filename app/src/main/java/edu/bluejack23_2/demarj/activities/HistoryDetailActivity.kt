package edu.bluejack23_2.demarj.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import edu.bluejack23_2.demarj.databinding.ActivityHistoryDetailBinding
import edu.bluejack23_2.demarj.model.History
import edu.bluejack23_2.demarj.viewmodel.TransactionViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class HistoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryDetailBinding
    private lateinit var viewModel: TransactionViewModel
    private lateinit var transactionId: String
    private val PICK_IMAGE_REQUEST = 1

    private fun formatToRupiah(amount: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return format.format(amount).replace("Rp", "Rp ").replace(",00", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("DATA", History::class.java)
        } else {
            intent.getParcelableExtra<History>("DATA")
        }

        if (data != null) {
            transactionId = data.transaction.transactionId!!

            consumeData(data)

            cancelListener(data)

            uploadProofListener()
        }
    }

    private fun uploadProofListener() {
        binding.btnUploadProof.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }

        viewModel.uploadResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Proof image uploaded successfully!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Failed to upload proof image.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cancelListener(data: History) {
        binding.btnCancel.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cancel Order Confirmation")
            builder.setMessage("\n" +
                    "Are you sure you want to cancel your order?")

            builder.setPositiveButton("Ok") { dialog, _ ->
                onConfirmedCancel(data)
                dialog.dismiss()
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun onConfirmedCancel(data: History) {
        viewModel.deleteTransaction(data.transaction.transactionId!!, data.preOrder.po_end_date!!)

        viewModel.deleteResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Transaction deleted successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to delete transaction.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun consumeData(data: History) {
        Glide.with(this).load(data.preOrder.po_img).into(binding.imgPO)
        binding.tvHistoryPOName.text = data.preOrder.po_name
        binding.tvHistoryPOPrice.text = formatToRupiah(data.preOrder.po_price!!)
        binding.tvHistoryPODesc.text = data.preOrder.po_desc
        binding.tvHistoryEndDate.text = data.preOrder.po_end_date
        binding.tvHistoryReadyDate.text = data.preOrder.po_ready_date

        Glide.with(this).load(data.store.profile_picture).into(binding.imgStore)
        binding.tvStore.text = data.store.store_name

        binding.tvHistoryQty.text = if (data.transaction.large == true) {
            data.transaction.quantity.toString()
        } else {
            data.transaction.quantity.toString() + " (large size)"
        }
        binding.tvHistoryPrice.text = formatToRupiah(data.transaction.total_price!!)
        binding.tvHistoryNotes.text = data.transaction.notes
        binding.tvHistoryOrderStatus.text = if (data.transaction.taken == true) {
            "Taken"
        } else {
            "Not Taken"
        }
        binding.tvHistoryPaymentStatus.text = if (data.transaction.paid == true) {
            "Paid"
        } else {
            "Unpaid"
        }

        val today = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val ready = dateFormat.parse(data.preOrder.po_ready_date!!)
        val end = dateFormat.parse(data.preOrder.po_end_date!!)

//        Log.d("END", "$end")
//        Log.d("READY", "$ready")
//        Log.d("TODAY", "$today")
//        Log.d("CEKUP", "${ready.before(today)}")

        if (ready != null) {
            binding.btnUploadProof.visibility = if (today.after(ready)) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        if (end != null) {
            binding.btnCancel.visibility = if (end.before(today)) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data!!
            viewModel.uploadProofImage(transactionId, imageUri)
        }
    }
}