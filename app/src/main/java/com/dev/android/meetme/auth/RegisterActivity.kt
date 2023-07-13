package com.dev.android.meetme.auth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.dev.android.meetme.activity.MainActivity
import com.dev.android.meetme.R
import com.dev.android.meetme.databinding.ActivityRegisterBinding
import com.dev.android.meetme.model.UserDataModel
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var btnDialog:CircularProgressButton

    private var imageUrl:Uri?=null

    private val selectImage=registerForActivityResult(ActivityResultContracts.GetContent()){
         imageUrl=it

        binding.userProfile.setImageURI(imageUrl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        btnDialog=findViewById(R.id.RegisterBtn)


        //Get Image from user
        binding.userProfile.setOnClickListener {
            selectImage.launch("image/*")
        }

        //Save Data
        binding.RegisterBtn.setOnClickListener {
            val name=binding.userName.text.toString()
            val email=binding.userEmail.text.toString()
            val city=binding.userCity.text.toString()
            validateUser(name,email,city)
            btnDialog.startAnimation()
        }
    }

    private fun validateUser(name: String, email: String, city: String) {
        if(name.isBlank()){
            binding.userName.error="Name required"
        }else if(email.isBlank()){
            binding.userEmail.error="Email required"
        }else if(city.isBlank()){
            binding.userCity.error="City required"
        }else if(!binding.termsAndCondition.isChecked){
            binding.termsAndCondition.error="Please check terms and Condition"
        }else if(imageUrl==null){
            Toast.makeText(this,"Please upload your profile",Toast.LENGTH_SHORT).show()
        }else{
            uploadImage()
        }
    }

    private fun uploadImage() {

        val storageRef=FirebaseStorage.getInstance().getReference("Profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("profile.jpg")

        storageRef.putFile(imageUrl!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { image->
                    storeData(image)
                }.addOnFailureListener {
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            }

    }

    private fun storeData(imageUrl: Uri) {
        val data=UserDataModel(
            name=binding.userName.text.toString(),
            email=binding.userEmail.text.toString(),
            city=binding.userCity.text.toString(),
            image = imageUrl.toString(),
            number = FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString()
        )

        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString())
            .setValue(data)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    btnDialog.revertAnimation()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    Toast.makeText(this,"Save Data Successfully",Toast.LENGTH_SHORT).show()
                }else{
                    btnDialog.revertAnimation()
                    Toast.makeText(this,task.exception!!.message,Toast.LENGTH_SHORT).show()
                }
            }
    }
}

