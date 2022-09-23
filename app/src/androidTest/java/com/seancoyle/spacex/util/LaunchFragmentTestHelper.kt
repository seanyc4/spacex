package com.seancoyle.spacex.util

import android.view.View
import androidx.annotation.DrawableRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import com.seancoyle.spacex.R
import com.seancoyle.launch_models.model.launch.LaunchModel
import com.seancoyle.launch_datasource.network.abstraction.datetransformer.DateTransformer
import com.seancoyle.spacex.framework.presentation.launch.HEADER_COUNT
import com.seancoyle.ui_launch.ui.adapter.LaunchAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

@ExperimentalCoroutinesApi
@FlowPreview
fun launchesFragmentTestHelper(func: LaunchFragmentTestHelper.() -> Unit) =
    LaunchFragmentTestHelper().apply { func() }

@ExperimentalCoroutinesApi
@FlowPreview
class LaunchFragmentTestHelper {

    fun waitViewShown(matcher: Matcher<View>) {
        val idlingResource: IdlingResource = ViewShownIdlingResource(matcher, isDisplayed())
        try {
            IdlingRegistry.getInstance().register(idlingResource)
            Espresso.onView(withId(0)).check(ViewAssertions.doesNotExist())
        } finally {
            IdlingRegistry.getInstance().unregister(idlingResource)
        }
    }

    fun checkRecyclerItemsDaysSinceDisplaysCorrectly(
        expectedFilterResults: List<LaunchModel>,
        dateTransformer: DateTransformer
    ) {
        launchesFragmentTestHelper {
            Espresso.onView(recyclerViewMatcher)
                .check(RecyclerViewItemCountAssertion(expectedFilterResults.size))
            for (entity in expectedFilterResults) {
                scrollToRecyclerViewItemWithText(recyclerViewMatcher, entity.missionName)

                // Checks each view holder and verifies the correct
                // launch "days from now" or "days since now"
                // Checks the launch LocalDateTime and determines if a past date or not : true/false
                // Verifies text based on past/future

                if (dateTransformer.isPastLaunch(entity.launchDateLocalDateTime)) {
                    Espresso.onView(
                        withRecyclerView(R.id.rv_launch).atPositionOnView(
                            expectedFilterResults.indexOf(entity).plus(HEADER_COUNT),
                            R.id.days_title
                        )
                    ).check(ViewAssertions.matches(withText(R.string.days_since_now)))
                } else {
                    Espresso.onView(
                        withRecyclerView(R.id.rv_launch).atPositionOnView(
                            expectedFilterResults.indexOf(entity).plus(HEADER_COUNT),
                            R.id.days_title
                        )
                    ).check(ViewAssertions.matches(withText(R.string.days_from_now)))
                }
            }
        }
    }

    fun checkRecyclerItemsDateMatchesFilteredDate(
        expectedFilterResults: List<LaunchModel>,
        year: String? = ""
    ) {
        launchesFragmentTestHelper {
            Espresso.onView(recyclerViewMatcher)
                .check(RecyclerViewItemCountAssertion(expectedFilterResults.size))
            for (entity in expectedFilterResults) {
                scrollToRecyclerViewItemWithText(recyclerViewMatcher, entity.missionName)

                // Checks each view holder and verifies the correct
                // launch date is displayed
                Espresso.onView(
                    withRecyclerView(R.id.rv_launch).atPositionOnView(
                        expectedFilterResults.indexOf(entity).plus(HEADER_COUNT),
                        R.id.date_time_data
                    )
                ).check(ViewAssertions.matches(withText(CoreMatchers.containsString(year))))

            }
        }
    }

    fun checkRecyclerItemsLaunchStatusMatchesFilteredLaunchStatus(
        expectedFilterResults: List<LaunchModel>,
        @DrawableRes launchSuccessIcon: Int
    ) {
        launchesFragmentTestHelper {
            Espresso.onView(recyclerViewMatcher)
                .check(RecyclerViewItemCountAssertion(expectedFilterResults.size))
            for (entity in expectedFilterResults) {
                scrollToRecyclerViewItemWithText(recyclerViewMatcher, entity.missionName)

                // Checks each view holder and verifies the correct
                // launch image is displayed
                Espresso.onView(
                    withRecyclerView(R.id.rv_launch).atPositionOnView(
                        expectedFilterResults.indexOf(entity).plus(HEADER_COUNT),
                        R.id.launch_success_image
                    )
                ).check(ViewAssertions.matches(matchDrawable(launchSuccessIcon)))

            }
        }
    }

