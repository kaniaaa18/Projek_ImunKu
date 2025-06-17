package com.example.projek_imunku.ui.home;

// Kelas Model untuk menyimpan data artikel
public class Article {
    private String title;
    private String pdfFileName;

    public Article(String title, String pdfFileName) {
        this.title = title;
        this.pdfFileName = pdfFileName;
    }

    public String getTitle() {
        return title;
    }

    public String getPdfFileName() {
        return pdfFileName;
    }
}