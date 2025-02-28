package com.runtime.rebel.instahire.utils

import android.os.Environment
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File
import java.io.FileOutputStream


object PDFGenerator {
    fun createPDF(file: File, content: String) {
        try {
            val writer = PdfWriter(file)
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc)
            document.add(Paragraph(content))
            document.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}