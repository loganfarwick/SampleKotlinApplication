package app.sampleapplication

import app.sampleapplication.service.IPlantService
import app.sampleapplication.service.PlantService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get()) }
    single<IPlantService> { PlantService() }
}