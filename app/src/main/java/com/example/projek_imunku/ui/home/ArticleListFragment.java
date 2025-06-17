package com.example.projek_imunku.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ArticleListFragment extends Fragment {

    public static final String ARG_CATEGORY = "category_argument";
    private RecyclerView recyclerView;
    private TextView tvCategoryTitle;
    private String category;
    private ArticleAdapter adapter;

    public ArticleListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(ARG_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_articles);
        tvCategoryTitle = view.findViewById(R.id.tv_category_title);

        setupRecyclerView();
        loadArticlesBasedOnCategory();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ArticleAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    private void loadArticlesBasedOnCategory() {
        List<Article> articles = new ArrayList<>();
        String title = "";
        switch (category) {
            case DashboardFragment.JENIS_IMUNISASI:
                title = "Jenis-jenis Imunisasi";
                articles.add(new Article("Gambaran Imunisasi Dasar Lengkap pada Bayi usia 0-12 di Provinsi Jawa Timur tahun 2016 hingga 2018", "gambaran_imunisasi_jatim_2016_2018.pdf"));
                articles.add(new Article("Analisis Faktor Pemberian Imunisasi Dasar Lengkap pada Bayi di Puskesmas Tamalate Makassar", "analisis_faktor_imunisasi_makassar.pdf"));
                break;
            case DashboardFragment.MANFAAT_IMUNISASI:
                title = "Manfaat Imunisasi";
                articles.add(new Article("Analisis Faktor-Faktor yang Mempengaruhi Pemberian Imunisasi Dasar pada Bayi", "analisis_faktor_pemberian_imunisasi.pdf"));
                articles.add(new Article("Penyuluhan Tentang Pentingnya Imunisasi Dasar Lengkap Di Wilayah Kerja Puskesmas Cihideung Kota Tasikmalaya", "penyuluhan_imunisasi_tasikmalaya.pdf"));
                break;
            case DashboardFragment.MITOS_FAKTA:
                title = "Mitos dan Fakta Imunisasi";
                articles.add(new Article("Sosialisasi Bulan Imunisasi Anak Nasional dan Edukasi Pentingnya Imunisasi Dasar Lengkap pada Anak di Desa Cibanteng", "sosialisasi_imunisasi_cibanteng.pdf"));
                articles.add(new Article("Hubungan Pengetahuan Orang Tua, Ketersediaan Fasilitas Kesehatan dan Peran Petugas Kesehatan terhadap Pelaksanaan Imunisasi Dasar Lengkap pada Baduta", "hubungan_pengetahuan_orangtua_imunisasi.pdf"));
                break;
        }

        tvCategoryTitle.setText(title);
        adapter.updateArticles(articles);
    }
}