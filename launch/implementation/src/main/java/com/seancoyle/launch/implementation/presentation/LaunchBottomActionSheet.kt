package com.seancoyle.launch.implementation.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.seancoyle.core_ui.extensions.parcelable
import com.seancoyle.launch.api.domain.model.LinkType
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.implementation.R
import com.seancoyle.launch.implementation.presentation.composables.LaunchBottomSheetCard
import com.seancoyle.launch.implementation.presentation.composables.LaunchBottomSheetExitButton

internal class LaunchBottomActionSheet : BottomSheetDialogFragment() {

    private var linkTypes: List<LinkType>? = null

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            getLinksFromBundle()

            setContent {
                Column() {
                    LaunchBottomSheetCard(linkTypes = linkTypes)
                    LaunchBottomSheetExitButton { dismiss() }
                }
            }
        }
    }

    private fun getLinksFromBundle() {
        val links: Links? = arguments?.parcelable(LINKS_KEY)
        links?.let {
            linkTypes = listOfNotNull(
                createLinkType(titleRes = R.string.article, link = it.articleLink) { link -> onLinkClicked(link) },
                createLinkType(titleRes = R.string.webcast, link = it.webcastLink) { link -> onLinkClicked(link) },
                createLinkType(titleRes = R.string.wikipedia, link = it.wikiLink) { link -> onLinkClicked(link) }
            )
        }
    }

    private fun onLinkClicked(link: String?) {
        setFragmentResult(
            LINKS_KEY,
            bundleOf(LINKS_KEY to link)
        )
        dismiss()
    }

}