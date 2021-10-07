package com.example.project4.ui.main

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
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

/**
 * This is the fragment for displaying lottery data for the Mega Millions. Contains widgets that represent winnings
 * amount, the megaplier, the winning numbers, the date of the drawing, and the winnings amount and date for the upcoming
 * drawing. Contains an array of strings called numbers (not misleading at all) that contains the winning numbers from the lottery
 * that will be converted into a single string to be displayed in the user interface. In order to acquire current data on the Mega Millions
 * lottery, an API call is used to return the relevant data.
 */
class MegaMillionsFragment : Fragment() {

    companion object {
        fun newInstance() = MegaMillionsFragment()
    }

    private val viewModel: MainViewModel by activityViewModels() // used to communicate with the ViewModel
    private var numbers = arrayOf("00, ", "00, ", "00, ", "00, ", "00, ", "00") // used to hold the values of the numbers gotten from API call

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // if an API request has not been made, then the call is made to acquire current lottery data
        if (!viewModel.mm_requestMade) {
            /*** To avoid unnecessary API calls, comment out getMegaMillionsData.*/
            getMegaMillionsData()
        }
        return inflater.inflate(R.layout.fragment_megamillions_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Widgets
        val mmJackpotText: TextView = view.findViewById(R.id.mmJackpotText)
        val megaplierText: TextView = view.findViewById(R.id.megaplierText)
        val mmNumbersText: TextView = view.findViewById(R.id.mmNumbersText)
        val mmDateText: TextView = view.findViewById(R.id.mmDateText)
        val mmNextJackpotText: TextView = view.findViewById(R.id.mmNextJackpotText)
        val mmNextDateText: TextView = view.findViewById(R.id.mmNextDateText)

        // Update existing currency as the currency may have changed in settings
        mmJackpotText.text = convertCurrency(viewModel.mm_pot.value.toString())
        mmNextJackpotText.text = convertCurrency(viewModel.mm_nextpot.value.toString())

        /**
         * This block of code uses Observers to update the various widgets in the user interface
         * using the API data that is stored in the View Model. It also adds the winning numbers
         * from the API call to the numbers array, and then calls the formatNumbers function to
         * display the winning numbers in the appropriate TextView.
         */
        viewModel.mm_pot.observe(viewLifecycleOwner, Observer { text ->
            mmJackpotText.text = convertCurrency(text.toString())
        })

        viewModel.mm_megaplier.observe(viewLifecycleOwner, Observer { text ->
            megaplierText.text = text.toString()
        })

        viewModel.mm_n1.observe(viewLifecycleOwner, Observer { text ->
            numbers[0] = "$text, "
            mmNumbersText.text = formatNumbers()
        })

        viewModel.mm_n2.observe(viewLifecycleOwner, Observer { text ->
            numbers[1] = "$text, "
            mmNumbersText.text = formatNumbers()
        })

        viewModel.mm_n3.observe(viewLifecycleOwner, Observer { text ->
            numbers[2] = "$text, "
            mmNumbersText.text = formatNumbers()
        })

        viewModel.mm_n4.observe(viewLifecycleOwner, Observer { text ->
            numbers[3] = "$text, "
            mmNumbersText.text = formatNumbers()
        })

        viewModel.mm_n5.observe(viewLifecycleOwner, Observer { text ->
            numbers[4] = "$text, "
            mmNumbersText.text = formatNumbers()
        })

        viewModel.mm_mb.observe(viewLifecycleOwner, Observer { text ->
            numbers[5] = "$text"
            mmNumbersText.text = formatNumbers()
        })

        viewModel.mm_date.observe(viewLifecycleOwner, Observer { text ->
            mmDateText.text = text.toString()
        })

        viewModel.mm_nextpot.observe(viewLifecycleOwner, Observer { text ->
            mmNextJackpotText.text = convertCurrency(text.toString())
        })

        viewModel.mm_nextdate.observe(viewLifecycleOwner, Observer { text ->
            mmNextDateText.text = "on $text"
        })
        // end widget updating for UI
    }

    /** Performs the API call to get current data on the Mega Millions lottery. Uses the Volley class
     * and overrides getHeaders in order to properly display API data, since the headers are necessary
     * for this particular API call. When the data is acquired, the View Model is updated by sending the
     * JSON object using the updateMegaMillions function in viewModel.
     */
    private fun getMegaMillionsData() {
        val queue = Volley.newRequestQueue(this.activity)

        // API url
        val url = "https://api.collectapi.com/chancegame/usaMegaMillions"

        val stringRequest = object: StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Handle response
                val data = JSONObject(response)

                // update View Model with API data
                viewModel.updateMegaMillions(data)
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
     * as a single string in the UI. formatNumbers facilitates this exchange and returns the string to the TextView responsible for displaying
     * the winning numbers in the UI.
     */
    private fun formatNumbers(): String {
        var result = ""
        for (element in numbers) {
            result += element
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
        val number = text.slice(1 until text.indexOf(' '))
        var result: String = "" // converted amount

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