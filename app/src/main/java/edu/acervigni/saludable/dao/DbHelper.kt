package edu.acervigni.tp2.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import edu.acervigni.saludable.model.Comida
import edu.acervigni.saludable.model.Usuario
import java.lang.Exception

class DbHelper(
    context: Context, factory: SQLiteDatabase.CursorFactory?
) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {

        private val DATABASE_NAME = "saludable.db"
        private val DATABASE_VERSION = 1

        val TABLE_NAME = "usuarios"
        val COLUMN_ID = "id"
        val COLUMN_N_DOC = "n_doc"
        val COLUMN_NOMBRE = "nombre"
        val COLUMN_APELLIDO = "apellido"
        val COLUMN_FECHA_NAC = "fecha_nac"
        val COLUMN_GENERO = "genero"
        val COLUMN_LOCALIDAD = "localidad"
        val COLUMN_TRATAMIENTO = "tratamiento"
        val COLUMN_USERNAME = "username"
        val COLUMN_PASSWORD = "password"

        val TABLE_NAME_C = "comidas"
        val COLUMN_ID_C = "id"
        val COLUMN_TIPO = "tipo_comida"
        val COLUMN_PRINCIPAL = "principal"
        val COLUMN_SECUNDARIO = "secundario"
        val COLUMN_BEBIDA = "bebida"
        val COLUMN_BPOSTRE = "b_postre"
        val COLUMN_POSTRE = "postre"
        val COLUMN_BTENTACION = "b_tentacion"
        val COLUMN_TENTACION = "tentacion"
        val COLUMN_HAMBRE = "hambre"
        val COLUMN_IDUSUARIO = "id_usuario"
        val COLUMN_FECHA_HORA = "fecha_hora"
    }

    override fun onCreate(db: SQLiteDatabase?){
        var createTable = ("CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TIPO + " TEXT, " + COLUMN_N_DOC + " TEXT, " + COLUMN_NOMBRE + " TEXT, " +
                COLUMN_APELLIDO + " TEXT, " + COLUMN_FECHA_NAC + " TEXT, " + COLUMN_GENERO + " TEXT, " +
                COLUMN_LOCALIDAD + " TEXT, " + COLUMN_TRATAMIENTO + " TEXT, " +
                COLUMN_USERNAME + " TEXT, " + COLUMN_PASSWORD + " TEXT )")

        db?.execSQL(createTable)

        createTable = ("CREATE TABLE " + TABLE_NAME_C + " ( " + COLUMN_ID_C + " INTEGER PRIMARY KEY, " +
                COLUMN_TIPO + " TEXT, " + COLUMN_PRINCIPAL + " TEXT, " + COLUMN_SECUNDARIO + " TEXT, " +
                COLUMN_BEBIDA + " TEXT, " + COLUMN_BPOSTRE + " TEXT, " + COLUMN_POSTRE + " TEXT, " +
                COLUMN_BTENTACION + " TEXT, " + COLUMN_TENTACION + " TEXT, " +
                COLUMN_HAMBRE + " TEXT, " + COLUMN_IDUSUARIO + " INTEGER, " + COLUMN_FECHA_HORA + " TEXT )")

        db?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun saveUsuario (usuario: Usuario) : Boolean {

        try {
            val db = this.writableDatabase

            val values = ContentValues()

            values.put("n_doc",usuario.numeroDocumento)
            values.put("nombre",usuario.nombre)
            values.put("apellido",usuario.apellido)
            values.put("fecha_nac",usuario.fechaNacimiento)
            values.put("genero",usuario.sexo)
            values.put("localidad",usuario.localidad)
            values.put("tratamiento",usuario.tratamiento)
            values.put("username",usuario.username)
            values.put("password",usuario.password)

            db.insert(TABLE_NAME,null,values)
            return true
        } catch (e : Exception){
            Log.e("ERROR", "Error al guardar usuario")
        }
        return false
    }

    fun crearUsuario (cursor: Cursor) : Usuario {
        val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
        val numeroDocumeto = cursor.getString(cursor.getColumnIndex(COLUMN_N_DOC))
        val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE))
        val apellido = cursor.getString(cursor.getColumnIndex(COLUMN_APELLIDO))
        val fecha = cursor.getString(cursor.getColumnIndex(COLUMN_FECHA_NAC))
        val genero = cursor.getString(cursor.getColumnIndex(COLUMN_GENERO))
        val localidad = cursor.getString(cursor.getColumnIndex(COLUMN_LOCALIDAD))
        val tratamiento = cursor.getString(cursor.getColumnIndex(COLUMN_TRATAMIENTO))
        val username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME))
        val password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))

        return Usuario(id,numeroDocumeto,nombre,apellido,fecha,genero,localidad,
           tratamiento,username,password)
    }

    fun obtenerUsuarios () : ArrayList<Usuario>? {
        val usuarios : ArrayList<Usuario> = ArrayList()

        try {

            val db = this.readableDatabase
            val query ="SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_APELLIDO

            val cursor = db.rawQuery(query,null)
            if(cursor.moveToFirst())
            {
                do {
                    val p : Usuario = crearUsuario(cursor)
                    usuarios.add(p)

                } while (cursor.moveToNext())
            }

            return usuarios
        } catch (e: Exception){
            Log.e("ERROR", "Al obtener los usuarios")
        }
        return usuarios
    }

    fun saveComida (comida: Comida) : Boolean {

        try {
            val db = this.writableDatabase

            val values = ContentValues()

            values.put("tipo_comida",comida.tipoComida)
            values.put("principal",comida.comidaPrincipal)
            values.put("secundario",comida.comidaSecundaria)
            values.put("bebida",comida.bebida)
            values.put("b_postre",comida.postre)
            values.put("postre",comida.descPostre)
            values.put("b_tentacion",comida.tentacion)
            values.put("tentacion",comida.descTentacion)
            values.put("hambre",comida.hambre)
            values.put("id_usuario",comida.idUsuario)
            values.put("fecha_hora",comida.fechaHora)

            db.insert(TABLE_NAME_C,null,values)
            return true
        } catch (e : Exception){
            Log.e("ERROR", "Error al guardar comida")
        }
        return false
    }

    fun obtenerComidas () : ArrayList<Comida>? {
        val comidas : ArrayList<Comida> = ArrayList()

        try {

            val db = this.readableDatabase
            val query ="SELECT * FROM " + TABLE_NAME_C

            val cursor = db.rawQuery(query,null)
            if(cursor.moveToFirst())
            {
                do {
                    val c : Comida = crearComida(cursor)
                    comidas.add(c)

                } while (cursor.moveToNext())
            }

            return comidas
        } catch (e: Exception){
            Log.e("ERROR", "Al obtener las comidas")
        }
        return comidas
    }

    fun crearComida (cursor: Cursor) : Comida {
        val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_C))
        val tipo = cursor.getString(cursor.getColumnIndex(COLUMN_TIPO))
        val principal = cursor.getString(cursor.getColumnIndex(COLUMN_PRINCIPAL))
        val secundaria = cursor.getString(cursor.getColumnIndex(COLUMN_SECUNDARIO))
        val bebida = cursor.getString(cursor.getColumnIndex(COLUMN_BEBIDA))
        val bpostre = cursor.getString(cursor.getColumnIndex(COLUMN_BPOSTRE))
        val dpostre = cursor.getString(cursor.getColumnIndex(COLUMN_POSTRE))
        val btentacion = cursor.getString(cursor.getColumnIndex(COLUMN_BTENTACION))
        val dtentacion = cursor.getString(cursor.getColumnIndex(COLUMN_TENTACION))
        val bhambre = cursor.getString(cursor.getColumnIndex(COLUMN_HAMBRE))
        val idUsuario = cursor.getInt(cursor.getColumnIndex(COLUMN_IDUSUARIO))
        val fechaHora = cursor.getString(cursor.getColumnIndex(COLUMN_FECHA_HORA))

        return Comida(id,tipo,principal,secundaria,bebida,bpostre,dpostre,
            btentacion,dtentacion,bhambre,idUsuario,fechaHora)
    }
}