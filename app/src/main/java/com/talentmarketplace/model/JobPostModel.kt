package com.talentmarketplace.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import java.util.UUID

data class JobPostModel(
    var jobPostID: String = "",
    val userID: String = "",
    var companyName: String = "",
    var title: String = "",
    var description: String = "",
    var payRange: String = "",
    var startDate: Timestamp = Timestamp(0, 0),
    var location: LatLng? = null
)
