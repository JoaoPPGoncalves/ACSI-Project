package com.example.tub.ui.qrcode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tub.R;
import com.example.tub.databinding.FragmentQrcodeBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.time.LocalDateTime;


public class QrCodeFragment extends Fragment {



    private FragmentQrcodeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*QrCodeViewModel qrCodeViewModel =
                new ViewModelProvider(this).get(QrCodeViewModel.class);

        binding = FragmentQrcodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;*/
        View view = inflater.inflate(R.layout.fragment_qrcode, container, false);
        ImageView imvQrCode = (ImageView) view.findViewById(R.id.qr_code_image);

        Bitmap bitmap = null;

        String idCliente = getString(R.string.id_cliente);
        String idConta = getString(R.string.cartao_cliente_id);
        LocalDateTime date = LocalDateTime.now();

        String resultado = idCliente + "," + idConta + "," + date.toString();
        TextView textview = view.findViewById(R.id.textViewqrcode);
        textview.setText(resultado);


        try {
            bitmap = textToImage(resultado, 300, 300);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        imvQrCode.setImageBitmap(bitmap);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
