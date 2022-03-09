package com.example.rapidchidori_mad5254_project.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rapidchidori_mad5254_project.data.models.SignUpQuestionsInfo
import com.example.rapidchidori_mad5254_project.helper.Constants.QUESTION_INFO
import com.example.rapidchidori_mad5254_project.ui.fragments.SingleQuestionFragment

class QuestionsAdapter(
    fragment: Fragment,
    private val questions: MutableList<SignUpQuestionsInfo>
) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = questions.size

    override fun createFragment(position: Int): Fragment {
        val fragment = SingleQuestionFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(QUESTION_INFO, questions[position])
        }
        return fragment
    }
}