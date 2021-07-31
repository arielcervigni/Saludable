package edu.acervigni.saludable.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.acervigni.saludable.R
import edu.acervigni.saludable.connectionHelper.ConnectionHelper
import edu.acervigni.saludable.databinding.ActivityLoginBinding
import edu.acervigni.saludable.model.Comida
import edu.acervigni.saludable.model.Usuario
import edu.acervigni.saludable.viewmodel.ComidaViewModel
import edu.acervigni.saludable.viewmodel.UsuarioViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var usuarioViewModel: UsuarioViewModel
    private lateinit var comidaViewModel: ComidaViewModel
    private lateinit var usernames: ArrayList<String>
    private var comidas: ArrayList<Comida>? = null
    private var usuarios: ArrayList<Usuario>? = null
    private lateinit var oldUsername: String
    private lateinit var usuarioEditado: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioViewModel = ViewModelProvider(this).get(UsuarioViewModel::class.java)
        comidaViewModel = ViewModelProvider(this).get(ComidaViewModel::class.java)
        usuarios = ArrayList()
        comidas = ArrayList()
        usernames = ArrayList()
        oldUsername = ""

        usuarios = usuarioViewModel.obtenerUsuarios(this)
        comidas = comidaViewModel.obtenerComidas(this)

        Log.d("ARIEL", usuarios.toString())
        Log.d("ARIEL", comidas.toString())

        if (!ConnectionHelper.hayInternet(this)) {
            ConnectionHelper.crearAlertDialogoInternet(this)


        } else {
            cargarUsuarios()

            if (usuarios!!.isNotEmpty() || comidas!!.isNotEmpty())
                crearAlertDialogoSincro(this)
        }


        binding.btnLIniciar.setOnClickListener {

            binding.lProgressbar.visibility = View.VISIBLE
            if (!binding.etLUsername.text.isNullOrEmpty() && !binding.etLPassword.text.isNullOrEmpty()) {
                if (!ConnectionHelper.hayInternet(this)) {
                    loginLocal(it.context)
                } else {
                    loginFB(it.context)
                }
            } else {
                Toast.makeText(this, "INGRESE NOMBRE DE USUARIO Y/O CONTRASEÑA", Toast.LENGTH_SHORT)
                    .show()
            }


        }

        binding.btnLRegistrar.setOnClickListener {
            startActivity(Intent(it.context, RegistrarseActivity::class.java))
            finish()

        }

        binding.btnLCambiarUsername.setOnClickListener {
            if (!binding.etLNuevousername.text.isNullOrEmpty()) {
                if (usernames.contains(binding.etLNuevousername.text.toString())) {
                    binding.lResultado.text =
                        "El nombre de usuario ingresado no se encuentra disponible."
                } else {
                    usuarioEditado.username = binding.etLNuevousername.text.toString()


                    if (usuarioViewModel.guardarUsuarioFB(usuarioEditado)) {
                        if (usuarioViewModel.borrarTablaUsuarios(it.context)) {
                            binding.tipnuevoNombre.visibility = View.GONE
                            binding.btnLCambiarUsername.visibility = View.GONE
                            binding.tipNombre.visibility = View.VISIBLE
                            binding.tipPassword.visibility = View.VISIBLE
                            binding.btnLIniciar.visibility = View.VISIBLE
                            binding.btnLRegistrar.visibility = View.VISIBLE

                            if(comidas!!.isNotEmpty()){
                                if(sincronizarComidas(it.context)) {
                                    binding.lResultado.text = "DATOS GUARDADOS CORRECTAMENTE"
                                }

                            }
                        }
                    }
                }
            } else {
                binding.lResultado.text = "Ingrese un nombre de usuario"
            }

        }

    }

    private fun loginLocal(context: Context) {
        val idUsuario = usuarioViewModel.login(
            this, binding.etLUsername.text.toString(), binding.etLPassword.text.toString()
        )

        if (idUsuario > 0) {
            val i = Intent(context, ComidaActivity::class.java)
            i.putExtra("username", binding.etLUsername.text.toString())
            startActivity(i)
            finish()
        } else {
            binding.lResultado.text = "USUARIO O CONTRASEÑA INCORRECTO"
            binding.lProgressbar.visibility = View.GONE
        }
    }

    private fun loginFB(context: Context) {
        /*  CREO QUE ESTO NO VA ACÁ PERO NO PUDE HACER QUE ME DEVUELVA UN TRUE O FALSE EL UsuarioViewModel  */
        val docRef = usuarioViewModel.loginFB(binding.etLUsername.text.toString())

        docRef.get().addOnSuccessListener { documentSnapshot ->
            val password = documentSnapshot.data?.get("password")

            if (password == binding.etLPassword.text.toString()) {
                val i = Intent(context, ComidaActivity::class.java)
                i.putExtra("username", binding.etLUsername.text.toString())
                startActivity(i)
                finish()
            } else {
                binding.lResultado.text = "USUARIO O CONTRASEÑA INCORRECTO"
                binding.lProgressbar.visibility = View.GONE
            }

        }
    }

    private fun sincronizarUsuarios(context: Context): Boolean {
        usuarios = usuarioViewModel.obtenerUsuarios(context)
        var rta = false
        if (!usuarios?.isEmpty()!!) {

            /* Esto es para verificar el usuario*/

            if (usuarios != null) {
                for (u: Usuario in usuarios!!) {
                    val index = usernames.indexOf(u.username)
                    if (index >= 0) {
                        val dni = usernames[index + 1]
                        val alert =
                            AlertDialog.Builder(context).setIcon(R.drawable.ic_baseline_cancel_24)
                                .setTitle("Error al guardar usuario.")
                                .setMessage("Su dni es: " + dni + "?")
                                .setNegativeButton("NO") { _, _ ->
                                    oldUsername = u.username
                                    Log.d("ARIEL", "oldUsername:" + oldUsername)
                                    usuarioEditado = u
                                    binding.tipnuevoNombre.visibility = View.VISIBLE
                                    binding.btnLCambiarUsername.visibility = View.VISIBLE
                                    binding.lResultado.text = "Ingrese su nuevo usuario."
                                    binding.tipNombre.visibility = View.GONE
                                    binding.tipPassword.visibility = View.GONE
                                    binding.btnLIniciar.visibility = View.GONE
                                    binding.btnLRegistrar.visibility = View.GONE

                                }
                                .setPositiveButton("SI") { _, _ ->
                                    rta = usuarioViewModel.borrarTablaUsuarios(context)
                                }
                                .show()
                        return rta
                    } else {
                        if (usuarioViewModel.guardarUsuarioFB(u)) {
                            if (usuarioViewModel.borrarTablaUsuarios(context)) {
                                return true
                            }
                        }
                    }

                }
            }
        }
        return false
    }


    /* Esto es el original */

