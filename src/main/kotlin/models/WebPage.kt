package models

data class Date (
        val year : Int,
        val month : Int,
        val day : Int
)

data class WebPage (
    var url : String,
    var keywords : List<String>,
    var published : Date
)