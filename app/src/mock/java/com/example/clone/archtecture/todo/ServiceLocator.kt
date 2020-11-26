package com.example.clone.archtecture.todo

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.example.clone.archtecture.todo.data.source.TaskRepository

/*TODO
 * 1. synchronized(lock) 은 무엇인가?
 * 2. lock 이란?
 * 3. @Volatile 어노테이션의 역할은?
 *  : 일단 주석 에 JVM 지원필드를 휘발성으로 표시함. 이 필드에 기록한 내용이 다른 스레드에 즉시 표시됨 이라고 적혀 있음
 * 4. @VisibleForTesting 의 역할은 ?
 *   : 주석 번역
 *      클래스, 메서드 또는 필드의 가시성이 완화되어 코드를 테스트 가능하게 만드는 데 필요한 것보다 더 널리 표시됨을 나타냅니다.
 *      테스트 용이 아니라면 가시성을 선택적으로 지정할 수 있습니다. 이를 통해 도구가 프로덕션 코드 내에서 의도하지 않은 액세스를 포착 할 수 있습니다.
 * 5. tasksRepository ?: tasksRepository ?: createTaskRepository(context) 이 때 taskRepository 가 null 이면 어떤 순서로 실행되는가?
 */
object ServiceLocator {

    @Volatile
    var tasksRepository: TaskRepository? = null
        @VisibleForTesting set // set 하는 내역이 테스트에 쓰인다는 것을 명시적으로 알리기 위함인가?

    fun providerTasksRepository(context: Context) {
        synchronized(this) {
            return tasksRepository ?: tasksRepository ?: createTaskRepository(context)
        }
    }
}