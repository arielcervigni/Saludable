package edu.acervigni.saludable.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import edu.acervigni.saludable.dao.BdFirebase
import edu.acervigni.saludable.model.Comida
import edu.acervigni.tp2.dao.DbHelper

class ComidaViewModel : ViewModel() {

    fun guardarComida (context: Context, comida: Comida) : Boolean {
        val db = DbHelper (context, null)
        return db.saveComida(comida)
    }
    fun obtenerComidas(context: Context) : ArrayList<Comida>?{
        val db = DbHelper (context, null)
        return db.obtenerComidas()
    }
    fun borrarTablaComidas(context: Context) : Boolean {
        val db = DbHelper (context, null)
        return db.borrarTablaComidas()
    }

    fun guardarComidaFB (comida: Comida) : Boolean {
        val bdFb = BdFirebase()
        return bdFb.guardarComidaFB(comida)
    }
}