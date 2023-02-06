package dao

import retrofit2.Call
import app.sampleapplication.dto.Plant
import retrofit2.http.GET

interface IPlantDAO {

    @GET("/perl/mobile/viewplantsjsonarray.pl")
    fun getAllPlants() : Call<ArrayList<Plant>>

}