package com.jefferson.apps.real_world.android.real_worldandroidapp.search.presentation

import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apps.real_world.real_worldandroidapp.RxImmediateSchedulerRule
import com.apps.real_world.real_worldandroidapp.common.data.FakeRepository
import com.jefferson.apps.real_world.android.real_worldandroidapp.R
import com.jefferson.apps.real_world.android.real_worldandroidapp.launchFragmentInHiltContainer
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.SearchFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SearchFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val rxImmediateSheduler = RxImmediateSchedulerRule()

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun searchFragment_testSearch_success(){
        val nameToSearch = FakeRepository().remotelySearchableAnimal.name
        launchFragmentInHiltContainer<SearchFragment>()

        with(onView(withId(R.id.search))){
            perform(click())
            perform(typeSearchViewText(nameToSearch))
        }

        with(onView(withId(R.id.searchRecyclerView))){
            check(matches(childCountIs(1)))
            check(matches(hasDescendant(withText(nameToSearch))))
        }
    }

    private fun typeSearchViewText(text: String) : ViewAction{
        return object : ViewAction{
            override fun getDescription(): String {
                return "Type in SearchView"
            }

            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as SearchView).setQuery(text, false)
            }
        }
    }

    private fun childCountIs(expetedChildCount: Int) : Matcher<View>{
        return object: BoundedMatcher<View, RecyclerView>(RecyclerView::class.java){
            override fun describeTo(description: Description?) {
                description?.appendText("RecyclerView with item count $expetedChildCount")
            }

            override fun matchesSafely(item: RecyclerView?): Boolean {
                return item?.adapter?.itemCount == expetedChildCount
            }
        }
    }
}
