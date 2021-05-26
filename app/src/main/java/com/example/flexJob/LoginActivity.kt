package com.example.flexJob

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flexJob.databinding.LoginActivityBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var _binding: LoginActivityBinding
    private val reqCode: Int = 123
    private val firebaseAuth = FirebaseAuth.getInstance()
    var callBackManager: CallbackManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val forgetPass = _binding.textForgetPassActivityLogin
        val haveAccount = _binding.textHaveAccActivityLogin
        val btnLogin = _binding.BTNLoginActivityLogin
        val etLogin = _binding.ETLoginActivityLogin
        val etPassword = _binding.ETLoginActivityLogin
        val google = _binding.SignGoogleRegisterActivity
        val facebook = _binding.SignFacebookRegisterActivity

        forgetPass.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgetPassActivity::class.java))
        }
        haveAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
        btnLogin.setOnClickListener {
            val enterEmail: String = resources.getString(R.string.enter_email)
            val enterPassword: String = resources.getString(R.string.enter_password)
            val validEmailPassword: String = resources.getString(R.string.exception_login)

            when {
                TextUtils.isEmpty(etLogin.text.toString().trim { it <= ' ' }) -> {
                    etLogin.error = enterEmail
                    etLogin.requestFocus()
                    return@setOnClickListener
                }
                TextUtils.isEmpty(
                    etPassword.text.toString().trim { it <= ' ' }) -> {
                    etPassword.error = enterPassword
                    etPassword.requestFocus()
                    return@setOnClickListener
                }
                else -> {
                    val email: String = etLogin.text.toString().trim { it <= ' ' }
                    val password: String =
                        etLogin.text.toString().trim { it <= ' ' }

                    // Create an instance and create a register a user with email and password.
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            // If the registration is successfully done
                            if (task.isSuccessful) {

                                // Firebase register user
//                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    this@LoginActivity,
                                    R.string.Login_successful,
                                    Toast.LENGTH_SHORT
                                ).show()
//                              if all operation successfully, going to MainActivty
                                if (task.isSuccessful) {
                                    val intent =
                                        Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finish()

                                } else {
//                                If the login is not successful then show error message.
                                    Toast.makeText(
                                        this@LoginActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                etLogin.error = validEmailPassword
                                etLogin.requestFocus()
                                return@addOnCompleteListener
                            }
                        }
                }
            }
        }

        /**
         * There started Google auth in login
         */
        // Configure Google Sign In inside onCreate method
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // getting the value of gso inside the GoogleSigninClient
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // initialize the firebaseAuth variable
        FirebaseAuth.getInstance()
        google.setOnClickListener {
            signInGoogle()
        }
        /**
         *  There End Google auth ...*/

        /**
         *  There start Facebook auth firebase*/
        callBackManager = CallbackManager.Factory.create()
        facebook.setReadPermissions(R.string.e_mail.toString())
        facebook.setOnClickListener {
            signin()
        }
        /**
         *  There end Facebook auth firebase
         *  */

    }

    /**
     * There start Google auth in Firebase
     * ...*/
    // signInGoogle() function
    private fun signInGoogle() {

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, reqCode)
    }

    // onActivityResult() function : this is where we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Faceebook
        callBackManager?.onActivityResult(requestCode, resultCode, data)
        if (requestCode == reqCode) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    // handleResult() function -  this is where we update the UI after Google signin takes place
    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // UpdateUI() function - this is where we specify what UI updation are needed after google signin has taken place.
    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                SavedPreference.setEmail(this, account.email.toString())
                SavedPreference.setUsername(this, account.displayName.toString())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    /**
     * There end Google auth in Firebase     ...*/

    /**
     *  Facebook Auth Firebase Started*/
    private fun signin() {
        _binding.SignFacebookRegisterActivity.registerCallback(callBackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFaceBookAccessToken(result!!.accessToken)

            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {

            }
        })
    }

    private fun handleFaceBookAccessToken(accessToken: AccessToken?) {
        // get the credentials
        val credentials = FacebookAuthProvider.getCredential(accessToken!!.token)
        firebaseAuth.signInWithCredential(credentials)
            .addOnSuccessListener { result ->
                // get the email
                val email = result.user?.email
                Toast.makeText(this, "You logged in with $email", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }

    }
    /**
     * Facebook Auth Firebase Ended*/
}
