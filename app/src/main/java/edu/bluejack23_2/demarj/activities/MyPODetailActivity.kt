package edu.bluejack23_2.demarj.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import edu.bluejack23_2.demarj.adapter.BuyerAdapter
import edu.bluejack23_2.demarj.adapter.HistoryAdapter
import edu.bluejack23_2.demarj.databinding.ActivityMyPodetailBinding
import edu.bluejack23_2.demarj.model.History
import edu.bluejack23_2.demarj.model.PreOrder
import edu.bluejack23_2.demarj.model.TransactionWithUser
import edu.bluejack23_2.demarj.viewmodel.PreOrderViewModel
import edu.bluejack23_2.demarj.viewmodel.TransactionViewModel
import java.text.NumberFormat
import java.util.*

class MyPODetailActivity : AppCompatActivity() {

    private lateinit var viewModel: TransactionViewModel
    private lateinit var preOrderViewModel: PreOrderViewModel
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
        preOrderViewModel = ViewModelProvider(this).get(PreOrderViewModel::class.java)

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
                if (buyers.isEmpty()) {
                    binding.tvNoData.visibility = View.VISIBLE
                }
                else {
                    adapter = BuyerAdapter(this, buyers)
                    binding.rvBuyer.adapter = adapter
                    binding.rvBuyer.layoutManager = LinearLayoutManager(this)
                    binding.rvBuyer.setHasFixedSize(true)
                    binding.tvNoData.visibility = View.GONE

                    adapter.setOnItemClickCallback(object : BuyerAdapter.IOnBuyerClickCallback {

                        override fun onProofClicked(data: TransactionWithUser) {
                            openPopUp(data.transaction.img_proof!!)
                        }

                        override fun onPaidChecked(data: TransactionWithUser, checked: Boolean, context: Context) {
                            viewModel.updatePaidStatus(data.transaction.transactionId!!, checked) { isSuccess, message ->
                                if (isSuccess) {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Update payment status failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onTakenChecked(data: TransactionWithUser, checked: Boolean, context: Context) {
                            viewModel.updateTakenStatus(data.transaction.transactionId!!, checked) { isSuccess, message ->
                                if (isSuccess) {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Update taken status failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    })
                }
            }

            viewModel.totalPrice.observe(this) { totalPrice ->
                binding.tvIncome.text = formatToRupiah(totalPrice)
            }
        }
    }

    private fun openPopUp(image: String) {
        val builder = Dialog(this)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.setOnDismissListener {  }

        val cardView = CardView(this)
        cardView.cardElevation = 4F
        cardView.radius = 16F

        val imageView = ImageView(this)
        Glide.with(this).load(image).fitCenter().override(1000, 1600).into(imageView)

        cardView.addView(imageView)

        val layout = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        builder.addContentView(cardView, layout)
        builder.show()
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
            val intent= Intent(this, UpdatePOActivity::class.java)
            intent.putExtra("DATA", data)
            startActivity(intent)
        }

        binding.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete Confirmation")
            builder.setMessage("\n" +
                    "Are you sure you want to delete Pre Order?")

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
        preOrderViewModel.deleteStatus.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show()
            }
        }

        preOrderViewModel.deleteProduct(data.poId!!)
    }
}