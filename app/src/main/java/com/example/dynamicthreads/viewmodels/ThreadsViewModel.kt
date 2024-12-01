package com.example.dynamicthreads.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dynamicthreads.models.ThreadItem

class ThreadsViewModel:ViewModel() {
    private val _threadList = MutableLiveData<ArrayList<ThreadItem>>(ArrayList())
    val threadList: LiveData<ArrayList<ThreadItem>> = _threadList
    
    fun addThreadToList(text:String){
        val currentThreadList = _threadList.value ?: ArrayList()
        currentThreadList.add(ThreadItem(text))
        _threadList.value = currentThreadList
    }
    fun removeFromList(position:Int){
        val currentThreadList = _threadList.value ?: return
        currentThreadList.removeAt(position)
        _threadList.value = currentThreadList
    }
    
    fun clearList(){
        _threadList.value?.clear()
    }
}