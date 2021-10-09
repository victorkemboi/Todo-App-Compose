package com.mes.todo.domain.di

import com.mes.todo.domain.usecases.TodoUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

private val useCaseModule: Module = module {
    single { TodoUseCase(get()) }
}

val domainModules: List<Module> = listOf(
    useCaseModule
)