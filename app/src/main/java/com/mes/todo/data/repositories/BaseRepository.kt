package com.mes.todo.data.repositories

interface BaseRepository<T> {
    suspend fun save(item: T)
    suspend fun save(items: List<T>)
    suspend fun update(item: T)
    suspend fun update(items: List<T>)
    suspend fun delete(item: T)
}