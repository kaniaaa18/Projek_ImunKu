package com.example.projek_imunku.ui.home;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articleList;

    public ArticleAdapter(List<Article> articleList) {
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_card, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.tvArticleTitle.setText(article.getTitle());

        holder.itemView.setOnClickListener(v -> {
            File downloadsDir = v.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            File pdfFile = new File(downloadsDir, article.getPdfFileName());

            if (pdfFile.exists()) {
                Uri path = FileProvider.getUriForFile(v.getContext(), v.getContext().getApplicationContext().getPackageName() + ".provider", pdfFile);
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path, "application/pdf");
                pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    v.getContext().startActivity(pdfIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(v.getContext(), "Tidak ada aplikasi untuk membuka PDF.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(v.getContext(), "File PDF tidak ditemukan: " + article.getPdfFileName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public void updateArticles(List<Article> newArticles) {
        this.articleList.clear();
        this.articleList.addAll(newArticles);
        notifyDataSetChanged();
    }


    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView tvArticleTitle;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvArticleTitle = itemView.findViewById(R.id.tv_article_title);
        }
    }
}