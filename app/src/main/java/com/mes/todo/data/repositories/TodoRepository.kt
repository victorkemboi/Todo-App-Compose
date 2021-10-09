package com.mes.todo.data.repositories

import com.mes.todo.data.daos.TodoDao
import com.mes.todo.data.entities.Todo
import io.realm.RealmResults
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface TodoRepository: BaseRepository<Todo> {
    fun fetchAll(): Flow<RealmResults<Todo>>
    fun clearTodos()
}

class TodoRepositoryImpl(
    private val todoDao: TodoDao
): TodoRepository {

    @ExperimentalCoroutinesApi
    override fun fetchAll(): Flow<RealmResults<Todo>> = todoDao.fetchAll()
    override fun clearTodos() = todoDao.clearTodos()

    override suspend fun save(item: Todo) = todoDao.insert(item = item)

    override suspend fun save(items: List<Todo>) = todoDao.insert(items = items)

    override suspend fun update(item: Todo) = todoDao.update(item = item)

    override suspend fun update(items: List<Todo>) = todoDao.update(items = items)

    override suspend fun delete(item: Todo) = todoDao.delete(item = item)
}