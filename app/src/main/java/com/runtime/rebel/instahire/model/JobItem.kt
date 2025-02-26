package com.runtime.rebel.instahire.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class JobItem (
    @SerializedName("id")
    val id: String,
    @SerializedName("role")
    val role: String? = "Engineer",
    @SerializedName("text")
    val text: String? = "Alphabet is looking to hire Android Dev with 2 years of experience. Must have knowlege in jetpack components, MVVM, gRPC, retrofit etc. Apply now!",
    @SerializedName("company_name")
    val companyName: String? = "Alphabet Inc",
    @SerializedName("location")
    val location: String? = "San Fransisco, CA",
    @SerializedName("url")
    val url: String? = "www.google.com",
    @SerializedName("keywords")
    val keywords: List<String> = emptyList(),
    @SerializedName("date_posted")
    val datePosted: String? = "Today",
    @SerializedName("source")
    val source: String? = "Trust Me bro"
): Parcelable, DiffUtil.ItemCallback<JobItem>() {
    override fun areItemsTheSame(oldItem: JobItem, newItem: JobItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: JobItem, newItem: JobItem): Boolean {
        return oldItem == newItem
    }
}