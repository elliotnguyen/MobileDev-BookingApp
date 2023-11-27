package com.example.ticketbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ticketbooking.Model.TimeModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class PurchaseResultActivity extends AppCompatActivity {
    ImageView qrCodeImageview;
    TextView movieName;
    TextView cinemaName;
    TextView date;
    TextView time;
    TextView seat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_result);

        qrCodeImageview = findViewById(R.id.activity_purchase_result_image);
        movieName = findViewById(R.id.activity_purchase_result_movie_name);
        cinemaName = findViewById(R.id.activity_purchase_result_cinema_name);
        date = findViewById(R.id.activity_purchase_result_date);
        time = findViewById(R.id.activity_purchase_result_time);
        seat = findViewById(R.id.activity_purchase_result_seat);

        String MovieName = getIntent().getStringExtra("movieName");
        movieName.setText(MovieName);
        String CinemaName = getIntent().getStringExtra("cinemaName");
        cinemaName.setText(CinemaName);
        String Date = getIntent().getStringExtra("date");
        date.setText(Date);
        String Time = getIntent().getStringExtra("time");
        time.setText(TimeModel.showTimeWithFormat(Time));
        String Seat = getIntent().getStringExtra("seat");
        seat.setText(Seat);

        generateQRCode(MovieName, Date, Time, Seat);

        ImageView backBtn = findViewById(R.id.activity_purchase_result_close_btn);
        backBtn.setOnClickListener(view -> finish());

    }

    private void generateQRCode(String movieName, String date, String time, String seat) {
        String content = movieName.trim() + date.trim() + time.trim() + seat.trim();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 800, 800);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImageview.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}