package com.mes.todo.data.di

import androidx.room.Room
import com.mes.todo.data.Database
import com.mes.todo.data.repositories.TodoRepository
import com.mes.todo.data.repositories.TodoRepositoryImpl
import com.mes.todo.utils.DatabaseConstants.DATABASE_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModule: Module = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            Database::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }
}

private val daoModule: Module = module {
    single { get<Database>().TodoDao() }
}

private val repositoryModule: Module = module {
    single<TodoRepository> { TodoRepositoryImpl(get()) }
}

val dataModules: List<Module> = listOf(
    databaseModule,
    daoModule,
    repositoryModule
)