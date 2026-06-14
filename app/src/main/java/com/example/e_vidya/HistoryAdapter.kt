package com.example.e_vidya

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(
    private val list: List<History>
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val txtSubject: TextView =
            view.findViewById(R.id.txtSubject)

        val txtScore: TextView =
            view.findViewById(R.id.txtScore)

        val txtDate: TextView =
            view.findViewById(R.id.txtDate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_history,
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

        val history = list[position]

        holder.txtSubject.text =
            history.subject

        holder.txtScore.text =
            "${history.score}/${history.total}  •  ${history.percentage}%"

        holder.txtDate.text =
            history.date
    }
}