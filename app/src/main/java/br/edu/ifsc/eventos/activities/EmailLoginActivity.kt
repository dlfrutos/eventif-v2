package br.edu.ifsc.eventos.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import br.edu.ifsc.eventos.R
import br.edu.ifsc.eventos.R.id.txtUser
import com.google.firebase.auth.FirebaseAuth

class EmailLoginActivity : AppCompatActivity() {

    private lateinit var txtUser: EditText
    private lateinit var txtPassword2: EditText

    private lateinit var progressBar2: ProgressBar

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtUser=findViewById(R.id.txtUser)
        txtPassword2=findViewById(R.id.txtPassword2)
        progressBar2 = findViewById(R.id.progressBar2)
        auth= FirebaseAuth.getInstance()
    }

    fun forgotPassword(view:View){
        startActivity(Intent(this,ForgotPasswordActivity::class.java))
    }

    fun register(view:View){
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    fun login(view:View){
        loginUser()
    }

    private fun loginUser(){
        val user:String=txtUser.text.toString()
        val password:String=txtPassword2.text.toString()

        if(!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)  ){
            progressBar2.visibility=View.VISIBLE

            auth.signInWithEmailAndPassword(user, password)
                    .addOnCompleteListener(this){
                        task ->
                        if(task.isSuccessful){
                            action()
                        }else{
                            Toast.makeText(this, "Erro no login.", Toast.LENGTH_LONG).show()
                        }
                    }
        }

    }

    private fun action(){
        startActivity(Intent(this, SelectEventActivity::class.java))
    }
}
