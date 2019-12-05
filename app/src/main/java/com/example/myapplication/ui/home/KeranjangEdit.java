package com.example.myapplication.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.AppExecutors;
import com.example.myapplication.model.Keranjang;
import com.example.myapplication.model.KeranjangWithRelations;

public class KeranjangEdit extends AppCompatActivity {
    AppDatabase mDb;
    EditText mKuantiti;
    Button mSubmit;
    int mIdProduk;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang_edit);
        mDb = AppDatabase.getDatabase(getApplicationContext());
        mKuantiti = findViewById(R.id.etKuantiti);
        mSubmit = findViewById(R.id.btnSaveKuantiti);
        intent = getIntent();

        mIdProduk = getIntent().getIntExtra("update", -1);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                KeranjangWithRelations keranjang = mDb.keranjangDao().loadKeranjangById(mIdProduk);
                setUiData(keranjang);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
    }

    public void onSubmit() {
        final Keranjang data = new Keranjang(
                mIdProduk,
                Integer.parseInt(mKuantiti.getText().toString())
        );

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                data.setId(mIdProduk);
                mDb.keranjangDao().updateKeranjang(data);
                finish();
            }
        });


    }

    public void setUiData(final KeranjangWithRelations keranjang) {
        if (keranjang == null) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mKuantiti = findViewById(R.id.etKuantiti);
                mKuantiti.setText(String.valueOf(keranjang.keranjang.getQty()));
            }
        });
    }

}
