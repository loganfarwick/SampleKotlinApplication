package app.sampleapplication

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    internal val NEW_SPECIMEN = "New Specimen"
    var plants : MutableLiveData<List<Plant>> = MutableLiveData<List<Plant>>()
    var specimens : MutableLiveData<List<Specimen>> = MutableLiveData<List<Specimen>>()
    var selectedSpecimen by mutableStateOf(Specimen())

    private lateinit var firestore: FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToSpecimens()
    }

    private fun listenToSpecimens() {
       firestore.collection("specimens").addSnapshotListener {
           snapshot, e ->
           // handle the error if there is one, and then return
           if (e != null) {
               Log.w("Listen failed", e)
                return@addSnapshotListener
           }
           // if we reached this point, there was not an error
           snapshot?.let {
               val allSpecimens = ArrayList<Specimen>()
               allSpecimens.add(Specimen(plantName = NEW_SPECIMEN))
               val documents = snapshot.documents
               documents.forEach {
                   val specimen = it.toObject(Specimen::class.java)
                   specimen?.let {
                       allSpecimens.add(it)
                   }
               }
               specimens.value = allSpecimens
           }
       }
    }

    fun fetchPlants() {
        viewModelScope.launch {
            var innerPlants = plantService.fetchPlants()
            plants.postValue(innerPlants)
        }

    }

    fun saveSpecimen() {
        val document = if (selectedSpecimen.specimenID == null || selectedSpecimen.specimenID.isEmpty()) {
            // create a new specimen
            firestore.collection("specimens").document()}
        else {
            // update an existing specimen.
            firestore.collection("specimens").document(selectedSpecimen.specimenID)
        }
        selectedSpecimen.specimenID = document.id
        val handle = document.set(selectedSpecimen)
        handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
        handle.addOnFailureListener { Log.e("Firebase", "Save failed $it")}
    }
}