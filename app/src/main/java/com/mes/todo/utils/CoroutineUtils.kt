package com.mes.todo.utils

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

fun CoroutineScope.safeLaunchWithIo(
    block: suspend CoroutineScope.() -> Unit
): Job =
    this.launch(Dispatchers.IO) {
        try {
            block()
        } catch (ce: CancellationException) {
            // You can ignore or log this exception
        } catch (e: Exception) {
            // Here it's better to at least log the exception
            Timber.d(e)
        }
    }

fun CoroutineScope.safeLaunchWithDefault(
    block: suspend CoroutineScope.() -> Unit
): Job =
    this.launch(Dispatchers.Default) {
        try {
            block()
        } catch (ce: CancellationException) {
            // You can ignore or log this exception
        } catch (e: Exception) {
            // Here it's better to at least log the exception
            Timber.d(e)
        }
    }

fun CoroutineScope.safeLaunchWithMain(
    block: suspend CoroutineScope.() -> Unit
): Job =
    this.launch(Dispatchers.Main) {
        try {
            block()
        } catch (ce: CancellationException) {
            // You can ignore or log this exception
        } catch (e: Exception) {
            // Here it's better to at least log the exception
            Timber.d(e)
        }
    }

fun CoroutineScope.safeLaunch(
    block: suspend CoroutineScope.() -> Unit
): Job =
    this.launch {
        try {
            block()
        } catch (ce: CancellationException) {
            // You can ignore or log this exception
            Timber.d(ce)
        } catch (e: Exception) {
            // Here it's better to at least log the exception
            Timber.d(e)
        }
    }

fun LifecycleCoroutineScope.safeLaunchWhenCreated(
    block: suspend LifecycleCoroutineScope.() -> Unit
): Job =
    this.launchWhenCreated {
        try {
            block()
        } catch (ce: CancellationException) {
            // You can ignore or log this exception
            Timber.d(ce)
        } catch (e: Exception) {
            // Here it's better to at least log the exception
            Timber.d(e)
        }
    }

fun LifecycleCoroutineScope.safeLaunchWhenResumed(
    block: suspend LifecycleCoroutineScope.() -> Unit
): Job =
    this.launchWhenResumed {
        try {
            block()
        } catch (ce: CancellationException) {
            // You can ignore or log this exception
            Timber.d(ce)
        } catch (e: Exception) {
            // Here it's better to at least log the exception
            Timber.d(e)
        }
    }

fun LifecycleCoroutineScope.safeLaunchWhenStarted(
    block: suspend LifecycleCoroutineScope.() -> Unit
): Job =
    this.launchWhenStarted {
        try {
            block()
        } catch (ce: CancellationException) {
            // You can ignore or log this exception
            Timber.d(ce)
        } catch (e: Exception) {
            // Here it's better to at least log the exception
            Timber.d(e)
        }
    }
