package com.example.readmate.ui.settings.fragments

import android.view.LayoutInflater
import com.example.readmate.databinding.FragmentTermsConditionsBinding
import com.example.readmate.ui.base.BaseFragment

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class TermsAndConditionsFragment : BaseFragment<FragmentTermsConditionsBinding>() {

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentTermsConditionsBinding =
        FragmentTermsConditionsBinding.inflate(layoutInflater)

    override fun onViewReady() {
        super.onViewReady()
        navigateBack(binding.navigateBack)
    }
}