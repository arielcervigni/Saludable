package edu.acervigni.saludable.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import edu.acervigni.saludable.databinding.ActivityLoginBinding
import edu.acervigni.saludable.model.Usuario
import edu.acervigni.saludable.viewmodel.UsuarioViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var usuarioViewModel: UsuarioViewModel
    private var usuarios: ArrayList<Usuario>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioViewModel = ViewModelProvider(this).get(UsuarioViewModel::class.java)
        usuarios = usuarioViewModel.obtenerUsuarios(this)


        binding.btnLIniciar.setOnClickListener {

        /* CREO QUE ESTO NO VA ACÁ PERO NO PUDE HACER QUE ME DEVUELVA UN TRUE O FALSE EL UsuarioViewModel*/
            val docRef = usuarioViewModel.loginFB(binding.etLUsername.text.toString())

            docRef.get().addOnSuccessListener { documentSnapshot ->
                val password = documentSnapshot.data?.get("password")

                if(password == binding.etLPassword.text.toString()) {
                    val i = Intent(it.context, ComidaActivity::class.java)
                    i.putExtra("username",binding.etLUsername.text.toString())
                    startActivity(i)
                    finish()
                }
            }

            /** ESTO ES DE SQLite
            val idUsuario = usuarioViewModel.login(
            this, binding.etLUsername.text.toString(), binding.etLPassword.text.toString()
            )
            if (idUsuario > 0) {
                val i = Intent(it.context, ComidaActivity::class.java)
                i.putExtra("idUsuario",idUsuario)
                i.putExtra("username",binding.etLUsername.text.toString())
                startActivity(i)
                finish()
            } else {
                Toast.makeText(it.context, "USUARIO O CONTRASEÑA INCORRECTO", Toast.LENGTH_SHORT)
                    .show()
            }*/

        }

        binding.btnLRegistrar.setOnClickListener {
            startActivity(Intent(it.context, RegistrarseActivity::class.java))
            finish()

        }

    }


}