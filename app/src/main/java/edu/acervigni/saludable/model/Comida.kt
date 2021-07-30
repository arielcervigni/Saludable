package edu.acervigni.saludable.model

import java.io.Serializable
import java.math.BigInteger
import java.util.*

data class Comida (val id: Int = 0,
                  var tipoComida: String,
                  var comidaPrincipal: String,
                  var comidaSecundaria: String,
                  var bebida: String,
                  val postre: String,
                  var descPostre : String,
                  var tentacion : String,
                  var descTentacion : String,
                  var hambre : String,
                  var username : String,
                  var fechaHora : String

) : Serializable
