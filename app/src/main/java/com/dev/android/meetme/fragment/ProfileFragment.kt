package com.dev.android.meetme.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.android.meetme.R
import com.dev.android.meetme.auth.LoginActivity
import com.dev.android.meetme.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(layoutInflater)

        binding.profileName.isEnabled=false
        binding.profileEmail.isEnabled=false
        binding.profileNumber.isEnabled=false
        binding.profileCity.isEnabled=false

        binding.txtLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(),LoginActivity::class.java))
        }
        return binding.root
    }


}