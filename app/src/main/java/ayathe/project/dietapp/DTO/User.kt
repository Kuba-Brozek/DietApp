package ayathe.project.dietapp.DTO


data class User(
    var userId: String? = null,
    var username: String? = null,
    var email: String? = null,
    var image: String? = null,
    var age: Int? = null,
    var weight: Double? = null,
    var height: Int? = null,
    var destination: String? = null,
    var startingDate: String? = null
)

