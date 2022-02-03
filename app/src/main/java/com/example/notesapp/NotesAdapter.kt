package com.example.notesapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmResults

class NotesAdapter(private var context: Context, private var notesList: RealmResults<Notes>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!) {
        val title: TextView = itemView!!.findViewById(R.id.titleTv)
        val desc: TextView = itemView!!.findViewById(R.id.descTV)
        val time: TextView = itemView!!.findViewById(R.id.timeTV)
        val id : TextView = itemView!!.findViewById(R.id.idTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.notes_rv_layout, parent, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.titleTv).text = notesList[position]!!.title
        holder.itemView.findViewById<TextView>(R.id.descTV).text = notesList[position]!!.description

//        if (notesList[position]!!.description?.lines()?.count()?.compareTo(4) == 1) {
//            holder.itemView.findViewById<TextView>(R.id.descTV).text = "${notesList[position]!!.description} ..."
//        }

        holder.itemView.findViewById<TextView>(R.id.idTv).text = notesList[position]!!.id.toString()
        holder.itemView.findViewById<TextView>(R.id.timeTV).text = notesList[position]!!.time

        //For opening separate page for item.
        holder.itemView.findViewById<CardView>(R.id.card).setOnClickListener {
            val intent = Intent(context, NoteOpen::class.java)
            intent.putExtra("title", notesList[position]!!.title.toString())
            intent.putExtra("description", notesList[position]!!.description.toString())
            intent.putExtra("time", notesList[position]!!.time.toString())
            context.startActivity(intent)
        }

        //for deleting the note.
        holder.itemView.findViewById<CardView>(R.id.card).setOnLongClickListener {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setMessage("You sure you want to delete it?")
                .setTitle("Delete Note!!")
                .setPositiveButton("Yes, Delete"){_,_->
                    val realm = Realm.getDefaultInstance()
                    try {
                        realm.beginTransaction()
                        notesList[position]?.deleteFromRealm()
                        notifyDataSetChanged()
                        realm.commitTransaction()
                    } catch (e : Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            alertDialog.show()
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return notesList.size
    }



}