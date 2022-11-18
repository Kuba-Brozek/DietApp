package ayathe.project.dietapp.DTO

data class Meal(
    var name: String? = null,
    var date: String? = null,
    var grams: Int? = null,
    var cals: Int? = null
)

data class DayInfo(
    var date: String? = null,
    val dayDescription: String? = null,
    var dayIndex: Int? = null,
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
