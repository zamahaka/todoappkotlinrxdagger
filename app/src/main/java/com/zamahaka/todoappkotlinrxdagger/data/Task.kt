/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zamahaka.todoappkotlinrxdagger.data

import java.util.*


data class Task
/**
 * Use this constructor to specify a completed Task if the Task already has an id (copy of
 * another Task).
 *
 * @param title       title of the task
 * @param description description of the task
 * @param id          id of the task
 * @param isCompleted   true if the task is completed, false if it's active
 */
@JvmOverloads constructor(val title: String?,
                          val description: String?,
                          val id: String = UUID.randomUUID().toString(),
                          val isCompleted: Boolean = false) {

    constructor(title: String?, description: String?, isCompleted: Boolean)
            : this(title, description, UUID.randomUUID().toString(), isCompleted)

    fun getTitleForList() = if (title.isNullOrEmpty()) description else title

    fun isActive() = !isCompleted

    fun isEmpty() = title.isNullOrEmpty() && description.isNullOrEmpty()

}
