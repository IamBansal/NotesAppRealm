package com.example.notesapp

import android.annotation.SuppressLint
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
import com.google.firebase.database.FirebaseDatabase

class CustomCloudAdapter(private var context: Context, private var data : ArrayList<CustomCloud>) : RecyclerView.Adapter<CustomCloudAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleTv)
        val desc: TextView = itemView.findViewById(R.id.descTV)
        val time: TextView = itemView.findViewById(R.id.timeTV)
//        val id : TextView = itemView.findViewById(R.id.idTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_rv_layout, parent, false))

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        val keyId = /.

        holder.itemView.findViewById<TextView>(R.id.titleTv).text = data[position].Title
        holder.itemView.findViewById<TextView>(R.id.descTV).text = data[position].Description
//        holder.itemView.findViewById<TextView>(R.id.idTv).text = data[position].id.toString()
        holder.itemView.findViewById<TextView>(R.id.timeTV).text = data[position].Time

        //For opening separate page for item.
        holder.itemView.findViewById<CardView>(R.id.card).setOnClickListener {
            val intent = Intent(context, NoteOpen::class.java)
            intent.putExtra("title", data[position].Title)
            intent.putExtra("description", data[position].Description)
            intent.putExtra("time", data[position].Time)
            context.startActivity(intent)
        }

        //For deleting the note.
        holder.itemView.findViewById<CardView>(R.id.card).setOnLongClickListener {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setMessage("You sure you want to delete it?")
                .setTitle("Delete Note!!")
                .setPositiveButton("Yes, Delete"){_,_->
                    FirebaseDatabase.getInstance().reference.child("Notes")
                        .child(position.toString()).removeValue().addOnSuccessListener {
                                Toast.makeText(context, "Deleted. ${position}", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    notifyDataSetChanged()
                }
            alertDialog.show()
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}