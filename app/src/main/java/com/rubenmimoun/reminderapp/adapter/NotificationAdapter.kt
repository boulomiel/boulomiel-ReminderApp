package com.rubenmimoun.reminderapp.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.rubenmimoun.reminderapp.Notif
import com.rubenmimoun.reminderapp.R
import com.rubenmimoun.reminderapp.database.Database


@RequiresApi(Build.VERSION_CODES.P)
class NotificationAdapter( private val list: ArrayList<Notif>) : RecyclerView.Adapter<NotificationAdapter.NotifViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifViewHolder {
        val inflater = LayoutInflater.from(parent.context)


        return NotifViewHolder(inflater,parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder:NotifViewHolder, position: Int) {
        val notif = list[position]

        holder.bind(notif)
    }


    class NotifViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item, parent, false)) {

        private var dateView: TextView? = null
        private var timeView: TextView? = null
        private var todo : TextView? =null


        init {
            dateView = itemView.findViewById(R.id.date_item)
            timeView = itemView.findViewById(R.id.time_item)
            todo = itemView.findViewById(R.id.todo_item)
        }

        fun bind(notif: Notif) {
            dateView?.text = notif.date
            timeView?.text = notif.time
            todo?.text = notif.todo
        }

    }



}