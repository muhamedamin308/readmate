package com.example.readmate.ui.dialog

import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.readmate.R
import com.example.readmate.data.model.firebase.CreditCard
import com.example.readmate.ui.payment.viewmodel.PaymentViewModel
import com.example.readmate.util.showMessage
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText



fun Fragment.addNewCreditCardDialog(
    viewModel: PaymentViewModel,
    onAddClick: (CreditCard) -> Unit
) {
    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.layout_dialog_add_credit_card, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val cardNumberInput = view.findViewById<TextInputEditText>(R.id.et_card_number)
    val cardHolderNameInput = view.findViewById<TextInputEditText>(R.id.et_card_name)
    val expiryDateInput = view.findViewById<TextInputEditText>(R.id.et_card_date)
    val cvvInput = view.findViewById<TextInputEditText>(R.id.et_card_cvv)
    val checkForUniqueCardNumber: (String, (Boolean) -> Unit) -> Unit = { cardNumber, callback ->
        viewModel.checkForUniqueCreditCardNumber(cardNumber, callback)
    }
    val checkForCardValidation: (CreditCard) -> Boolean = { creditCard ->
        viewModel.isValidCreditCard(creditCard)
    }
    val addCreditCard = view.findViewById<Button>(R.id.btn_add_card)
    val cancel = view.findViewById<AppCompatButton>(R.id.btn_cancel)

    addCreditCard.setOnClickListener {
        val cardNumber = cardNumberInput.text.toString().trim()
        val cardHolderName = cardHolderNameInput.text.toString().trim()
        val expiryDate = expiryDateInput.text.toString().trim()
        val cvv = cvvInput.text.toString().trim()

        if (cardNumber.isEmpty() || cardHolderName.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
            context?.showMessage("Please fill empty fields!")
            return@setOnClickListener
        }

        val creditCard = CreditCard(
            cardNumber = cardNumber,
            cardHolderName = cardHolderName,
            expirationDate = expiryDate,
            cvv = cvv
        )

        if (checkForCardValidation(creditCard)) {
            checkForUniqueCardNumber(cardNumber) { exists ->
                if (exists) {
                    context?.showMessage("This card number is already in use.")
                } else {
                    onAddClick(creditCard)
                    dialog.dismiss()
                }
            }
        } else {
            context?.showMessage("Please enter valid card details!")
        }
    }

    cancel.setOnClickListener {
        dialog.dismiss()
    }
}