package com.suka.superahorro.packages

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.suka.superahorro.R


fun createInputDialog(dialog: Dialog, desc: String, value: Any, onOkClicked: (String)->Unit ) {
    dialog.setContentView(R.layout.dialog_edit)
    dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    val btOk : Button = dialog.findViewById(R.id.btOk)
    val btCancel : Button = dialog.findViewById(R.id.btCancel)
    val input : EditText = dialog.findViewById(R.id.inputDialogEdit)
    val txtDesc: TextView = dialog.findViewById(R.id.txtDialogEdit)

//    when (value) {
//        is String -> input.setText(value)
//        is UnitValue -> input.setText(value.value?.toString() ?: "")
//        else -> input.setText(value.toString())
//    }
    input.setText(value.toString())
    if ( value is String ) input.inputType = InputType.TYPE_CLASS_TEXT

    txtDesc.text = desc

    btOk.setOnClickListener{
        onOkClicked(input.text.toString())
        dialog.dismiss()
    }
    btCancel.setOnClickListener{
        dialog.dismiss()
    }

    dialog.setOnShowListener{
        Handler().postDelayed({
            input.requestFocus()
            input.selectAll()
            val imm = dialog.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT)
        }, 200)
    }
    dialog.show()

}


fun <T> Fragment.createAutoCompleteDialog(desc: String, options: MutableList<T>, onOkClicked: ((String)->Unit)?=null, onOptionSelected: ((T)->Unit)?=null ) {
    val dialog = Dialog(requireActivity())
    dialog.setContentView(R.layout.dialog_autocomplete)
    dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    val btOk : Button = dialog.findViewById(R.id.btOk)
    val btCancel : Button = dialog.findViewById(R.id.btCancel)
    val input : AutoCompleteTextView = dialog.findViewById(R.id.inputDialogEdit)
    val txtDesc: TextView = dialog.findViewById(R.id.txtDialogEdit)

    txtDesc.text = desc

    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, options)
    input.setAdapter(adapter)
    if ( onOptionSelected != null ) {
        input.setOnItemClickListener { parent, view, position, id ->
            val selectedOption = parent.getItemAtPosition(position) as T
            onOptionSelected(selectedOption)
            dialog.dismiss()
        }
    }

    if ( onOkClicked != null ) {
        btOk.setOnClickListener {
            onOkClicked(input.text.toString())
            dialog.dismiss()
        }
    }
    else btOk.visibility = View.GONE

    btCancel.setOnClickListener{
        dialog.dismiss()
    }

    dialog.setOnShowListener{
        Handler().postDelayed({
            input.requestFocus()
            input.selectAll()
            val imm = dialog.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT)
        }, 200)
    }
    dialog.show()
}