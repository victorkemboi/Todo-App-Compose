package com.mes.todo.data.daos

import com.mes.todo.data.entities.Todo
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.delete
import io.realm.kotlin.where
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class TodoDao(
    private val realm: Realm
) : BaseDao<Todo> {

    @ExperimentalCoroutinesApi
    fun fetchAll(): Flow<RealmResults<Todo>> = callbackFlow {
        val todos: RealmResults<Todo> = realm.where<Todo>().findAllAsync()
        val listener = todos.addChangeListener { collection, _ ->
            this@callbackFlow.trySendBlocking(collection).onFailure { throwable ->
                Timber.d("error at callback: $throwable")
            }
        }
        awaitClose {
            todos.removeAllChangeListeners()
        }
    }

    fun clearTodos() {
        realm.executeTransactionAsync {
            it.delete<Todo>()
        }
    }

    override suspend fun insert(item: Todo) {
        realm.executeTransactionAsync { transactionRealm ->
            transactionRealm.insert(item)
        }
    }

    override suspend fun insert(vararg items: Todo) {
        realm.executeTransactionAsync { transactionRealm ->
            transactionRealm.insert(items.toList())
        }
    }

    override suspend fun insert(items: List<Todo>) {
        realm.executeTransactionAsync { transactionRealm ->
            transactionRealm.insert(items.toList())
        }
    }

    override suspend fun update(item: Todo) {
        realm.executeTransactionAsync { transactionRealm ->
            var innerTodo: Todo =
                transactionRealm.where<Todo>().equalTo("id", item.id).findFirst()
                    ?: return@executeTransactionAsync
            innerTodo = item
        }
    }

    override suspend fun update(items: List<Todo>) {
        realm.executeTransaction { transactionRealm ->
            items.forEach { item ->
                var innerTodo: Todo? =
                    transactionRealm.where<Todo>().equalTo("id", item.id).findFirst()
                        ?: return@forEach
                innerTodo = item
            }


        }
    }

    override suspend fun delete(item: Todo) {
        realm.executeTransactionAsync { transactionRealm ->
            val innerTodo: Todo? =
                transactionRealm.where<Todo>().equalTo("id", item.id).findFirst()!!
            innerTodo?.deleteFromRealm()
        }
    }
}