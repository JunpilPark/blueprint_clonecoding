package com.example.clone.archtecture.todo.addedittask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.clone.archtecture.todo.R

class AddEditTaskFragment: Fragment() {œ

    private lateinit var viewDataBinding: AddtaskFragBinding

    // KTX를 사용한 ViewModel 생성
    // onenote:https://d.docs.live.net/ec6f776545651643/문서/프로그래밍/Kotlin.one#KTX%20by%20viewModels%20에%20의한%20AAC-ViewModel%20생성&section-id={CDCDC80E-CCEC-FC40-B811-5DCF760A538C}&page-id={96E63446-CF92-974F-B8E0-1CD58DF0A1B0}&end
    private val viewModel by viewModels<AddEditTaskViewModel> {

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        // inflate 는 xml을 코드로 변환하는 과정을 이야기 함.
        // 상세하게 과정과 원리를 깊이 파보자.
        // 그리고 더불어 compose component 로 레이아웃을 만드는 방법도 알아보자.
        // 추가적으로 inflater.inflate(R.layout.addtask_frag, container, false) false 의 정확한 의미는?
        val root = inflater.inflate(R.layout.addtask_frag, container, false)
        viewDataBinding = AddtaskFragBinding.bind(root).apply {
            this.viewModel = viewModel
        }

        viewDataBinding.lifecycleOwner =  this.viewLifecycleOwner
        return viewDataBinding.root
    }
}