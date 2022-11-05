package ayathe.project.scheduleapp.DTO

data class Day(
    var name: String? = null,
    var date: String? = null,
    var description: String? = null,
    var category: String? = null
)

data class Day2(
    var date: String? = null,
    var kcalGoal: Int? = null,
    var kcalEaten: Int? = null,
    var productsEaten: List<Product>? = null,
    var activitiesMade: List<Int>? = null,
    var kcalBurnt: Int? = null
)

data class Product(
    var name: String? = null,
    var productWeight: Int? = null,
    val kcalIn100: Int? = null,
)

data class ProductFromJSON(
    var Nazwa: String? = null,
    var Kcal: Int? = null,
    var Weglowodany: Double? = null,
    var Bialko: Double? = null,
    var Tluszcz: Double? = null
)
