package br.com.douglasmotta.estudoskotlin

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    /*myFirstCoroutine()
    myFirstCoroutineExplicitBlocking()
    waitingForAJob()
    waitingForAJobExtract()
    coroutinesAreLightWeight()
    threads()*/
    coroutinesAreLikeDaemonThreads()
}

private fun myFirstCoroutine() {
    println("My first coroutine")

    launch { // inicia um novo coroutine em background e continua a execução (não bloqueia a thread principal)
        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
        println("World!") // print after delay
    }

    // Executa imediatamente, pois a thread não está bloqueada, enquanto o coroutine está atrasado
    println("Hello,") // main thread continues while coroutine is delayed
    Thread.sleep(2000L) // block main thread for 2 seconds to keep JVM alive
}

private fun myFirstCoroutineExplicitBlocking() {
    println("My first coroutine explicit blocking")

    launch { // launch new coroutine in background and continue
        delay(1000L)
        println("World!")
    }
    println("Hello,") // main thread continues here immediately
    runBlocking {     // but this expression blocks the main thread
        delay(2000L)  // ... while we delay for 2 seconds to keep JVM alive
    }
}

private fun waitingForAJob() = runBlocking {
    val job = launch { // launch new coroutine and keep a reference to its Job
        delay(1000L)
        println("World!")
    }
    println("Hello,")
    job.join() // wait until child coroutine completes
}

private fun waitingForAJobExtract() = runBlocking {
    val job = launch { doWork() }
    println("Hello,")
    job.join()
}

private suspend fun doWork() {
    delay(1000L)
    println("World!")
}

private fun coroutinesAreLightWeight() = runBlocking {
    val jobs = List(100_000) { // launch a lot of coroutines and list their jobs
        launch {
            delay(1000L)
            print(".")
        }
    }
    jobs.forEach { it.join() } // wait for all jobs to complete
}

private fun threads() {
    val jobs = List(100_000) {
        thread {
            Thread.sleep(1000L)
            print(".")
        }
    }

    // out of memory error
    jobs.forEach { it.join() }
}

private fun coroutinesAreLikeDaemonThreads() = runBlocking {
    launch {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L) // just quit after delay
}