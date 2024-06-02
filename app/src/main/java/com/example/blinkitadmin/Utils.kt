package com.example.blinkitadmin

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.blinkitadmin.databinding.ProgressDialogBinding
import com.google.firebase.auth.FirebaseAuth

object Utils {

    private var dialog: AlertDialog? = null
    private var firebaseAuth: FirebaseAuth? = null

    fun showDialog(context: Context, message: String) {
        val progress = ProgressDialogBinding.inflate(LayoutInflater.from(context))
        progress.tvMessage.text = message
        dialog = AlertDialog.Builder(context).setView(progress.root).setCancelable(false).create()
        dialog!!.show()
    }

    fun hideDialog() {
        dialog?.dismiss()
    }

    fun showToast(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getAuthInstance(): FirebaseAuth {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance()
        }
        return firebaseAuth!!
    }

    fun getCurrentUserId(): String {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            return uid
        } else {
            return ""
        }
    }


}