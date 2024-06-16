package edu.bluejack23_2.demarj.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import edu.bluejack23_2.demarj.R
import edu.bluejack23_2.demarj.activities.MainActivity
import edu.bluejack23_2.demarj.databinding.FragmentProfileBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private var _profileBinding: FragmentProfileBinding? = null
    private val profileBinding get() = _profileBinding!!
    private lateinit var sharedPreferences: SharedPreferences

    private fun loadProfileData() {
        val fullname = sharedPreferences.getString("fullname", "Full Name")
        val email = sharedPreferences.getString("email", "Email")
        val phoneNumber = sharedPreferences.getString("phone_number", "Phone Number")
        val profilePicture = sharedPreferences.getString("profile_picture", "")

        profileBinding.userFullname.text = fullname
        profileBinding.userEmail.text = email
        profileBinding.userPhoneNumber.text = phoneNumber

        if (!profilePicture.isNullOrEmpty()) {
            Glide.with(this).load(profilePicture).into(profileBinding.imgProfile)
        } else {
            profileBinding.imgProfile.setImageResource(R.drawable.dummy_profile)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _profileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = profileBinding.root

        val activity = requireActivity() as MainActivity
        activity.binding.header.visibility = View.GONE

        sharedPreferences = activity.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        loadProfileData()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _profileBinding = null
    }

}