package com.zamahaka.todoappkotlinrxdagger

import android.content.Context
import com.zamahaka.todoappkotlinrxdagger.data.source.remote.TasksRemoteDataSource
import com.zamahaka.todoappkotlinrxdagger.data.source.TasksRepository
import com.zamahaka.todoappkotlinrxdagger.util.schedulers.SchedulerProvider

/**
 * Created by Yura Stetsyc on 6/23/17.
 */
object Injection {
    fun provideTasksRepository(context: Context) {
        return TasksRepository.getInstance(
                TasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(context, SchedulerProvider)
        )
    }
}