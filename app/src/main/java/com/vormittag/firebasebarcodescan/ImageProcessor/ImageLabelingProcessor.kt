package com.vormittag.firebasebarcodescan.ImageProcessor

import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.vormittag.firebasebarcodescan.common.VisionProcessorBase
import com.vormittag.firebasebarcodescan.common.CameraImageGraphic
import com.vormittag.firebasebarcodescan.common.FrameMetadata
import com.vormittag.firebasebarcodescan.common.GraphicOverlay
import java.io.IOException

/** Custom Image Classifier Demo.  */
class ImageLabelingProcessor : VisionProcessorBase<List<FirebaseVisionImageLabel>>() {

    private val detector: FirebaseVisionImageLabeler = FirebaseVision.getInstance().onDeviceImageLabeler

    override fun stop() {
        try {
            detector.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Text Detector: $e")
        }
    }

    override fun detectInImage(image: FirebaseVisionImage): Task<List<FirebaseVisionImageLabel>> {
        return detector.processImage(image)
    }

    override fun onSuccess(
        originalCameraImage: Bitmap?,
        labels: List<FirebaseVisionImageLabel>,
        frameMetadata: FrameMetadata,
        graphicOverlay: GraphicOverlay
    ) {
        graphicOverlay.clear()
        originalCameraImage.let { image ->
            val imageGraphic = CameraImageGraphic(graphicOverlay, image)
            graphicOverlay.add(imageGraphic)
        }
        val labelGraphic =
            LabelGraphic(graphicOverlay, labels)
        graphicOverlay.add(labelGraphic)
        graphicOverlay.postInvalidate()
    }

    override fun onFailure(e: Exception) {
        Log.w(TAG, "Label detection failed.$e")
    }

    companion object {

        private const val TAG = "ImageLabelingProcessor"
    }
}
