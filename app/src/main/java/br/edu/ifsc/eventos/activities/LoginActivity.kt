package br.edu.ifsc.eventos.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.edu.ifsc.eventos.R
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import android.content.Intent
import android.R.attr.data
import android.graphics.Paint
import android.widget.Toast
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import javax.security.auth.callback.Callback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import com.facebook.login.widget.LoginButton
import android.widget.TextView
import android.graphics.Paint.UNDERLINE_TEXT_FLAG
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private var googleApiClient: GoogleApiClient? = null
    private var signInButton: SignInButton? = null
    private var loginButton: LoginButton? = null
    val SIGN_IN_CODE = 777;
    var callbackManager = CallbackManager.Factory.create();
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        progressBar =findViewById(R.id.progressBar4)


        getSupportActionBar()!!.setTitle("Login")

        // Google Login
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        signInButton = findViewById(R.id.signInButton);
        signInButton!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
                startActivityForResult(intent, SIGN_IN_CODE)
            }
        });

        // Facebook Login
        loginButton = findViewById<View>(R.id.login_button) as LoginButton

        // Callback registration
        loginButton?.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                goMainScreen();
            }

            override fun onCancel() {
            }

            override fun onError(exception: FacebookException) {
                System.out.println(exception)
            }
        })

        val tv = findViewById<View>(R.id.textView) as TextView
        tv.setPaintFlags(tv.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        tv.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View) {
                goMainScreen()
            }
        })


        txtLoginEmail.setOnClickListener{
            progressBar.visibility=View.VISIBLE
            val intent = Intent(this, EmailLoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility=View.GONE

    }
    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this, "Falha na conexão.", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SIGN_IN_CODE){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        if(result.isSuccess()) {
            goMainScreen();
        } else {
            Toast.makeText(this, getString(R.string.not_log_in), Toast.LENGTH_SHORT).show()
        }
    }

    private fun goMainScreen() {
        val intent = Intent(this, SelectEventActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}