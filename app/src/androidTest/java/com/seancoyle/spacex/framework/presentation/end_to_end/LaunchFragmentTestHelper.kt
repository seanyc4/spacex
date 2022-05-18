package com.seancoyle.spacex.framework.presentation.end_to_end

import android.content.Context
import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import com.seancoyle.spacex.R
import com.seancoyle.spacex.framework.presentation.launch.LaunchViewModel.Companion.LAUNCH_PREFERENCES
import com.seancoyle.spacex.framework.presentation.launch.adapter.LaunchAdapter
import org.hamcrest.Matcher


fun launchesFragmentTestHelper(func: LaunchFragmentTestHelper.() -> Unit) =
    LaunchFragmentTestHelper().apply { func() }

class LaunchFragmentTestHelper {


    fun performClick(view: Matcher<View>) = apply {
        Espresso.onView(view).perform(ViewActions.click())
    }

    fun performRecyclerViewClick(
        view: Matcher<View>,
        position: Int
    ) = apply {
        Espresso.onView(view).perform(
            RecyclerViewActions.actionOnItemAtPosition<LaunchAdapter.LaunchViewHolder>(
                position,
                ViewActions.click()
            )
        )
    }

    fun performTypeText(
        view: Matcher<View>,
        text: String
    ) = apply {
        Espresso.onView(view).perform(ViewActions.typeText(text))
    }

    fun checkViewIsDisplayed(view: Matcher<View>) = apply {
        Espresso.onView(view).check(
            ViewAssertions.matches(
                isDisplayed()
            )
        )
    }

    fun checkViewIsNotDisplayed(view: Matcher<View>) = apply {
        Espresso.onView(view).check(
            ViewAssertions.doesNotExist()
        )
    }

    fun verifyCorrectTextIsDisplayed(view: Matcher<View>, text: Int) = apply {
        Espresso.onView(view).check(
            ViewAssertions.matches(
                withText(text)
            )
        )
    }

    fun verifyViewIsChecked(view: Matcher<View>) = apply {
        Espresso.onView(view).check(
            ViewAssertions.matches(isChecked())
        )
    }

    fun verifyViewIsNotChecked(view: Matcher<View>) = apply {
        Espresso.onView(view).check(
            ViewAssertions.matches(isNotChecked())
        )
    }

    fun scrollToRecyclerViewItemWithText(view: Matcher<View>, text: String) = apply {
        Espresso.onView(view).perform(
            scrollTo<LaunchAdapter.LaunchViewHolder>(
                hasDescendant(withText(text))
            )
        )
    }

    fun scrollToRecyclerViewItemWithId(view: Matcher<View>, id: Int) = apply {
        Espresso.onView(view).perform(
            scrollTo<LaunchAdapter.LaunchViewHolder>(
                hasDescendant(withId(id))
            )
        )
    }

    fun clearSharedPreferences(context: Context){
        val preferences = context.getSharedPreferences(
            LAUNCH_PREFERENCES,
            Context.MODE_PRIVATE
        )
        val editor = preferences.edit()
        editor.clear()
        editor.commit()
    }


    companion object {

        val appTitleViewMatcher: Matcher<View> = withId(R.id.toolbar_title)
        val filterButtonViewMatcher: Matcher<View> = withId(R.id.filter_btn)
        val recyclerviewHeadingTitleMatcher: Matcher<View> = withId(R.id.section_title)
        val companyInfoMatcher: Matcher<View> = withId(R.id.company_info)
        const val recyclerViewId = R.id.rv_launch
        val recyclerViewMatcher: Matcher<View> = withId(R.id.rv_launch)
        val filterDialogViewMatcher: Matcher<View> = withId(R.id.filter_dialog_container)
        val filterYearViewMatcher: Matcher<View> = withId(R.id.search_query)
        val filterByYearTitleViewMatcher: Matcher<View> = withId(R.id.filter_title)
        val launchStatusTitleViewMatcher: Matcher<View> = withId(R.id.launch_status_title)
        val ascDescTitleViewMatcher: Matcher<View> = withId(R.id.asc_desc_title)
        val filterLaunchStatusAllViewMatcher: Matcher<View> = withId(R.id.filter_all)
        val filterLaunchStatusSuccessViewMatcher: Matcher<View> = withId(R.id.filter_success)
        val filterLaunchStatusFailureViewMatcher: Matcher<View> = withId(R.id.filter_failure)
        val filterLaunchStatusUnknownViewMatcher: Matcher<View> = withId(R.id.filter_unknown)
        val filterAscDescSwitchViewMatcher: Matcher<View> = withId(R.id.order_switch)
        val filterCancelButtonViewMatcher: Matcher<View> = withId(R.id.cancel_btn)
        val filterApplyButtonViewMatcher: Matcher<View> = withId(R.id.apply_btn)
        val bottomSheetViewMatcher: Matcher<View> = withId(R.id.links_container)
        val bottomSheetLinksTitleViewMatcher: Matcher<View> = withId(R.id.links_title)
        val bottomSheetArticleTitleViewMatcher: Matcher<View> = withId(R.id.article_link)
        val bottomSheetYoutubeTitleViewMatcher: Matcher<View> = withId(R.id.youtube_link)
        val bottomSheetWikipediaTitleViewMatcher: Matcher<View> = withId(R.id.wiki_link)
        val bottomSheetCancelButtonViewMatcher: Matcher<View> = withId(R.id.exit_btn)
    }
}