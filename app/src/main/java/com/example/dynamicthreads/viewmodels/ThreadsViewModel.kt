package com.example.dynamicthreads.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dynamicthreads.Data.ThreadData
import com.example.dynamicthreads.models.ThreadItem

class ThreadsViewModel:ViewModel() {
    private val _threadList = MutableLiveData<ArrayList<ThreadItem>>(ArrayList())
    val threadList: LiveData<ArrayList<ThreadItem>> = _threadList
    
    init {
        _threadList.value = ThreadData().getThreads()
    }
    
    fun addThreadToList(text:String){
        val currentThreadList = _threadList.value ?: ArrayList()
        currentThreadList.add(ThreadItem(text))
        _threadList.value = currentThreadList
    }
    
    fun editItem(position:Int, newValue:ThreadItem){
        _threadList.value?.let { 
            it[position] = newValue
            _threadList.value = it
        }
    }
    
    fun removeFromList(position:Int){
        _threadList.value?.let { 
            it.removeAt(position)
            _threadList.value = it
        }
    }
    
    fun clearList(){
        _threadList.value?.clear()
    }
}