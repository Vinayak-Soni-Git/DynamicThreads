package com.example.dynamicthreads

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicthreads.adapters.ThreadsAdapter
import com.example.dynamicthreads.models.ThreadPublishRequest
import com.example.dynamicthreads.models.ThreadRequest
import com.example.dynamicthreads.models.TokenResponse
import com.example.dynamicthreads.network.ThreadsApi
import com.example.dynamicthreads.network.TokenService
import com.example.dynamicthreads.utils.Constants
import com.example.dynamicthreads.utils.SwipeToDeleteCallback
import com.example.dynamicthreads.utils.SwipeToEditCallback
import com.example.dynamicthreads.viewmodels.ThreadsViewModel
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var threadsViewModel: ThreadsViewModel
    private lateinit var threadsAdapter: ThreadsAdapter
    private lateinit var threadsRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        threadsViewModel = ViewModelProvider(this)[ThreadsViewModel::class.java]
        threadsRecyclerView = findViewById(R.id.threadsRecyclerView)
        threadsAdapter = ThreadsAdapter(this, ArrayList())

        threadsViewModel.threadList.observe(this) { items ->
            threadsAdapter.updateData(items)
        }

        threadsRecyclerView.layoutManager = LinearLayoutManager(this)
        threadsRecyclerView.adapter = threadsAdapter
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        threadsRecyclerView.addItemDecoration(dividerItemDecoration)

        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = threadsRecyclerView.adapter as ThreadsAdapter
                adapter.editItem(viewHolder.adapterPosition)
            }

        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(threadsRecyclerView)

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = threadsRecyclerView.adapter as ThreadsAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(threadsRecyclerView)

        val clearListButton: Button = findViewById(R.id.clearListButton)
        val addThreadButton:Button = findViewById(R.id.addThreadButton)
        val uploadAllButton: Button = findViewById(R.id.uploadAllThreadsButton)
        

        clearListButton.setOnClickListener {
            if (threadsViewModel.threadList.value?.size!! > 0) {
                threadsViewModel.clearList()
                threadsAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "No item to delete in the list",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        
        addThreadButton.setOnClickListener{
            val dialog = Dialog(this@MainActivity)
            dialog.setContentView(R.layout.add_thread_layout)
            dialog.window?.setLayout(1000, 500)
            val editText:EditText = dialog.findViewById(R.id.addThreadEditText)
            val saveButton:Button = dialog.findViewById(R.id.addButton)
            dialog.setCanceledOnTouchOutside(false)

            saveButton.setOnClickListener{
                threadsViewModel.addThreadToList(editText.text.toString())
                threadsAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
            dialog.show()
        }
        
        uploadAllButton.setOnClickListener{
            val handler = Handler(Looper.getMainLooper())
            threadsViewModel.threadList.value?.forEachIndexed { index, item ->
                handler.postDelayed({
                    publishThread(item.text)
                }, 4000L)
            }
        }

    }

    private fun publishThread(text: String) {
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
            val response = threadsApi.createThread(Constants.USER_ID, threadRequest)
            Log.d("responseId", response.id)

            val threadPublishRequest = ThreadPublishRequest(creation_id = response.id)
            val response2 = threadsApi.publishThread(Constants.USER_ID, threadPublishRequest)
            Log.d("response2", response2.toString())
        }
    }
    private fun getLongLivedToken(){
        CoroutineScope(Dispatchers.IO).launch { 
            val retrofit:Retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val shortLivedAccessToken = "THQWJWUXA3WHlmLWZAoa3pfbmpmcDcyVXZAMNHM3LXJNX1V3bHU4elh2T1" +
                    "k4NU5ZAMXRQS1NwMGdZARFFQbW84RHFVLXlXNUZAIR2hsbTNtS3FMMms1ekVxQUs0Q2M2UWtJa3R6" +
                    "Q01zR2xiZAlNmSzVic0NuSHlQZAnVQMUxsVll1dm13bVZA1cUNHdWozRTlwbWJlME1qV3cZD"
            val service:TokenService = retrofit.create(TokenService::class.java)
            val listCall:Call<TokenResponse> = service.getLongLivedToken("th_exchange_token", Constants.APP_SECRET, shortLivedAccessToken)
            listCall.enqueue(object:Callback<TokenResponse>{
                override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                    if(response.isSuccessful){
                        val tokenResponse:TokenResponse = response.body()!!
                        Log.d("LongLivedToken", "$tokenResponse")
                    }else{
                        val responseCode = response.code()
                        when(responseCode){
                            400 -> {
                                Log.e("Error 400", "Bad Connection")
                            }
                            404 -> {
                                Log.e("Error 404", "Not Found")
                            }else -> {
                            Log.e("Error", "Generic Error")
                        }
                        }
                    }
                }

                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                    Log.e("Errorrrrr", t.message.toString())
                }

            })
        }
    }
}