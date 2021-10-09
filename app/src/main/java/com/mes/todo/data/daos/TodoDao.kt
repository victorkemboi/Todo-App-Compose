package com.mes.todo.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.mes.todo.data.entities.Todo

@Dao
interface TodoDao : BaseDao<Todo> {

    @Query("SELECT * FROM Todo ORDER BY createdAt")
    fun fetchAll(): PagingSource<Int, Todo>

    @Query("DELETE FROM Todo")
    fun clearTodos()
}