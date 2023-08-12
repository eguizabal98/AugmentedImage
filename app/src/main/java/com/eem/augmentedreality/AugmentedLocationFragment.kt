package com.eem.augmentedreality

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import com.google.ar.core.Anchor
import com.google.ar.core.TrackingState
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.math.Rotation
import java.lang.Thread.sleep
import kotlinx.coroutines.delay

class AugmentedLocationFragment : Fragment(R.layout.fragment_augmented_image) {

    private lateinit var sceneView: ArSceneView
    private lateinit var sliderX: Slider
    private lateinit var sliderY: Slider
    private lateinit var sliderZ: Slider
    private lateinit var sliderQ: Slider
    private var lastAnchor: Anchor? = null
    private var valueX: Float = 0.0f
    private var valueY: Float = 0.0f
    private var valueZ: Float = 0.0f
    private var valueQ: Float = 0.0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sceneView = view.findViewById(R.id.sceneView)
        sliderX = view.findViewById(R.id.pgb_x)
        sliderY = view.findViewById(R.id.pgb_y)
        sliderZ = view.findViewById(R.id.pgb_z)
        sliderQ = view.findViewById(R.id.pgb_q)

        sceneView.geospatialEnabled = true

        earthList.forEach {
            sceneView.addArModelNode(it.modelUrl, it.id.toString())
        }

        sceneView.onFrame = {
            updateNodeAnchor()
        }

        sliderX.addOnChangeListener { slider, value, fromUser ->
            valueX = value
            Log.d("Geo", "valueX: $valueX")
            checkEarthTracking()
        }
        sliderY.addOnChangeListener { slider, value, fromUser ->
            valueY = value
            Log.d("Geo", "valueY: $valueY")
            checkEarthTracking()
        }
        sliderZ.addOnChangeListener { slider, value, fromUser ->
            valueZ = value
            Log.d("Geo", "valueZ: $valueZ")
            checkEarthTracking()
        }
        sliderQ.addOnChangeListener { slider, value, fromUser ->
            valueQ = value
            Log.d("Geo", "valueQ: $valueQ")
            checkEarthTracking()
        }
    }

    private fun checkEarthTracking() {
        val earth = sceneView.arSession?.earth ?: return
        if (earth.trackingState == TrackingState.TRACKING) {
            // Place the earth anchor at the same altitude as that of the camera to make it easier to view.
            earthList.forEach { earthNode ->
                earthNode.anchor?.detach()
                earthNode.anchor = sceneView.arSession?.createAnchor(earth.createAnchor(
                    earthNode.latitude,
                    earthNode.longitude,
                    earthNode.altitude,
                    valueX,
                    valueY,
                    valueZ,
                    valueQ
                ).pose)
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
                13.967393862906361,
                -89.56818565263623,
                738.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                26532,
                13.967423144167224,
                -89.56817322829788,
                738.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                36723,
                13.967455870277897,
                -89.56816257886501,
                738.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                4672123,
                13.967492041236923,
                -89.5681466047157,
                737.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                55489,
                13.967529934616474,
                -89.56813240547184,
                737.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                68923,
                13.967574717693356,
                -89.56812353094445,
                737.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/direction_arrow.glb"
            ),
            GeoSpatialNodeInfo(
                8658421,
                13.967617778336006,
                -89.5681111066061,
                737.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                947345,
                13.96765276112298,
                -89.56809533292856,
                737.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                1021519,
                13.967682465123442,
                -89.56807770953783,
                737.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),

            GeoSpatialNodeInfo(
                1157123,
                13.96773197178236,
                -89.56805081067827,
                736.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/direction_arrow.glb"
            ),

            GeoSpatialNodeInfo(
                1243643,
                13.967727471177437,
                -89.56800257824045,
                736.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                138761,
                13.96771937008836,
                -89.56794878052135,
                735.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                1494451,
                13.96771306924112,
                -89.56789869298977,
                734.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),
            GeoSpatialNodeInfo(
                15923,
                13.967704968151535,
                -89.56785231564571,
                734.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/sphere-blue.glb"
            ),

            GeoSpatialNodeInfo(
                1676123,
                13.967758075289135,
                -89.56778460472337,
                740.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "https://raw.githubusercontent.com/eguizabal98/eguizabal98/main/point_map.glb"
            )
        )

        val model2List = listOf(
            GeoSpatialNodeInfo(
                1123,
                13.967590457443158, -89.56809221762066,
                738.0,
                floatArrayOf(0f, 0f, 0f, 0f),
                "models/drone.glb"
            )
        )
    }
}