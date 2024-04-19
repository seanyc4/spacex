package com.seancoyle.feature.launch.implementation.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.compose.content
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.seancoyle.core.ui.parcelable
import com.seancoyle.core.ui.theme.AppTheme
import com.seancoyle.feature.launch.api.domain.model.LinkType
import com.seancoyle.feature.launch.api.domain.model.Links
import com.seancoyle.feature.launch.implementation.R

@ExperimentalMaterialApi
@ExperimentalMaterial3WindowSizeClassApi
internal class LaunchBottomSheetFragment : BottomSheetDialogFragment() {

    private var linkTypes: List<LinkType>? = null

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        getLinksFromBundle()

        val windowSize = calculateWindowSizeClass(requireActivity())

        AppTheme {
            LaunchBottomSheetScreen(
                linkTypes = linkTypes,
                dismiss = { dismiss() },
                windowSize = windowSize
            )
        }
    }

    private fun getLinksFromBundle() {
        val links: Links? = arguments?.parcelable(LINKS_KEY)
        links?.let {
            linkTypes = listOfNotNull(
                createLinkType(
                    titleRes = R.string.article,
                    link = it.articleLink
                ) { link -> onLinkClicked(link) },
                createLinkType(
                    titleRes = R.string.webcast,
                    link = it.webcastLink
                ) { link -> onLinkClicked(link) },
                createLinkType(
                    titleRes = R.string.wikipedia,
                    link = it.wikiLink
                ) { link -> onLinkClicked(link) }
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