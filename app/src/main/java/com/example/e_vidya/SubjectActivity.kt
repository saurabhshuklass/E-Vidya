package com.example.e_vidya

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SubjectActivity : AppCompatActivity() {

    private lateinit var edtSearch: EditText
    private lateinit var recyclerSubjects: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_subject)

        edtSearch =
            findViewById(R.id.edtSearch)

        recyclerSubjects =
            findViewById(R.id.recyclerSubjects)

        val subjectList = listOf(

            Subject(
                "Mathematics",
                R.drawable.math
            ),

            Subject(
                "Physics",
                R.drawable.physics
            ),

            Subject(
                "Chemistry",
                R.drawable.chemistry
            ),

            Subject(
                "Biology",
                R.drawable.biology
            ),

            Subject(
                "Computer",
                R.drawable.computer
            ),

            Subject(
                "English",
                R.drawable.english
            )
        )

        val filteredList =
            subjectList.toMutableList()

        val standardName =
            intent.getStringExtra("standard")
                ?: "9th Standard"

        val adapter =
            SubjectAdapter(
                this,
                filteredList,
                standardName
            )

        recyclerSubjects.layoutManager =
            GridLayoutManager(this, 2)

        recyclerSubjects.adapter =
            adapter

        edtSearch.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    filteredList.clear()

                    filteredList.addAll(

                        subjectList.filter {

                            it.name.contains(
                                s.toString(),
                                true
                            )
                        }
                    )

                    adapter.notifyDataSetChanged()
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {}
            }
        )
    }
}