package com.eem.augmentedreality.cloud

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.eem.augmentedreality.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.ar.core.Anchor
import com.google.ar.core.Session
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.utils.doOnApplyWindowInsets

@Suppress("DEPRECATION")
class CloudAnchorFragment : Fragment(R.layout.fragment_cloud_anchor) {

    private lateinit var loadingView: View
    private lateinit var cloudAnchorID: EditText
    private lateinit var hostButton: Button
    private lateinit var resolveButton: Button
    private lateinit var actionButton: ExtendedFloatingActionButton

    private lateinit var sceneView: ArSceneView
    private lateinit var cloudAnchorNode: ArModelNode

    private var mode = Mode.INIT

    private var isLoading = false
        set(value) {
            field = value
            loadingView.isGone = !value
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView(view)

        sceneView = view.findViewById(R.id.sceneView)
        sceneView.apply {
            cloudAnchorEnabled = true
        }

        cloudAnchorNode = ArModelNode(
            engine = sceneView.engine,
            placementMode = PlacementMode.BEST_AVAILABLE
        ).apply {
            parent = sceneView
            isVisible = false
            loadModelGlbAsync(glbFileLocation = "models/drone.glb") {
                isLoading = false
            }
        }
    }

    private fun actionButtonClicked() {
        when (mode) {
            Mode.INIT -> {}

            Mode.HOST -> {
                val frame = sceneView.currentFrame ?: return

                if (!cloudAnchorNode.isAnchored) {
                    cloudAnchorNode.anchor()
                }

                if (sceneView.arSession?.estimateFeatureMapQualityForHosting(frame.camera.pose) == Session.FeatureMapQuality.INSUFFICIENT) {
                    showToastError()
                    return
                }

                cloudAnchorNode.hostCloudAnchor { anchor: Anchor, success: Boolean ->
                    if (success) {
                        cloudAnchorID.setText(anchor.cloudAnchorId)
                        selectMode(Mode.STANDBY)
                    } else {
                        selectMode(Mode.HOST)
                    }
                }

                actionButton.apply {
                    setText(R.string.hosting)
                    isEnabled = true
                }
            }

            Mode.RESOLVE -> {
                cloudAnchorNode.resolveCloudAnchor(cloudAnchorID.text.toString()) { anchor: Anchor, success: Boolean ->
                    if (success) {
                        cloudAnchorNode.isVisible = true
                        selectMode(Mode.STANDBY)
                    } else {
                        selectMode(Mode.RESOLVE)
                    }
                }

                actionButton.apply {
                    setText(R.string.resolving)
                    isEnabled = false
                }
            }

            Mode.STANDBY -> {
                cloudAnchorNode.detachAnchor()
                selectMode(Mode.INIT)
            }
        }
    }

    private fun setUpView(view: View) {
        val topGuideline = view.findViewById<Guideline>(R.id.topGuideline)
        topGuideline.doOnApplyWindowInsets { systemBarsInsets ->
            // Add the action bar margin
            val actionBarHeight =
                (requireActivity() as AppCompatActivity).supportActionBar?.height ?: 0
            topGuideline.setGuidelineBegin(systemBarsInsets.top + actionBarHeight)
        }
        val bottomGuideline = view.findViewById<Guideline>(R.id.bottomGuideline)
        bottomGuideline.doOnApplyWindowInsets { systemBarsInsets ->
            // Add the navigation bar margin
            bottomGuideline.setGuidelineEnd(systemBarsInsets.bottom)
        }

        loadingView = view.findViewById(R.id.loadingView)

        actionButton = view.findViewById(R.id.actionButton)
        actionButton.setOnClickListener {
            actionButtonClicked()
        }

        cloudAnchorID = view.findViewById(R.id.editText)
        cloudAnchorID.addTextChangedListener {
            actionButton.isEnabled = !it.isNullOrBlank()
        }

        hostButton = view.findViewById(R.id.hostButton)
        hostButton.setOnClickListener {
            selectMode(Mode.HOST)
        }

        resolveButton = view.findViewById(R.id.resolveButton)
        resolveButton.setOnClickListener {
            selectMode(Mode.RESOLVE)
        }

        isLoading = true
    }

    private fun selectMode(mode: Mode) {
        this.mode = mode

        when (mode) {
            Mode.INIT -> {
                cloudAnchorID.isVisible = false
                hostButton.isVisible = true
                resolveButton.isVisible = true
                actionButton.isVisible = false
                cloudAnchorNode.isVisible = false
            }

            Mode.HOST -> {
                hostButton.isVisible = false
                resolveButton.isVisible = false
                actionButton.apply {
                    setIconResource(R.drawable.ic_host)
                    setText(R.string.host)
                    isVisible = true
                    isEnabled = true
                }
                cloudAnchorNode.isVisible = true
            }

            Mode.RESOLVE -> {
                cloudAnchorID.isVisible = true
                hostButton.isVisible = false
                resolveButton.isVisible = false
                actionButton.apply {
                    setIconResource(R.drawable.ic_resolve)
                    setText(R.string.resolve)
                    isVisible = true
                    isEnabled = cloudAnchorID.text.isNotEmpty()
                }
            }

            Mode.STANDBY -> {
                cloudAnchorID.isVisible = true
                actionButton.apply {
                    setIconResource(R.drawable.ic_reset)
                    setText(R.string.reset)
                    isEnabled = true
                }
            }
        }
    }

    private fun showToastError() {
        Toast.makeText(context, R.string.insufficient_visual_data, Toast.LENGTH_LONG)
            .show()
    }

    private enum class Mode {
        INIT, HOST, RESOLVE, STANDBY
    }

    companion object {
        private const val TAG = "MainFragment"
    }
}