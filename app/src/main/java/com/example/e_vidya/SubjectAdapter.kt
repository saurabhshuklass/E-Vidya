package com.example.e_vidya

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SubjectAdapter(
    private val context: Context,
    private val list: List<Subject>,
    private val standardName: String
) : RecyclerView.Adapter<SubjectAdapter.ViewHolder>() {

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val imgSubject: ImageView =
            view.findViewById(R.id.imgSubject)

        val txtSubjectName: TextView =
            view.findViewById(R.id.txtSubjectName)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_subject,
                    parent,
                    false
                )

        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val subject = list[position]

        holder.txtSubjectName.text =
            subject.name

        holder.imgSubject.setImageResource(
            subject.image
        )

        holder.itemView.setOnClickListener {

            val intent =
                Intent(
                    context,
                    QuizActivity::class.java
                )

            intent.putExtra(
                "subjectName",
                subject.name
            )

            intent.putExtra(
                "standardName",
                standardName
            )

            context.startActivity(intent)
        }
    }
}