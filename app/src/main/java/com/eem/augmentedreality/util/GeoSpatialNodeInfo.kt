package com.eem.augmentedreality.util

import com.google.ar.core.Anchor

data class GeoSpatialNodeInfo(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val rotation: FloatArray,
    val modelUrl: String,
    var anchor: Anchor? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GeoSpatialNodeInfo

        if (id != other.id) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (altitude != other.altitude) return false
        if (!rotation.contentEquals(other.rotation)) return false
        if (modelUrl != other.modelUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + altitude.hashCode()
        result = 31 * result + rotation.contentHashCode()
        result = 31 * result + modelUrl.hashCode()
        return result
    }

}