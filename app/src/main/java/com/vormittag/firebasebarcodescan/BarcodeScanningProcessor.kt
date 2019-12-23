package com.vormittag.firebasebarcodescan


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.samples.apps.mlkit.kotlin.barcodescanning.BarcodeGraphic
import com.vormittag.firebasebarcodescan.common.CameraImageGraphic
import com.vormittag.firebasebarcodescan.common.FrameMetadata
import com.vormittag.firebasebarcodescan.common.GraphicOverlay
import java.io.IOException

/** Barcode Detector Demo.  */
class BarcodeScanningProcessor(activity: AppCompatActivity) : VisionProcessorBase<List<FirebaseVisionBarcode>>() {
    val myActivity = activity;
    // Note that if you know which format of barcode your app is dealing with, detection will be
    // faster to specify the supported barcode formats one by one, e.g.

//    private val options = FirebaseVisionBarcodeDetectorOptions.Builder()
//        .setBarcodeFormats(
//            FirebaseVisionBarcode.FORMAT_QR_CODE,
//            FirebaseVisionBarcode.FORMAT_CODE_128,
//            FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
//        .build()
    private val detector: FirebaseVisionBarcodeDetector by lazy {
        FirebaseVision.getInstance().visionBarcodeDetector
        //FirebaseVision.getInstance().getVisionBarcodeDetector(options)

    }

    override fun stop() {
        try {
            detector.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Barcode Detector: $e")
        }
    }

    override fun detectInImage(image: FirebaseVisionImage): Task<List<FirebaseVisionBarcode>> {
        return detector.detectInImage(image)
    }

    override fun onSuccess(
        originalCameraImage: Bitmap?,
        barcodes: List<FirebaseVisionBarcode>,
        frameMetadata: FrameMetadata,
        graphicOverlay: GraphicOverlay
    ) {
        graphicOverlay.clear()

        originalCameraImage?.let {
            val imageGraphic = CameraImageGraphic(graphicOverlay, it)
            graphicOverlay.add(imageGraphic)
        }

        barcodes.forEach {
            val barcodeGraphic = BarcodeGraphic(graphicOverlay, it)
            graphicOverlay.add(barcodeGraphic)
            goBackToMainAcitivity(it.rawValue!!)
        }
        graphicOverlay.postInvalidate()
    }

    override fun onFailure(e: Exception) {
        Log.e(TAG, "Barcode detection failed $e")
    }

    private fun goBackToMainAcitivity(barcodeString:String){
        Log.v(TAG, "goBackToMainAcitivity $barcodeString")
        val intent = Intent()
        intent.putExtra("barcodeString", barcodeString)
        myActivity.setResult(Activity.RESULT_OK, intent)
        myActivity.finish()
    }

    companion object {

        private const val TAG = "BarcodeScanProc"
    }
}