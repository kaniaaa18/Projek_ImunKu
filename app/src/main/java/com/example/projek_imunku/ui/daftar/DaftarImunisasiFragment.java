package com.example.projek_imunku.ui.daftar;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projek_imunku.R;
import com.example.projek_imunku.ui.lokasi.Puskesmas;
import com.example.projek_imunku.ui.lokasi.PuskesmasAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;


public class DaftarImunisasiFragment extends Fragment {

    private RecyclerView recyclerView;
    private DaftarImunisasiAdapter daftarImunisasiAdapter;
    private List<DaftarImunisasi> daftarImunisasiList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LineChart chartIMT;

    public DaftarImunisasiFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_imunisasi, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewDaftarImunisasi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        chartIMT = view.findViewById(R.id.chartIMT);

        loadDataAndShowChart();

        daftarImunisasiAdapter = new DaftarImunisasiAdapter(getContext(), daftarImunisasiList);
        recyclerView.setAdapter(daftarImunisasiAdapter);

        fetchDaftarImunisasiData();

        return view;
    }


    private void fetchDaftarImunisasiData() {
        db.collection("daftarImunisasi")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DaftarImunisasi> fetchedList = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DaftarImunisasi imunisasi = document.toObject(DaftarImunisasi.class);
                            fetchedList.add(imunisasi);
                        }

                        // Perbarui adapter RecyclerView
                        daftarImunisasiList.clear();
                        daftarImunisasiList.addAll(fetchedList);
                        daftarImunisasiAdapter.notifyDataSetChanged();

                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                        Toast.makeText(getContext(), "Gagal memuat data imunisasi", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadDataAndShowChart() {
        db.collection("data_imt")
                .orderBy("date")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Entry> entries = new ArrayList<>();
                    List<String> labelDate = new ArrayList<>();

                    int index = 0;
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        DaftarImunisasi data = doc.toObject(DaftarImunisasi.class);
                        Float imt = data.getImt();
                        String tanggal = data.getdate();

                        if (imt != null && tanggal != null && !tanggal.isEmpty()) {
                            entries.add(new Entry(index, imt));
                            labelDate.add(tanggal);
                            index++;
                        }
                    }

                    if (!entries.isEmpty()) {
                        LineDataSet dataSet = new LineDataSet(entries, "Grafik IMT");

                        // 🎨 Tampilan garis & titik
                        dataSet.setColor(Color.parseColor("#2196F3")); // Warna biru cerah
                        dataSet.setCircleColor(Color.parseColor("#1976D2")); // Warna lingkaran
                        dataSet.setLineWidth(2.5f);
                        dataSet.setCircleRadius(5f);
                        dataSet.setDrawCircleHole(true);
                        dataSet.setCircleHoleRadius(2.5f);
                        dataSet.setValueTextSize(10f);
                        dataSet.setValueTextColor(Color.DKGRAY);
                        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Garis lengkung
                        dataSet.setDrawFilled(true);
                        dataSet.setFillColor(Color.parseColor("#90CAF9")); // Warna latar area di bawah garis

                        // ⬇ Buat dan pasang data ke chart
                        LineData lineData = new LineData(dataSet);
                        chartIMT.setData(lineData);

                        // 📝 Deskripsi grafik
                        chartIMT.getDescription().setText("Riwayat Indeks Massa Tubuh");
                        chartIMT.getDescription().setTextSize(12f);

                        // 🕹️ Interaksi
                        chartIMT.setTouchEnabled(true);
                        chartIMT.setPinchZoom(true);
                        chartIMT.setScaleEnabled(true);
                        chartIMT.setDragEnabled(true);

                        // 🧭 Axis X (tanggal)
                        XAxis xAxis = chartIMT.getXAxis();
                        xAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                int i = (int) value;
                                if (i >= 0 && i < labelDate.size()) {
                                    return labelDate.get(i);
                                } else {
                                    return "";
                                }
                            }
                        });
                        xAxis.setGranularity(1f);
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setTextSize(10f);
                        xAxis.setDrawGridLines(false);

                        // 🧭 Axis Y
                        chartIMT.getAxisLeft().setTextSize(10f);
                        chartIMT.getAxisLeft().setDrawGridLines(true);
                        chartIMT.getAxisRight().setEnabled(false);

                        // 🌈 Gaya umum chart
                        chartIMT.setDrawGridBackground(false);
                        chartIMT.setBackgroundColor(Color.WHITE);
                        chartIMT.animateX(1000);
                        chartIMT.getLegend().setTextSize(12f);

                        chartIMT.invalidate(); // Refresh
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Gagal memuat data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}