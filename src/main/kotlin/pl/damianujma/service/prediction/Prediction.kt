package pl.damianujma.service.prediction

@kotlinx.serialization.Serializable
data class Prediction(
    val date: String?,
    val temp: Double,
    val description: String,
)