    fun checkRecyclerItemsLaunchStatusMatchesFilteredLaunchStatusAndYearMatchesFilteredYear(
        expectedFilterResults: List<LaunchModel>,
        year: String,
        @DrawableRes launchSuccessIcon: Int
    ) {
        launchesFragmentTestHelper {

            Espresso.onView(recyclerViewMatcher)
                .check(RecyclerViewItemCountAssertion(expectedFilterResults.size))
            for (entity in expectedFilterResults) {
                scrollToRecyclerViewItemWithText(recyclerViewMatcher, entity.missionName)

                // Checks each view holder and verifies the correct
                // launch date is displayed
                Espresso.onView(
                    withRecyclerView(R.id.rv_launch).atPositionOnView(
                        expectedFilterResults.indexOf(entity).plus(HEADER_COUNT),
                        R.id.date_time_data
                    )
                ).check(ViewAssertions.matches(withText(CoreMatchers.containsString(year))))

                // Checks each view holder and verifies the correct
                // launch image is displayed
                Espresso.onView(
                    withRecyclerView(R.id.rv_launch).atPositionOnView(
                        expectedFilterResults.indexOf(entity).plus(HEADER_COUNT),
                        R.id.launch_success_image
                    )
                ).check(ViewAssertions.matches(matchDrawable(launchSuccessIcon)))
            }

        }
    }

    fun verifyAllBottomSheetTextViewsDisplayCorrectTitles() {
        launchesFragmentTestHelper {
            verifyCorrectTextIsDisplayed(
                bottomSheetLinksTitleViewMatcher,
                text = R.string.links
            )
            verifyCorrectTextIsDisplayed(
                bottomSheetArticleTitleViewMatcher,
                text = R.string.article
            )
            verifyCorrectTextIsDisplayed(
                bottomSheetWebcastTitleViewMatcher,
                text = R.string.webcast
            )
            verifyCorrectTextIsDisplayed(
                bottomSheetWikipediaTitleViewMatcher,
                text = R.string.wikipedia
            )
            verifyCorrectTextIsDisplayed(
                bottomSheetCancelButtonViewMatcher,
                text = R.string.text_cancel
            )
        }
    }

    fun verifyAllFilterDialogsTextViewsDisplayCorrectText() {
        launchesFragmentTestHelper {
            verifyCorrectTextIsDisplayed(
                filterByYearTitleViewMatcher,
                text = R.string.filter_by_year
            )
            verifyCorrectTextIsDisplayed(
                launchStatusTitleViewMatcher,
                text = R.string.launch_status
            )
            verifyCorrectTextIsDisplayed(
                ascDescTitleViewMatcher,
                text = R.string.asc_desc
            )
            verifyCorrectTextIsDisplayed(
                filterCancelButtonViewMatcher,
                text = R.string.text_cancel
            )
            verifyCorrectTextIsDisplayed(
                filterApplyButtonViewMatcher,
                text = R.string.text_apply
            )
            verifyCorrectTextIsDisplayed(
                filterLaunchStatusAllViewMatcher,
                text = R.string.all
            )
            verifyCorrectTextIsDisplayed(
                filterLaunchStatusSuccessViewMatcher,
                text = R.string.success
            )
            verifyCorrectTextIsDisplayed(
                filterLaunchStatusFailureViewMatcher,
                text = R.string.failure
            )
            verifyCorrectTextIsDisplayed(
                filterLaunchStatusUnknownViewMatcher,
                text = R.string.unknown
            )
        }
    }


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

    fun verifyViewIsDisplayed(view: Matcher<View>) = apply {
        Espresso.onView(view).check(
            ViewAssertions.matches(
                isDisplayed()
            )
        )
    }

    fun verifyViewIsNotVisible(view: Matcher<View>) = apply {
        Espresso.onView(view).check(
            ViewAssertions.matches(
                not(
                    isDisplayed()
                )
            )
        )
    }

    fun verifyViewIsNotDisplayed(view: Matcher<View>) = apply {
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

    companion object {

        val appTitleViewMatcher: Matcher<View> = withId(R.id.toolbar_title)
        val filterButtonViewMatcher: Matcher<View> = withId(R.id.filter_btn)
        val recyclerViewMatcher: Matcher<View> = withId(R.id.rv_launch)
        val filterDialogViewMatcher: Matcher<View> = withId(R.id.filter_dialog_container)
        val filterYearViewMatcher: Matcher<View> = withId(R.id.year_query)
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
        val bottomSheetWebcastTitleViewMatcher: Matcher<View> = withId(R.id.webcast_link)
        val bottomSheetWikipediaTitleViewMatcher: Matcher<View> = withId(R.id.wiki_link)
        val bottomSheetCancelButtonViewMatcher: Matcher<View> = withId(R.id.exit_btn)
        val materialDialogViewMatcher: Matcher<View> = withId(R.id.md_content_layout)
        val materialDialogTitleViewMatcher: Matcher<View> = withId(R.id.md_text_title)
        val materialDialogMessageViewMatcher: Matcher<View> = withId(R.id.md_text_message)
        val materialDialogPositiveBtnViewMatcher: Matcher<View> = withId(R.id.md_button_positive)
    }
}