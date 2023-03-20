package app.sampleapplication.dto

data class Specimen(
    var plantId : Int = 0,
    var plantName: String = "",
    var specimenID: String = "",
    var location: String = "",
    var description: String = "",
    var datePlanted: String = "") {

    override fun toString(): String {
        return "$plantName $description $location"
    }
}