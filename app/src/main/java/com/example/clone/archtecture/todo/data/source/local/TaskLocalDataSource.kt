package com.example.clone.archtecture.todo.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.clone.archtecture.todo.data.Result
import com.example.clone.archtecture.todo.data.Result.Success
import com.example.clone.archtecture.todo.data.Result.Error

import com.example.clone.archtecture.todo.data.Task
import com.example.clone.archtecture.todo.data.source.TaskDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

// TaskLocalDataSource 의 constructor 에 internal 를 붙여 모듈안에서만 인스턴스를 생성할 수 있도록 함
class TaskLocalDataSource internal constructor(
    private val taskDao: TaskDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskDataSource {

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        return taskDao.observeTask().map {
            Success(it)
        }
    }

    override suspend fun getTasks(): Result<List<Task>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(taskDao.getTasks())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun refreshTasks() {
        // NO - OP
        throw UnsupportedOperationException()
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        return taskDao.observeTaskById(taskId).map {
            Success(it)
        }
    }

    override suspend fun getTask(taskId: String): Result<Task> = withContext(ioDispatcher) {
        try {
            val task = taskDao.getTaskById(taskId)
            if(task != null) {
                return@withContext Success(task)
            }
            else {
                return@withContext Error(Exception("Task is null"))
            }
        }
        catch (e: Exception) {
            return@withContext Error(e)
        }
    }

    override suspend fun refreshTask(taskId: String) {
        // NO - OP
        throw UnsupportedOperationException()
    }

    override suspend fun saveTask(task: Task) = withContext(ioDispatcher) {
        taskDao.insertTask(task)
    }

    override suspend fun completeTask(task: Task) {
        withContext(ioDispatcher) {
            taskDao.updateComplete(task.id, true)
        }
    }

    override suspend fun completeTask(taskId: String) {
        withContext(ioDispatcher) {
            taskDao.updateComplete(taskId, true)
        }
    }

    override suspend fun activateTask(task: Task) {
        withContext(ioDispatcher) {
            taskDao.updateComplete(task.id, false)
        }
    }

    override suspend fun activateTask(taskId: String) {
        withContext(ioDispatcher) {
            taskDao.updateComplete(taskId, false)
        }
    }

    override suspend fun clearCompletedTasks() {
        withContext(ioDispatcher) {
            taskDao.deleteCompleteTasks()
        }
    }

    override suspend fun deleteAllTasks() = withContext(ioDispatcher) {
            taskDao.deleteTasks()
        }

    override suspend fun deleteTask(taskId: String) = withContext<Unit>(ioDispatcher) {
            taskDao.deleteTaskById(taskId)
    }

}