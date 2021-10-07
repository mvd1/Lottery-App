package com.example.project4.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject

/**
 * The View Model contains all of the data that is ahown in the user interface. This data includes the following:
 *
 *      1. Text views and variables for the Powerball fragment
 *      2. Text views and variables for the Mega Millions fragment
 *      3. The chosen currency from the settings fragment
 *      4. Boolean values that keep track of whether or not an API call was made to get the lottery data.
 *         This is important so that the program does not make an new API call each time the fragment is changed,
 *         which would quickly waste the 500 API calls that are allowed for a 30-day period.
 *
 * Much of this data is MutableLiveData, updated whenever the data in the corresponding fragment changes. Two
 * separate functions, called updatePowerBall and updateMegaMillions, serve to update the variables based on the JSON
 * object that is passed in to the function from the corresponding fragment.
 */
class MainViewModel : ViewModel() {

    // current chosen currency
    var currency: String = "EUR"

    // boolean variables that change when an API request has successfully been made
    var pb_requestMade = false
    var mm_requestMade = false

    // data for the Powerball fragment
    var pb_powerplay = MutableLiveData<String>() // powerplay var
    var pb_pot = MutableLiveData<String>() // total possible winnings
    var pb_date = MutableLiveData<String>() // date of drawing
    var pb_n1 = MutableLiveData<String>() // first number
    var pb_n2 = MutableLiveData<String>() // second number
    var pb_n3 = MutableLiveData<String>() // third number
    var pb_n4 = MutableLiveData<String>() // fourth number
    var pb_n5 = MutableLiveData<String>() // fifth number
    var pb_nextdate = MutableLiveData<String>() // date of next drawing
    var pb_nextpot = MutableLiveData<String>() // winnings for next drawing

    // data for the Mega Millions fragment
    var mm_megaplier = MutableLiveData<String>() // megaplier var
    var mm_pot = MutableLiveData<String>() // total possible winnings
    var mm_date = MutableLiveData<String>() // date of drawing
    var mm_n1 = MutableLiveData<String>() // first number
    var mm_n2 = MutableLiveData<String>() // second number
    var mm_n3 = MutableLiveData<String>() // third number
    var mm_n4 = MutableLiveData<String>() // fourth number
    var mm_n5 = MutableLiveData<String>() // fifth number
    var mm_mb = MutableLiveData<String>() // megaball number
    var mm_nextdate = MutableLiveData<String>() // date of next drawing
    var mm_nextpot = MutableLiveData<String>() // winnings for next drawing

    // update all of the Powerball values based on the data from the JSON object
    fun updatePowerBall(data: JSONObject) {
        pb_powerplay.value = data.getJSONObject("result").getString("powerplay")
        pb_pot.value = data.getJSONObject("result").getString("jackpot")
        pb_date.value = data.getJSONObject("result").getString("date")

        pb_n1.value = data.getJSONObject("result").getJSONObject("numbers").getString("n1")
        pb_n2.value = data.getJSONObject("result").getJSONObject("numbers").getString("n2")
        pb_n3.value = data.getJSONObject("result").getJSONObject("numbers").getString("n3")
        pb_n4.value = data.getJSONObject("result").getJSONObject("numbers").getString("n4")
        pb_n5.value = data.getJSONObject("result").getJSONObject("numbers").getString("n5")

        pb_nextdate.value = data.getJSONObject("result").getJSONObject("next-jackpot").getString("date")
        pb_nextpot.value = data.getJSONObject("result").getJSONObject("next-jackpot").getString("amount")

        pb_requestMade = true
    }

    // update all of the Mega Millions values based on the data from the JSON object
    fun updateMegaMillions(data: JSONObject) {

        mm_megaplier.value = data.getJSONObject("result").getString("megaplier")
        mm_pot.value = data.getJSONObject("result").getString("jackpot")
        mm_date.value = data.getJSONObject("result").getString("date")

        mm_n1.value = data.getJSONObject("result").getJSONObject("numbers").getString("n1")
        mm_n2.value = data.getJSONObject("result").getJSONObject("numbers").getString("n2")
        mm_n3.value = data.getJSONObject("result").getJSONObject("numbers").getString("n3")
        mm_n4.value = data.getJSONObject("result").getJSONObject("numbers").getString("n4")
        mm_n5.value = data.getJSONObject("result").getJSONObject("numbers").getString("n5")
        mm_mb.value = data.getJSONObject("result").getJSONObject("numbers").getString("mb")

        mm_nextdate.value = data.getJSONObject("result").getJSONObject("next-jackpot").getString("date")
        mm_nextpot.value = data.getJSONObject("result").getJSONObject("next-jackpot").getString("amount")

        mm_requestMade = true
    }

}