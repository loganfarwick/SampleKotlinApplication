package app.sampleapplication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.sampleapplication.dto.Plant
import app.sampleapplication.dto.Specimen
import app.sampleapplication.service.IPlantService
import app.sampleapplication.service.PlantService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.launch

class MainViewModel(var plantService : IPlantService = PlantService()) : ViewModel() {

    var plants : MutableLiveData<List<Plant>> = MutableLiveData<List<Plant>>()

    private lateinit var firestore: FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun fetchPlants() {
        viewModelScope.launch {
            var innerPlants = plantService.fetchPlants()
            plants.postValue(innerPlants)
        }

    }

    fun save(specimen: Specimen) {
        val document = if (specimen.specimenID == null || specimen.specimenID.isEmpty()) {
            // create a new specimen
            firestore.collection("specimens").document()}
        else {
            // update an existing specimen.
            firestore.collection("specimens").document(specimen.specimenID)
        }
        specimen.specimenID = document.id
        val handle = document.set(specimen)
        handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
        handle.addOnFailureListener { Log.e("Firebase", "Save failed $it")}
    }
}