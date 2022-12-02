package ayathe.project.dietapp.DTO

import com.google.gson.annotations.SerializedName

data class ProductFromJSON(
    @SerializedName("Nazwa")
    var name: String? = null,
    @SerializedName("Kcal")
    var kcal: Int? = null,
    @SerializedName("Weglowodany")
    var carbs: Double? = null,
    @SerializedName("Bialko")
    var protein: Double? = null,
    @SerializedName("Tluszcz")
    var fat: Double? = null
)