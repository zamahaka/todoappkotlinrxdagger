package com.zamahaka.todoappkotlinrxdagger.tasks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zamahaka.todoappkotlinrxdagger.R

class TasksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)
    }

    companion object {
        val CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY"
    }
}
