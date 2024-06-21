package edu.bluejack23_2.demarj.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack23_2.demarj.R
import edu.bluejack23_2.demarj.activities.MainActivity
import edu.bluejack23_2.demarj.adapter.ListPreOrderAdapter
import edu.bluejack23_2.demarj.databinding.FragmentHomeBinding
import edu.bluejack23_2.demarj.factory.PreOrderViewModelFactory
import edu.bluejack23_2.demarj.repository.PreOrderRepository
import edu.bluejack23_2.demarj.viewmodel.PreOrderViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var _homeBinding: FragmentHomeBinding? = null
    private val homeBinding get() = _homeBinding!!
    private lateinit var viewModel: PreOrderViewModel
    private lateinit var adapter: ListPreOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = homeBinding.root

        val activity = requireActivity() as MainActivity
        activity.binding.header.visibility = View.VISIBLE
        activity.binding.tvPageTitle.text = "Home"
        activity.binding.tvPageSubtitle.text = "What would you like to order?"

        homeBinding.loadingBar.visibility = View.VISIBLE

        // Initialize Repository
        val repository = PreOrderRepository()

        // Initialize ViewModel
        val viewModelFactory = PreOrderViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PreOrderViewModel::class.java)

        // Setup RecyclerView and Adapter
        setupRecyclerView()

        // Fetch data and observe
        observePreOrders()

        return view
    }

    private fun setupRecyclerView() {
        adapter = ListPreOrderAdapter(arrayListOf())
        homeBinding.rvPreOrder.adapter = adapter
        homeBinding.rvPreOrder.layoutManager = LinearLayoutManager(context)
        homeBinding.rvPreOrder.setHasFixedSize(true)


    }

    private fun observePreOrders() {
        viewModel.preOrders.observe(viewLifecycleOwner, Observer { preOrders ->
            preOrders?.let {
                Log.d("HomeFragment", "PreOrders received: $it")
                adapter.updateData(it)
            }
        })

        viewModel.storeNames.observe(viewLifecycleOwner, Observer { storeNames ->
            storeNames?.let {
                adapter.updateStoreNames(it)
            }
        })

        homeBinding.loadingBar.visibility = View.GONE

        viewModel.fetchAllPreOrders()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeBinding = null
    }

}