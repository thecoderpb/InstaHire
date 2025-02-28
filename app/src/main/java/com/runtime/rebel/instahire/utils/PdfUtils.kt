package com.runtime.rebel.instahire.utils

import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor
import java.io.File

object PdfUtils {
    fun extractTextFromPDF(pdfPath: String): String {
        val text = StringBuilder()
        try {
            val pdfDoc = PdfDocument(PdfReader(File(pdfPath)))
            for (i in 1..pdfDoc.numberOfPages) {
                text.append(PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i))).append("\n")
            }
            pdfDoc.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return text.toString()
    }
}