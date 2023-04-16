package com.example.dietapp2.DTO

data class Sport(
    var name: String = "",
    var kcal1hr: Int = 0
){
    override fun toString(): String {
        return "$name / kcal: $kcal1hr"
    }
}
