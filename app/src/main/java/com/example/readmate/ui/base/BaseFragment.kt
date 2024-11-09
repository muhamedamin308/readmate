package com.example.readmate.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun inflateBinding(layoutInflater: LayoutInflater): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = inflateBinding(inflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    open fun onViewReady() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewReady()
    }

    fun showMessage(message: String?) {
        requireContext().showMessage(message ?: "Unknown Message!")
    }

    fun viewVisibility(progressBar: View, isVisible: Boolean) {
        if (isVisible) progressBar.show() else progressBar.gone()
    }

    fun navigateBack(backIcon: ImageView) {
        backIcon.setOnClickListener { findNavController().navigateUp() }
    }

    fun setupRecyclerView(
        recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, orientation: Int
    ) {
        recyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext(), orientation, false)
        }
    }

}