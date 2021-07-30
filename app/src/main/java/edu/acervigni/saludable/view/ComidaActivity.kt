package edu.acervigni.saludable.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import edu.acervigni.saludable.R
import edu.acervigni.saludable.databinding.ActivityComidaBinding
import edu.acervigni.saludable.model.Comida
import edu.acervigni.saludable.viewmodel.ComidaViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ComidaActivity : AppCompatActivity() {

    lateinit var binding: ActivityComidaBinding
    lateinit var comidaViewModel: ComidaViewModel

    lateinit var tipoComida: String
    var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComidaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        comidaViewModel = ViewModelProvider(this).get(ComidaViewModel::class.java)

        username = intent.getStringExtra("username")
        cargarTiposComidas()

        binding.rBtnGuardarComida.setOnClickListener {

            val comida: Comida? = crearComida()
            if(comida!= null){
                guardarComidaLocal(it.context, comida)
            }


        }


        binding.rBtnCerrarSesion.setOnClickListener {
            startActivity(Intent(it.context, LoginActivity::class.java))
            finish()
        }

    }

    private fun cargarTiposComidas() {

        val items = listOf("Desayuno", "Almuerzo", "Merienda", "Cena")
        val adapter = ArrayAdapter(this, R.layout.items_tipo, items)
        binding.cSpTipo.adapter = adapter

        binding.cSpTipo.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                tipoComida = items[p2]
                if (p2 == 1 || p2 == 3) {
                    binding.cLayoutPostre.visibility = View.VISIBLE

                } else {
                    binding.cLayoutPostre.visibility = View.GONE
                    binding.cTipPostre.visibility = View.GONE
                    binding.cEtPostre.setText("")
                    binding.cRbPostreNo.isChecked = true
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }

    fun mostrarPostre(view: View?) {

        val opt: RadioButton = findViewById(binding.cRgPostre.checkedRadioButtonId)
        if (opt == binding.cRbPostreSi) {
            binding.cTipPostre.visibility = View.VISIBLE
        } else {
            binding.cTipPostre.visibility = View.GONE
            binding.cEtPostre.setText("")
        }
    }

    fun mostrarTentacion(view: View?) {

        val opt: RadioButton = findViewById(binding.cRgTentacion.checkedRadioButtonId)
        if (opt == binding.cRbTentacionSi) {
            binding.cTipTentacion.visibility = View.VISIBLE
        } else {
            binding.cTipTentacion.visibility = View.GONE
            binding.cEtTentacion.setText("")
        }
    }

    private fun crearComida(): Comida? {
        if (binding.cEtPrincipal.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, "Complete comida principal.", Toast.LENGTH_SHORT).show()
            return null
        }

        if (binding.cRbPostreSi.isChecked && binding.cEtPostre.text.isNullOrEmpty()) {
            Toast.makeText(this, "Complete descripción de postre.", Toast.LENGTH_SHORT).show()
            return null
        }

        if (binding.cRbTentacionSi.isChecked && binding.cEtTentacion.text.isNullOrEmpty()) {
            Toast.makeText(this, "Complete descripción de qué quería comer.", Toast.LENGTH_SHORT)
                .show()
            return null
        }

        val fecha = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)


        return Comida(
            0, tipoComida, binding.cEtPrincipal.text.toString(),
            binding.cEtSecundaria.text.toString(), binding.cEtBebida.text.toString(),
            binding.cRbPostreSi.isChecked.toString(), binding.cEtPostre.text.toString(),
            binding.cRbTentacionSi.isChecked.toString(), binding.cEtTentacion.text.toString(),
            binding.cRbHambreSi.isChecked.toString(), username!!, fecha
        )
    }

    private fun guardarComidaLocal(context: Context, comida : Comida) {

        if (comida != null) {
            if (comidaViewModel.guardarComida(context, comida)) {
                Toast.makeText(context, "COMIDA GUARDADA CON ÉXITO!!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(context,TragoActivity::class.java))
                finish()
            } else
                Toast.makeText(context, "ERROR AL GUARDAR LA COMIDA!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarComidaFB(context: Context, comida: Comida) {
        if(comidaViewModel.guardarComidaFB(comida)) {
            Toast.makeText(context,"USUARIO REGISTRADO CON ÉXITO !!",Toast.LENGTH_SHORT).show()
            startActivity(Intent(context,TragoActivity::class.java))
            finish()
        } else {
            Toast.makeText(context,"ERROR AL REGISTRAR EL USUARIO !!",Toast.LENGTH_SHORT).show()
        }
    }
    private fun obtenerComidasLocal(): ArrayList<Comida>? {
        return comidaViewModel.obtenerComidas(this)
    }
}