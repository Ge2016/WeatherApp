package com.example.weatherapp

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ErrorDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("Error fetching data. Try again")
            .setPositiveButton(R.string.ok, null).create()

    companion object{
        const val TAG = "ErrorDialogFragment"
    }
}