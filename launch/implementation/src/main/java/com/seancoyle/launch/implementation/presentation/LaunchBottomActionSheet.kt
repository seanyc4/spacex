package com.seancoyle.launch.implementation.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.seancoyle.launch.api.model.LinkType
import com.seancoyle.launch.api.model.Links
import com.seancoyle.launch.implementation.R
import com.seancoyle.launch.implementation.presentation.composables.LaunchBottomSheetCard
import com.seancoyle.launch.implementation.presentation.composables.LaunchBottomSheetExitButton

class LaunchBottomActionSheet : BottomSheetDialogFragment() {

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
                    LaunchBottomSheetCard(
                        linkTypes = linkTypes
                    )

                    LaunchBottomSheetExitButton {
                        dismiss()
                    }
                }
            }
        }
    }

    private fun getLinksFromBundle() {
        val links: Links? = arguments?.getParcelable(LINKS_KEY)
        links?.let {
            linkTypes = listOfNotNull(
                createLinkType(R.string.article, it.articleLink),
                createLinkType(R.string.webcast, it.webcastLink),
                createLinkType(R.string.wikipedia, it.wikiLink)
            )
        }
    }

    private fun createLinkType(
        @StringRes titleRes: Int,
        link: String?
    ): LinkType? {
        return link?.let {
            LinkType(
                titleRes, it,
                onClick = { onLinkClicked(link) }
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