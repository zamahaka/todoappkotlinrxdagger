package com.zamahaka.todoappkotlinrxdagger.data.source

import com.zamahaka.todoappkotlinrxdagger.data.Task
import rx.Observable


/**
 * Created by Yura Stetsyc on 6/22/17.
 */
class TasksRepository
private constructor(private val mTasksRemoteDataSource: TasksDataSource,
                    private val mTasksLocalDataSource: TasksDataSource) : TasksDataSource {

    private val mCachedTasks: MutableMap<String, Task> = linkedMapOf()
    private var mCacheIsDirty = false

    override val tasks: Observable<MutableList<Task>>
        get() {
            if (mCachedTasks.isNotEmpty() && !mCacheIsDirty)
                return Observable.from(mCachedTasks.values).toList()

            val remoteTasks = getAndSaveRemoteTasks()

            return if (mCacheIsDirty) remoteTasks else {
                val localTasks = getAndCacheLocalTasks()

                Observable.concat(localTasks, remoteTasks).filter { !it.isEmpty() }.first()
            }
        }

    override fun getTask(taskId: String): Observable<Task> {
        val cachedTask = getTaskWithId(taskId)

        return if (cachedTask != null) Observable.just(cachedTask) else {
            val localTask = getTaskWithIdFromLocalRepository(taskId)
            val remoteTask = getTaskWithIdFromRemoteRepository(taskId)

            Observable.concat(localTask, remoteTask).first()
                    .map { it ?: throw NoSuchElementException("No task found with taskId $taskId") }
        }
    }

    override fun saveTask(task: Task) {
        mTasksRemoteDataSource.saveTask(task)
        mTasksLocalDataSource.saveTask(task)

        mCachedTasks.put(task.id, task)
    }

    override fun completeTask(task: Task) {
        mTasksRemoteDataSource.completeTask(task)
        mTasksLocalDataSource.completeTask(task)

        val completedTask = Task(task.title, task.description, task.id, true)

        mCachedTasks.put(task.id, completedTask)
    }

    override fun completeTask(taskId: String) {
        val taskWithId = getTaskWithId(taskId)
        if (taskWithId != null) completeTask(taskWithId)
    }

    override fun activateTask(task: Task) {
        mTasksRemoteDataSource.activateTask(task)
        mTasksLocalDataSource.activateTask(task)

        val activeTask = Task(task.title, task.description, task.id)

        mCachedTasks.put(task.id, activeTask)
    }

    override fun activateTask(taskId: String) {
        val taskWithId = getTaskWithId(taskId)
        if (taskWithId != null) activateTask(taskWithId)
    }

    override fun clearCompletedTasks() {
        mTasksRemoteDataSource.clearCompletedTasks()
        mTasksLocalDataSource.clearCompletedTasks()

        val iterator = mCachedTasks.entries.iterator()
        while (iterator.hasNext()) {
            val (_, task) = iterator.next()
            if (task.isCompleted) iterator.remove()
        }
    }

    override fun refreshTasks() {
        mCacheIsDirty = true
    }

    override fun deleteAllTasks() {
        mTasksRemoteDataSource.deleteAllTasks()
        mTasksLocalDataSource.deleteAllTasks()

        mCachedTasks.clear()
    }

    override fun deleteTask(taskId: String) {
        mTasksRemoteDataSource.deleteTask(taskId)
        mTasksLocalDataSource.deleteTask(taskId)

        mCachedTasks.remove(taskId)
    }

    private fun getAndCacheLocalTasks(): Observable<MutableList<Task>> {
        return mTasksLocalDataSource
                .tasks
                .flatMap { tasks ->
                    Observable.from(tasks)
                            .doOnNext { task -> mCachedTasks.put(task.id, task) }
                            .toList()
                }
    }

    private fun getAndSaveRemoteTasks(): Observable<MutableList<Task>> {
        return mTasksRemoteDataSource
                .tasks
                .flatMap { tasks ->
                    Observable.from(tasks).doOnNext { task ->
                        mTasksLocalDataSource.saveTask(task)
                        mCachedTasks.put(task.id, task)
                    }.toList()
                }
                .doOnCompleted({ mCacheIsDirty = false })
    }

    private fun getTaskWithId(taskId: String): Task? = mCachedTasks[taskId]

    private fun getTaskWithIdFromLocalRepository(taskId: String): Observable<Task>
            = mTasksLocalDataSource
            .getTask(taskId)
            .doOnNext { mCachedTasks.put(taskId, it) }
            .first()

    private fun getTaskWithIdFromRemoteRepository(taskId: String): Observable<Task>
            = mTasksRemoteDataSource
            .getTask(taskId)
            .doOnNext {
                mTasksLocalDataSource.saveTask(it)
                mCachedTasks.put(it.id, it)
            }


    companion object {
        private var INSTANCE: TasksRepository? = null

        fun getInstance(tasksRemoteDataSource: TasksDataSource,
                        tasksLocalDataSource: TasksDataSource): TasksRepository = INSTANCE ?: run {
            val tasksRepository = TasksRepository(tasksRemoteDataSource, tasksLocalDataSource)
            INSTANCE = tasksRepository

            return@run tasksRepository
        }
    }

}