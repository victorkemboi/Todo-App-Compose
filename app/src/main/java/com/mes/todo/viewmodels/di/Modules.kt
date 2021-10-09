package com.mes.todo.viewmodels.di

import com.mes.todo.viewmodels.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val viewModelModule: Module = module {
    viewModel { TodoViewModel(get()) }
}

private val utilModule: Module = module {
    single<Job> { SupervisorJob() }
    single { CoroutineScope(Dispatchers.IO + get<Job>()) }
}

val appModules: List<Module> = listOf(
    viewModelModule,
    utilModule
)