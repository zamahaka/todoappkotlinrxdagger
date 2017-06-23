package com.zamahaka.todoappkotlinrxdagger.util.schedulers

import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Provides different types of schedulers.
 */
object SchedulerProvider : BaseSchedulerProvider {
    override val computation: Scheduler
        get() = Schedulers.computation()
    override val io: Scheduler
        get() = Schedulers.io()
    override val ui: Scheduler
        get() = AndroidSchedulers.mainThread()
}
