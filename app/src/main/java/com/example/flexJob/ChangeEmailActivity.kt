package com.example.flexJob

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flexJob.databinding.ChangeEmailActivityBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChangeEmailActivity : AppCompatActivity() {
    private lateinit var _binding: ChangeEmailActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ChangeEmailActivityBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val emailActivity = _binding.BTNChangeEmailActivity
        val oldEmail = _binding.ETOldLoginChangeEmailActivity
        val newEmail = _binding.ETNewEmailChangeEmailActivity


        emailActivity.setOnClickListener {
            when {
                TextUtils.isEmpty(
                    oldEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        R.string.text_old_email,
                        Toast.LENGTH_LONG
                    ).show()
                }
                TextUtils.isEmpty(
                    newEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        R.string.text_new_email,
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    val user = Firebase.auth.currentUser
                    val newEmail: String =
                        newEmail.text.toString().trim { it <= ' ' }
                    user!!.updateEmail(newEmail)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    R.string.email_update,
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this, SettingsActivity::class.java))
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                finish()
                            } else
                                Toast.makeText(
                                    this,
                                    R.string.Error_server,
                                    Toast.LENGTH_LONG
                                ).show()
                        }
                }
            }
        }
    }
}