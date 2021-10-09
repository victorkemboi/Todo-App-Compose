package com.mes.todo.data.daos

import androidx.paging.PagingSource
import androidx.room.Query
import com.mes.todo.data.entities.Todo

interface TodoDao: BaseDao<Todo> {
    @Query("SELECT * FROM Todo")
    fun fetchAll(): PagingSource<Int, Todo>
}