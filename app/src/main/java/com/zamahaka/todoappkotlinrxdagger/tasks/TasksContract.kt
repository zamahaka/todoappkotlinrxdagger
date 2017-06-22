package com.zamahaka.todoappkotlinrxdagger.tasks

import com.zamahaka.todoappkotlinrxdagger.ActiveView
import com.zamahaka.todoappkotlinrxdagger.BasePresenter
import com.zamahaka.todoappkotlinrxdagger.BaseView
import com.zamahaka.todoappkotlinrxdagger.LoadingView
import com.zamahaka.todoappkotlinrxdagger.data.Task

/**
 * Created by Yura Stetsyc on 6/22/17.
 */
interface TasksContract {

    interface View : BaseView<Presenter>, ActiveView, LoadingView {
        fun showTasks(tasks: List<Task>)
        fun showAddTask()
        fun showTaskDetailsUi(taskId: String)
        fun showTaskMarkedComplete()
        fun showTaskMarkedActive()
        fun showCompletedTasksCleared()
        fun showLoadingTasksError()
        fun showNoTasks()
        fun showActiveFilterLabel()
        fun showCompletedFilterLabel()
        fun showAllFilterLabel()
        fun showNoActiveTasks()
        fun showNoCompletedTasks()
        fun showSuccessfullySavedMessage()
        fun showFilteringPopUpMenu()
    }

    interface Presenter : BasePresenter {
        fun result(requestCode: Int, resultCode: Int)
        fun loadTasks(forceUpdate: Boolean)
        fun addNewTask()
        fun openTaskDetails(requestedTask: Task)
        fun completeTask(completedTask: Task)
        fun activateTask(activeTask: Task)
        fun clearCompletedTasks()
        fun setFiltering(requestType: TasksFilterType)
        fun getFiltering(): TasksFilterType
    }

}