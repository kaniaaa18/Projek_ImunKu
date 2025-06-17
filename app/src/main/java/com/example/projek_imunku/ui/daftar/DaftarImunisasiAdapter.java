package com.example.projek_imunku.ui.daftar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projek_imunku.R;

import java.util.List;
import android.view.LayoutInflater;


public class DaftarImunisasiAdapter extends RecyclerView.Adapter<DaftarImunisasiAdapter.DaftarImunisasiViewHolder> {

    private Context context;
    private List<DaftarImunisasi> DaftarImunisasiList;

    public DaftarImunisasiAdapter(Context context, List<DaftarImunisasi> puskesmasList) {
        this.context = context;
        this.DaftarImunisasiList = puskesmasList;
    }

    public void setDaftarImunisasiList(List<DaftarImunisasi> puskesmasList) {
        this.DaftarImunisasiList = puskesmasList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DaftarImunisasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_daftar_card, parent, false);
        return new DaftarImunisasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaftarImunisasiViewHolder holder, int position) {
        DaftarImunisasi daftarImunisasi = DaftarImunisasiList.get(position);

        holder.nameTextView.setText(daftarImunisasi.getName());
        holder.ageTextView.setText("Usia: " + daftarImunisasi.getAge());
        holder.typeTextView.setText("Jenis Imunisasi: " + daftarImunisasi.getType());
        holder.descTextView.setText("Deskripsi: " + daftarImunisasi.getDesc());
        holder.priceTextView.setText("Kisaran Harga: " + daftarImunisasi.getPrice());
    }

    @Override
    public int getItemCount() {
        return DaftarImunisasiList.size();
    }

    static class DaftarImunisasiViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView ageTextView;
        TextView typeTextView;
        TextView descTextView;
        TextView priceTextView;

        DaftarImunisasiViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_DI_name);
            ageTextView = itemView.findViewById(R.id.text_DI_age);
            typeTextView = itemView.findViewById(R.id.text_DI_type);
            descTextView = itemView.findViewById(R.id.text_DI_desc);
            priceTextView = itemView.findViewById(R.id.text_DI_price);
        }
    }
}
