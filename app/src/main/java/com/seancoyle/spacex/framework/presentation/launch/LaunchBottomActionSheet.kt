package com.seancoyle.spacex.framework.presentation.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.seancoyle.spacex.R
import com.seancoyle.spacex.business.domain.model.launch.Links
import com.seancoyle.spacex.databinding.FragmentLaunchBottomActionSheetBinding
import com.seancoyle.spacex.framework.presentation.common.gone
import com.seancoyle.spacex.framework.presentation.common.viewBinding


class LaunchBottomActionSheet : BottomSheetDialogFragment() {

    private val binding by viewBinding(FragmentLaunchBottomActionSheetBinding::bind)
    private var links: Links? = null

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_launch_bottom_action_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setListeners()
    }

    private fun setupUI() {
        with(binding) {

            links = arguments?.getParcelable(LINKS_KEY)

            // hide links which are null or empty
            if (links?.articleLink.isNullOrEmpty()) {
                tvArticle.gone()
                divider2.gone()
            }

            if (links?.videoLink.isNullOrEmpty()) {
                tvYoutube.gone()
                divider3.gone()
            }

            if (links?.wikipedia.isNullOrEmpty()) {
                tvWiki.gone()
                divider3.gone()
            }

        }
    }

    private fun setListeners() {
        with(binding) {

            exitBtn.setOnClickListener {
                dismiss()
            }

            // Send the result back to the fragment
            tvArticle.setOnClickListener {
                setFragmentResult(LINKS_KEY, bundleOf(LINKS_KEY to links!!.articleLink))
                dismiss()
            }

            tvYoutube.setOnClickListener {
                setFragmentResult(LINKS_KEY, bundleOf(LINKS_KEY to links!!.videoLink))
                dismiss()
            }

            tvWiki.setOnClickListener {
                setFragmentResult(LINKS_KEY, bundleOf(LINKS_KEY to links!!.wikipedia))
                dismiss()
            }

        }
    }
}