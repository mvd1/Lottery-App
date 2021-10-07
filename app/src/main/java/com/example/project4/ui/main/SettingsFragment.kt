package com.example.project4.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.activityViewModels
import com.example.project4.R

/**
 * Defines the functionality of the settings fragment, which controls the settings page from the menu.
 * In settings, the user is able to change the currency being displayed from USD to either EUR or CAD. Contains
 * overrides for onCreateView, onViewCreated, and a function called radioButtonClicked, which updates variables in
 * the ViewModel. The three separate currencies are represented by radio buttons that the user clicks to show their
 * choice of currency.
 */
class SettingsFragment : Fragment() {

    // create a new instance of the settings fragment
    companion object {
        fun newInstance() = SettingsFragment()
    }

    // used to communicate with the ViewModel
    private val viewModel: MainViewModel by activityViewModels()

    // inflate the fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }


    // create radio button variables for each radio button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usdRadio: RadioButton = view.findViewById(R.id.usdRadio)
        val eurRadio: RadioButton = view.findViewById(R.id.eurRadio)
        val cadRadio: RadioButton = view.findViewById(R.id.cadRadio)

        // call radioButtonClicked when the radio button is clicked, passing in the radio button that was clicked
        usdRadio.setOnClickListener{ radioButtonClicked(usdRadio) }
        eurRadio.setOnClickListener{ radioButtonClicked(eurRadio) }
        cadRadio.setOnClickListener{ radioButtonClicked(cadRadio) }

        // toggle the correct radio button based on the current value of currency
        when (viewModel.currency) {
            "USD" ->
                usdRadio.toggle()
            "EUR" ->
                eurRadio.toggle()
            "CAD" ->
                cadRadio.toggle()
        }
    }

    // When a radio button is clicked, the currency variable in the ViewModel is updated
    private fun radioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked, then update the ViewModel based on the choice
            when (view.getId()) {
                R.id.usdRadio ->
                    if (checked) {
                        viewModel.currency = "USD"
                    }
                R.id.eurRadio ->
                    if (checked) {
                        viewModel.currency = "EUR"
                    }
                R.id.cadRadio ->
                    if (checked) {
                        viewModel.currency = "CAD"
                    }
            }
        }
    }


}