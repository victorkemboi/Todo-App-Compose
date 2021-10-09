package com.mes.todo.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mes.todo.data.daos.TodoDao
import com.mes.todo.data.entities.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository : BaseRepository<Todo> {
    fun fetchAll(): Flow<PagingData<Todo>>
    suspend fun clearTodos()
}

class TodoRepositoryImpl(
    private val todoDao: TodoDao
) : TodoRepository {

    @ExperimentalPagingApi
    override fun fetchAll(): Flow<PagingData<Todo>> =
        Pager(
            config = PagingConfig(
                pageSize = 50,
                enablePlaceholders = false,
                prefetchDistance = 2
            ),
            remoteMediator = null,
            pagingSourceFactory = {
                todoDao.fetchAll()
            }
        ).flow

    override suspend fun clearTodos() = todoDao.clearTodos()

    override suspend fun save(item: Todo): Long = todoDao.insert(item = item)

    override suspend fun save(items: List<Todo>): List<Long> = todoDao.insert(items = items)

    override suspend fun update(item: Todo) = todoDao.update(item = item)

    override suspend fun update(items: List<Todo>) = todoDao.update(items = items)

    override suspend fun delete(item: Todo) = todoDao.delete(item = item)
}