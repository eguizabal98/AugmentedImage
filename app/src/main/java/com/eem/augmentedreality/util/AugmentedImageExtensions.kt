package com.eem.augmentedreality.util

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import com.eem.augmentedreality.override.AugmentedImageNode
import com.eem.augmentedreality.override.VideoNode
import io.github.sceneview.SceneView
import io.github.sceneview.ar.node.ArModelNode

/**
 * Adds an augmented image node with an associated 3D model to the scene view (SceneView).
 *
 * @param context The application context.
 * @param name The name of the augmented image and associated 3D model.
 * @param imagePath The file path of the image for the augmented image.
 * @param glbPath The file path of the glb file for the associated 3D model.
 */
fun SceneView.addAugmentedImageNode(
    context: Context,
    name: String,
    imagePath: String,
    glbPath: String,
    scale: Float = 1f,
) {
    addChild(
        AugmentedImageNode(
            engine,
            imageName = name,
            bitmap = context.assets.open(imagePath).use(BitmapFactory::decodeStream)
        ).apply {
            loadModelGlbAsync(
                glbFileLocation = glbPath,
                scaleToUnits = scale
            )
        }
    )
}

/**
 * Adds an augmented video node with an associated 3D model and video to the scene view (SceneView).
 *
 * @param context The application context.
 * @param name The name of the augmented image node.
 * @param imagePath The file path of the image for the augmented video.
 * @param videoPath The file path of the video for the augmented video node.
 */
fun SceneView.addAugmentedVideoNode(
    context: Context,
    name: String,
    imagePath: String,
    videoPath: String,
) {
    var videoNode: VideoNode? = null
    addChild(
        AugmentedImageNode(
            engine = engine,
            imageName = name,
            bitmap = context.assets.open(imagePath)
                .use(BitmapFactory::decodeStream),
            onUpdate = { node, _ ->
                if (node.isTracking) {
                    if (videoNode?.player?.isPlaying?.not() == true) {
                        videoNode?.player?.start()
                    }
                }
            }
        ).apply {
            videoNode = VideoNode(
                engine, MediaPlayer().apply {
                    val assetFileDescriptor = context.assets.openFd(videoPath)
                    setDataSource(
                        assetFileDescriptor.fileDescriptor,
                        assetFileDescriptor.startOffset,
                        assetFileDescriptor.length
                    )
                    isLooping = true
                    setOnPreparedListener {
                        if ((videoNode?.parent as? AugmentedImageNode)?.isTracking == true) {
                            start()
                        }
                    }
                    prepareAsync()
                },
                scaleToUnits = 0.3f
            )
            videoNode?.let { safeVideoNode ->
                addChild(
                    ArModelNode(
                        engine = engine,
                        followHitPosition = true
                    ).addChild(safeVideoNode)
                )
            }
        }
    )
}