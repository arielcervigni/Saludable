package edu.acervigni.saludable.model

import java.io.Serializable
import java.math.BigInteger

data class Usuario (
    val id: Int = 0,
    var numeroDocumento: String,
    var nombre: String,
    var apellido: String,
    val fechaNacimiento: String,
    var sexo : String,
    var localidad : String,
    var tratamiento : String,
    var username : String,
    var password : String

) : Serializable