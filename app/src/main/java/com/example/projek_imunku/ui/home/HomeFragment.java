package com.example.projek_imunku.ui.home;

import com.example.projek_imunku.ui.profile.ProfileFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projek_imunku.R;

public class HomeFragment extends Fragment {

    public static final String JENIS_IMUNISASI = "jenis_imunisasi";
    public static final String MANFAAT_IMUNISASI = "manfaat_imunisasi";
    public static final String MITOS_FAKTA = "mitos_fakta";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView userNameTextView = view.findViewById(R.id.user_name);

        SharedPreferences prefs = requireActivity().getSharedPreferences(ProfileFragment.PROFILE_PREFS, Context.MODE_PRIVATE);

        String defaultName = "Pengguna";
        String userName = prefs.getString(ProfileFragment.KEY_NAME, defaultName);

        if (userName.equals(defaultName)) {
            userNameTextView.setText("Selamat Datang, " + userName + "!");
        }
        else {
            userNameTextView.setText("Halo, " + userName + "!");
        }

        LinearLayout categoryJenis = view.findViewById(R.id.category_jenis_imunisasi);
        LinearLayout categoryManfaat = view.findViewById(R.id.category_manfaat_imunisasi);
        LinearLayout categoryMitos = view.findViewById(R.id.category_mitos_fakta);

        categoryJenis.setOnClickListener(v -> navigateToArticleList(JENIS_IMUNISASI));
        categoryManfaat.setOnClickListener(v -> navigateToArticleList(MANFAAT_IMUNISASI));
        categoryMitos.setOnClickListener(v -> navigateToArticleList(MITOS_FAKTA));
    }

    private void navigateToArticleList (String category){
        ArticleListFragment articleListFragment = new ArticleListFragment();

        Bundle args = new Bundle();
        args.putString(ArticleListFragment.ARG_CATEGORY, category);
        articleListFragment.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, articleListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}