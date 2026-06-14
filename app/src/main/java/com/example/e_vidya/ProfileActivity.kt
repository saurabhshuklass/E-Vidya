package com.example.e_vidya

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.content.SharedPreferences
import android.widget.ImageView

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var imgProfile: ImageView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btnEditProfile: Button
    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtHighestScore: TextView
    private lateinit var txtTotalQuiz: TextView

    private lateinit var btnLogout: Button
    private lateinit var btnHistory: Button
    private lateinit var btnNotification: Button
    private lateinit var btnShare: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        txtName = findViewById(R.id.txtName)
        txtEmail = findViewById(R.id.txtEmail)
        txtHighestScore = findViewById(R.id.txtHighestScore)
        txtTotalQuiz = findViewById(R.id.txtTotalQuiz)
        imgProfile = findViewById(R.id.imgProfile)
        btnLogout = findViewById(R.id.btnLogout)
        btnHistory = findViewById(R.id.btnHistory)
        btnNotification = findViewById(R.id.btnNotification)
        btnShare = findViewById(R.id.btnShare)
        btnEditProfile = findViewById(R.id.btnEditProfile)

        loadProfile()

        btnLogout.setOnClickListener {

            sharedPreferences =
                getSharedPreferences(
                    "EVidyaPrefs",
                    MODE_PRIVATE
                )

            sharedPreferences.edit()
                .putBoolean(
                    "rememberMe",
                    false
                )
                .apply()

            auth.signOut()

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            finishAffinity()
        }

        btnHistory.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    HistoryActivity::class.java
                )
            )
        }

        btnNotification.setOnClickListener {

            Toast.makeText(
                this,
                "Coming Soon",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnShare.setOnClickListener {

            val shareIntent = Intent(Intent.ACTION_SEND)

            shareIntent.type = "text/plain"

            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Download E-Vidya Quiz App"
            )

            startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Share Via"
                )
            )
        }

        btnEditProfile.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    EditProfileActivity::class.java
                )
            )
        }
    }

    private fun loadProfile() {

        val uid = auth.currentUser?.uid ?: return

        firestore.collection("Users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->

                txtName.text =
                    document.getString("name")

                txtEmail.text =
                    document.getString("email")

                txtTotalQuiz.text =
                    (document.getLong("totalQuiz") ?: 0).toString()

                txtHighestScore.text =
                    (document.getLong("highestScore") ?: 0).toString() + "%"
            }
    }
}