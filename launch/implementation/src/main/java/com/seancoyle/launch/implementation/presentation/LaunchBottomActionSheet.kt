package com.seancoyle.launch.implementation.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.seancoyle.launch.api.model.LinkType
import com.seancoyle.launch.api.model.Links
import com.seancoyle.launch.implementation.R
import com.seancoyle.launch.implementation.databinding.FragmentLaunchBottomActionSheetBinding
import com.seancoyle.launch.implementation.presentation.composables.LaunchBottomSheetCard
import com.seancoyle.launch.implementation.presentation.composables.LaunchBottomSheetExitButton

class LaunchBottomActionSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentLaunchBottomActionSheetBinding? = null
    private val binding get() = _binding!!
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
        _binding = FragmentLaunchBottomActionSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        with(binding) {

           val links: Links? = arguments?.getParcelable(LINKS_KEY)
            links?.apply {
                 linkTypes = listOfNotNull(
                    articleLink?.let {
                        LinkType(
                            R.string.article,
                            it
                        ) { onLinkClicked(it) }
                    },
                    webcastLink?.let {
                        LinkType(
                            R.string.webcast,
                            it
                        ) { onLinkClicked(it) }
                    },
                    wikiLink?.let {
                        LinkType(
                            R.string.wikipedia,
                            it
                        ) { onLinkClicked(it) }
                    }
                )
            }

            linksCv.setContent {
                MaterialTheme {
                    LaunchBottomSheetCard(
                        linkTypes = linkTypes
                    )
                }
            }

            exitBtn.setContent {
                MaterialTheme {
                    LaunchBottomSheetExitButton {
                        dismiss()
                    }
                }
            }
        }
    }

    private fun onLinkClicked(link: String?) {
        setFragmentResult(
            LINKS_KEY,
            bundleOf(LINKS_KEY to link)
        )
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}