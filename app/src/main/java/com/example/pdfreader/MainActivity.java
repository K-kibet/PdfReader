package com.example.pdfreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MainAdapter adapter;
    List<File> pdfList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runtimePermission();
    }

    private void runtimePermission() {
        Dexter.withContext(MainActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displayPdf();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }


    public ArrayList<File> findPdf (File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();


        for(File singleFile: files) {
            if(singleFile.isDirectory() && !singleFile.isHidden()) {
                arrayList.addAll(findPdf(singleFile));
            } else {
                if(singleFile.getName().endsWith(".pdf")) {
                    arrayList.add(singleFile);
                }
            }
        }
        System.out.println(arrayList);
        return arrayList;
    }


    public void displayPdf () {
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        pdfList = new ArrayList<>();
        pdfList.addAll(findPdf(Environment.getExternalStorageDirectory()));
        adapter = new MainAdapter(MainActivity.this, pdfList);
        recyclerView.setAdapter(adapter);
    }
}