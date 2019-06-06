package com.usb.pdfdocumentcreator

import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import android.Manifest.permission
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionRequest


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        generate_pdf.setOnClickListener{

            Dexter.withActivity(this)
                .withPermission(permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                    }

                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        createPdf("Sample Text Data")
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {/* ... */
                    }
                }).check()

        }
    }

    private fun createPdf(text: String) {
        val document = PdfDocument()
        val pageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(2480, 3508, 1).create()
        val page: PdfDocument.Page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        paint.textSize = 60f
        canvas.drawText(text, 240.toFloat(), 300.toFloat(), paint)
        document.finishPage(page)

        // write the document content
        val directoryPath = Environment.getExternalStorageDirectory().getPath() + "/usbpdf/"
        val file = File(directoryPath)
        if (!file.exists()) {
            file.mkdirs()
        }
        val targetPdf = directoryPath + "sample.pdf"
        val filePath = File(targetPdf)
        try {
            document.writeTo(FileOutputStream(filePath))
            Toast.makeText(this, "Document Created Successfully", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Log.e("main", "error " + e.toString())
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show()
        }
        document.close()
    }
}
