package edu.acervigni.saludable.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Trago(@SerializedName("strDrink") var nombre : String,
                 @SerializedName("strDrinkThumb") var imagen : String,
                 @SerializedName("strAlcoholic") var alcoholic : String,
                 @SerializedName("strCategory") var categoria : String
) : Serializable
