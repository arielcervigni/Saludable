package edu.acervigni.saludable.api

import edu.acervigni.saludable.model.ArregloTrago
import edu.acervigni.saludable.model.Trago
import retrofit2.Call
import retrofit2.http.GET


interface APIService {
    @GET("api/json/v1/1/random.php")
    fun getTrago(): Call<ArregloTrago>
}