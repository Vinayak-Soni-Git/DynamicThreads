package com.example.dynamicthreads.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicthreads.Data.ThreadData
import com.example.dynamicthreads.R
import com.example.dynamicthreads.adapters.ThreadsAdapter
import com.example.dynamicthreads.models.ThreadItem
import com.example.dynamicthreads.utils.SwipeToDeleteCallback
import com.example.dynamicthreads.utils.SwipeToEditCallback

class ThreadsListActivity : AppCompatActivity() {
    private lateinit var threadList:ArrayList<ThreadItem>
    private lateinit var threadListDataRecyclerView: RecyclerView
    private lateinit var threadsAdapter:ThreadsAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_threads_list)
        
        threadList = ThreadData().getThreads()
        threadListDataRecyclerView = findViewById(R.id.threadDataListRecyclerView)
        threadsAdapter = ThreadsAdapter(this, threadList)
        
        threadListDataRecyclerView.layoutManager = LinearLayoutManager(this)
        threadListDataRecyclerView.adapter = threadsAdapter
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        threadListDataRecyclerView.addItemDecoration(dividerItemDecoration)

        val editSwipeHandler = object: SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = threadListDataRecyclerView.adapter as ThreadsAdapter
                adapter.editItem(viewHolder.adapterPosition)
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(threadListDataRecyclerView)

        val deleteSwipeHandler = object: SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = threadListDataRecyclerView.adapter as ThreadsAdapter
                adapter.removeAt(viewHolder.adapterPosition)

            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(threadListDataRecyclerView)
        
    }
}