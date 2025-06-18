package com.example.projek_imunku.ui.jadwal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.projek_imunku.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class JadwalImunisasiFragment extends Fragment {

    // Declare UI elements
    private Button btnTambah, btnEdit, btnSimpan;
    private Spinner spinnerNamaAnak, spinnerJenisImunisasi;
    private EditText etTanggalImunisasi;
    private RelativeLayout datePickerLayout;
    private ImageView iconKalender;


    // BARU: Wadah untuk menampung kartu-kartu record
    private LinearLayout containerRecords;

    private final Calendar myCalendar = Calendar.getInstance();
    private int recordCounter = 0; // Untuk memberi nomor pada judul record

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_jadwal_imunisasi);

        initializeViews();
        setupSpinners();
        setupListeners();
    }

    private void initializeViews() {
        btnTambah = findViewById(R.id.btnTambah);
        btnEdit = findViewById(R.id.btnEdit);
        btnSimpan = findViewById(R.id.btnSimpan);

        spinnerNamaAnak = findViewById(R.id.spinnerNamaAnak);
        spinnerJenisImunisasi = findViewById(R.id.spinnerJenisImunisasi);

        etTanggalImunisasi = findViewById(R.id.etTanggalImunisasi);
        datePickerLayout = (RelativeLayout) etTanggalImunisasi.getParent();
        iconKalender = findViewById(R.id.icon_kalender);

        // BARU: Inisialisasi wadah/container
        containerRecords = findViewById(R.id.container_records);
    }

    private void setupSpinners() {
        // (Tidak ada perubahan di sini, biarkan seperti semula)
        List<String> namaAnakList = new ArrayList<>();
        namaAnakList.add("Pilih Nama Anak");
        namaAnakList.add("Aulia Putri");
        namaAnakList.add("Budi Santoso");
        namaAnakList.add("Citra Lestari");
        ArrayAdapter<String> namaAnakAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, namaAnakList);
        namaAnakAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNamaAnak.setAdapter(namaAnakAdapter);

        List<String> jenisImunisasiList = new ArrayList<>();
        jenisImunisasiList.add("Pilih Jenis Imunisasi");
        jenisImunisasiList.add("BCG");
        jenisImunisasiList.add("DPT-HB-Hib");
        jenisImunisasiList.add("Polio");
        jenisImunisasiList.add("Campak");
        ArrayAdapter<String> jenisImunisasiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenisImunisasiList);
        jenisImunisasiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisImunisasi.setAdapter(jenisImunisasiAdapter);
    }

    private void setupListeners() {
        btnTambah.setOnClickListener(v -> {
            Toast.makeText(JadwalImunisasiFragment.this, "Silakan isi form", Toast.LENGTH_SHORT).show();
            clearForm();
        });

        btnEdit.setOnClickListener(v -> {
            Toast.makeText(JadwalImunisasiFragment.this, "Fitur Edit belum diimplementasikan", Toast.LENGTH_SHORT).show();
        });

        // DIUBAH: Logika tombol Simpan
        btnSimpan.setOnClickListener(v -> {
            String namaAnak = spinnerNamaAnak.getSelectedItem().toString();
            String jenisImunisasi = spinnerJenisImunisasi.getSelectedItem().toString();
            String tanggal = etTanggalImunisasi.getText().toString();

            if (namaAnak.equals("Pilih Nama Anak") || jenisImunisasi.equals("Pilih Jenis Imunisasi") || tanggal.isEmpty()) {
                Toast.makeText(JadwalImunisasiFragment.this, "Harap lengkapi semua data", Toast.LENGTH_SHORT).show();
            } else {
                // Panggil fungsi untuk menambahkan kartu baru
                addRecordCard(namaAnak, jenisImunisasi, tanggal);
                Toast.makeText(JadwalImunisasiFragment.this, "Data Disimpan!", Toast.LENGTH_SHORT).show();
                // Kosongkan form setelah berhasil menyimpan
                clearForm();
            }
        });

        // (Tidak ada perubahan pada Date Picker Listener, biarkan seperti semula)
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        View.OnClickListener dateClickListener = v -> new DatePickerDialog(
                JadwalImunisasiFragment.this,
                dateSetListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show();

        datePickerLayout.setOnClickListener(dateClickListener);
        iconKalender.setOnClickListener(dateClickListener);
    }

    // BARU: Fungsi untuk membuat dan menambahkan kartu record baru secara dinamis
    private void addRecordCard(String namaAnak, String jenisImunisasi, String tanggal) {
        recordCounter++; // Tambah nomor record
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Buat view baru dari template item_imunisasi_record.xml
        View recordView = inflater.inflate(R.layout.item_imunisasi_record, containerRecords, false);

        // Dapatkan referensi ke elemen di dalam kartu
        TextView tvRecordTitle = recordView.findViewById(R.id.tvRecordTitle);
        TextView tvNamaAnak = recordView.findViewById(R.id.tvNamaAnak);
        TextView tvJenisImunisasi = recordView.findViewById(R.id.tvJenisImunisasi);
        TextView tvTanggalImunisasi = recordView.findViewById(R.id.tvTanggalImunisasi);
        ImageView deleteIcon = recordView.findViewById(R.id.delete_icon);
        CardView card = (CardView) recordView; // Dapatkan referensi ke CardView itu sendiri

        // Ganti warna kartu agar bervariasi
        if (recordCounter % 2 == 0) {
            card.setCardBackgroundColor(getResources().getColor(R.color.blue_card)); // Anda perlu mendefinisikan warna ini di colors.xml
        } else {
            card.setCardBackgroundColor(getResources().getColor(R.color.orange_card)); // Anda perlu mendefinisikan warna ini di colors.xml
        }


        // Set data ke dalam elemen-elemen tersebut
        tvRecordTitle.setText("Jadwal Imunisasi " + recordCounter);
        tvNamaAnak.setText(namaAnak);
        tvJenisImunisasi.setText(jenisImunisasi);
        tvTanggalImunisasi.setText(tanggal);

        // Set listener untuk ikon hapus PADA KARTU YANG BARU DIBUAT INI
        deleteIcon.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Hapus Data")
                    .setMessage("Apakah Anda yakin ingin menghapus jadwal untuk " + namaAnak + "?")
                    .setPositiveButton("Hapus", (dialog, which) -> {
                        // Hapus view kartu ini dari wadahnya
                        containerRecords.removeView(recordView);
                        Toast.makeText(JadwalImunisasiFragment.this, "Data dihapus", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Batal", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        // Tambahkan kartu yang sudah jadi ke dalam wadah
        containerRecords.addView(recordView);
    }

    private void updateLabel() {
        String myFormat = "dd MMMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("id", "ID"));
        etTanggalImunisasi.setText(sdf.format(myCalendar.getTime()));
    }

    private void clearForm() {
        spinnerNamaAnak.setSelection(0);
        spinnerJenisImunisasi.setSelection(0);
        etTanggalImunisasi.setText("");
    }
}