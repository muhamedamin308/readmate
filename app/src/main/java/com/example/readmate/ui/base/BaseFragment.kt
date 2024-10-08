package com.example.readmate.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.readmate.util.gone
import com.example.readmate.util.show
import com.example.readmate.util.showMessage

/**
 * @author Muhamed Amin Hassan on 08,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    protected lateinit var binding: VB

    abstract fun inflateBinding(layoutInflater: LayoutInflater): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflateBinding(layoutInflater).also { binding = it }.root

    open fun onViewReady() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewReady()
    }

    fun showMessage(message: String?) {
        requireContext().showMessage("Message: ${message ?: "Unknown Message!"}")
    }

    fun viewVisibility(progressBar: View, isVisible: Boolean) {
        if (isVisible) progressBar.show() else progressBar.gone()
    }

}