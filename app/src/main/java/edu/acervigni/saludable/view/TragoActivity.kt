package edu.acervigni.saludable.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import edu.acervigni.saludable.R
import edu.acervigni.saludable.databinding.ActivityTragoBinding
import edu.acervigni.saludable.model.ArregloTrago
import edu.acervigni.saludable.model.Comida
import edu.acervigni.saludable.model.Trago
import edu.acervigni.saludable.viewmodel.TragoViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TragoActivity : AppCompatActivity() {
    lateinit var tragoViewModel : TragoViewModel

    lateinit var tv_nombre : TextView
    lateinit var tv_alcohol : TextView
    lateinit var iv_img : ImageView
    lateinit var tv_titulo : TextView
    lateinit var btn_OtraComida : Button
    lateinit var progressBar : ProgressBar
    var idUsuario : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_trago)

        idUsuario = intent.getIntExtra("idUsuario",0)
        tragoViewModel = ViewModelProvider(this).get(TragoViewModel::class.java)

        inicializar()
        tv_titulo.text = "Aguarda un momento.. \nEstamos buscando un trago para vos.."

        obtenerTrago()
        progressBar.visibility = View.GONE

        btn_OtraComida.setOnClickListener {
            val i = Intent(it.context,ComidaActivity::class.java)
            i.putExtra("idUsuario",idUsuario)
            startActivity(i)
            finish()
        }


    }

    fun inicializar () {
        tv_alcohol = findViewById(R.id.t_tv_alcohol)
        tv_nombre = findViewById(R.id.t_tv_nombreTrago)
        iv_img = findViewById(R.id.t_iv_imgTrago)
        tv_titulo = findViewById(R.id.t_tv_titulo)
        btn_OtraComida = findViewById(R.id.t_btn_nuevaComida)
        progressBar = findViewById(R.id.t_pb_progress)
    }

    fun obtenerTrago() {
        tragoViewModel.obtenerTrago()
            .enqueue(object : Callback<ArregloTrago>{
                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<ArregloTrago>, t: Throwable) {
                    Log.d("Retrofit","ERROR")
                    tv_nombre.setText("No fue posible regalarte un trago. Lo sentimos.")
                    tv_alcohol.setText("Volvé a intentarlo la próxima.")
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<ArregloTrago>,
                    response: Response<ArregloTrago>
                ) {
                    if(response.body() != null){

                        val data  = response.body()
                        val trago : Trago = data!!.drinks[0]

                        Glide.with(this@TragoActivity)
                            .load(trago.imagen)
                            .centerCrop()
                            .into(iv_img)

                        tv_titulo.setText("Gracias por haber registrado tu comida.! \n"  +
                                "Te regalamos el siguiente trago:")
                        tv_nombre.setText(trago.nombre)
                        tv_alcohol.setText(trago.categoria + " - " + trago.alcoholic)

                    }
                }
            })
    }
}