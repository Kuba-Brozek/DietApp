package ayathe.project.dietapp.DTO

data class DayInfo(
    var date: String? = null,
    var dayIndex: Int? = null,
    var kcalGoal: Int? = null,
    var kcalEaten: Int? = null,
    var activitiesMade: List<String>? = null,
    var kcalBurnt: Int? = null
)