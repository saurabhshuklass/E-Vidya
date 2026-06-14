package com.example.e_vidya

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var tvForgot: TextView
    private lateinit var checkRemember: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        sharedPreferences =
            getSharedPreferences(
                "EVidyaPrefs",
                MODE_PRIVATE
            )

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
        tvForgot = findViewById(R.id.tvForgot)

        checkRemember =
            findViewById(R.id.checkRemember)

        btnLogin.setOnClickListener {

            val email =
                etEmail.text.toString().trim()

            val password =
                etPassword.text.toString().trim()

            if (email.isEmpty()) {

                etEmail.error = "Enter Email"
                return@setOnClickListener
            }

            if (password.isEmpty()) {

                etPassword.error = "Enter Password"
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(
                email,
                password
            ).addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    sharedPreferences.edit()
                        .putBoolean(
                            "rememberMe",
                            checkRemember.isChecked
                        )
                        .apply()

                    Toast.makeText(
                        this,
                        "Welcome to E-Vidya",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
                        )
                    )

                    finish()

                } else {

                    Toast.makeText(
                        this,
                        "Invalid Email or Password",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        tvRegister.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }

        tvForgot.setOnClickListener {

            val email =
                etEmail.text.toString().trim()

            if (email.isEmpty()) {

                Toast.makeText(
                    this,
                    "Enter Email First",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {

                        Toast.makeText(
                            this,
                            "Password Reset Link Sent",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .addOnFailureListener {

                        Toast.makeText(
                            this,
                            it.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        }
    }

}
