package edu.bluejack23_2.demarj.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack23_2.demarj.R
import edu.bluejack23_2.demarj.activities.MainActivity
import edu.bluejack23_2.demarj.adapter.MyPreOrderAdapter
import edu.bluejack23_2.demarj.databinding.FragmentMyPoBinding
import edu.bluejack23_2.demarj.viewmodel.PreOrderViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [MyPOFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyPOFragment : Fragment() {

    private var _myPOBinding: FragmentMyPoBinding? = null
    private val myPOBinding get() = _myPOBinding!!
    private lateinit var viewModel: PreOrderViewModel
    private lateinit var adapter: MyPreOrderAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _myPOBinding = FragmentMyPoBinding.inflate(inflater, container, false)
        val view = myPOBinding.root

        val activity = requireActivity() as MainActivity

        myPOBinding.loadingBar.visibility = View.VISIBLE

        sharedPreferences = activity.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(PreOrderViewModel::class.java)

        val storeId = sharedPreferences.getString("user_id", null)

//        Log.d("STOREID", "onViewCreated: $storeId")

        if (storeId != null) {
            viewModel.fetchPreOrderByStoreId(storeId)
            viewModel.preOrdersByStoreId.observe(viewLifecycleOwner) { preOrders ->
                adapter = MyPreOrderAdapter(preOrders)
                myPOBinding.rvMyPO.adapter = adapter
                myPOBinding.rvMyPO.layoutManager = GridLayoutManager(context, 2)
                myPOBinding.rvMyPO.setHasFixedSize(true)
                myPOBinding.loadingBar.visibility = View.GONE

//                Log.d("MYPO", "onViewCreated: $preOrders")
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _myPOBinding = null
    }

}