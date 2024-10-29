package com.example.readmate.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.example.readmate.R
import com.example.readmate.data.model.firebase.CreditCard
import com.example.readmate.util.showMessage
import com.google.android.material.textfield.TextInputEditText

/**
 * @author Muhamed Amin Hassan on 29,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class CreditCardDialog(
    context: Context,
    private val onAddClick: (CreditCard) -> Unit
) : Dialog(context) {

    private lateinit var cardNumberInput: TextInputEditText
    private lateinit var cardHolderNameInput: TextInputEditText
    private lateinit var expiryDateInput: TextInputEditText
    private lateinit var cvvInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view =
            LayoutInflater.from(context).inflate(R.layout.layout_dialog_add_credit_card, null)
        setContentView(view)
        setCancelable(false)

        cardNumberInput = view.findViewById(R.id.et_card_number)
        cardHolderNameInput = view.findViewById(R.id.et_card_name)
        expiryDateInput = view.findViewById(R.id.et_card_date)
        cvvInput = view.findViewById(R.id.et_card_cvv)

        view.findViewById<View>(R.id.btn_add_card).setOnClickListener {
            val cardNumber = cardNumberInput.text.toString().trim()
            val cardHolderName = cardHolderNameInput.text.toString().trim()
            val expiryDate = expiryDateInput.text.toString().trim()
            val cvv = cvvInput.text.toString().trim()

            if (cardNumber.isEmpty() || cardHolderName.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                context.showMessage("Please fill empty fields!")
                return@setOnClickListener
            }

            val creditCard = CreditCard(
                cardNumber = cardNumber,
                cardHolderName = cardHolderName,
                expirationDate = expiryDate,
                cvv = cvv
            )
            onAddClick(creditCard)
            dismiss()
        }

        view.findViewById<View>(R.id.btn_cancel).setOnClickListener {
            dismiss()
        }
    }
}


fun Fragment.addNewCreditCardDialog(
    onAddClick: (CreditCard) -> Unit
) {
    val dialog = CreditCardDialog(requireContext(), onAddClick)
    dialog.show()
}