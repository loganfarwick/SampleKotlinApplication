package app.sampleapplication.service

import app.sampleapplication.RetroFitClientInstance
import app.sampleapplication.dto.Plant
import dao.IPlantDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class PlantService {

    suspend fun fetchPlants() : List<Plant>? {
        return withContext(Dispatchers.IO) {
            val service = RetroFitClientInstance.retrofitInstance?.create(IPlantDAO::class.java)
            val plants = async {service?.getAllPlants()}
            var result = plants.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }
}