package com.mes.todo.data.di

import com.mes.todo.data.daos.TodoDao
import com.mes.todo.data.repositories.TodoRepository
import com.mes.todo.data.repositories.TodoRepositoryImpl
import com.mes.todo.utils.DatabaseConstants.REALM_NAME
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModule: Module = module {

    single {
        val config = RealmConfiguration.Builder().name(REALM_NAME).build()
        val backgroundThreadRealm: Realm = Realm.getInstance(config)
        backgroundThreadRealm
    }
}

private val daoModule: Module = module {
    single { TodoDao(get()) }
}

private val repositoryModule: Module = module {
    single<TodoRepository> { TodoRepositoryImpl(get()) }
}

val dataModules: List<Module> = listOf(
    databaseModule,
    daoModule,
    repositoryModule
)