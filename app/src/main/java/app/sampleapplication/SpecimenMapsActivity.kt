package app.sampleapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.runtime.getValue

class SpecimenMapsActivity : ComponentActivity() {
    private val viewModel : MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpecimenMap()
        }
    }

    @Composable
    private fun SpecimenMap() {
        val specimens by viewModel.specimens.observeAsState(initial = emptyList())
        val cincinnati = LatLng(39.44, -84.51)
        val cameraPosition = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(cincinnati, 13f)
        }
        GoogleMap (
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPosition
        ) { specimens.forEach {
                specimen ->
                if (specimen.latitude.isNotEmpty() && specimen.longitude.isNotEmpty()) {
                    val specimenPosition =
                        LatLng(specimen.latitude.toDouble(), specimen.longitude.toDouble())
                    Marker(
                        position = specimenPosition,
                        title = specimen.plantName,
                        snippet = specimen.description
                    )
                }
            }
        }
    }
}