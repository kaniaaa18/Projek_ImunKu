package com.example.projek_imunku.ui.jadwal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.projek_imunku.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class input_data_anak extends Fragment {

    private EditText etTanggalPengukuran;
    private Spinner spinnerJenisKelamin;
    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_data_anak, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTanggalPengukuran = view.findViewById(R.id.etTanggalPengukuran);
        spinnerJenisKelamin = view.findViewById(R.id.spinnerJenisKelamin);
        ImageView iconKalender = view.findViewById(R.id.icon_kalender_pengukuran);
        Button btnSimpan = view.findViewById(R.id.btnSimpanDataAnak);

        // Setup Spinner
        setupJenisKelaminSpinner();

        // Setup Date Picker
        DatePickerDialog.OnDateSetListener date = (view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        etTanggalPengukuran.setOnClickListener(v -> showDatePicker(date));
        iconKalender.setOnClickListener(v -> showDatePicker(date));

        btnSimpan.setOnClickListener(v -> {
            // Logika untuk menyimpan data anak
            Toast.makeText(getContext(), "Data Anak Disimpan!", Toast.LENGTH_SHORT).show();
            // Kembali ke halaman sebelumnya
            getParentFragmentManager().popBackStack();
        });
    }

    private void setupJenisKelaminSpinner() {
        String[] jenisKelamin = new String[]{"Laki-laki", "Perempuan"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, jenisKelamin);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisKelamin.setAdapter(adapter);
    }

    private void showDatePicker(DatePickerDialog.OnDateSetListener date) {
        new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        etTanggalPengukuran.setText(sdf.format(myCalendar.getTime()));
    }
}