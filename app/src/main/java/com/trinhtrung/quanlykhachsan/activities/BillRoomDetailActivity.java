package com.trinhtrung.quanlykhachsan.activities;

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
import com.trinhtrung.quanlykhachsan.models.BillRoomModel;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class BillRoomDetailActivity extends AppCompatActivity {
    private TextView txtMaKH, txtTenKH, txtSoPhong, txtLoaiPhong, txtGiaPhong, txtNgayDK, txtNgayDi, txtThanhTien;
    private Button btnCreatePdf;
    private ProgressBar progressBar;
    private Toolbar toolbar ;
    private String strMaKH,strTenKH, strTenKH1,strSoPhong,strLoaiPhong,strGiaPhong,strNgayDK,strNgayDi,strThanhTien;
    // private List<BillServiceModel> billServiceModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_room_detail);

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

                confirmPrint(strMaKH,strTenKH,strTenKH1,strSoPhong,strLoaiPhong,strGiaPhong, strNgayDK, strNgayDi,strThanhTien);
            }
        });

    }

    private void setData() {

        Intent intent = getIntent();
        BillRoomModel billRoomModel = (BillRoomModel) intent.getSerializableExtra("dataBillRoom");
        txtMaKH.setText(billRoomModel.getMaKH());
        txtTenKH.setText( billRoomModel.getHoKH() + " "+ billRoomModel.getTenKH());
        txtSoPhong.setText(String.valueOf(billRoomModel.getSoPhong()));
        txtLoaiPhong.setText(billRoomModel.getLoaiPhong());
        txtGiaPhong.setText(String.valueOf(billRoomModel.getGiaPhong()));
        txtNgayDK.setText(String.valueOf(billRoomModel.getNgayDK()));
        txtNgayDi.setText(String.valueOf(billRoomModel.getNgayDi()));

        //tinh so ngay
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dateNgayDK = sdf.parse(billRoomModel.getNgayDK());
            Date dateNgayDi = sdf.parse(billRoomModel.getNgayDi());
            long startValue = dateNgayDK.getTime();
            long endValue = dateNgayDi.getTime();
            long tmp = Math.abs(startValue-endValue);

            long resultDay = tmp/(24*60*60*1000);
            long totalPrice = (long) ((billRoomModel.getGiaPhong() * resultDay));
            txtThanhTien.setText(String.valueOf(totalPrice));
            strThanhTien = String.valueOf(totalPrice);

        } catch (ParseException e) {
            e.printStackTrace();
        }







        strMaKH = billRoomModel.getMaKH();
        strTenKH = billRoomModel.getHoKH() + " "+ billRoomModel.getTenKH();
        strTenKH1 = billRoomModel.getHoKH() + billRoomModel.getTenKH();
        strSoPhong = String.valueOf(billRoomModel.getSoPhong());
        strLoaiPhong = billRoomModel.getLoaiPhong();

        strGiaPhong = String.valueOf(billRoomModel.getGiaPhong());
        strNgayDK = billRoomModel.getNgayDK();
        strNgayDi = billRoomModel.getNgayDi();


    }


    private void initUi() {
        txtMaKH = findViewById(R.id.txt_billroom_maKH);
        txtTenKH =findViewById(R.id.txt_billroom_tenKH);
        txtSoPhong = findViewById(R.id.txt_billroom_soPhong);
        txtLoaiPhong = findViewById(R.id.txt_billroom_loaiPhong);
        txtGiaPhong = findViewById(R.id.txt_billroom_giaPhong);
        txtNgayDK = findViewById(R.id.txt_billroom_ngayDK);
        txtNgayDi = findViewById(R.id.txt_billroom_ngayDi);


        txtThanhTien = findViewById(R.id.txt_billroom_thanhTien);


        btnCreatePdf = findViewById(R.id.btn_create_pdf);
        progressBar = findViewById(R.id.progressbar_pdf);
        toolbar = findViewById(R.id.toolbar_BillRoom_Detail);

    }

    private void createPdf(String maKH, String tenKH, String soPhong, String loaiPhong,
                           String giaPhong, String ngayDK, String NgayDi, String thanhTien,String tenFile) throws FileNotFoundException {

        //DIRECTORY_DOWNLOADS
        //Th?? m???c ti??u chu???n ????? ?????t c??c t???p ???? ???????c ng?????i d??ng t???i xu???ng
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

        File file = new File(pdfPath, tenFile + ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);
        pdfDocument.setDefaultPageSize(PageSize.A6);
        document.setMargins(0,0,0,0);
        Drawable d = ContextCompat.getDrawable(BillRoomDetailActivity.this,R.drawable.invoice_pdf);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100,stream);
        byte[] bitmapData = stream.toByteArray();
        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);

        Paragraph reportSalary = new Paragraph("Report Bill Room").setBold().setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER);
        PdfFont font = null;

        try {

            font = PdfFontFactory.createFont("assets/fonts/vuArial.ttf", PdfEncodings.IDENTITY_H, true);


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(BillRoomDetailActivity.this, "font error", Toast.LENGTH_SHORT).show();
        }
        // PdfFont font = PdfFontFactory.createFont( fontProgram,  Encoding. ) ;
        // Text text1 = new Text("M?? ch???m c??ng").setFont(font);
        //  Text textTenSP = new Text(tenSP).setFont(font);


        Paragraph address = new Paragraph("97 ???????ng Man Thi???n ph?????ng Hi???p Ph?? TP.HCM").setFont(font).setTextAlignment(TextAlignment.CENTER).setFontSize(12);
        float[] with = {110f,110f};
        Table table = new Table(with);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addCell(new Cell().add(new Paragraph("M?? kh??ch h??ng")));
        table.addCell(new Cell().add(new Paragraph(maKH).setFont(font)));

        table.addCell(new Cell().add(new Paragraph("T??n kh??ch h??ng").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(tenKH).setFont(font)));

        table.addCell(new Cell().add(new Paragraph("S??? ph??ng").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(soPhong).setFont(font)));

        table.addCell(new Cell().add(new Paragraph("Lo???i ph??ng").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(loaiPhong).setFont(font)));

        table.addCell(new Cell().add(new Paragraph("Gi?? Ph??ng").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(giaPhong)));


        table.addCell(new Cell().add(new Paragraph("Ng??y b???t ?????u thu??").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(ngayDK)));


        table.addCell(new Cell().add(new Paragraph("Ng??y k???t th??c thu??").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(NgayDi)));


        table.addCell(new Cell().add(new Paragraph("Th??nh ti???n").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(thanhTien).setFont(font)));


        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        table.addCell(new Cell().add(new Paragraph("Ng??y xu???t file").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(LocalDate.now().format(dateFormatter).toString())));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a");
        table.addCell(new Cell().add(new Paragraph("Th???i gian xu???t file PDF").setFont(font)));
        table.addCell(new Cell().add(new Paragraph(LocalTime.now().format(timeFormatter).toString())));


        BarcodeQRCode qrCode = new BarcodeQRCode("M?? kh??ch h??ng: " + maKH+ "\n" + "T??n kh??ch h??ng: " + tenKH + "\n" + "S??? ph??ng: " + soPhong
                + "\n" + "Lo???i ph??ng: " + loaiPhong + "Gi?? ph??ng " + giaPhong + "\n" + "Th??nh ti???n: " + thanhTien + "\n"
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

    private void confirmPrint(String maKH, String tenKH,String tenKH1, String soPhong, String loaiPhong,
                              String giaPhong, String ngayDK, String NgayDi,String thanhTien){
        Dialog dialog = new Dialog(BillRoomDetailActivity.this);
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

        tvTitle.setText("B???n c?? mu???n t???o file PDF cho bill c???a kh??ch h??ng:  " + tenKH.toString());
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
                    ActivityCompat.requestPermissions(BillRoomDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

                    String strFileName = edtFileName.getText().toString().trim();
                    if (strFileName.isEmpty()){
                        strFileName = maKH + tenKH1 ;
                    }
                    createPdf( maKH,  tenKH,  soPhong,  loaiPhong,
                             giaPhong, ngayDK, NgayDi,  thanhTien,strFileName);
                    dialog.dismiss();
                }catch (Exception e){
                    Log.d("checkPDF", e.toString());
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText("t???o file PDF kh??ng th??nh c??ng \n vui l??ng ki???m tra l???i t??n file");

                }

            }
        });

        dialog.show();

    }
}