package com.trinhtrung.quanlykhachsan;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {
    private View mView;
    private EditText edtOldPassword, edtNewPassword, edtConfirmNewPassword;
    private Button btnChangePassword;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_change_password, container, false);
        unitUi();

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChangePassword();
            }
        });
        return mView;
        // return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void unitUi() {
        progressDialog = new ProgressDialog(getActivity());
        edtOldPassword = mView.findViewById(R.id.edt_old_password);
        edtNewPassword = mView.findViewById(R.id.edt_new_password);
        edtConfirmNewPassword = mView.findViewById(R.id.edt_confirm_new_password);
        btnChangePassword = mView.findViewById(R.id.btn_change_password);

    }

    private void onClickChangePassword() {
        String strNewPassword = edtNewPassword.getText().toString().trim();
        String strConfirmNewPassword = edtConfirmNewPassword.getText().toString().trim();
        String oldpass = edtOldPassword.getText().toString().trim();//
        if(strNewPassword.isEmpty() || strConfirmNewPassword.isEmpty() || oldpass.isEmpty()){
            Toast.makeText(getActivity(), "Không được để trống", Toast.LENGTH_LONG).show();
            return;
        }


        if(strNewPassword.equals(strConfirmNewPassword)) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            progressDialog.show();
            final String email = user.getEmail();//
            AuthCredential credential = EmailAuthProvider.getCredential(email, oldpass);//
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        user.updatePassword(strConfirmNewPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "User password updated thành công.", Toast.LENGTH_SHORT).show();


                                        } else {
                                            //thường sẽ show dialog re-authenticate
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "User password updated thất bại.", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "PaswordOld false", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }else {
            Toast.makeText(getActivity(), "Mật khẩu mới không khớp", Toast.LENGTH_LONG).show();
        }
    }
}