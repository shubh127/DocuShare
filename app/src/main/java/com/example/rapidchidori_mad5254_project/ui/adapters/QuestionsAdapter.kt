package com.example.rapidchidori_mad5254_project.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rapidchidori_mad5254_project.data.models.ui.SignUpQuestionsInfo
import com.example.rapidchidori_mad5254_project.helper.Constants.IS_LAST_QUESTION
import com.example.rapidchidori_mad5254_project.helper.Constants.QUESTION_INFO
import com.example.rapidchidori_mad5254_project.ui.fragments.SingleQuestionFragment
import com.example.rapidchidori_mad5254_project.ui.interfaces.QuestionNextListener

class QuestionsAdapter(
    fragment: Fragment,
    private val questions: MutableList<SignUpQuestionsInfo>,
    private val listener: QuestionNextListener
) :
    FragmentStateAdapter(fragment) {
    lateinit var fragment: SingleQuestionFragment

    override fun getItemCount(): Int = questions.size

    override fun createFragment(position: Int): Fragment {

        fragment = SingleQuestionFragment(listener)
        fragment.arguments = Bundle().apply {
            putParcelable(QUESTION_INFO, questions[position])
            putBoolean(IS_LAST_QUESTION, position + 1 == questions.size)
        }
        return fragment
    }
}