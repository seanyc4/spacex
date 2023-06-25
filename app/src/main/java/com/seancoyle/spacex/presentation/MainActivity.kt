package com.seancoyle.spacex.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.seancoyle.core.domain.MessageDisplayType.Dialog
import com.seancoyle.core.domain.MessageDisplayType.None
import com.seancoyle.core.domain.MessageDisplayType.Toast
import com.seancoyle.core.domain.MessageType
import com.seancoyle.core.domain.Response
import com.seancoyle.core.domain.StateMessageCallback
import com.seancoyle.core.domain.UIInteractionHandler
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.core_ui.extensions.displayToast
import com.seancoyle.spacex.R
import com.seancoyle.spacex.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    UIInteractionHandler {

    private lateinit var binding :ActivityMainBinding
    private var dialogInView: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun displayProgressBar(isDisplayed: Boolean) {
        with(binding) {
            progressBar.setContent {
                com.seancoyle.core_ui.composables.CircularProgressBar(
                    isDisplayed = isDisplayed,
                    verticalBias = 0.5f
                )
            }
        }
    }

    override fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {
        when (response.messageDisplayType) {

            is Toast -> {
                response.message?.let {
                    displayToast(
                        message = it,
                        stateMessageCallback = stateMessageCallback
                    )
                }
            }

            is Dialog -> {
                displayDialog(
                    response = response,
                    stateMessageCallback = stateMessageCallback
                )
            }

            is None -> {
                printLogDebug("onResponseReceived: ", response.message.orEmpty())
                stateMessageCallback.removeMessageFromStack()
            }
        }
    }

    private fun displayDialog(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {
        response.message?.let { message ->
            dialogInView = when (response.messageType) {

                is MessageType.Error -> {
                    displayErrorDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                is MessageType.Success -> {
                    displaySuccessDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                is MessageType.Info -> {
                    displayInfoDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                else -> {
                    // do nothing
                    stateMessageCallback.removeMessageFromStack()
                    null
                }
            }
        } ?: stateMessageCallback.removeMessageFromStack()
    }

    override fun onPause() {
        super.onPause()
        if (dialogInView != null) {
            (dialogInView as MaterialDialog).dismiss()
            dialogInView = null
        }
    }

    private fun displaySuccessDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show {
                title(R.string.text_success)
                message(text = message)
                cornerRadius(res = R.dimen.default_corner_radius)
                positiveButton(R.string.text_ok) {
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun displayErrorDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show {
                title(R.string.text_error)
                message(text = message)
                cornerRadius(res = R.dimen.default_corner_radius)
                positiveButton(R.string.text_ok) {
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun displayInfoDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show {
                title(R.string.text_info)
                message(text = message)
                cornerRadius(res = R.dimen.default_corner_radius)
                positiveButton(R.string.text_ok) {
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

}