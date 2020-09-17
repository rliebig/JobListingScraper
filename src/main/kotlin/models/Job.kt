package models

import java.util.*

data class Job (
    val webPage : WebPage,
    val filteredText : String,
    val published : Date
)