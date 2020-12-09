package com.example.clone.archtecture.todo

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/*
TODO
1. JvmOverloads 에노테이션의 의미가 뭐지?? 왜 붙이는 거지??
 */
/**
 * 간접 하위 보기를 지원하는 SwipeRefreshLayout 을 확장한 스크롤 뷰
 *
 * [SwipeRefreshLayout]은 스크롤 뷰가 직계 자식 일 때 예상대로 작동합니다.
 * 뷰가 맨 위에있을 때만 새로 고침을 트리거합니다.
 * 이 클래스는이 동작을 제어하는 뷰를 정의하는 방법 (@link #setScrollUpChild})을 추가합니다.
 *
 */
class ScrollChildSwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    var scrollUpChild: View? = null

    override fun canChildScrollUp(): Boolean =
            scrollUpChild?.canScrollVertically(-1) ?: super.canChildScrollUp()
}