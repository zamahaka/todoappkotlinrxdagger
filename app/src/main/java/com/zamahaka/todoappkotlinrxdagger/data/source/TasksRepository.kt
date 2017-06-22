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
        throw UnsupportedOperationException("not implemented")
    }

    override fun saveTask(task: Task) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun completeTask(task: Task) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun completeTask(taskId: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun activateTask(task: Task) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun activateTask(taskId: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun clearCompletedTasks() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun refreshTasks() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun deleteAllTasks() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun deleteTask(taskId: String) {
        throw UnsupportedOperationException("not implemented")
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