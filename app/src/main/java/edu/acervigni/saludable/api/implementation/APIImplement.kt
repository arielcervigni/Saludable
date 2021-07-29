package edu.acervigni.saludable.api.implementation

import edu.acervigni.saludable.api.APIService
import edu.acervigni.saludable.model.ArregloTrago
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIImplement {

    private  fun getRetrofit() : Retrofit{

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://www.thecocktaildb.com/")
            .build()
    }

    fun getTrago(): Call<ArregloTrago>{
        return getRetrofit().create(APIService::class.java).getTrago()
    }
}