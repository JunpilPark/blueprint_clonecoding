package com.example.clone.archtecture.todo.data.source

import androidx.lifecycle.LiveData
import com.example.clone.archtecture.todo.data.Result
import com.example.clone.archtecture.todo.data.Result.*
import com.example.clone.archtecture.todo.data.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class DefaultTaskRepository(
        private val taskRemoteDataSource: TaskDataSource,
        private val taskLocalDataSource: TaskDataSource,
        private val ioDispatcher: CoroutineDispatcher
): TaskRepository {

    override fun observeTask(): LiveData<Result<List<Task>>> {
        return taskLocalDataSource.observeTasks()
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        return taskLocalDataSource.observeTask(taskId)
    }

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        if(forceUpdate) {
            try {
                updateTasksFromRemoteDataSource()
            } catch (ex: Exception) {
                return Error(ex)
            }
        }
        return taskLocalDataSource.getTasks()
    }

    private suspend fun updateTasksFromRemoteDataSource() {
        val remoteTasks = taskRemoteDataSource.getTasks()

        if(remoteTasks is Success) {
            taskLocalDataSource.deleteAllTasks()
            remoteTasks.data.forEach {task ->
                taskLocalDataSource.saveTask(task)
            }
        } else if( remoteTasks is Error) {
            throw remoteTasks.exception
        }
    }

    override suspend fun refreshTasks() {
        updateTasksFromRemoteDataSource()
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        if(forceUpdate) {
            updateTaskFromRemoteDataSource(taskId)
        }
        return taskLocalDataSource.getTask(taskId)
    }

    private suspend fun updateTaskFromRemoteDataSource(taskId: String) {
        val task = taskRemoteDataSource.getTask(taskId)
        if(task is Success) {
            taskLocalDataSource.saveTask(task.data)
        }
    }

    override suspend fun refreshTask(taskId: String) {
        updateTaskFromRemoteDataSource(taskId)
    }

    override suspend fun saveTask(task: Task) {
        //TODO 여긴 coroutineScope를 사용하고, getTask, refreshTask 에는 corutineScope 가 없는 이유는 뭐지?
        coroutineScope {
            launch { taskRemoteDataSource.saveTask(task) }
            launch { taskLocalDataSource.saveTask(task) }
        }
    }

    override suspend fun completeTask(task: Task) {
        coroutineScope {
            launch { taskRemoteDataSource.completeTask(task) }
            launch { taskLocalDataSource.completeTask(task) }
        }
    }

    override suspend fun completeTask(taskId: String) {
        // TODO 여기는 또 왜 withContext
        // TODO 언제 coroutinScope를 쓰고, 언제 withContext 를 사용하는거지?
        // TODO as 에 대해서 알아보자. 만약에 as 키워드로 변환 할수 없는 claas 라면 null 로 변환되나? 그래서 as? 를 붙였나?
        withContext(ioDispatcher) {
            (getTaskWithId(taskId) as? Success)?.let {it ->
                completeTask(it.data)
            }
        }
    }

    private suspend fun getTaskWithId(taskId: String): Result<Task> {
        return taskLocalDataSource.getTask(taskId)
    }

    override suspend fun activateTask(task: Task) {
        coroutineScope {
            launch { taskRemoteDataSource.activateTask(task) }
            launch { taskLocalDataSource.activateTask(task) }
        }
    }

    override suspend fun activateTask(taskId: String) {
        withContext(ioDispatcher) {
            (getTaskWithId(taskId) as? Success)?.let {it ->
                activateTask(it.data)
            }
        }
    }

    override suspend fun clearCompletedTasks() {
        coroutineScope {
            launch { taskRemoteDataSource.clearCompletedTasks() }
            launch { taskLocalDataSource.clearCompletedTasks() }
        }
    }

    override suspend fun deleteAllTasks() {
        coroutineScope {
            launch { taskRemoteDataSource.deleteAllTasks() }
            launch { taskLocalDataSource.deleteAllTasks() }
        }    }

    override suspend fun deleteTask(taskId: String) {
        coroutineScope {
            launch { taskRemoteDataSource.deleteTask(taskId) }
            launch { taskLocalDataSource.deleteTask(taskId) }
        }
    }
}