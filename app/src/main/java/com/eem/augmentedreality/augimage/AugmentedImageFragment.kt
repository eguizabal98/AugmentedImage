package com.eem.augmentedreality.augimage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.eem.augmentedreality.R
import com.eem.augmentedreality.override.VideoNode
import com.eem.augmentedreality.util.addAugmentedImageNode
import com.eem.augmentedreality.util.addAugmentedVideoNode
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.node.Node

class AugmentedImageFragment : Fragment(R.layout.fragment_augmented_image) {

    private lateinit var sceneView: ArSceneView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sceneView = view.findViewById(R.id.sceneView)

        sceneView.addAugmentedImageNode(
            context = requireContext(),
            name = "drone",
            imagePath = "augmentedimages/drone.png",
            glbPath = "models/drone.glb"
        )

        sceneView.addAugmentedImageNode(
            context = requireContext(),
            name = "spider",
            imagePath = "augmentedimages/p1.jpg",
            glbPath = "models/t1.glb",
            scale = 0.5f
        )

        sceneView.addAugmentedVideoNode(
            context = requireContext(),
            name = "videoNode",
            imagePath = "augmentedimages/video.png",
            videoPath = "vid.mp4"
        )
    }

    override fun onPause() {
        super.onPause()
        sceneView.children.forEach {
            pauseVideoNode(it)
        }
    }

    private fun pauseVideoNode(node: Node) {
        (node as? VideoNode)?.player?.stop() ?: run {
            if (node.children.isNotEmpty()) {
                node.children.forEach {
                    pauseVideoNode(it)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sceneView.children.forEach {
            pauseVideoNode(it)
        }
    }
}