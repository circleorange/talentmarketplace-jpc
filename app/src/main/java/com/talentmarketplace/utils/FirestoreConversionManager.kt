package com.talentmarketplace.utils

import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class FirestoreConversionManager {
    companion object {
        fun payRangeToString(payRange: ClosedFloatingPointRange<Float>): String {
            return "${payRange.start}-${payRange.endInclusive}"
        }

        fun payRangeFromString(payRangeString: String): ClosedFloatingPointRange<Float> {
            val (start, end) = payRangeString.split("-").map { it.toFloat() }
            return start..end
        }

        fun localDateToTimestamp(localDate: LocalDate): Timestamp {
            val instant = localDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()

            val date = Date.from(instant)

            return Timestamp(date)
        }

        fun localDateFromTimestamp(timestamp: Timestamp): LocalDate {
            return timestamp
                .toDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
    }
}