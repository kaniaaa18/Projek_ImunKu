package com.example.projek_imunku.ui.lokasi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projek_imunku.R;
import com.example.projek_imunku.ui.lokasi.Puskesmas;

import java.util.List;
import java.util.ArrayList;

public class PuskesmasAdapter extends RecyclerView.Adapter<PuskesmasAdapter.PuskesmasViewHolder> {

    private Context context;
    private List<Puskesmas> puskesmasList;

    public PuskesmasAdapter(Context context, List<Puskesmas> puskesmasList) {
        this.context = context;
        this.puskesmasList = puskesmasList;
    }

    public void setPuskesmasList(List<Puskesmas> puskesmasList) {
        this.puskesmasList = puskesmasList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PuskesmasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_puskesmas_card, parent, false);
        return new PuskesmasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PuskesmasViewHolder holder, int position) {
        Puskesmas puskesmas = puskesmasList.get(position);
        holder.namaTextView.setText("Nama: " + puskesmas.getName());
        holder.alamatTextView.setText("Alamat: " + puskesmas.getAddress());
        holder.jarakTextView.setText(String.format("Jarak: %.2f km", puskesmas.getDistance()));
        holder.gMapsTextView.setOnClickListener(v -> {
            String url = puskesmas.getLink();
            if (url != null && !url.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                intent.setPackage("com.google.android.apps.maps");

                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "Google Maps tidak ditemukan.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Link Google Maps tidak tersedia.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return puskesmasList.size();
    }

    // ViewHolder untuk item RecyclerView
    static class PuskesmasViewHolder extends RecyclerView.ViewHolder {
        TextView namaTextView;
        TextView alamatTextView;
        TextView jarakTextView;
        TextView gMapsTextView;

        PuskesmasViewHolder(View itemView) {
            super(itemView);
            namaTextView = itemView.findViewById(R.id.text_puskesmas_name);
            alamatTextView = itemView.findViewById(R.id.text_puskesmas_address);
            jarakTextView = itemView.findViewById(R.id.text_puskesmas_distance);
            gMapsTextView = itemView.findViewById(R.id.tv_buka_gmaps);
        }
    }
}
