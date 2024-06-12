package edu.bluejack23_2.demarj.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.bluejack23_2.demarj.R
import edu.bluejack23_2.demarj.activities.MainActivity
import edu.bluejack23_2.demarj.databinding.FragmentHomeBinding

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

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeBinding = null
    }

}