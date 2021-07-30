package edu.acervigni.saludable.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import edu.acervigni.saludable.R
import edu.acervigni.saludable.connectionHelper.ConnectionHelper
import edu.acervigni.saludable.databinding.DialogTragoBinding
import edu.acervigni.saludable.model.ArregloTrago
import edu.acervigni.saludable.model.Trago
import edu.acervigni.saludable.viewmodel.TragoViewModel
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DialogTrago() : DialogFragment() {

    private lateinit var tragoViewModel: TragoViewModel

    lateinit var tv_titulo : TextView
    lateinit var tv_nombre : TextView
    lateinit var tv_alcohol : TextView
    lateinit var btn_Volver : Button
    lateinit var progressBar : ProgressBar
    lateinit var iv_img: ImageView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View? = inflater.inflate(R.layout.dialog_trago, container, false)

        if (v != null) {
            inicializar(v)
            if(!ConnectionHelper.hayInternet(v.context)){
                tv_titulo.text = "Comida guardada con éxito!!"
                progressBar.visibility = View.GONE
            } else {
                tragoViewModel = ViewModelProvider(this).get(TragoViewModel::class.java)
                tv_titulo.text = "Aguarda un momento.. \nEstamos buscando un trago para vos.."
                obtenerTrago()
            }

        }


        btn_Volver.setOnClickListener {
            dismissAllowingStateLoss()
        }

        return v
    }

    fun obtenerTrago() {
        tragoViewModel.obtenerTrago()
            .enqueue(object : Callback<ArregloTrago> {
                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<ArregloTrago>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    tv_titulo.text = "No fue posible regalarte un trago. Lo sentimos."
                    tv_alcohol.text = "Volvé a intentarlo la próxima."
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<ArregloTrago>,
                    response: Response<ArregloTrago>
                ) {
                    progressBar.visibility = View.GONE
                    if(response.body() != null){

                        val data  = response.body()
                        val trago : Trago = data!!.drinks[0]

                        Glide.with(this@DialogTrago)
                            .load(trago.imagen)
                            .centerCrop()
                            .into(iv_img)

                        tv_titulo.setText("Gracias por haber registrado tu comida.! \n"  +
                                "Te regalamos el siguiente trago:")
                        tv_nombre.text = trago.nombre
                        tv_alcohol.text = trago.categoria + " - " + trago.alcoholic

                    }
                }
            })
    }

    fun inicializar (v:View) {
        tv_alcohol = v.findViewById(R.id.dt_tv_alcohol)
        tv_nombre = v.findViewById(R.id.dt_tv_nombreTrago)
        iv_img = v.findViewById(R.id.dt_iv_imgTrago)
        tv_titulo = v.findViewById(R.id.dt_tv_titulo)
        btn_Volver = v.findViewById(R.id.dt_btn_nuevaComida)
        progressBar = v.findViewById(R.id.dt_pb_progress)
    }
}

