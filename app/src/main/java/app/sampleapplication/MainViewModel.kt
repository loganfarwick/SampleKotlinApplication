package app.sampleapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.sampleapplication.dto.Plant
import app.sampleapplication.service.IPlantService
import app.sampleapplication.service.PlantService
import kotlinx.coroutines.launch

class MainViewModel(var plantService : IPlantService = PlantService()) : ViewModel() {
    var plants : MutableLiveData<List<Plant>> = MutableLiveData<List<Plant>>()

    fun fetchPlants() {
        viewModelScope.launch {
            var innerPlants = plantService.fetchPlants()
            plants.postValue(innerPlants)
        }
    }
}