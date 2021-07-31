package edu.acervigni.saludable.dao

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import edu.acervigni.saludable.model.Comida
import edu.acervigni.saludable.model.Usuario
import java.lang.Exception

class BdFirebase  {

    private val db = FirebaseFirestore.getInstance()

    fun guardarComidaFB(comida: Comida): Boolean {

        try {
            db.collection("comidas").document(comida.username + "-" + comida.fechaHora)
                .set(
                    hashMapOf(
                        "tipoComida" to comida.tipoComida,
                        "comidaPrincipal" to comida.comidaPrincipal,
                        "comidaSecundaria" to comida.comidaSecundaria,
                        "bebida" to comida.bebida,
                        "bPostre" to comida.postre,
                        "dPostre" to comida.descPostre,
                        "bTentacion" to comida.tentacion,
                        "dTentacion" to comida.descTentacion,
                        "bHambre" to comida.hambre,
                        "username" to comida.username,
                        "fechaHora" to comida.fechaHora
                    )
                )

            return true
        } catch (e: Exception) {
            Log.e("ERROR FB", e.message.toString())
        }
        return false
    }

    fun guardarUsuarioFB(usuario: Usuario): Boolean {
        try {
            db.collection("usuarios").document(usuario.username)
                .set(
                    hashMapOf(
                        "numeroDocumento" to usuario.numeroDocumento.toString(),
                        "nombre" to usuario.nombre,
                        "apellido" to usuario.apellido,
                        "fechaNacimiento" to usuario.fechaNacimiento,
                        "genero" to usuario.sexo,
                        "localidad" to usuario.localidad,
                        "tratamiento" to usuario.tratamiento,
                        "password" to usuario.password,
                        "username" to usuario.username
                    )
                )
            return true
        } catch (e: Exception) {
            Log.e("ERROR FB", e.message.toString())
        }
        return false
    }

    fun loginFB(username: String) : DocumentReference {
        val rta : DocumentReference = db.collection("usuarios").document(username)
        return rta
    }

    fun obtenerUsuariosFB(): CollectionReference {
        val rta : CollectionReference = db.collection("usuarios")
        return rta
    }
}