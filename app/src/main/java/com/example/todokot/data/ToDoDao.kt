package com.example.todokot.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todokot.data.models.ToDoData

@Dao
interface ToDoDao {

    @Query(value = "SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllData() : LiveData<List<ToDoData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(toDoData: ToDoData)
}