package com.example.e_vidya

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerStandard: RecyclerView

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var txtWelcome: TextView
    private lateinit var txtTotalQuiz: TextView
    private lateinit var txtHighestScore: TextView

    private lateinit var imgProfile: ImageView
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        txtWelcome = findViewById(R.id.txtWelcome)
        txtTotalQuiz = findViewById(R.id.txtTotalQuiz)
        txtHighestScore = findViewById(R.id.txtHighestScore)

        recyclerStandard = findViewById(R.id.recyclerStandard)

        imgProfile = findViewById(R.id.imgProfile)

        bottomNavigation =
            findViewById(R.id.bottomNav)

        imgProfile.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ProfileActivity::class.java
                )
            )
        }

        bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> {
                    true
                }

                R.id.nav_history -> {

                    startActivity(
                        Intent(
                            this,
                            HistoryActivity::class.java
                        )
                    )

                    true
                }

                R.id.nav_profile -> {

                    startActivity(
                        Intent(
                            this,
                            ProfileActivity::class.java
                        )
                    )

                    true
                }

                else -> false
            }
        }

        loadUserData()

        val standardList = listOf(

            Standard("9th Standard"),
            Standard("10th Standard"),
            Standard("11th Standard"),
            Standard("12th Standard")

        )

        recyclerStandard.layoutManager =
            GridLayoutManager(this, 2)

        recyclerStandard.adapter =
            StandardAdapter(
                this,
                standardList
            )
    }

    override fun onDestroy() {
        super.onDestroy()

        val prefs =
            getSharedPreferences(
                "EVidyaPrefs",
                MODE_PRIVATE
            )

        val remember =
            prefs.getBoolean(
                "rememberMe",
                false
            )

        if (!remember) {
            FirebaseAuth.getInstance()
                .signOut()
        }
    }

    private fun loadUserData() {

        val uid = auth.currentUser?.uid ?: return

        firestore.collection("Users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->

                val name =
                    document.getString("name")
                        ?: "Student"

                val totalQuiz =
                    document.getLong("totalQuiz")
                        ?: 0

                val highestScore =
                    document.getLong("highestScore")
                        ?: 0

                txtWelcome.text =
                    "Hello, $name 👋"

                txtTotalQuiz.text =
                    totalQuiz.toString()

                txtHighestScore.text =
                    "$highestScore%"
            }
    }

}
