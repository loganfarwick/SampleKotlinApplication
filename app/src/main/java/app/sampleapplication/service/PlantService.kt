package app.sampleapplication.service

import app.sampleapplication.RetroFitClientInstance
import app.sampleapplication.dto.Plant
import app.sampleapplication.dao.IPlantDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

interface IPlantService {
    suspend fun fetchPlants() : List<Plant>?
}

class PlantService : IPlantService {

    override suspend fun fetchPlants() : List<Plant>? {
        return withContext(Dispatchers.IO) {
            val service = RetroFitClientInstance.retrofitInstance?.create(IPlantDAO::class.java)
            val plants = async { service?.getAllPlants() }
            return@withContext plants.await()?.awaitResponse<ArrayList<Plant>>()?.body()
        }
    }
}