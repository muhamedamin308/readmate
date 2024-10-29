package com.example.readmate.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.readmate.R

/**
 * @author Muhamed Amin Hassan on 29,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */


class CustomAlertDialog(
    context: Context,
    private val title: String,
    private val message: String,
    private val positiveTitle: String,
    private val onPositiveAction: (() -> Unit)? = null,
    private val onNegativeAction: (() -> Unit)? = null
) : Dialog(context) {

    private lateinit var tvTitle: TextView
    private lateinit var tvBody: TextView
    private lateinit var btnPositive: AppCompatButton
    private lateinit var btnNegative: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view =
            LayoutInflater.from(context).inflate(R.layout.layou_dialog_custom_alert_dialog, null)
        setContentView(view)
        setCancelable(false)

        tvTitle = view.findViewById(R.id.tv_title)
        tvBody = view.findViewById(R.id.tv_body)
        btnPositive = view.findViewById(R.id.btn_preform_action)
        btnNegative = view.findViewById(R.id.btn_cancel)

        tvTitle.text = title
        tvBody.text = message
        btnPositive.text = positiveTitle

        btnPositive.setOnClickListener {
            onPositiveAction?.invoke()
            dismiss()
        }

        btnNegative.setOnClickListener {
            onNegativeAction?.invoke()
            dismiss()
        }
    }
}


fun Fragment.customAlertDialog(
    title: String,
    message: String,
    positiveTitle: String,
    onPositiveAction: (() -> Unit)? = null,
    onNegativeAction: (() -> Unit)? = null
) {
    val dialog = CustomAlertDialog(
        requireContext(),
        title,
        message,
        positiveTitle,
        onPositiveAction,
        onNegativeAction
    )
    dialog.show()
}
