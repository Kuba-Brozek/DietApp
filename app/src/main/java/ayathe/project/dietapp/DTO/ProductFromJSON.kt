package ayathe.project.dietapp.DTO

data class ProductFromJSON(
    var Nazwa: String? = null,
    var Kcal: Int? = null,
    var Weglowodany: Double? = null,
    var Bialko: Double? = null,
    var Tluszcz: Double? = null
)