package edu.bluejack23_2.demarj.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack23_2.demarj.R
import edu.bluejack23_2.demarj.activities.HistoryDetailActivity
import edu.bluejack23_2.demarj.activities.MainActivity
import edu.bluejack23_2.demarj.adapter.HistoryAdapter
import edu.bluejack23_2.demarj.databinding.FragmentHistoryBinding
import edu.bluejack23_2.demarj.databinding.FragmentHomeBinding
import edu.bluejack23_2.demarj.databinding.FragmentMyPoBinding
import edu.bluejack23_2.demarj.model.History
import edu.bluejack23_2.demarj.viewmodel.PreOrderViewModel
import edu.bluejack23_2.demarj.viewmodel.TransactionViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {

    private var _historyBinding: FragmentHistoryBinding? = null
    private val historyBinding get() = _historyBinding!!
    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter: HistoryAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _historyBinding = FragmentHistoryBinding.inflate(inflater, container, false)
        val view = historyBinding.root

        val activity = requireActivity() as MainActivity

        historyBinding.loadingBar.visibility = View.VISIBLE

        sharedPreferences = activity.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        val userId = sharedPreferences.getString("user_id", null)

        if (userId != null) {
            viewModel.fetchTransactionHistory(userId)

            viewModel.transactionHistory.observe(viewLifecycleOwner) { historyItems ->
                adapter = HistoryAdapter(historyItems)
                historyBinding.rvHistory.adapter = adapter
                historyBinding.rvHistory.layoutManager = LinearLayoutManager(context)
                historyBinding.rvHistory.setHasFixedSize(true)
                historyBinding.loadingBar.visibility = View.GONE

                adapter.setOnItemClickCallback(object : HistoryAdapter.IOnHistoryClickCallback {
                    override fun onHistoryClicked(data: History) {
                        val intentToDetail = Intent(requireActivity(), HistoryDetailActivity::class.java)
                        intentToDetail.putExtra("DATA", data)
                        startActivity(intentToDetail)
                    }
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _historyBinding = null
    }

}