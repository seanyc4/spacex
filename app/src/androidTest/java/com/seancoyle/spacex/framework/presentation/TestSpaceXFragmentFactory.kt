package com.seancoyle.spacex.framework.presentation

import androidx.fragment.app.FragmentFactory
import com.seancoyle.spacex.framework.presentation.launch.LaunchFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Singleton
class TestSpaceXFragmentFactory
@Inject
constructor() : FragmentFactory() {

    lateinit var uiController: UIController

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            LaunchFragment::class.java.name -> {
                val fragment = LaunchFragment()
                if (::uiController.isInitialized) {
                    fragment.setUIController(uiController)
                }
                fragment
            }

            else -> {
                super.instantiate(classLoader, className)
            }
        }

}