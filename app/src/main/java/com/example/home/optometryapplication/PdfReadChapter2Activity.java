package com.example.home.optometryapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

//taken from Github.com/barteksc
public class PdfReadChapter2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_read);

        //PDF view
        PDFView pdfView = findViewById(R.id.pdfViewer);
        pdfView.fromAsset("formulary.pdf")
                .pages(14,15,16,17,18,19,20,21,22,23,24,25,26
                        ,27,28,29,30,31,32,33,34,35,36,37,38,39
                        ,40,41,42,43,44,45,46,47,48,49,50,51,52
                        ,53,54,55,56,57,58,59,60,61)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .pageFitPolicy(FitPolicy.WIDTH)
                .load();
    }
}
