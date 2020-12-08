package com.example.clone.archtecture.todo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.clone.archtecture.todo.data.Result.*
import com.example.clone.archtecture.todo.data.source.TaskDataSource

/*
 * TODO
 * 1. LinkedHashMap??
 * 2/ TASKS_SERVICE_DATA.filterValues???
 */

object FakeTasksRemoteDataSource: TaskDataSource {

    private var TASKS_SERVICE_DATA: LinkedHashMap<String, Task> = LinkedHashMap()
    private val observableTasks = MutableLiveData<Result<List<Task>>>()


    override fun observeTasks(): LiveData<Result<List<Task>>> {
        return observableTasks
    }

    override suspend fun getTasks(): Result<List<Task>> {
        return Success(TASKS_SERVICE_DATA.values.toList())
    }

    override suspend fun refreshTasks() {
        observableTasks.postValue(getTasks())
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        return observableTasks.map { tasks ->
            when(tasks) {
                is Loading -> Loading
                is Error -> Error(tasks.exception)
                is Success -> {
                    val task = tasks.data.firstOrNull() { it.id == taskId }
                            ?: return@map Error(Exception("not pund"))
                    Success(task)
                }
            }
        }
    }

    override suspend fun getTask(taskId: String): Result<Task> {
        TASKS_SERVICE_DATA[taskId]?.let {
            return Success(it)
        }
        return Error(Exception("Could not find task"))
    }

    override suspend fun refreshTask(taskId: String) {
        refreshTasks()
    }

    override suspend fun saveTask(task: Task) {
        TASKS_SERVICE_DATA[task.id] = task
    }

    override suspend fun completeTask(task: Task) {
        val completedTask = Task(task.title, task.description, true, task.id)
        TASKS_SERVICE_DATA[task.id] = completedTask
    }

    override suspend fun completeTask(taskId: String) {
        // Not required for the remote data source.
    }

    override suspend fun activateTask(task: Task) {
        val activeTask = Task(task.title, task.description, false, task.id)
        TASKS_SERVICE_DATA[task.id] = activeTask
    }

    override suspend fun activateTask(taskId: String) {
        // Not required for the remote data source.
    }

    override suspend fun clearCompletedTasks() {
        TASKS_SERVICE_DATA.filterValues {
            !it.isComplete
        } as LinkedHashMap<String, Task>
    }

    override suspend fun deleteAllTasks() {
        TASKS_SERVICE_DATA.clear()
        refreshTasks()
    }

    override suspend fun deleteTask(taskId: String) {
        TASKS_SERVICE_DATA.remove(taskId)
        refreshTasks()
    }
}