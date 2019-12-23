package com.vormittag.firebasebarcodescan

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.ml.common.FirebaseMLException
import com.vormittag.firebasebarcodescan.TextProcessor.TextRecognitionProcessor
import com.vormittag.firebasebarcodescan.common.CameraSource
import com.vormittag.firebasebarcodescan.common.CameraSourcePreview
import com.vormittag.firebasebarcodescan.common.GraphicOverlay
import kotlinx.android.synthetic.main.activity_live_preview.*
import java.io.IOException

class LivePreview : AppCompatActivity() {
    companion object {
        private const val PERMISSION_REQUESTS = 1
        private const val TAG = "LivePreviewActivity"
        private const val BARCODE_OPTION = "Barcode"
        private const val TEXT_OPTION = "Text"
    }

    private var cameraSource: CameraSource? = null
    private var selectedModel = BARCODE_OPTION
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null

    private val requiredPermissions: Array<String?>
        get() {
            return try {
                val info = this.packageManager
                    .getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
                val ps = info.requestedPermissions
                if (ps != null && ps.isNotEmpty()) {
                    ps
                } else {
                    arrayOfNulls(0)
                }
            } catch (e: Exception) {
                arrayOfNulls(0)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_preview)
        val actionbar = supportActionBar
        actionbar!!.title = "Live Preview"
        actionbar.setDisplayHomeAsUpEnabled(true)

        var bundle :Bundle ?=intent.extras
        selectedModel = bundle!!.getString("mode", BARCODE_OPTION)

        preview = findViewById(R.id.firePreview)
        if (preview == null) {
            Log.d(TAG, "Preview is null")
        }
        graphicOverlay = findViewById(R.id.fireFaceOverlay)
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null")
        }

        if (allPermissionsGranted()) {
            createCameraSource(selectedModel)
        } else {
            getRuntimePermissions()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    public override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        startCamera()
    }

    /** Stops the camera.  */
    override fun onPause() {
        super.onPause()
        preview!!.stop()
    }

    public override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
    }

    private fun startCamera() {
        Log.v(TAG, "start camera");
        cameraSource?.let {
            try {
                if (firePreview == null) {
                    Log.d(TAG, "resume: Preview is null")
                }
                if (fireFaceOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null")
                }
                preview!!.start(cameraSource, graphicOverlay)
            } catch (e: IOException) {
                Log.e(TAG, "Unable to start camera source.", e)
                cameraSource?.release()
                cameraSource = null
            }
        }
    }

    private fun createCameraSource(model: String) {
        Log.v(TAG, "create camera source")
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = CameraSource(this, graphicOverlay)
        }

        try {
            when (model) {
                BARCODE_OPTION -> {
                    Log.i(TAG, "Using Barcode Detector Processor")
                    cameraSource?.setMachineLearningFrameProcessor(BarcodeScanningProcessor(this))
                }
                TEXT_OPTION -> {
                    Log.i(TAG, "Using Text Detector Processor")
                    cameraSource?.setMachineLearningFrameProcessor(TextRecognitionProcessor())
                }
                else -> Log.e(TAG, "Unknown model: $model")
            }
        } catch (e: FirebaseMLException) {
            Log.e(TAG, "can not create camera source: $model")
        }
    }



    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (!isPermissionGranted(this, permission!!)) {
                return false
            }
        }
        return true
    }

    private fun getRuntimePermissions() {
        val allNeededPermissions = arrayListOf<String>()
        for (permission in requiredPermissions) {
            if (!isPermissionGranted(this, permission!!)) {
                allNeededPermissions.add(permission)
            }
        }

        if (allNeededPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this, allNeededPermissions.toTypedArray(), PERMISSION_REQUESTS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "Permission granted!")
        if (allPermissionsGranted()) {
            createCameraSource(selectedModel)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG, "Permission granted: $permission")
            return true
        }
        Log.i(TAG, "Permission NOT granted: $permission")
        return false
    }

}
