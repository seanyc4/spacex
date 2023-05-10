package com.seancoyle.core.presentation

import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class UIInteractionHandlerDelegate(private val mockUIInteractionHandler: UIInteractionHandler? = null) :
    ReadOnlyProperty<Fragment, UIInteractionHandler> {

    private lateinit var uiInteractionHandler: UIInteractionHandler

    override fun getValue(thisRef: Fragment, property: KProperty<*>): UIInteractionHandler {
        if (!::uiInteractionHandler.isInitialized) {
            uiInteractionHandler = mockUIInteractionHandler
                ?: (thisRef.requireActivity() as? UIInteractionHandler)
                        ?: throw IllegalStateException("The context must implement UIInteractionHandler")
        }
        return uiInteractionHandler
    }
}
