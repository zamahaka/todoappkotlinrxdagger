package com.zamahaka.todoappkotlinrxdagger

import android.content.Context
import com.zamahaka.todoappkotlinrxdagger.data.source.TasksRepository
import com.zamahaka.todoappkotlinrxdagger.data.source.remote.TasksRemoteDataSource

/**
 * Created by Yura Stetsyc on 6/23/17.
 */
object Injection {
    fun provideTasksRepository(context: Context): TasksRepository {
        return TasksRepository.getInstance(TasksRemoteDataSource.getInstance(),
                TasksRemoteDataSource.getInstance())
    }
}