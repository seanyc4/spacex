package com.seancoyle.ui_launch.adapter

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.*
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.launch_domain.model.company.CompanySummary
import com.seancoyle.launch_domain.model.launch.LaunchModel
import com.seancoyle.launch_domain.model.launch.LaunchType
import com.seancoyle.launch_domain.model.launch.Links
import com.seancoyle.launch_domain.model.launch.SectionTitle
import com.seancoyle.ui_launch.glideLoadLaunchImage
import com.seancoyle.ui_launch.databinding.RvCompanyInfoItemBinding
import com.seancoyle.ui_launch.databinding.RvLaunchItemBinding
import com.seancoyle.ui_launch.databinding.RvSectionTitleItemBinding

class LaunchAdapter
constructor(
    private val interaction: Interaction? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<LaunchType>() {

        override fun areItemsTheSame(oldItem: LaunchType, newItem: LaunchType): Boolean {
            return oldItem.type == newItem.type
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: LaunchType, newItem: LaunchType): Boolean {
            return oldItem == newItem
        }

    }

    private val differ =
        AsyncListDiffer(
            LaunchRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(diffCallBack).build()
        )

    internal inner class LaunchRecyclerChangeCallback(
        private val adapter: LaunchAdapter
    ) : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {

            LaunchType.TYPE_TITLE -> {
                return SectionTitleViewHolder(
                    RvSectionTitleItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            LaunchType.TYPE_COMPANY -> {
                return CompanyViewHolder(
                    RvCompanyInfoItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            LaunchType.TYPE_LAUNCH -> {
                return LaunchViewHolder(
                    RvLaunchItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    interaction = interaction
                )
            }

            else -> throw ClassCastException("Unknown viewType $viewType")

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SectionTitleViewHolder -> {
                val sectionItem: SectionTitle = differ.currentList[position] as SectionTitle
                holder.bind(sectionItem)
            }

            is CompanyViewHolder -> {
                val companyInfo: CompanySummary = differ.currentList[position] as CompanySummary
                holder.bind(companyInfo)
            }

            is LaunchViewHolder -> {
                val launchItem: LaunchModel =
                    differ.currentList[position] as LaunchModel
                holder.bind(launchItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].type
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(launchList: List<LaunchType>) {
        val commitCallback = Runnable {
            // if process died must restore list position
             interaction?.restoreListPosition()
        }
        printLogDebug("list_adapter", "size: ${launchList.size}")
        differ.submitList(launchList, commitCallback)
    }

    inner class LaunchViewHolder
    constructor(
        private val binding: RvLaunchItemBinding,
        private val interaction: Interaction?,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LaunchModel) = with(itemView) {
            with(binding) {

                launchImage.glideLoadLaunchImage(item.links.missionImage, true)
                missionData.text = item.missionName
                dateTimeData.text = item.launchDate
                rocketData.text = item.rocket.rocketNameAndType
                daysData.text = item.launchDaysDifference
                launchSuccessImage.setImageResource(item.launchSuccessIcon)
                daysTitle.text = context.getString(item.daysToFromTitle)
            }

            setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item.links)
            }

        }
    }

    inner class CompanyViewHolder
    constructor(
        private val binding: RvCompanyInfoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CompanySummary) {
            with(binding) {

                companyInfo.text = item.summary

            }
        }
    }

    inner class SectionTitleViewHolder
    constructor(
        private val binding: RvSectionTitleItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SectionTitle) {
            with(binding) {
                sectionTitle.text = item.title
            }
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, launchLinks: Links)

        fun restoreListPosition()

    }

}













