package com.example.flexJob

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flexJob.databinding.ChangePasswordActivityBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var _binding: ChangePasswordActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ChangePasswordActivityBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val changePassword = _binding.BTNChangePasswordActivity
        val oldPassword = _binding.ETOldPassChangePasswordActivity
        val newPassword = _binding.ETNewPassChangePasswordActivity

        changePassword.setOnClickListener {
            when {
                TextUtils.isEmpty(
                    oldPassword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        R.string.text_old_password,
                        Toast.LENGTH_LONG
                    ).show()
                }
                TextUtils.isEmpty(
                    newPassword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        R.string.text_new_password,
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {
                    val user = Firebase.auth.currentUser
                    val newPassword: String =
                        _binding.ETNewPassChangePasswordActivity.text.toString().trim { it <= ' ' }
                    user!!.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    R.string.pass_update,
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