package edu.acervigni.saludable.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.getField
import com.google.gson.Gson
import edu.acervigni.saludable.R
import edu.acervigni.saludable.connectionHelper.ConnectionHelper
import edu.acervigni.saludable.databinding.ActivityRegistrarseBinding
import edu.acervigni.saludable.model.Usuario
import edu.acervigni.saludable.viewmodel.UsuarioViewModel
import java.util.*
import kotlin.collections.ArrayList

class RegistrarseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarseBinding
    private lateinit var usuarioViewModel : UsuarioViewModel
    private lateinit var usernames : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarseBinding.inflate(layoutInflater)
        usernames = ArrayList()

        setContentView(binding.root)

        usuarioViewModel = ViewModelProvider(this).get(UsuarioViewModel::class.java)

        if(ConnectionHelper.hayInternet(this))
            cargarUsuarios()

        cargarTratamientos()
        cargarDatePicker()


        binding.rBtnGuardarRegistro.setOnClickListener {
            val usuario : Usuario? = crearUsuario()
            if(usuario != null){
                if(!ConnectionHelper.hayInternet(it.context))
                    guardarUsuarioLocal(it.context,usuario)
                else{
                    guardarUsuarioFB(it.context, usuario)
                }

            }
        }

        binding.rBtnCerrarSesion.setOnClickListener {
            startActivity(Intent(it.context,LoginActivity::class.java))
            finish()
        }
    }

    fun cargarTratamientos() {

        val tratamientos: AutoCompleteTextView = binding.rAcTratamiento
        val items = listOf("Anorexia", "Bulimia", "Obesidad")
        val adapter = ArrayAdapter(this, R.layout.items_tipo, items)
        (tratamientos as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    fun cargarDatePicker() {

        val fechaNac: TextInputEditText = binding.rEtFechaNac
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecciona tu fecha de nacimiento")
                .setSelection(System.currentTimeMillis())
                .build()

        fechaNac.setOnClickListener {
            datePicker.show(supportFragmentManager, "FECHA_NACIMIENTO")
        }

        datePicker.addOnPositiveButtonClickListener {
            fechaNac.setText(datePicker.headerText)
        }
    }

    fun crearUsuario() : Usuario? {

        if(binding.rEtDocumento.text.isNullOrBlank() || binding.rEtDocumento.text!!.length < 7) {
            Toast.makeText(this,"Verifique N° de Documento", Toast.LENGTH_SHORT).show()
            return null
        }
        if(usernames.contains(binding.rEtDocumento.text.toString())){
            Toast.makeText(this,"El N° de Documento ya se encuentra registrado", Toast.LENGTH_SHORT).show()
            return null
        }

        if(binding.rEtNombre.text.isNullOrEmpty()) {
            Toast.makeText(this,"Complete el nombre", Toast.LENGTH_SHORT).show()
            return null
        }
        if(binding.rEtApellido.text.isNullOrEmpty()) {
            Toast.makeText(this,"Complete el apellido", Toast.LENGTH_SHORT).show()
            return null
        }

        if(binding.rEtFechaNac.text.isNullOrEmpty()) {
            Toast.makeText(this,"Complete fecha de nacimiento", Toast.LENGTH_SHORT).show()
            return null
        }

        val genero : String = obtenerGenero()

        if(binding.rEtLocalidad.text.isNullOrEmpty()) {
            Toast.makeText(this,"Complete la localidad", Toast.LENGTH_SHORT).show()
            return null
        }

        if(binding.rAcTratamiento.text.toString().equals("Tratamiento")){
            Toast.makeText(this,"Complete su tratamiento", Toast.LENGTH_SHORT).show()
            return null
        }

        if(binding.rEtUsername.text.isNullOrEmpty()){
            Toast.makeText(this,"Complete su usuario", Toast.LENGTH_SHORT).show()
            return null
        }

        if(binding.rEtPassword.text.isNullOrEmpty()){
            Toast.makeText(this,"Complete su contraseña", Toast.LENGTH_SHORT).show()
            return null
        }

        if(usernames.contains(binding.rEtUsername.text.toString())){
            Toast.makeText(this,"El nombre de usuario elegido ya se encuentra registrado",Toast.LENGTH_SHORT).show()
            return null
        }

        if(binding.rEtConfPassword.text.isNullOrEmpty()){
            Toast.makeText(this,"Complete verificar contraseña",Toast.LENGTH_SHORT).show()
            if(!binding.rEtConfPassword.text?.equals(binding.rEtPassword.text)!!) {
                Toast.makeText(this,"Verifique su contraseña",Toast.LENGTH_SHORT).show()
                return null
            }
            return null
        }




        return Usuario(0,binding.rEtDocumento.text.toString(),
            binding.rEtNombre.text.toString(),
            binding.rEtApellido.text.toString(),
            binding.rEtFechaNac.text.toString(),
            genero,
            binding.rEtLocalidad.text.toString(),
            binding.rAcTratamiento.text.toString(),
            binding.rEtUsername.text.toString(),
            binding.rEtPassword.text.toString())

    }

    private fun obtenerGenero(): String {
        return when (binding.rRgSexo.checkedRadioButtonId) {
            binding.rRbMasculino.id -> binding.rRbMasculino.text.toString()
            binding.rRbFemenino.id -> binding.rRbFemenino.text.toString()
            else -> binding.rRbOtro.text.toString()
        }
    }

    private fun guardarUsuarioLocal (context : Context, usuario : Usuario) {

        when (usuarioViewModel.guardarUsuario(context,usuario)) {
            1 -> {
                Toast.makeText(context, "USUARIO REGISTRADO CON ÉXITO !!", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(context, LoginActivity::class.java))
                finish()
            }
            2 -> { Toast.makeText(context,"EL N° DE DNI YA SE ENCUENTRA REGISTRADO !!",Toast.LENGTH_SHORT).show() }
            3 -> { Toast.makeText(context,"EL USUARIO YA SE ENCUENTRA REGISTRADO !!",Toast.LENGTH_SHORT).show() }
            else -> { Toast.makeText(context,"ERROR AL REGISTRAR EL USUARIO !!",Toast.LENGTH_SHORT).show() }
        }
    }

    private fun guardarUsuarioFB (context: Context, usuario : Usuario) {
        if(usuarioViewModel.guardarUsuarioFB(usuario)) {
            Toast.makeText(context,"USUARIO REGISTRADO CON ÉXITO !!",Toast.LENGTH_SHORT).show()
            startActivity(Intent(context,LoginActivity::class.java))
            finish()
        } else {
            Toast.makeText(context,"ERROR AL REGISTRAR EL USUARIO !!",Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarUsuarios () {
        val docRef = usuarioViewModel.obtenerUsuariosFB()
        docRef.get().addOnSuccessListener { result ->
            for (document in result)
            {
                try {
                    var data = document.getString("username")
                    if (data != null)
                        usernames.add(data)
                    data = document.getString("numeroDocumento")
                    if(data != null)
                        usernames.add(data)

                } catch (e : Exception) {
                    Log.e("Usuarios Ex: ",e.message.toString())
                }

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
}