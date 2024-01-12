package com.example.todokot.data.repository

import androidx.lifecycle.LiveData
import com.example.todokot.data.ToDoDao
import com.example.todokot.data.models.ToDoData

class ToDoRepository(private  val toDoDao: ToDoDao) {

    val getAllData : LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData){
        toDoDao.insert(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData){
        toDoDao.updateData(toDoData)
    }

    suspend fun deleteItem(toDoData: ToDoData){
        toDoDao.deleteItem(toDoData)
    }
}