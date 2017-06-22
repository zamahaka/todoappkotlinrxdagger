package com.zamahaka.todoappkotlinrxdagger

/**
 * Created by Yura Stetsyc on 6/22/17.
 */
interface BaseView<in T> {
    fun setPresenter(presenter: T)
}