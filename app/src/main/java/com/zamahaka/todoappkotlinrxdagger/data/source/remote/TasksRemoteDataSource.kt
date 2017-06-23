package com.zamahaka.todoappkotlinrxdagger.data.source.remote

import com.zamahaka.todoappkotlinrxdagger.data.Task
import com.zamahaka.todoappkotlinrxdagger.data.source.TasksDataSource
import rx.Observable
import java.util.concurrent.TimeUnit


/**
 * Created by Yura Stetsyc on 6/23/17.
 */
class TasksRemoteDataSource private constructor() : TasksDataSource {

    init {
        val task1 = Task("Build tower in Pisa", "Ground looks good, no foundation work required.")
        val task2 = Task("Finish bridge in Tacoma", "Found awesome girders at half the cost!")
        TASKS_SERVICE_DATA.put(task1.id, task1)
        TASKS_SERVICE_DATA.put(task2.id, task2)
    }

    override val tasks: Observable<MutableList<Task>> = Observable.from(TASKS_SERVICE_DATA.values)
            .delay(SERVICE_LATENCY_IN_MILLIS, TimeUnit.MILLISECONDS).toList()

    override fun getTask(taskId: String): Observable<Task> = TASKS_SERVICE_DATA[taskId]?.let {
        Observable.just(it).delay(SERVICE_LATENCY_IN_MILLIS, TimeUnit.MILLISECONDS)
    } ?: Observable.empty()

    override fun saveTask(task: Task) {
        TASKS_SERVICE_DATA.put(task.id, task)
    }

    override fun completeTask(task: Task) {
        val completedTask = Task(task.title, task.description, task.id, true)
        TASKS_SERVICE_DATA.put(task.id, completedTask)
    }

    override fun completeTask(taskId: String) {
        val (title, description, id, _) = TASKS_SERVICE_DATA.getOrDefault(taskId, Task("", "", ""))
        TASKS_SERVICE_DATA.put(taskId, Task(title, description, id, true))
    }

    override fun activateTask(task: Task) {
        val activeTask = Task(task.title, task.description, task.id)
        TASKS_SERVICE_DATA.put(task.id, activeTask)
    }

    override fun activateTask(taskId: String) {
        val (title, description, id, _) = TASKS_SERVICE_DATA.getOrDefault(taskId, Task("", "", ""))
        TASKS_SERVICE_DATA.put(taskId, Task(title, description, id))
    }

    override fun clearCompletedTasks() {
        val iterator = TASKS_SERVICE_DATA.entries.iterator()
        while (iterator.hasNext()) {
            val (_, task) = iterator.next()
            if (task.isCompleted) iterator.remove()
        }
    }

    override fun refreshTasks() {

    }

    override fun deleteAllTasks() = TASKS_SERVICE_DATA.clear()

    override fun deleteTask(taskId: String) {
        TASKS_SERVICE_DATA.remove(taskId)
    }

    companion object {
        val SERVICE_LATENCY_IN_MILLIS = 2000L
        private val TASKS_SERVICE_DATA = mutableMapOf<String, Task>()

        private var sINSTANCE: TasksRemoteDataSource? = null

        fun getInstance() = sINSTANCE ?: run {
            val dataSource = TasksRemoteDataSource()
            sINSTANCE = dataSource

            dataSource
        }
    }

}