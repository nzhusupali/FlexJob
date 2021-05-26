package com.example.flexJob

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flexJob.databinding.DeleteUserActivityBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DeleteUserActivity : AppCompatActivity() {
    private lateinit var _binding: DeleteUserActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DeleteUserActivityBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val deleteAccount = _binding.BTNDeleteAccountActivity
        val deleteET = _binding.ETDeleteUserActivity

        deleteAccount.setOnClickListener {
            lateinit var mGoogleSignInClient: GoogleSignInClient

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

            val tag = "delete_user_activity"
            val user = Firebase.auth.currentUser!!
            val enterEmail: String = resources.getString(R.string.enter_email)
            val errorServer: String = resources.getString(R.string.Error_server)

            val email = deleteET.toString().trim()
            if (email.isEmpty()) {
                deleteET.error = enterEmail
                deleteET.requestFocus()
                return@setOnClickListener
            }

            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent =
                            Intent(this, MainActivity::class.java)
                        Toast.makeText(
                            this,
                            R.string.Acc_deleted,
                            Toast.LENGTH_LONG
                        ).show()
                        mGoogleSignInClient.signOut()
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            R.string.Error_server,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
