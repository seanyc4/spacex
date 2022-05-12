package com.seancoyle.spacex.framework.presentation.launch.adapter

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.seancoyle.spacex.business.domain.model.company.CompanySummary
import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.business.domain.model.launch.LaunchType
import com.seancoyle.spacex.business.domain.model.launch.SectionTitle
import com.seancoyle.spacex.databinding.RvCompanyInfoItemBinding
import com.seancoyle.spacex.databinding.RvLaunchItemBinding
import com.seancoyle.spacex.databinding.RvSectionTitleItemBinding
import com.seancoyle.spacex.framework.presentation.launch.glideLoadLaunchImage

class LaunchAdapter
constructor(
    private val interaction: Interaction? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var launchList: List<LaunchType> = emptyList()

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
                val sectionItem: SectionTitle = launchList[position] as SectionTitle
                holder.bind(sectionItem)
            }

            is CompanyViewHolder -> {
                val companyInfo: CompanySummary = launchList[position] as CompanySummary
                holder.bind(companyInfo)
            }

            is LaunchViewHolder -> {
                val launchItem: LaunchDomainEntity = launchList[position] as LaunchDomainEntity
                holder.bind(launchItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return launchList[position].type
    }

    override fun getItemCount(): Int {
        return if (!launchList.isNullOrEmpty()) {
            launchList.size
        } else 0
    }

    fun updateAdapter(launchList: List<LaunchType>) {
        this.launchList = launchList
        notifyDataSetChanged()
    }

    inner class LaunchViewHolder
    constructor(
        private val binding: RvLaunchItemBinding,
        private val interaction: Interaction?,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LaunchDomainEntity) = with(itemView) {
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
                interaction?.onItemSelected(absoluteAdapterPosition, item)
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

        fun onItemSelected(position: Int, selectedItem: LaunchDomainEntity)

    }

}













