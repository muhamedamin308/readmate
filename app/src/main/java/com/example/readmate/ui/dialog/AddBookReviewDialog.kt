package com.example.readmate.ui.dialog

import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.readmate.R
import com.example.readmate.data.model.firebase.Review
import com.example.readmate.data.model.firebase.ReviewedUser
import com.example.readmate.util.showMessage
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


fun Fragment.addNewBookReview(
    reviewedUser: ReviewedUser,
    onAddClick: (Review) -> Unit
) {
    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.layout_dialog_add_review, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val reviewText = view.findViewById<EditText>(R.id.et_review)
    val send = view.findViewById<AppCompatButton>(R.id.btn_add_review)
    val cancel = view.findViewById<AppCompatButton>(R.id.btn_cancel)

    send.setOnClickListener {
        val userReview = reviewText.text.toString().trim()

        if (userReview.isEmpty()) {
            context?.showMessage("Please add your review!")
            return@setOnClickListener
        }

        val review = Review(
            reviewedUser,
            userReview,
            System.currentTimeMillis()
        )
        onAddClick(review)
        dialog.dismiss()
    }

    cancel.setOnClickListener {
        dialog.dismiss()
    }
}