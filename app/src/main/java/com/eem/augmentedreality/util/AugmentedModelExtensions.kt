package com.eem.augmentedreality.util

import io.github.sceneview.SceneView
import io.github.sceneview.ar.node.ArModelNode

fun SceneView.addArModelNode(
    glbPath: String,
    name: String = "",
): ArModelNode {
    val model: ArModelNode = ArModelNode(engine).apply {
        loadModelGlbAsync(
            glbFileLocation = glbPath
        )
    }
    if (name.isNotEmpty()) model.name = name
    model.applyPoseRotation = true
    addChild(model)
    return model
}
