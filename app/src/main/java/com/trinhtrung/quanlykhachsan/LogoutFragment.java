package com.trinhtrung.quanlykhachsan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.trinhtrung.quanlykhachsan.activities.LoginActivity;


public class LogoutFragment extends Fragment {
    // private ProgressDialog progressDialog;
    public LogoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // progressDialog = new ProgressDialog(getContext());
        // progressDialog.show();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logout, container, false);
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            //   progressDialog.dismiss();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}