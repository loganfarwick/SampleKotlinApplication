package app.sampleapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitClientInstance {
    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://plantplaces.com/"

    val retrofitInstance : Retrofit?
        get() {
            // has this object been created yet?
            if (retrofit == null) {
                // create it
                retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}