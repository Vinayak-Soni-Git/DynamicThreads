package com.example.dynamicthreads

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicthreads.activities.ThreadsListActivity
import com.example.dynamicthreads.adapters.ThreadsAdapter
import com.example.dynamicthreads.models.ThreadItem
import com.example.dynamicthreads.models.ThreadPublishRequest
import com.example.dynamicthreads.models.ThreadRequest
import com.example.dynamicthreads.network.ThreadsApi
import com.example.dynamicthreads.viewmodels.ThreadsViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var threadsViewModel: ThreadsViewModel
    private lateinit var threadsAdapter: ThreadsAdapter
    private lateinit var threadsRecyclerView: RecyclerView
    private var threadList:ArrayList<ThreadItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        threadsViewModel = ViewModelProvider(this)[ThreadsViewModel::class.java]
        threadsRecyclerView = findViewById(R.id.threadsRecyclerView)
        threadList = threadsViewModel.threadList.value!!
        threadsAdapter = ThreadsAdapter(this, threadList)
        
        threadsRecyclerView.layoutManager = LinearLayoutManager(this)
        threadsRecyclerView.adapter = threadsAdapter
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        threadsRecyclerView.addItemDecoration(dividerItemDecoration)
        
        val addThreadToListButton:Button = findViewById(R.id.addThreadToListButton)
        val clearListButton:Button = findViewById(R.id.clearListButton)
        val uploadAllButton:Button = findViewById(R.id.uploadAllThreadsButton)
        
        uploadAllButton.setOnClickListener{
            startActivity(Intent(this@MainActivity, ThreadsListActivity::class.java))
        }
        
        val addThreadEditText:TextInputEditText = findViewById(R.id.threadEditText)
        addThreadToListButton.setOnClickListener{
            if(threadsViewModel.threadList.value!!.size < 10){
                if(addThreadEditText.text.toString().isNotEmpty()){
                    val thread:String = addThreadEditText.text.toString().trim()
                    threadsViewModel.addThreadToList(thread)
                    threadsAdapter.notifyDataSetChanged()
                    addThreadEditText.text?.clear()
                }else{
                    Toast.makeText(this@MainActivity, "Please enter some text", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@MainActivity, "you can not add items more than 10", Toast.LENGTH_SHORT).show()
            }
            
        }
        clearListButton.setOnClickListener{
            if (threadList.size > 0){
                threadsViewModel.clearList()
                threadsAdapter.notifyDataSetChanged()
            }else{
                Toast.makeText(this@MainActivity, "No item to delete in the list", Toast.LENGTH_SHORT).show()
            }
        }
        
    }
    
    private fun doTheWork(text:String){
        CoroutineScope(Dispatchers.IO).launch {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val gson = GsonBuilder().create()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://graph.threads.net/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            val threadsApi = retrofit.create(ThreadsApi::class.java)

            val threadRequest = ThreadRequest(text = text)
            val userId = "27863565976622483"
            val response = threadsApi.createThread(userId, threadRequest)
            Log.d("responseId", response.id)
            
            val threadPublishRequest = ThreadPublishRequest(creation_id = response.id)
            val response2 = threadsApi.publishThread(userId, threadPublishRequest)
            Log.d("response2", response2.toString())
        }
    }
    
}