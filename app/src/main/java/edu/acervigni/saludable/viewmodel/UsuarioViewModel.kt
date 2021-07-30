package edu.acervigni.saludable.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import edu.acervigni.saludable.dao.BdFirebase
import edu.acervigni.saludable.model.Usuario
import edu.acervigni.tp2.dao.DbHelper

class UsuarioViewModel : ViewModel() {

    /* LOCAL */
    fun guardarUsuario (context: Context, usuario: Usuario) : Int {
        val db = DbHelper (context, null)
        return db.saveUsuario(usuario)
    }
    fun obtenerUsuarios (context: Context) : ArrayList<Usuario>?{
        val db = DbHelper (context, null)
        return db.obtenerUsuarios()
    }
    fun borrarTablaUsuarios (context: Context) : Boolean {
        val db = DbHelper(context,null)
        return db.borrarTablaUsuarios()
    }
    fun login(context: Context, username : String, password : String) : Int {
        val usuarios: ArrayList<Usuario>? = obtenerUsuarios(context)
        Log.d("usuarios", usuarios.toString())
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

    /* FB */
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