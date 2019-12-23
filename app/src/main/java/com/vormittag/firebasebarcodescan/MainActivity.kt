package com.vormittag.firebasebarcodescan


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val BARCODE_OPTION = "Barcode"
        private const val TEXT_OPTION = "Text"
        private const val LIVE_PREVIEW = 1;
    }

    private var barcodeInput: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findView()
    }

    private fun findView(){
        barcodeInput = findViewById(R.id.barcodeInput);
    }

    fun checkBtnClick(view: View) {
        var id = view.id;
        if (id == R.id.barcodeBtn) {
            gotoLivePreview(BARCODE_OPTION)
        } else if (id == R.id.labelBtn) {
            gotoLivePreview(TEXT_OPTION)
        }
    }

    private fun gotoLivePreview(type:String) {
        val intent = Intent(this, LivePreview::class.java)
        intent.putExtra("mode", type)
        startActivityForResult(intent, LIVE_PREVIEW)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LIVE_PREVIEW) {
            if (resultCode == Activity.RESULT_OK) {
                var barcodeString = data!!.getStringExtra("barcodeString")
                Log.v(TAG, "onActivityResult $barcodeString ")
                barcodeInput!!.setText(barcodeString)
            }
        }
    }
}