//            for (u: Usuario in usuarios!!) {
//                    if(usuarioViewModel.guardarUsuarioFB(u)) {
//                        binding.lResultado.text ="USUARIOS SINCRONIZADOS"
//                    }
//                }
//            }
//            if(usuarioViewModel.borrarTablaUsuarios(context)) {
//                return true
//         }
//        return false
//    }

    private fun sincronizarComidas(context: Context): Boolean {
        val comidas: ArrayList<Comida>? = comidaViewModel.obtenerComidas(context)
        if (comidas?.isNotEmpty()!!) {
            for (c: Comida in comidas) {
                Log.d("ARIEL", "c.username : " + c.username + " oldUsername: " + oldUsername)
                if (c.username == oldUsername)
                    c.username = usuarioEditado.username

                if (comidaViewModel.guardarComidaFB(c)) {
                    //Toast.makeText(this,"USUARIOS SINCRONIZADOS",Toast.LENGTH_SHORT).show()
                    Log.d("ARIEL", "comidas" + c.username)
                }
            }
            if (comidaViewModel.borrarTablaComidas(context)) {
                oldUsername = ""
                return true
            }
        }
        return false
    }

    private fun crearAlertDialogoSincro(context: Context) {
        val alert: AlertDialog? =
            AlertDialog.Builder(context).setIcon(R.drawable.ic_baseline_cancel_24)
                .setTitle("Tenemos datos para guardar.")
                .setMessage("¿Desea sincronizar ahora la aplicación?.\n\n")
                .setCancelable(true)
                .setPositiveButton(
                    "ACEPTAR",
                    DialogInterface.OnClickListener { dialogInterface, i ->

                        var rta = false
                        if (usuarios!!.isNotEmpty() && comidas!!.isNotEmpty()) {
                            if (sincronizarUsuarios(context)) {
                                if (sincronizarComidas(context))
                                    rta = true
                            }
                        } else if (usuarios!!.isEmpty() && comidas!!.isNotEmpty()) {
                            if (sincronizarComidas(context))
                                rta = true
                        } else {
                            if (sincronizarUsuarios(context))
                                rta = true
                        }

                        if (rta)
                            binding.lResultado.text = "DATOS GUARDADOS CORRECTAMENTE"

                    })
                .show()
    }


    private fun cargarUsuarios() {
        val docRef = usuarioViewModel.obtenerUsuariosFB()
        docRef.get().addOnSuccessListener { result ->
            for (document in result) {
                try {
                    var data = document.getString("username")
                    if (data != null)
                        usernames.add(data)

                    data = document.getString("numeroDocumento")
                    if (data != null)
                        usernames.add(data)

                } catch (e: Exception) {
                    Log.d("Usuarios Ex: ", e.message.toString())
                }

            }
        }
    }
}
