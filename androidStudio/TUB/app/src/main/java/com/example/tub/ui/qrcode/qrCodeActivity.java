package com.example.tub.ui.qrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tub.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

// Importe as bibliotecas necessárias

public class qrCodeActivity extends AppCompatActivity {
    private Button btnGenerate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_qrcode); // Defina o layout da qrCodeActivity
        //btnGenerate = (Button) findViewById(R.id.button_generate);

        Context a = this;

        //Button button = (Button) findViewById(R.id.button_generate); // Use o ID correto do Button da qrCodeActivity
        //ImageView qr_code = findViewById(R.id.qr_code_image); // Use o ID correto do ImageView da qrCodeActivity

//        btnGenerate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(a, "Button clickedasdasdasdad", Toast.LENGTH_SHORT).show();
//
//
//                try {
//                    Bitmap bitmap = textToImage("your_text_info", 500, 500);
//                    Toast.makeText(a, "pisisss", Toast.LENGTH_SHORT).show();
//                    ImageView imvQrCode = (ImageView) findViewById(R.id.qr_code_image);
//                    imvQrCode.setImageBitmap(bitmap);
//                } catch (WriterException | NullPointerException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    private Bitmap textToImage(String text, int width, int height) throws WriterException, NullPointerException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.DATA_MATRIX.QR_CODE, width, height, null);
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        int colorWhite = 0xFFFFFFFF;
        int colorBlack = 0xFF000000;

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? colorBlack : colorWhite;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}




//public class qrCodeActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        Button button = findViewById(R.id.button_generate);
//        ImageView imageView = findViewById(R.id.qr_code);
//
//     /*  button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//
//                try {
//                    BitMatrix bitMatrix = multiFormatWriter.encode(editText.getText().toString(), BarcodeFormat.QR_CODE,300,300);
//
//                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//
//                    imageView.setImageBitmap(bitmap);
//
//                }catch (WriterException e){
//                    throw  new RuntimeException(e);
//                }
//            }
//        });*/
//    }

