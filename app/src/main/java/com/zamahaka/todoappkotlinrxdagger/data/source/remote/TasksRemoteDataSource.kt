package com.zamahaka.todoappkotlinrxdagger.data.source

import com.zamahaka.todoappkotlinrxdagger.data.Task
import rx.Observable


/**
 * Created by Yura Stetsyc on 6/23/17.
 */
class FakeTasksRemoteDataSource private constructor() : TasksDataSource {

    override val tasks: Observable<MutableList<Task>> = Observable.from(TASKS_SERVICE_DATA.values).toList()

    override fun getTask(taskId: String): Observable<Task> = Observable.just(TASKS_SERVICE_DATA[taskId])

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
        private val TASKS_SERVICE_DATA = mutableMapOf<String, Task>()

        private var INSTANCE: FakeTasksRemoteDataSource? = null

        fun getInstance() = INSTANCE ?: run {
            val dataSource = FakeTasksRemoteDataSource()
            INSTANCE = dataSource

            dataSource
        }
    }

}