package app.sampleapplication.service

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.room.Room
import app.sampleapplication.RetroFitClientInstance
import app.sampleapplication.dao.ILocalPlantDAO
import app.sampleapplication.dto.Plant
import app.sampleapplication.dao.IPlantDAO
import app.sampleapplication.dao.PlantDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

interface IPlantService {
    suspend fun fetchPlants() : List<Plant>?
    fun getLocalPlantDAO() : ILocalPlantDAO
}

class PlantService(val application: Application) : IPlantService {

    lateinit var db : PlantDatabase
    override suspend fun fetchPlants() : List<Plant>? {
        return withContext(Dispatchers.IO) {
            val service = RetroFitClientInstance.retrofitInstance?.create(IPlantDAO::class.java)
            val plants = async { service?.getAllPlants() }
            val result = plants.await()?.awaitResponse<ArrayList<Plant>>()?.body()
            updateLocalPlants(result)
            return@withContext result
        }
    }

    private suspend fun updateLocalPlants(plants : ArrayList<Plant>?) {
        try {
            plants?.let {
                val localPlantDAO = getLocalPlantDAO()
                localPlantDAO.insertAll(plants)
            }
        } catch (e: Exception) {
            Log.e(TAG, "error saving plants ${e.message}")
        }
    }

    override fun getLocalPlantDAO() : ILocalPlantDAO {
        if (!this::db.isInitialized) {
            db = Room.databaseBuilder(application, PlantDatabase::class.java, "myplants").build()
        }
        return db.localPlantDAO()
    }
}