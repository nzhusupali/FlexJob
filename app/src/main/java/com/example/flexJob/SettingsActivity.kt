package com.example.flexJob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.flexJob.databinding.SettingsActivityBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsActivity : AppCompatActivity() {
    private lateinit var _binding: SettingsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = SettingsActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)

        val email = _binding.CNGLoginInfSettingsActivity
        val editProfile = _binding.editProfile
        val changePassword = _binding.BTNChangePasswordSettingsActivity
        val deleteAccount = _binding.BTNDeleteAccountSettingsActivity
        val notification = _binding.notification
        val darkTheme = _binding.darkTheme
        val language = _binding.language
        val exit = _binding.BTNExitSettingsActivity

        /**
         * When entering settings, it first checks if you are logged in. If so, the settings are started;
         * if not, go to LoginActivity.kt
         */
        /**
         * Check Auth status START
         */
        val checkAuth = Firebase.auth
        val checkAuthStatus = checkAuth.currentUser
        if (checkAuthStatus != null) {
            email.text = FirebaseAuth.getInstance().currentUser!!.email
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        /**
         * Check Auth status END
         */

        editProfile.setOnClickListener {
            startActivity(Intent(this, ChangeEmailActivity::class.java))
        }
        deleteAccount.setOnClickListener {
            val mAlertDialog = AlertDialog.Builder(this)
            mAlertDialog.setTitle(R.string.Dell_acc)
            mAlertDialog.setMessage(R.string.Sure_delete_acc)
            mAlertDialog.setPositiveButton(R.string.YesPositive) { _, _ ->
                startActivity(Intent(this, DeleteUserActivity::class.java))
            }
            mAlertDialog.setNegativeButton(R.string.NoNegative) { _, _ -> Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP }

            val nAlertDialog = mAlertDialog.create()
            nAlertDialog.show()
        }
        changePassword.setOnClickListener {
            val mAlertDialog = AlertDialog.Builder(this)
            mAlertDialog.setTitle(R.string.Change_pass)
            mAlertDialog.setMessage(R.string.sure_change_pass)
            mAlertDialog.setPositiveButton(R.string.YesPositive) { _, _ ->
                startActivity(Intent(this, ChangePasswordActivity::class.java))
            }
            mAlertDialog.setNegativeButton(R.string.NoNegative) { _, _ ->
                Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
            }
            val nAlertDialog = mAlertDialog.create()
            nAlertDialog.show()
        }

        /**
         * Exit account start
         */
        lateinit var mGoogleSignInClient: GoogleSignInClient
        val auth by lazy {
            FirebaseAuth.getInstance()
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        exit.setOnClickListener {
            val mAlertDialogInExitButton = AlertDialog.Builder(this)
            mAlertDialogInExitButton.setTitle(R.string.logOut)
            mAlertDialogInExitButton.setMessage(R.string.Sure_exit_acc)
            mAlertDialogInExitButton.setCancelable(false)
            mAlertDialogInExitButton.setPositiveButton(R.string.YesPositive) { _, _ ->
                FirebaseAuth.getInstance().signOut()
                mGoogleSignInClient.signOut()
                startActivity(Intent(this, MainActivity::class.java))
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            mAlertDialogInExitButton.setNegativeButton(R.string.NoNegative) { _, _ ->
                Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
            }
            val nAlertDialogExitButton = mAlertDialogInExitButton.create()
            nAlertDialogExitButton.show()
        }
        /**
         * Exit account end
         */
    }

}
