package com.example.clone.archtecture.todo

import android.app.Application
import androidx.viewbinding.BuildConfig
import com.example.clone.archtecture.todo.data.source.TaskRepository
import timber.log.Timber

/**
 * 이 Application은 repository를 느슨하게 제공한다.
 * 이 Sample을 단순화 하기 위해서 Service Locator 패턴이 사용됩니다.
 * (샘플이기 때문에 Service Locator 패턴을 쓴다는 말 같은데...)
 *
 * Dependency Injection 프레임 워크를 고려해라.
 *
 * 또한 DEBUG BuildConfig에서 Timber를 설정합니다.
 * 프로덕션 설정에 대한 Timber의 문서를 읽으십시오.
 */
// TODO Service Locator 패턴, Dependency Injection 프레임 워크 : dagger, Timber ??
class TodoApplication: Application() {

    val taskRepository: TaskRepository
        get() = ServiceLocator.provideTasksRepository(this)

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}