package edu.acervigni.saludable.viewmodel

import androidx.lifecycle.ViewModel
import edu.acervigni.saludable.api.implementation.APIImplement
import edu.acervigni.saludable.model.ArregloTrago
import edu.acervigni.saludable.model.Trago
import retrofit2.Call

class TragoViewModel : ViewModel() {

    fun obtenerTrago (): Call<ArregloTrago> {
        val api: APIImplement = APIImplement()

        return api.getTrago()
    }

}