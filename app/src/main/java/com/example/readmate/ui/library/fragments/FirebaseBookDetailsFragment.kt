package com.example.readmate.ui.library.fragments

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.readmate.databinding.FragmentFirebaseBookDetailsBinding
import com.example.readmate.ui.base.BaseFragment

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class FirebaseBookDetailsFragment : BaseFragment<FragmentFirebaseBookDetailsBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentFirebaseBookDetailsBinding =
        FragmentFirebaseBookDetailsBinding.inflate(layoutInflater)

}