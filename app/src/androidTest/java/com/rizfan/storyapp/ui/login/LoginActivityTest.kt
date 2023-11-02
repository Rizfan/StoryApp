package com.rizfan.storyapp.ui.login

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.rizfan.storyapp.R
import com.rizfan.storyapp.data.utils.EspressoIdlingResource
import com.rizfan.storyapp.ui.view.login.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loadLogin_Success() {
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
        onView(withId(R.id.emailEditText)).perform(
            typeText("nama@mail.com"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.passwordEditText)).perform(
            typeText("qweqwe123"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.loginButton)).perform(click())
    }

}