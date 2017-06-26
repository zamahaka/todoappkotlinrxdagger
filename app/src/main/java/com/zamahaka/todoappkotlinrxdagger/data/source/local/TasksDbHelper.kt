package com.zamahaka.todoappkotlinrxdagger.data.source.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



/**
 * Created by Yura Stetsyc on 6/23/17.
 */
class TasksDbHelper(context: Context) : SQLiteOpenHelper(context, TasksDbHelper.DATABASE_NAME, null, TasksDbHelper.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Not required as at version 1
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Not required as at version 1
    }

    companion object {
        val DATABASE_VERSION = 1

        val DATABASE_NAME = "Tasks.db"

        private val TEXT_TYPE = " TEXT"

        private val BOOLEAN_TYPE = " INTEGER"

        private val COMMA_SEP = ","

        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TasksPersistenceContract.TaskEntry.TABLE_NAME + " (" +
                        TasksPersistenceContract.TaskEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                        TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                        TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                        TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                        TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED + BOOLEAN_TYPE +
                        " )"
    }
}