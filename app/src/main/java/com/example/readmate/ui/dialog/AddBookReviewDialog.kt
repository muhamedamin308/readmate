package com.example.readmate.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.example.readmate.R
import com.example.readmate.data.model.firebase.Review
import com.example.readmate.data.model.firebase.ReviewedUser
import com.example.readmate.util.showMessage
import com.google.android.material.textfield.TextInputEditText

/**
 * @author Muhamed Amin Hassan on 29,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class BookReviewDialog(
    context: Context,
    private val reviewedUser: ReviewedUser,
    private val onAddClick: (Review) -> Unit
) : Dialog(context) {

    private lateinit var reviewText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view =
            LayoutInflater.from(context).inflate(R.layout.layou_dialog_add_review, null)
        setContentView(view)
        setCancelable(true)


        reviewText = view.findViewById(R.id.et_review)

        view.findViewById<View>(R.id.btn_add_review).setOnClickListener {
            val userReview = reviewText.text.toString().trim()

            if (userReview.isEmpty()) {
                context.showMessage("Please add your review!")
                return@setOnClickListener
            }

            val review = Review(
                reviewedUser,
                userReview,
                System.currentTimeMillis()
            )
            onAddClick(review)
            dismiss()
        }

        view.findViewById<View>(R.id.btn_cancel).setOnClickListener {
            dismiss()
        }
    }
}


fun Fragment.addNewBookReview(
    reviewedUser: ReviewedUser,
    onAddClick: (Review) -> Unit
) {
    val dialog = BookReviewDialog(requireContext(), reviewedUser, onAddClick)
    dialog.show()
}