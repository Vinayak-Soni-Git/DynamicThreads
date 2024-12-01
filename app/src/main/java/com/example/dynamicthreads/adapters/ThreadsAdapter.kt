package com.example.dynamicthreads.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicthreads.R
import com.example.dynamicthreads.models.ThreadItem

class ThreadsAdapter(private val context:Context, private val threadList:ArrayList<ThreadItem>): RecyclerView.Adapter<ThreadsAdapter.ViewHolder>() {
    
    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val threadTextView:TextView = itemView.findViewById(R.id.aThreadTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.thread_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return threadList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val threadItem = threadList[position]
        holder.threadTextView.text = threadItem.text
    }
    
    fun editItem(position:Int){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.edit_thread_layout)
        val editText:EditText = dialog.findViewById(R.id.editThreadEditText)
        val editButton:Button = dialog.findViewById(R.id.editButton)
        var threadItem = threadList[position]
        editText.setText(threadItem.text)
        dialog.setCanceledOnTouchOutside(false)
        
        editButton.setOnClickListener{
            threadItem.text = editText.text.toString()
            notifyItemChanged(position)
            dialog.dismiss()
            Toast.makeText(context, "Thread Item edited", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }
    
    fun removeAt(position:Int){
        threadList.removeAt(position)
        notifyItemRemoved(position)
        Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
    }
}