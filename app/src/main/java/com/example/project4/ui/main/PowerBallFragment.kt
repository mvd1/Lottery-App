package com.example.project4.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project4.R
import org.json.JSONObject
import kotlin.math.floor

class PowerBallFragment : Fragment() {

    companion object {
        fun newInstance() = PowerBallFragment()
    }

    private val viewModel: MainViewModel by activityViewModels()
    private var numbers = arrayOf("00, ", "00, ", "00, ", "00, ", "00")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // if an API request has not been made, then the call is made to acquire current lottery data
        if (!viewModel.pb_requestMade) {
            /*** To avoid unnecessary API calls, comment out getPowerBallData.*/
            getPowerBallData()
        }
        return inflater.inflate(R.layout.powerball_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Widgets
        val pbJackpotText: TextView = view.findViewById(R.id.pbJackpotText)
        val powerplayText: TextView = view.findViewById(R.id.powerplayText)
        val pbNumbersText: TextView = view.findViewById(R.id.pbNumbersText)
        val pbDateText: TextView = view.findViewById(R.id.pbDateText)
        val pbNextJackpotText: TextView = view.findViewById(R.id.pbNextJackpotText)

        // Update existing currency as the currency may have changed in settings
        pbJackpotText.text = convertCurrency(viewModel.pb_pot.value.toString())
        pbNextJackpotText.text = convertCurrency(viewModel.pb_nextpot.value.toString())

        /**
         * This block of code uses Observers to update the various widgets in the user interface
         * using the API data that is stored in the View Model. It also adds the winning numbers
         * from the API call to the numbers array, and then calls the formatNumbers function to
         * display the winning numbers in the appropriate TextView.
         */
        viewModel.pb_pot.observe(viewLifecycleOwner, Observer { text ->
            pbJackpotText.text = convertCurrency(text.toString())
        })

        viewModel.pb_powerplay.observe(viewLifecycleOwner, Observer { text ->
            powerplayText.text = text.toString()
        })

        viewModel.pb_n1.observe(viewLifecycleOwner, Observer { text ->
            numbers[0] = "$text, "
            pbNumbersText.text = formatNumbers()
        })

        viewModel.pb_n2.observe(viewLifecycleOwner, Observer { text ->
            numbers[1] = "$text, "
            pbNumbersText.text = formatNumbers()
        })

        viewModel.pb_n3.observe(viewLifecycleOwner, Observer { text ->
            numbers[2] = "$text, "
            pbNumbersText.text = formatNumbers()
        })

        viewModel.pb_n4.observe(viewLifecycleOwner, Observer { text ->
            numbers[3] = "$text, "
            pbNumbersText.text = formatNumbers()
        })

        viewModel.pb_n5.observe(viewLifecycleOwner, Observer { text ->
            numbers[4] = "$text"
            pbNumbersText.text = formatNumbers()
        })

        viewModel.pb_date.observe(viewLifecycleOwner, Observer { text ->
            pbDateText.text = text.toString()
        })

        viewModel.pb_nextpot.observe(viewLifecycleOwner, Observer { text ->
            pbNextJackpotText.text = "${convertCurrency(text.toString())} at ${viewModel.pb_nextdate.value}"
        })

        viewModel.pb_nextdate.observe(viewLifecycleOwner, Observer { text ->
            pbNextJackpotText.text = "${viewModel.pb_nextpot.value} at $text"
        })
    }

    /** Performs the API call to get current data on the Powerball lottery. Uses the Volley class
     * and overrides getHeaders in order to properly display API data, since the headers are necessary
     * for this particular API call. When the data is acquired, the View Model is updated by sending the
     * JSON object using the updatePowerBall function in viewModel.
     */
    private fun getPowerBallData() {
        val queue = Volley.newRequestQueue(this.activity)

        // API url
        val url = "https://api.collectapi.com/chancegame/usaPowerball"

        val stringRequest = object: StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Handle response
                val data = JSONObject(response)

                // update View Model with API data
                viewModel.updatePowerBall(data)
            },
            Response.ErrorListener {print("Error")})
        {
            // headers for API call
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["content-type"] = "application/json"
                headers["authorization"] = "apikey 0eoTmuU6RN4sXvASNdP3uL:4OysGATlrhxUiRyF3pvOIN"
                return headers
            }
        }
        queue.add(stringRequest)
    }

    /**
     * formatNumbers is used to convert the values stored in the numbers array to a single string that will be displayed in the user interface.
     * Since each of the lottery numbers is its own separate JSON object, they are stored separately in the View Model, but must be displayed
     * as a single string in the UI. formatNumbers facilitates this exchange and returns the string to the TextView repsonsible for displaying
     * the winning numbers in the UI.
     */
    private fun formatNumbers(): String {
        var result = ""
        for (i in 0..numbers.size - 1) {
            result += numbers[i]
        }
        return result
    }

    /**
     * Used to convert the current shown currency to a the newly chosen currency. Each time a fragment is loaded
     * convertCurrency is called to see if the currency has changed. Then, the currency that the user has chosen is displayed
     * to the user interface.
     */
    private fun convertCurrency(text: String): String {
        if (text == "null") {
            return "Loading..."
        }

        // split text to only include the number and currency sign
        val number = text.slice(1..(text.indexOf(' ') - 1))
        var result: String = ""

        // convert USD (default) to EUR or CAD
        when (viewModel.currency) {
            "USD" ->
                result = "$$number Million"
            "EUR" ->
                result = "€${floor(number.toInt() * 0.84).toInt()} Million"
            "CAD" ->
                result = "$${floor(number.toInt() * 1.25).toInt()} Million"
        }
        return result
    }
}