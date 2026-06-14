package com.example.e_vidya

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var btnSave: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_profile)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        edtName =
            findViewById(R.id.edtName)

        btnSave =
            findViewById(R.id.btnSave)

        loadProfile()

        btnSave.setOnClickListener {

            updateProfile()
        }
    }

    private fun loadProfile() {

        val uid =
            auth.currentUser?.uid ?: return

        firestore.collection("Users")
            .document(uid)
            .get()
            .addOnSuccessListener {

                edtName.setText(
                    it.getString("name")
                )
            }
    }

    private fun updateProfile() {

        val uid =
            auth.currentUser?.uid ?: return

        val name =
            edtName.text.toString().trim()

        firestore.collection("Users")
            .document(uid)
            .update(
                "name",
                name
            )
            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Profile Updated",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }
    }
}