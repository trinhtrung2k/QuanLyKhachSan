package com.trinhtrung.quanlykhachsan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.trinhtrung.quanlykhachsan.models.UserModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    CircleImageView profileImg;
    TextView name, email, number, address;
    Button update;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase db;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        profileImg = view.findViewById(R.id.profile_img);
        name = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,24);




            }
        });

        db.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        Glide.with(getContext()).load(userModel.getProfileImg()).error(R.drawable.profile).into(profileImg);
                        name.setText(userModel.getName());
                        email.setText(userModel.getEmail());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
/*
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  updateUserProfile();
            }
        });*/
        return view;
    }

   /* private void updateUserProfile() {

        int PhoneNumber = Integer.parseInt(String.valueOf(number.getText()));
        String strAddress = address.getText().toString();
        String strName = name.getText().toString();
        String strEmail = email.getText().toString();
            UserModel user = new UserModel(strEmail,strName,PhoneNumber,strAddress);

            db.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).setValue(user);


    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 24 && data != null && data.getData() != null ){
       // if (data.getData() != null){
            Uri profileUri = data.getData();
            try {
                profileImg.setImageURI(profileUri);

                final StorageReference reference = storage.getReference().child("profile_picture")
                        .child(FirebaseAuth.getInstance().getUid());
                reference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     //   Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "Đã tải lên", Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                db.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                        .child("profileImg").setValue(uri.toString());
                               // Toast.makeText(getContext(), "Profile Picture Uploaded", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), "Ảnh hồ sơ đã được tải lên", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });

            }catch (Exception e){
                Toast.makeText(getContext(), "Tải lên ảnh hồ sơ không thành công", Toast.LENGTH_SHORT).show();

            }

        }
    }
}