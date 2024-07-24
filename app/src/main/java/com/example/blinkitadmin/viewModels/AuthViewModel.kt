package com.example.blinkitadmin.viewModels

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.blinkitadmin.model.Admin
import com.example.blinkitadmin.utils.Utils
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class AuthViewModel: ViewModel() {

    private val _isSignedInSuccessfully = MutableStateFlow<Boolean>(false)
    val isSignedInSuccessfully = _isSignedInSuccessfully

    private val _isSignedUpSuccessfully = MutableStateFlow<Boolean>(false)
    val isSignedUpSuccessfully = _isSignedUpSuccessfully

    private val _isCurrentUser = MutableStateFlow<Boolean>(false)
    val isCurrentUser = _isCurrentUser

    init {
        Utils.getAuthInstance().currentUser?.let {
            _isCurrentUser.value = true
        }
    }

    fun signInWithCredentials(email: String, password: String, context: Context) {
        Utils.getAuthInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    Utils.showToast(context, "LoggedIn Successfully")
                    isSignedInSuccessfully.value = true
                }else{
                    Utils.showToast(context, "Wrong Credentials")
                }
            }
    }

    fun signUpWithCredentials(email: String, password: String, admin: Admin, context: Context){
        Utils.getAuthInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                admin.uid = Utils.getCurrentUserId()
                if(task.isSuccessful){
                    FirebaseDatabase.getInstance().getReference("Admins").child("AdminInfo").child(admin.uid!!).setValue(admin)
                    isSignedUpSuccessfully.value = true
                }else{
                    Utils.showToast(context, "Admin Registered Unsuccessfully")
                }
            }
    }
}