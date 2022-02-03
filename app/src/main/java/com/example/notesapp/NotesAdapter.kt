package com.example.notesapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults

class NotesAdapter(private var context: Context, private var notesList: RealmResults<Notes>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!) {
        val title: TextView = itemView!!.findViewById(R.id.titleTv)
        val desc: TextView = itemView!!.findViewById(R.id.descTV)
        val id : TextView = itemView!!.findViewById(R.id.idTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.notes_rv_layout, parent, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.titleTv).text = notesList[position]!!.title
        holder.itemView.findViewById<TextView>(R.id.descTV).text = notesList[position]!!.description
        holder.itemView.findViewById<TextView>(R.id.idTv).text = notesList[position]!!.id.toString()
    }

    override fun getItemCount(): Int {
        return notesList.size
    }



}