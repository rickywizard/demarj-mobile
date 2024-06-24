package edu.bluejack23_2.demarj.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack23_2.demarj.R
import edu.bluejack23_2.demarj.activities.DetailPOActivity
import edu.bluejack23_2.demarj.activities.MainActivity
import edu.bluejack23_2.demarj.adapter.ListPreOrderAdapter
import edu.bluejack23_2.demarj.adapter.NotificationAdapter
import edu.bluejack23_2.demarj.databinding.FragmentHomeBinding
import edu.bluejack23_2.demarj.databinding.FragmentNotificationBinding
import edu.bluejack23_2.demarj.model.PreOrderWithStore
import edu.bluejack23_2.demarj.viewmodel.NotificationViewModel
import edu.bluejack23_2.demarj.viewmodel.PreOrderViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationFragment : Fragment() {

    private var _notificationBinding: FragmentNotificationBinding? = null
    private val notificationBinding get() = _notificationBinding!!
    private lateinit var viewModel: NotificationViewModel
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _notificationBinding = FragmentNotificationBinding.inflate(inflater, container, false)
        val view = notificationBinding.root

        val activity = requireActivity() as MainActivity
        activity.binding.header.visibility = View.VISIBLE
        activity.binding.tvPageTitle.text = "Notifications"
        activity.binding.tvPageSubtitle.text = "What notifications are there?"

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)

        viewModel.notifResult.observe(viewLifecycleOwner) { notif ->
            adapter = List(preOrdersWithStore)
            homeBinding.rvPreOrder.adapter = adapter
            homeBinding.rvPreOrder.layoutManager = LinearLayoutManager(context)
            homeBinding.rvPreOrder.setHasFixedSize(true)
            homeBinding.loadingBar.visibility = View.GONE

            adapter.setOnItemClickCallback(object : ListPreOrderAdapter.IOnPreOrderClickCallback {
                override fun onPreOrderClicked(data: PreOrderWithStore) {
                    val intentToDetail = Intent(requireActivity(), DetailPOActivity::class.java)
                    intentToDetail.putExtra("DATA", data)
                    startActivity(intentToDetail)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _notificationBinding = null
    }

}