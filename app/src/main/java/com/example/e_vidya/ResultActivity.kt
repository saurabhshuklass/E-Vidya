package com.example.e_vidya

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_result)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val txtSubject = findViewById<TextView>(R.id.txtSubject)
        val txtScore = findViewById<TextView>(R.id.txtScore)
        val txtPercentage = findViewById<TextView>(R.id.txtPercentage)
        val txtCorrect = findViewById<TextView>(R.id.txtCorrect)
        val txtWrong = findViewById<TextView>(R.id.txtWrong)

        val btnRetry = findViewById<Button>(R.id.btnRetry)
        val btnHome = findViewById<Button>(R.id.btnHome)
        val txtRemark = findViewById<TextView>(R.id.txtRemark)

        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 0)

        val subject =
            intent.getStringExtra("subject")
                ?: "Unknown"

        val standard =
            intent.getStringExtra("standard")
                ?: ""

        val percentage =
            (score * 100) / total

        txtSubject.text =
            "$standard • $subject"

        txtScore.text =
            "$score / $total"

        txtPercentage.text =
            "$percentage%"

        txtRemark.text =
            when {


                percentage >= 90 ->
                    "🏆 Excellent!"

                percentage >= 75 ->
                    "🎉 Very Good!"

                percentage >= 50 ->
                    "👍 Good Job!"

                else ->
                    "📚 Keep Practicing!"
            }

        if (percentage == 100) {

            txtRemark.text =
                "🏅 Perfect Score!"
        }

        txtCorrect.text =
            "✅ Correct : $score"

        txtWrong.text =
            "❌ Wrong : ${total - score}"

        saveQuizResult(
            subject,
            score,
            total,
            percentage
        )

        btnRetry.setOnClickListener {

            val retryIntent =
                Intent(
                    this,
                    QuizActivity::class.java
                )

            retryIntent.putExtra(
                "subjectName",
                subject
            )

            startActivity(retryIntent)
            finish()
        }

        btnHome.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )

            finish()
        }
    }

    private fun saveQuizResult(
        subject: String,
        score: Int,
        total: Int,
        percentage: Int
    ) {

        val uid =
            auth.currentUser?.uid ?: return

        val currentDate =
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).format(Date())

        val historyMap =
            hashMapOf(
                "userId" to uid,
                "subject" to subject,
                "score" to score,
                "total" to total,
                "percentage" to percentage,
                "date" to currentDate
            )

        firestore.collection("QuizHistory")
            .add(historyMap)
            .addOnSuccessListener {
                updateUserStats(
                    percentage
                )
            }
    }

    private fun updateUserStats(
        percentage: Int
    ) {

        val uid =
            auth.currentUser?.uid ?: return

        val userRef =
            firestore.collection("Users")
                .document(uid)

        userRef.get()
            .addOnSuccessListener { document ->

                val totalQuiz =
                    document.getLong("totalQuiz")
                        ?.toInt() ?: 0

                val highestScore =
                    document.getLong("highestScore")
                        ?.toInt() ?: 0

                val newHighest =
                    maxOf(
                        highestScore,
                        percentage
                    )

                userRef.update(
                    mapOf(
                        "totalQuiz" to totalQuiz + 1,
                        "highestScore" to newHighest
                    )
                )
            }
    }
}