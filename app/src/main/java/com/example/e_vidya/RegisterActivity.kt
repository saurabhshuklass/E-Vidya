package com.example.e_vidya

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText

    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView
    private lateinit var checkTerms: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)

        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)
        checkTerms = findViewById(R.id.checkTerms)

        btnRegister.setOnClickListener {

            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (name.isEmpty()) {
                etName.error = "Enter Name"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                etEmail.error = "Enter Email"
                return@setOnClickListener
            }

            if (password.length < 6) {
                etPassword.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(
                    this,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!checkTerms.isChecked) {
                Toast.makeText(
                    this,
                    "Please accept Terms & Conditions",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            btnRegister.isEnabled = false

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val uid = auth.currentUser!!.uid

                        val userData = hashMapOf(
                            "name" to name,
                            "email" to email,
                            "totalQuiz" to 0,
                            "highestScore" to 0
                        )

                        firestore.collection("Users")
                            .document(uid)
                            .set(userData)
                            .addOnSuccessListener {

                                Toast.makeText(
                                    this,
                                    "Account Created Successfully",
                                    Toast.LENGTH_LONG
                                ).show()

                                auth.signOut()

                                startActivity(
                                    Intent(
                                        this,
                                        LoginActivity::class.java
                                    )
                                )

                                finish()
                            }
                            .addOnFailureListener { e ->

                                btnRegister.isEnabled = true

                                Toast.makeText(
                                    this,
                                    "Firestore Error: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                    } else {

                        btnRegister.isEnabled = true

                        Toast.makeText(
                            this,
                            "Auth Error: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        tvLogin.setOnClickListener {
            finish()
        }
    }
}