package com.seancoyle.ui_launch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.seancoyle.launch_models.model.launch.Links
import com.seancoyle.ui_launch.R
import com.seancoyle.ui_launch.databinding.FragmentLaunchBottomActionSheetBinding
import com.seancoyle.ui_launch.ui.composables.LaunchBottomSheetCard
import com.seancoyle.ui_launch.ui.composables.LaunchBottomSheetExitButton


class LaunchBottomActionSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentLaunchBottomActionSheetBinding? = null
    private val binding get() = _binding!!
    private var links: Links? = null

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
        setListeners()
    }

    private fun setupUI() {
        with(binding) {

            links = arguments?.getParcelable(LINKS_KEY)

            linksCv.setContent {
                MaterialTheme {
                    LaunchBottomSheetCard(
                        articleLink = links?.articleLink,
                        webcastLink = links?.videoLink,
                        wikiLink = links?.wikipedia,
                        onArticleLinkClick = { onArticleClick() },
                        onWebcastLinkClick = { onWebClick() },
                        onWikiLinkClick = { onWikiLinkClicked() },
                        onExitBtn = { dismiss() }
                    )
                }
            }

            exitBtn.setContent {
                MaterialTheme {
                    LaunchBottomSheetExitButton{
                        dismiss()
                    }
                }
            }

            /*linksTitle.setContent {
                MaterialTheme {
                    LaunchBottomSheetHeader()
                }
            }

            articleLink.setContent{
                MaterialTheme {
                    LaunchBottomSheetTitle(name = getString(R.string.article))
                }
            }

            webcastLink.setContent{
                MaterialTheme {
                    LaunchBottomSheetTitle(name = getString(R.string.webcast))
                }
            }

            wikiLink.setContent{
                MaterialTheme {
                    LaunchBottomSheetTitle(name = getString(R.string.wikipedia))
                }
            }

            exitBtn.setContent {
                MaterialTheme {
                    LaunchBottomSheetExitButton{
                        dismiss()
                    }
                }
            }

            divider1.setContent {
                MaterialTheme {
                    LaunchBottomSheetDivider()
                }
            }

            divider2.setContent {
                MaterialTheme {
                    LaunchBottomSheetDivider()
                }
            }

            divider3.setContent {
                MaterialTheme {
                    LaunchBottomSheetDivider()
                }
            }*/

            // hide links which are null or empty
          /*  if (links?.articleLink.isNullOrEmpty()) {
                articleLink.gone()
                divider2.gone()
            }

            if (links?.videoLink.isNullOrEmpty()) {
                webcastLink.gone()
                divider3.gone()
            }

            if (links?.wikipedia.isNullOrEmpty()) {
                wikiLink.gone()
                divider3.gone()
            }*/

        }
    }

    private fun setListeners() {
        with(binding) {

            // Send the result back to the fragment
           /* articleLink.setOnClickListener {
                onArticleClick()
            }

            webcastLink.setOnClickListener {
                onWebClick()
            }

            wikiLink.setOnClickListener {
                onWikiLinkClicked()
            }*/

        }
    }

    private fun onWikiLinkClicked() {
        setFragmentResult(
            LINKS_KEY,
            bundleOf(LINKS_KEY to links!!.wikipedia)
        )
        dismiss()
    }

    private fun onWebClick() {
        setFragmentResult(
            LINKS_KEY,
            bundleOf(LINKS_KEY to links!!.videoLink)
        )
        dismiss()
    }

    private fun onArticleClick() {
        setFragmentResult(
            LINKS_KEY,
            bundleOf(LINKS_KEY to links!!.articleLink)
        )
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}