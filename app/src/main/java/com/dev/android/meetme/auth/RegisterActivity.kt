package com.dev.android.meetme.auth

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.dev.android.meetme.R
import com.dev.android.meetme.databinding.ActivityRegisterBinding
import com.google.firebase.database.DatabaseReference

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var databaseReference: DatabaseReference

    private var imageUri:Uri?=null

    private val selectImage=registerForActivityResult(ActivityResultContracts.GetContent()){
         imageUri=it

        binding.userProfile.setImageURI(imageUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        //Get Image from user
        binding.userProfile.setOnClickListener {
            selectImage.launch("image/*")
        }

        //Save Data
        binding.RegisterBtn.setOnClickListener {
            val name=binding.userName.text.toString()
            val email=binding.userEmail.text.toString()
            val city=binding.userCity.text.toString()

            ValidateUser(name,email,city)
        }
    }

    private fun ValidateUser(name: String, email: String, city: String) {
        if(name.isBlank()){
            binding.userName.error="Name required"
        }else if(email.isBlank()){
            binding.userEmail.error="Email required"
        }else if(city.isBlank()){
            binding.userCity.error="City required"
        }else if(!binding.termsAndCondition.isChecked){
            binding.termsAndCondition.error="Please check terms and Condition"
        }else if(imageUri==null){
            Toast.makeText(this,"Please upload your profile",Toast.LENGTH_SHORT).show()
        }
    }
}

