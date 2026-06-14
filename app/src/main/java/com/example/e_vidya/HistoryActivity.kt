package com.example.e_vidya

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerHistory: RecyclerView
    private lateinit var txtNoHistory: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val historyList =
        mutableListOf<History>()

    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_history)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        txtNoHistory =
            findViewById(R.id.txtNoHistory)

        recyclerHistory =
            findViewById(R.id.recyclerHistory)

        adapter =
            HistoryAdapter(historyList)

        recyclerHistory.layoutManager =
            LinearLayoutManager(this)

        recyclerHistory.adapter =
            adapter

        loadHistory()
    }

    private fun loadHistory() {

        val uid =
            auth.currentUser?.uid ?: return

        firestore.collection("QuizHistory")
            .whereEqualTo(
                "userId",
                uid
            )
            .get()
            .addOnSuccessListener { documents ->

                historyList.clear()

                for (document in documents) {

                    val history =
                        document.toObject(
                            History::class.java
                        )

                    historyList.add(history)
                }

                historyList.reverse()

                adapter.notifyDataSetChanged()

                if (historyList.isEmpty()) {

                    txtNoHistory.visibility =
                        View.VISIBLE

                } else {

                    txtNoHistory.visibility =
                        View.GONE
                }
            }
    }
}