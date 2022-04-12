package com.example.weatherapp

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ZipErrorDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("Invalid zip code. Try again")
            .setPositiveButton("OK", null)
            .create()

    companion object{
        const val TAG = "ZipErrorDialogFragment"
    }
}