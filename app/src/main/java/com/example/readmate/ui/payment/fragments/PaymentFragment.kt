package com.example.readmate.ui.payment.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.fragment.navArgs
import com.example.readmate.data.model.firebase.CreditCard
import com.example.readmate.data.model.firebase.MyBook
import com.example.readmate.databinding.FragmentPaymentBinding
import com.example.readmate.ui.base.BaseFragment

/**
 * @author Muhamed Amin Hassan on 07,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class PaymentFragment : BaseFragment<FragmentPaymentBinding>() {
    private val navArgs by navArgs<PaymentFragmentArgs>()
    private lateinit var book: MyBook
    private lateinit var creditCard: CreditCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book = navArgs.myBook
        creditCard = navArgs.creditCard
    }

    override fun inflateBinding(layoutInflater: LayoutInflater): FragmentPaymentBinding =
        FragmentPaymentBinding.inflate(layoutInflater)
}