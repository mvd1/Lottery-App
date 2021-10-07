package com.example.project4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import com.example.project4.ui.main.MegaMillionsFragment
import com.example.project4.ui.main.PowerBallFragment
import com.example.project4.ui.main.SettingsFragment

/**
 * The main activity is the entry point of the application: it creates the main activity layout that contains the various fragments that will be interacted with in the project.
 * Also includes the menu and the functionality that allows the user to interact with menu. Contains boolean variables to keep track of which fragment is open, as well as
 * a variable that controls which text appears on the button based upon the current fragment open. Includes overrides for onCreate, onCreateOptionsMenu, and onOptionsItemSelected,
 * as well as the function switchFragments, which switches between the Powerball and Mega Millions fragments.
 */
class MainActivity : AppCompatActivity() {

    private var powerBallOpen: Boolean = true // keeps track of the current fragment being used
    private var settingsOpen: Boolean = false // keeps track of whether or not the settings fragment is being used

    // override onCreate to create the main activity and create an instance of the Powerball fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PowerBallFragment.newInstance())
                    .commitNow()
        }

        var switchButton: Button = findViewById(R.id.switchButton) // switches between different fragments

        // changes the text on the button based on which fragment is currently loaded
        if (powerBallOpen) {
            switchButton.text = "Show Mega Millions"
        } else {
            switchButton.text = "Show PowerBall"
        }

        // creates a listener that switches fragments on button press
        switchButton.setOnClickListener { switchFragments(switchButton) }
    }

    // creates the menu for the application
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.app_menu, menu)
        return true
    }

    // Override to handle the item selection from the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // creates a variable for the button, which will update its text when the settings is opened
        var switchButton: Button = findViewById(R.id.switchButton)

        // Handle item selection
        return when (item.itemId) {
            R.id.openSettings -> {
                // Create an instance of the settings fragment for user to interact with
                settingsOpen = true
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance())
                    .commitNow()
                switchButton.text = "Close Settings"
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Used to control the fragment that is currently instantiated in the Main Activity. When the button is pressed,
     * the opposite fragment is then loaded into the user interface, and the boolean value current is updated along with
     * the text shown on the button. Also checks to see if the settings are open, and updates the settingsOpen boolean if
     * settings is open, since switching the fragment will mean that settings is closed.
     */
    private fun switchFragments(button: Button) {
        if (!powerBallOpen || settingsOpen) {
            if (settingsOpen) {
                powerBallOpen = true
            }
            button.text = "Show Mega Millions"
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PowerBallFragment.newInstance())
                .commitNow()
        } else {
            button.text = "Show PowerBall"
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MegaMillionsFragment.newInstance())
                .commitNow()
        }

        if (!settingsOpen) {
            powerBallOpen = !powerBallOpen
        }
        settingsOpen = false
    }
}