package com.dev.android.meetme.auth

import andreasagap.loadingbutton.ButtonLoading
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.dev.android.meetme.MainActivity
import com.dev.android.meetme.R
import com.dev.android.meetme.databinding.ActivityLoginBinding
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    val auth=FirebaseAuth.getInstance()
    private var verificationID:String?=null
    private lateinit var sendButton: CircularProgressButton
    private lateinit var verifyButton: CircularProgressButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        
       sendButton=findViewById(R.id.SendOtpBtn)
        verifyButton=findViewById(R.id.BtnVerifyOtp)

        //SendOTP Button click
        binding.SendOtpBtn.setOnClickListener {
            val number=binding.userNumber.text.toString()
            if(number.isEmpty()){
                binding.userNumber.focusable
                binding.userNumber.error="Required"
            }else{
                sendOTP(number)
            }
        }

        //VerifyOTP Button click
        binding.BtnVerifyOtp.setOnClickListener {
            val otp=binding.userOTP.text.toString()
            if(otp.isEmpty()){
                binding.userOTP.error="Required"
            }else{
                verifyOTP(otp)
            }
        }

    }

    //Verify otp
    private fun verifyOTP(otp: String) {
//        binding.SendOtpBtn.showLoadingButton()
//        binding.SendOtpBtn.onStartLoading()
        verifyButton.startAnimation()
        val credential=PhoneAuthProvider.getCredential(verificationID!!,otp)
        signInWithPhoneAuthCredential(credential)
    }

    //Send otp
    private fun sendOTP(number: String) {
//         binding.SendOtpBtn.onStartLoading()
        sendButton.startAnimation()
        val callbacks=object:PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                 binding.SendOtpBtn.showNormalButton()
                sendButton.revertAnimation()
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e:FirebaseException) {
                 Toast.makeText(this@LoginActivity,"Verification Failed",Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationID: String, token: PhoneAuthProvider.ForceResendingToken)
            {
                this@LoginActivity.verificationID=verificationID
//                binding.SendOtpBtn.showNormalButton()
                sendButton.revertAnimation()
                binding.numberLayout.visibility=GONE
                binding.otpLayout.visibility= VISIBLE
            }
        }

        val options=PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$number")
            .setTimeout(40L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    //Sign with Phone
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
//                binding.SendOtpBtn.showNormalButton()
                sendButton.revertAnimation()
                if(it.isSuccessful){
                    checkUserExist(binding.userNumber.text.toString())
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                 }else{
                     Toast.makeText(this,it.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserExist(number: String) {
         FirebaseDatabase.getInstance().getReference("Users").child(number)
             .addValueEventListener(object:ValueEventListener{
                 override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        finish()
                    }else{
                        startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
                        finish()
                    }
                }

                 override fun onCancelled(error: DatabaseError) {
                     Toast.makeText(this@LoginActivity,error.message,Toast.LENGTH_SHORT).show()
                 }

             })
    }


}