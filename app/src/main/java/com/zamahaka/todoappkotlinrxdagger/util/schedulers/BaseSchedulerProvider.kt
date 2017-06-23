package com.zamahaka.todoappkotlinrxdagger.util.schedulers

import rx.Scheduler

interface BaseSchedulerProvider {
    val computation: Scheduler
    val io: Scheduler
    val ui: Scheduler
}
