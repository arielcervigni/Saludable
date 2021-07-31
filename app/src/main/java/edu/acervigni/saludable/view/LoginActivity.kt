package edu.acervigni.saludable.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityDiagnosticsManager
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.text.BoringLayout
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
    private lateinit var usernames : ArrayList<String>
    private var comidas : ArrayList<Comida>? = null
    private var usuarios : ArrayList<Usuario>? = null


    private lateinit var usernameOld : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioViewModel = ViewModelProvider(this).get(UsuarioViewModel::class.java)
        comidaViewModel = ViewModelProvider(this).get(ComidaViewModel::class.java)
        usuarios = ArrayList()
        comidas = ArrayList()
        usernames = ArrayList()

        if(!ConnectionHelper.hayInternet(this)) {
            ConnectionHelper.crearAlertDialogoInternet(this)
        } else {
            usuarios = usuarioViewModel.obtenerUsuarios(this)
            comidas = comidaViewModel.obtenerComidas(this)
            if(usuarios!!.isNotEmpty() || comidas!!.isNotEmpty())
                crearAlertDialogoSincro(this)
        }


        binding.btnLIniciar.setOnClickListener {

            binding.lProgressbar.visibility = View.VISIBLE
            if(!binding.etLUsername.text.isNullOrEmpty() && !binding.etLPassword.text.isNullOrEmpty()) {
                if (!ConnectionHelper.hayInternet(this)) {
                    loginLocal(it.context)
                } else {
                    loginFB(it.context)
                }
            } else {
                Toast.makeText(this,"INGRESE NOMBRE DE USUARIO Y/O CONTRASEÑA",Toast.LENGTH_SHORT).show()
            }


        }

        binding.btnLRegistrar.setOnClickListener {
            startActivity(Intent(it.context, RegistrarseActivity::class.java))
            finish()

        }

    }

    private fun loginLocal (context : Context) {
        val idUsuario = usuarioViewModel.login(
        this, binding.etLUsername.text.toString(), binding.etLPassword.text.toString())

        if (idUsuario > 0) {
            val i = Intent(context, ComidaActivity::class.java)
            i.putExtra("username",binding.etLUsername.text.toString())
            startActivity(i)
            finish()
        } else {
            binding.lResultado.text = "USUARIO O CONTRASEÑA INCORRECTO"
        }
    }

    private fun loginFB(context : Context) {
        /*  CREO QUE ESTO NO VA ACÁ PERO NO PUDE HACER QUE ME DEVUELVA UN TRUE O FALSE EL UsuarioViewModel  */
        val docRef = usuarioViewModel.loginFB(binding.etLUsername.text.toString())

        docRef.get().addOnSuccessListener { documentSnapshot ->
            val password = documentSnapshot.data?.get("password")

            if(password == binding.etLPassword.text.toString()) {
                val i = Intent(context, ComidaActivity::class.java)
                i.putExtra("username",binding.etLUsername.text.toString())
                startActivity(i)
                finish()
            }
        }
    }

    private fun sincronizarUsuarios (context: Context) : Boolean {
        usuarios = usuarioViewModel.obtenerUsuarios(context)
        if(!usuarios?.isEmpty()!!) {
            for (u: Usuario in usuarios!!) {
                    if(usuarioViewModel.guardarUsuarioFB(u)) {
                        binding.lResultado.text ="USUARIOS SINCRONIZADOS"
                    }
                }
            }
            if(usuarioViewModel.borrarTablaUsuarios(context)) {
                return true
            }
        return false
    }

    private fun sincronizarComidas (context: Context) : Boolean {
        val comidas : ArrayList<Comida>? = comidaViewModel.obtenerComidas(context)
        if(!comidas?.isEmpty()!!) {
            for (c: Comida in comidas) {
                if(comidaViewModel.guardarComidaFB(c)) {
                    //Toast.makeText(this,"USUARIOS SINCRONIZADOS",Toast.LENGTH_SHORT).show()
                    Log.d("Sincro", "comidas" + c.username)
                }
            }
            if(comidaViewModel.borrarTablaComidas(context)) {
                Log.d("Sincro", comidas.size.toString() + " comidas guardadas correctamente")
                return true
            }
        }
        return false
    }

    private fun crearAlertDialogoSincro (context: Context) {
        val alert : AlertDialog? = AlertDialog.Builder(context).setIcon(R.drawable.ic_baseline_cancel_24)
            .setTitle("Tenemos datos para guardar.")
            .setMessage("¿Desea sincronizar ahora la aplicación?.\n\n")
            .setCancelable(true)
            .setPositiveButton("ACEPTAR", DialogInterface.OnClickListener { dialogInterface, i ->

                var rta = false
                if(usuarios!!.isNotEmpty())
                    if(sincronizarUsuarios(context))
                        rta = true

                if(comidas!!.isNotEmpty())
                    if(sincronizarComidas(context))
                        rta = true

                if(rta)
                    binding.lResultado.text = "DATOS GUARDADOS CORRECTAMENTE"

            })
            .show()
    }

}
