package com.eem.augmentedreality.geo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.eem.augmentedreality.R
import com.eem.augmentedreality.util.GeoSpatialNodeInfo
import com.eem.augmentedreality.util.addArModelNode
import com.google.ar.core.Config
import com.google.ar.core.TrackingState
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode

class AugmentedLocationFragment : Fragment(R.layout.fragment_augmented_image) {

    private lateinit var sceneView: ArSceneView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sceneView = view.findViewById(R.id.sceneView)

        sceneView.geospatialEnabled = true
        sceneView.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR

        earthList.forEach {
            sceneView.addArModelNode(it.modelPath, it.id.toString())
        }

        sceneView.onFrame = {
            checkEarthTracking()
            updateNodeAnchor()
        }
    }

    private fun checkEarthTracking() {
        val earth = sceneView.arSession?.earth ?: return
        if (earth.trackingState == TrackingState.TRACKING) {
            // Place the earth anchor at the same altitude as that of the camera to make it easier to view.
            earthList.forEach { earthNode ->
                earthNode.anchor?.detach()
                earthNode.anchor = earth.createAnchor(
                    earthNode.latitude,
                    earthNode.longitude,
                    earthNode.altitude,
                    earthNode.rotation[0],
                    earthNode.rotation[1],
                    earthNode.rotation[2],
                    earthNode.rotation[3]
                )
            }
        }
    }

    private fun updateNodeAnchor() {
        earthList.forEach { earthNode ->
            val childNode = sceneView.children.find { it.name == earthNode.id.toString() } as? ArModelNode
            earthNode.anchor?.let { anchor ->
                childNode?.anchor = anchor
            }
        }
    }

    companion object {
        val earthList = listOf(
            GeoSpatialNodeInfo(
                1123,
                13.988126094184624, -89.60466407335392,
                729.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                26532,
                13.988095490907185, -89.60465266580329,
                729.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                36723,
                13.98805772515495, -89.60464260031745,
                729.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                4672123,
                13.988031028671195, -89.6046305217344,
                729.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                554389,
                13.988014099189776, -89.60462448244064,
                729.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                515489,
                13.987971775486532, -89.60461173282523,
                729.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                554898,
                13.98794312559068, -89.60460636456611,
                729.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                68923,
                13.987888430325082, -89.6046137459224,
                729.0,
                floatArrayOf(0f, -1f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/direction_arrow.glb"
            ),
            GeoSpatialNodeInfo(
                8658421,
                13.987880616714639, -89.6046627312869,
                729.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                947345,
                13.987878663311996, -89.60474057104418,
                729.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                1021519,
                13.987882570117446, -89.60485263346146,
                729.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),

            GeoSpatialNodeInfo(
                1157123,
                13.987886476922654, -89.60492644702441,
                729.0,
                floatArrayOf(0f, 0f, -1f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/direction_arrow.glb"
            ),

            GeoSpatialNodeInfo(
                1243643,
                13.987792713578171, -89.60493047321926,
                735.0,
                floatArrayOf(0f, -1f, 0f, 0f),
                "models/planet.glb"
            )
        )
    }
}