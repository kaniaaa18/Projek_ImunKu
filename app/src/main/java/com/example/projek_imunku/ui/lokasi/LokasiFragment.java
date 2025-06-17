package com.example.projek_imunku.ui.lokasi;

import com.example.projek_imunku.ui.lokasi.Puskesmas;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projek_imunku.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import androidx.cardview.widget.CardView;

public class LokasiFragment extends Fragment {

    private GoogleMap mMap;
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private RecyclerView recyclerView;
    private PuskesmasAdapter puskesmasAdapter;
    private List<Puskesmas> puskesmasList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            Log.d("Lokasi", "Izin lokasi diberikan");
                            enableMyLocation();
                        } else {
                            Toast.makeText(getContext(), "Izin lokasi tidak diberikan", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lokasi, container, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(
                googleMap -> {
                    mMap = googleMap;
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
                    mMap.getUiSettings().setCompassEnabled(true);
                    mMap.getUiSettings().setMapToolbarEnabled(true);

                    checkLocationPermission();
                });

        recyclerView = view.findViewById(R.id.recycler_view_puskesmas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        puskesmasAdapter = new PuskesmasAdapter(getContext(), puskesmasList);
        recyclerView.setAdapter(puskesmasAdapter);

        return view;
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            tampilkanPuskesmasTerdekat();
        }
    }

    private double hitungJarak(double lat1, double long1, double lat2, double long2) {
        double earthRadius = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLong = Math.toRadians(long2 - long1);
        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLong / 2)
                        * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    @SuppressLint("MissingPermission")
    private void tampilkanPuskesmasTerdekat() {
        fusedLocationClient
                .getLastLocation()
                .addOnSuccessListener(
                        location -> {
                            if (location != null) {
                                double userLat = location.getLatitude();
                                double userLng = location.getLongitude();
                                Log.d("Lokasi", "Lokasi ditemukan: " + userLat + "," + userLng);

                                fetchPuskesmasData(userLat, userLng);
                            } else {
                                Toast.makeText(
                                                getContext(), "Lokasi tidak tersedia. Aktifkan GPS.", Toast.LENGTH_LONG)
                                        .show();
                                Log.e("Lokasi", "Gagal mendapatkan lokasi. Location = null");
                            }
                        })
                .addOnFailureListener(
                        e -> {
                            Toast.makeText(
                                            getContext(), "Gagal mendapatkan lokasi: " + e.getMessage(), Toast.LENGTH_LONG)
                                    .show();
                            Log.e("Lokasi", "Error getLastLocation: " + e.getMessage());
                        });
    }

    private void fetchPuskesmasData(double userLat, double userLng) {
        db.collection("puskesmas")
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                List<Puskesmas> fetchedPuskesmas = new ArrayList<>();
                                mMap.clear();

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Puskesmas puskesmas = document.toObject(Puskesmas.class);
                                    GeoPoint point = puskesmas.getPoint();
                                    if (point != null) {
                                        double lat = point.getLatitude();
                                        double lng = point.getLongitude();
                                        puskesmas.setDistance(hitungJarak(userLat, userLng, lat, lng));
                                    }

                                    fetchedPuskesmas.add(puskesmas);

                                    // Tambahkan marker ke peta untuk setiap puskesmas
                                    mMap.addMarker(
                                            new MarkerOptions()
                                                    .position(new LatLng(puskesmas.getPoint().getLatitude(), puskesmas.getPoint().getLongitude()))
                                                    .title(puskesmas.getName())
                                                    .snippet("Jarak: " + String.format("%.2f", puskesmas.getDistance()) + " km"));
                                }

                                // Urutkan berdasarkan jarak dan ambil 5 terdekat
                                fetchedPuskesmas.sort((p1, p2) -> Double.compare(p1.distance, p2.distance));
                                List<Puskesmas> terdekat =
                                        fetchedPuskesmas.subList(0, Math.min(5, fetchedPuskesmas.size()));

                                // Perbarui adapter RecyclerView
                                puskesmasList.clear();
                                puskesmasList.addAll(terdekat);
                                puskesmasAdapter.notifyDataSetChanged();

                                // Gerakkan kamera ke lokasi pengguna
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLat, userLng), 12));

                            } else {
                                Log.w("Lokasi", "Error getting documents.", task.getException());
                                Toast.makeText(getContext(), "Gagal memuat data puskesmas", Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    // --- Lifecycle Methods untuk MapView ---
    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mapView != null) {
            mapView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

}