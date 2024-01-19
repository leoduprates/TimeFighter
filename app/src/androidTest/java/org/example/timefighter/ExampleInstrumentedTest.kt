package org.example.timefighter

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.allOf

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun whenTapMeButtonIsPressedThenYourScoreIsIncreasedByOne() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.tapMeButton))
            .perform(click())

        onView(allOf(withId(R.id.gameScoreTextView), withSubstring("1")))
            .check(matches(isDisplayed()))
    }
}