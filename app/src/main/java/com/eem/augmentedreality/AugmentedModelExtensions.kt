package com.eem.augmentedreality

import com.google.ar.core.Earth
import io.github.sceneview.SceneView
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode

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
    addChild(model)
    return model
}

fun ArSceneView.addGeoArModelNode(
    glbPath: String,
    name: String = "",
): ArNode {
    val model: ArNode = ArNode(engine).apply {
        loadModelGlbAsync(
            glbFileLocation = glbPath
        )
    }
    model.anchor = this.arSession?.earth?.createAnchor(0.0, 0.0, 0.0, 0f, 0f, 0f, 0f)
    if (name.isNotEmpty()) model.name = name
    addChild(model)
    return model
}