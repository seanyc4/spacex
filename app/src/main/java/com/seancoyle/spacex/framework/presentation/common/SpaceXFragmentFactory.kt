package com.seancoyle.spacex.framework.presentation.common

import androidx.fragment.app.FragmentFactory
import com.seancoyle.spacex.framework.presentation.launch.LaunchFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class SpaceXFragmentFactory : FragmentFactory(){

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when(className){

            LaunchFragment::class.java.name -> {
                LaunchFragment()
            }

            else -> {
                super.instantiate(classLoader, className)
            }
        }
}