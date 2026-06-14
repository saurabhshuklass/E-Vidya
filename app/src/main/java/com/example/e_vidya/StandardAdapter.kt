package com.example.e_vidya

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StandardAdapter(
    private val context: Context,
    private val list: List<Standard>
) : RecyclerView.Adapter<StandardAdapter.ViewHolder>() {

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val txtStandard: TextView =
            view.findViewById(R.id.txtStandard)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(
                R.layout.item_standard,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val standard = list[position]

        holder.txtStandard.text = standard.name

        holder.itemView.setOnClickListener {

            val intent =
                Intent(
                    context,
                    SubjectActivity::class.java
                )

            intent.putExtra(
                "standard",
                standard.name
            )

            context.startActivity(intent)
        }
    }
}