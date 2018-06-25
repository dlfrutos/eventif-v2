
package br.edu.ifsc.eventos.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import br.edu.ifsc.eventos.R
import com.google.firebase.auth.FirebaseAuth


class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var txtEmail: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        txtEmail=findViewById(R.id.txtEmail)
        auth = FirebaseAuth.getInstance()
    }

    fun send(view: View){
        val email = txtEmail.text.toString()

        if(!TextUtils.isEmpty(email)){
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(this){
                        task ->
                        if(task.isSuccessful){
                            startActivity(Intent(this, SelectEventActivity::class.java))
                        }else{
                            Toast.makeText(this, "Erro ao enviar email.", Toast.LENGTH_LONG).show()
                        }
                    }
        }

    }

}
