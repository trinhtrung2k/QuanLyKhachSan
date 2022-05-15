package com.trinhtrung.quanlykhachsan.activities;

import static com.trinhtrung.quanlykhachsan.Api.GetPathFromUi.getPathFromUri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import com.trinhtrung.quanlykhachsan.R;
import com.trinhtrung.quanlykhachsan.models.StaffModel;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditStaffActivity extends AppCompatActivity {

    private EditText edtHoNV, edtTenNV,edtGioiTinh,edtSdt,edtCCCD, edtChucVu, edtDiaChi, edtNamSinh;
    private TextView edtMaNV,tv_message_edt_staff;
    private Button btnEdit,btnDelete;
    private Toolbar toolbar ;
    private ImageView imgSelectNV, imgUpload;
    private static final int STORAGE_PERMISSION_CODE = 2342;
    private int PICK_IMAGE_REQUEST = 22;
    private Uri filepath;
    private Bitmap bitmap;
    private String urlImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff);
        requestStoragePermission();
        initUi();
        setData();
        setEvent();

    }

    private void setEvent() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

 imgSelectNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filepath = null;
                ShowFileChooser();

            }
        });
 imgUpload.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         uploadImage();
     }
 });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String strMaCN = edtMaCN.getText().toString().trim();
                String strMaNV= edtMaNV.getText().toString().trim();
                String strHoNV= edtHoNV.getText().toString().trim();
                String strTenNV = edtTenNV.getText().toString().trim();
                String strNgaySinh = edtNamSinh.getText().toString().trim();
                String strGioiTinh = edtGioiTinh.getText().toString().trim();
                String strDiaChi = edtDiaChi.getText().toString().trim();
                String sdt = edtSdt.getText().toString().trim();
                String cccd = edtCCCD.getText().toString().trim();
                String strChucVu = edtChucVu.getText().toString().trim();


                if (strMaNV.isEmpty() || strHoNV.isEmpty() || strNgaySinh.isEmpty() || strGioiTinh.isEmpty()
                        || sdt.isEmpty() || cccd.isEmpty()  || strTenNV.isEmpty() || strDiaChi.isEmpty()   || strChucVu.isEmpty()){
                    tv_message_edt_staff.setVisibility(View.VISIBLE);
                    tv_message_edt_staff.setText("Vui Lòng nhập đủ thông tin, không được để trống");
                    tv_message_edt_staff.setTextColor(getResources().getColor(R.color.red));
                }
                else{
                    tv_message_edt_staff.setVisibility(View.GONE);

                    UpdateStaff();


                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTenNV =edtHoNV.getText().toString() + " " +edtTenNV.getText().toString();
                String strMaNV =edtMaNV.getText().toString();

                ConfirmDeteleStaff(strTenNV,strMaNV);
            }
        });



    }

    private void setData() {
        Intent intent = getIntent();
        StaffModel staffModel = (StaffModel) intent.getSerializableExtra("dataStaff");
        edtMaNV.setText(staffModel.getMaNV());
        edtHoNV.setText(staffModel.getHoNV());
        edtTenNV.setText(staffModel.getTenNV());
        edtNamSinh.setText(staffModel.getNgaySinh());
        edtGioiTinh.setText(staffModel.getGioiTinh());
        edtSdt.setText(String.valueOf(staffModel.getSDT()));
        edtCCCD.setText(String.valueOf(staffModel.getCCCD()));
        edtDiaChi.setText(staffModel.getDiaChi());
        urlImage = staffModel.getHinh();

        edtChucVu.setText(staffModel.getChucVu());
        Glide.with(this).load(urlImage).error(R.drawable.profile).into(imgSelectNV);

    }


    private void initUi() {
        edtMaNV = findViewById(R.id.edt_maNV);
        edtHoNV = findViewById(R.id.edt_hoNV);
        edtTenNV = findViewById(R.id.edt_tenNV);
        edtGioiTinh = findViewById(R.id.edt_gioitinhNV);
        edtSdt = findViewById(R.id.edt_sdtnv);
        edtCCCD = findViewById(R.id.edt_cccdNV);
        edtChucVu= findViewById(R.id.edt_chucvuNV);
        edtDiaChi = findViewById(R.id.edt_diachiNV);
        edtNamSinh = findViewById(R.id.edt_namsinhNV);
        imgSelectNV = findViewById(R.id.imgSelectNV);
        imgUpload = findViewById(R.id.img_upload);

        btnEdit = findViewById(R.id.btn_edit_staff);
        tv_message_edt_staff = findViewById(R.id.tv_message_edt_staff);
        toolbar = findViewById(R.id.toolbar_edtStaff);
        btnDelete = findViewById(R.id.btn_delete_staff);
    }
    private void UpdateStaff(){
      
        String url = "http://192.168.1.12:8081/QuanLyKhachSan/updateDataStaff.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")){
                    Toast.makeText(EditStaffActivity.this, "success", Toast.LENGTH_SHORT).show();
                    finish();

                }else {
                    Toast.makeText(EditStaffActivity.this, "error", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditStaffActivity.this, "xảy ra lỗi: " + error, Toast.LENGTH_LONG).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("maNV", edtMaNV.getText().toString().trim());
                params.put("hoNV",edtHoNV.getText().toString().trim());
                params.put("tenNV",edtTenNV.getText().toString().trim());
                params.put("gioiTinh",edtGioiTinh.getText().toString().trim());
                params.put("ngaySinh",edtNamSinh.getText().toString().trim());
                params.put("diaChi",edtDiaChi.getText().toString().trim());
                params.put("sdt",edtSdt.getText().toString().trim());
                params.put("cccd",edtCCCD.getText().toString().trim());
                params.put("chucVu",edtChucVu.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }


    private void ConfirmDeteleStaff(String tenNV, String maNV) {

        Dialog dialog = new Dialog(EditStaffActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_feeback);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes  =window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        //khi click ra ngoai thi se thoat
        if (Gravity.CENTER == Gravity.CENTER){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }

        TextView tvTitle = dialog.findViewById(R.id.tv_title_pdf);
        TextView tvMessage = dialog.findViewById(R.id.tv_add_Fail_pdf);
        tvTitle.setText("Bạn thực sự muốn xoá nhân viên " + tenNV.toString());
        Button btnOk = dialog.findViewById(R.id.btn_dialog_OK_pdf);
        Button btnCancel = dialog.findViewById(R.id.btn_dialog_Cancel_pdf);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCancel.isSelected();
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    btnOk.isSelected();
                    deleteStaff(String.valueOf(maNV));
                    dialog.dismiss();
                }catch (Exception e){
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText("Xoá không thành công do ràng buộc quan hệ");
                }

            }
        });

        dialog.show();



    }

    public void deleteStaff(String maNV ){


        String urlDelete = "http://192.168.1.12:8081/QuanLyKhachSan/deleteDataStaff.php";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlDelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(EditStaffActivity.this, "success", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(EditStaffActivity.this, "Khách hàng này đang chứa thông tin trong bảng khác", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditStaffActivity.this, "Xảy ra lỗi : " + error, Toast.LENGTH_LONG).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("maNVDelete",maNV);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void requestStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

      /*  if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }*/
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void ShowFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {


            filepath = data.getData();
            Uri profileUri = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
            //    imgSelectNV.setImageBitmap(bitmap);
                imgSelectNV.setImageURI(profileUri);


                Log.d("filepath", "duong dan: "+ (filepath));
              //  uploadBitmap();

            } catch (Exception ex) {

            }
        }
    }



    private void uploadImage() {
        String urlSelectImage= "http://192.168.1.12:8081/QuanLyKhachSan/updateImage.php";
        String strMaNV = edtMaNV.getText().toString().trim();

      //  String path = getPath(filepath);
        if (filepath == null){
            return;
        }




        try {
            String path =   getPathFromUri(getApplicationContext(),filepath);
            String uploadId = UUID.randomUUID().toString();
            new MultipartUploadRequest(getApplicationContext(), uploadId, urlSelectImage)

                    .addFileToUpload(path, "upload")
                    .addParameter("maNV",strMaNV)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();
            path = null;
            Log.d("filepathh",path);

        } catch (Exception ex) {
            Toast.makeText(EditStaffActivity.this, "Upload Image fail" , Toast.LENGTH_SHORT).show();
            Log.d("error",ex.toString());


        }

    }
/*

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    protected void onResume(){
        super.onResume();
        Log.d("onResume","onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy","onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("onStop","onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart","onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause","onPause");
    }*/
}