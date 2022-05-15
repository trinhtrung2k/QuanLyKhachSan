package com.trinhtrung.quanlykhachsan.activities;

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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.trinhtrung.quanlykhachsan.R;

import com.trinhtrung.quanlykhachsan.models.BillServiceModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class BillServiceDetailActivity extends AppCompatActivity {
    private TextView txtMaHD, txtTenHD, txtTenNV, txtTenDV, txtTenKH, txtGiaDV, txtThanhTien;
    private Button btnCreatePdf , btnDelete;
    private ProgressBar progressBar;
    private Toolbar toolbar ;
    private String strMaHD,strTenHD,strTenNV,strTenDV,strtenKH,strGiaDV,strThanhTien;
   // private List<BillServiceModel> billServiceModelList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);

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

        btnCreatePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmPrint(strMaHD,strTenHD,strTenNV,strTenDV,strtenKH,strGiaDV,strThanhTien);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDeteleRoom(strMaHD);
            }
        });

    }

    private void setData() {
      /*  Intent intent = getIntent();
        BillServiceModel billServiceModel = (BillServiceModel) intent.getSerializableExtra("dataBillService");*/
        Intent intent = getIntent();
        BillServiceModel billServiceModel = (BillServiceModel) intent.getSerializableExtra("dataBillService");
        txtMaHD.setText(billServiceModel.getMaHD());
        txtTenHD.setText(billServiceModel.getTenHD());
        txtTenNV.setText(billServiceModel.getHoNV() + " " + billServiceModel.getTenNV());
        txtTenDV.setText(billServiceModel.getTenDV());
        txtTenKH.setText(billServiceModel.getHoKH() + " " + billServiceModel.getTenKH());
        txtGiaDV.setText(String.valueOf(billServiceModel.getGiaDV()));


        int a1 = (int) ((billServiceModel.getGiaDV()) * 1.1);
        txtThanhTien.setText(String.valueOf(a1));

        strMaHD = billServiceModel.getMaHD();
        strTenHD = billServiceModel.getTenHD();
        strTenNV = billServiceModel.getHoNV() + " " + billServiceModel.getTenNV();
        strTenDV = billServiceModel.getTenDV();
        strtenKH = billServiceModel.getHoKH() + " " + billServiceModel.getTenKH();
        strGiaDV = String.valueOf(billServiceModel.getGiaDV());
        strThanhTien = String.valueOf(a1);

    }


    private void initUi() {
        txtMaHD = findViewById(R.id.txt_bill_mahd);
        txtTenHD =findViewById(R.id.txt_bill_tenHD);
        txtTenNV = findViewById(R.id.txt_bill_tenNV);
        txtTenDV = findViewById(R.id.txt_bill_tenDV);
        txtTenKH = findViewById(R.id.txt_bill_tenKH);
        txtGiaDV = findViewById(R.id.txt_bill_giaDV);

        txtThanhTien = findViewById(R.id.txt_bill_thanhtien);
        btnCreatePdf = findViewById(R.id.btn_create_pdf1);
        progressBar = findViewById(R.id.progressbar_pdf1);
        toolbar = findViewById(R.id.toolbar_BillServiceDetail);
        btnDelete = findViewById(R.id.btn_bill_delete);


    }

    private void createPdf(String maHD, String tenHD, String tenNV, String tenDV,
                           String tenKH, String giaDV, String thanhTien,String tenFile) throws FileNotFoundException {

        //DIRECTORY_DOWNLOADS
        //Thư mục tiêu chuẩn để đặt các tệp đã được người dùng tải xuống
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

        File file = new File(pdfPath, tenFile + ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);
        pdfDocument.setDefaultPageSize(PageSize.A6);
        document.setMargins(0,0,0,0);
        Drawable d = ContextCompat.getDrawable(BillServiceDetailActivity.this,R.drawable.invoice_pdf);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80,stream);
        byte[] bitmapData = stream.toByteArray();
        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);

        Paragraph reportSalary = new Paragraph("Report Bill Service").setBold().setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER);
        PdfFont font = null;

        try {

            font = PdfFontFactory.createFont("assets/fonts/vuArial.ttf", PdfEncodings.IDENTITY_H, true);


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(BillServiceDetailActivity.this, "font error", Toast.LENGTH_SHORT).show();
        }
        // PdfFont font = PdfFontFactory.createFont( fontProgram,  Encoding. ) ;
        // Text text1 = new Text("Mã chấm công").setFont(font);
        //  Text textTenSP = new Text(tenSP).setFont(font);


        Paragraph address = new Paragraph("97 đường Man Thiện phường Hiệp Phú TP.HCM").setFont(font).setTextAlignment(TextAlignment.CENTER).setFontSize(12);
        float[] with = {110f,110f};
        Table table = new Table(with);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addCell(new Cell().add(new Paragraph("Mã hoá đơn")));
        table.addCell(new Cell().add(new Paragraph(maHD).setFont(font)));

        table.addCell(new Cell().add(new Paragraph("Tên hoá đơn").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(tenHD).setFont(font)));

        table.addCell(new Cell().add(new Paragraph("Tên nhân viên").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(tenNV).setFont(font)));

        table.addCell(new Cell().add(new Paragraph("Tên dịch vụ").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(tenDV).setFont(font)));

        table.addCell(new Cell().add(new Paragraph("Tên khách hàng").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(tenKH)));


        table.addCell(new Cell().add(new Paragraph("Giá dịch vụ").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(giaDV).setFont(font)));

        table.addCell(new Cell().add(new Paragraph("Thành tiền").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(thanhTien).setFont(font)));


        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        table.addCell(new Cell().add(new Paragraph("Ngày xuất file").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(LocalDate.now().format(dateFormatter).toString())));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a");
        table.addCell(new Cell().add(new Paragraph("Thời gian xuất file PDF").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(LocalTime.now().format(timeFormatter).toString())));

        
        BarcodeQRCode qrCode = new BarcodeQRCode("Mã hoá đơn: " + maHD+ "\n" + "Tên hoá đơn: " + tenHD + "\n" + "Tên nhân viên: " + tenNV
                + "\n" + "Tên dịch vụ: " + tenDV +
                "\n" + "Tên khách hàng: " + tenKH +  "\n" + "Giá dịch vụ: " + giaDV + "\n" + "Thành tiền: " + thanhTien + "\n"
                + LocalDate.now().format(dateFormatter) + "\n" +  LocalTime.now().format(timeFormatter));


        PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK,pdfDocument);

        Image qrCodeImage = new Image(qrCodeObject).setWidth(80).setHorizontalAlignment(HorizontalAlignment.CENTER).setFont(font);

        document.add(image);
        document.add(reportSalary);
        document.add(address);
        document.add(table);
        document.add(qrCodeImage);

        document.close();


    }

    private void confirmPrint(String maHD, String tenHD, String tenNV, String tenDV,
                              String tenKH, String giaDV, String thanhTien){
        Dialog dialog = new Dialog(BillServiceDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_print_pdf);
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
        EditText edtFileName = dialog.findViewById(R.id.edt_filename_pdf);

        tvTitle.setText("Bạn có muốn tạo file PDF cho bill của khách hàng:  " + tenKH.toString());
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
                    btnCancel.isSelected();
                    ActivityCompat.requestPermissions(BillServiceDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

                    String strFileName = edtFileName.getText().toString().trim();
                    if (strFileName.isEmpty()){
                        strFileName = maHD ;
                    }
                    createPdf(maHD, tenHD, tenNV, tenDV, tenKH, giaDV ,thanhTien,strFileName);
                    dialog.dismiss();
                }catch (Exception e){
                    Log.d("checkPDF", e.toString());
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText("tạo file PDF không thành công \n vui lòng kiểm tra lại tên file");

                }

            }
        });

        dialog.show();

    }

    public void deleteHoaDonService(String maHD ){


        String urlDelete = "http://192.168.1.12:8081/QuanLyKhachSan/deleteDataBillService.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlDelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(BillServiceDetailActivity.this, "success", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(BillServiceDetailActivity.this, "Hóa đơn dịch vụ này đang chứa thông tin trong bảng khác", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BillServiceDetailActivity.this, "Xảy ra lỗi : " + error, Toast.LENGTH_LONG).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("maHDDelete",maHD);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void ConfirmDeteleRoom(String strMaHD) {

        Dialog dialog = new Dialog(BillServiceDetailActivity.this);
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
        tvTitle.setText("Bạn thực sự muốn xoá hóa đơn mã " + strMaHD.toString());
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
                    deleteHoaDonService(strMaHD);
                    dialog.dismiss();
                }catch (Exception e){
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText("Xoá không thành công do ràng buộc quan hệ");
                }

            }
        });

        dialog.show();



    }
}