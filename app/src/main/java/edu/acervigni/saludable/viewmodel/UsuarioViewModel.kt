package edu.acervigni.saludable.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import edu.acervigni.saludable.dao.BdFirebase
import edu.acervigni.saludable.model.Usuario
import edu.acervigni.tp2.dao.DbHelper

class UsuarioViewModel : ViewModel() {

    fun guardarUsuario (context: Context, usuario: Usuario) : Boolean {
        val db = DbHelper (context, null)
        return db.saveUsuario(usuario)
    }
    fun obtenerUsuarios (context: Context) : ArrayList<Usuario>?{
        val db = DbHelper (context, null)
        return db.obtenerUsuarios()
    }
    fun login(context: Context, username : String, password : String) : Int {
        val usuarios: ArrayList<Usuario>? = obtenerUsuarios(context)
        if(usuarios != null)
        {
            for(u : Usuario in usuarios)
            {
                if(u.username.equals(username) && u.password.equals(password))
                    return u.id
            }
        }
        return -1
    }

    fun guardarUsuarioFB (usuario: Usuario) : Boolean {
        val dbFB = BdFirebase()
        return dbFB.guardarUsuarioFB(usuario)
    }

    /* ME GUSTARÍA QUE ESTE VERIFIQUE SI EL USUARIO Y EL PW SON CORRECTOS Y DEVUELVA UN TRUE */
    fun loginFB (username : String) : DocumentReference {
        val dbFB = BdFirebase()
        val document = dbFB.loginFB(username)
        return document

    }

}