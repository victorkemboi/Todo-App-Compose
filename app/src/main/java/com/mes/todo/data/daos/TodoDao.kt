package com.mes.todo.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.mes.todo.data.entities.Todo

@Dao
interface TodoDao : BaseDao<Todo> {

    @Query("SELECT * FROM Todo WHERE isDone=0 ORDER BY createdAt")
    fun fetchPendingTodos(): PagingSource<Int, Todo>

    @Query("SELECT * FROM Todo WHERE isDone=1 ORDER BY createdAt")
    fun fetchDoneTodos(): PagingSource<Int, Todo>

    @Query("DELETE FROM Todo")
    suspend fun clearTodos()
}