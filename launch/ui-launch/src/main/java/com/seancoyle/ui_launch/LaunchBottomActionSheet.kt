package com.seancoyle.ui_launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.seancoyle.launch_models.model.launch.Links
import com.seancoyle.ui_base.gone
import com.seancoyle.ui_launch.databinding.FragmentLaunchBottomActionSheetBinding


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

            links = arguments?.getParcelable(com.seancoyle.ui_launch.LINKS_KEY)

            // hide links which are null or empty
            if (links?.articleLink.isNullOrEmpty()) {
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
            }

        }
    }

    private fun setListeners() {
        with(binding) {

            exitBtn.setOnClickListener {
                dismiss()
            }

            // Send the result back to the fragment
            articleLink.setOnClickListener {
                setFragmentResult(
                    LINKS_KEY,
                    bundleOf(LINKS_KEY to links!!.articleLink)
                )
                dismiss()
            }

            webcastLink.setOnClickListener {
                setFragmentResult(
                    LINKS_KEY,
                    bundleOf(LINKS_KEY to links!!.videoLink)
                )
                dismiss()
            }

            wikiLink.setOnClickListener {
                setFragmentResult(
                    LINKS_KEY,
                    bundleOf(LINKS_KEY to links!!.wikipedia)
                )
                dismiss()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}