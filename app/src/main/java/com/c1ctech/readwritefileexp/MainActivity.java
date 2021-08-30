package com.c1ctech.readwritefileexp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private String TAG_WRITE_READ_FILE = "TAG_WRITE_READ_FILE";

    EditText edtInput;
    Button btnWriteToFile, btnReadFromFile, btnCreateCachedFile, btnReadCachedFile;
    String userData;
    String fileName;
    String cacheFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getViewsById();

        fileName = "User";
        cacheFileName = "UserCache";

        //write to internal file
        btnWriteToFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userData = edtInput.getText().toString();
                if (TextUtils.isEmpty(userData)) {
                    Toast.makeText(getApplicationContext(), "Input data can not be empty.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    try {
                        FileOutputStream fileOutputStream = getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
                        writeDataToFile(fileOutputStream, userData);
                        Toast.makeText(getApplicationContext(), "Data has been written to file " + fileName + " under " + getFilesDir().getAbsolutePath(), Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException ex) {
                        Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
                    }

                }
            }
        });

        //read from internal file
        btnReadFromFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileInputStream fileInputStream = getApplicationContext().openFileInput(fileName);
                    String fileData = readDataFromFile(fileInputStream);
                    if (fileData.length() > 0) {
                        edtInput.setText(fileData);
                        edtInput.setTextColor(Color.BLUE);
                        Toast.makeText(getApplicationContext(), "Load saved data from file " + fileName, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Not load any data.", Toast.LENGTH_SHORT).show();
                    }
                } catch (FileNotFoundException ex) {
                    Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
                }
            }
        });

        //write to internal cache file
        btnCreateCachedFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userData = edtInput.getText().toString();
                if (TextUtils.isEmpty(userData)) {
                    Toast.makeText(getApplicationContext(), "Input data can not be empty.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    File file = new File(getCacheDir(), cacheFileName);
                    writeDataToFile(file, userData);
                    Toast.makeText(getApplicationContext(), "Cached file " + cacheFileName + " is created " + " under " + getCacheDir().getAbsolutePath(), Toast.LENGTH_LONG).show();
                }
            }
        });

        //read from cache file
        btnReadCachedFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Context ctx = getApplicationContext();
                    File cacheFileDir = new File(getCacheDir(), cacheFileName);
                    FileInputStream fileInputStream = new FileInputStream(cacheFileDir);
                    String fileData = readDataFromFile(fileInputStream);
                    if (fileData.length() > 0) {
                        edtInput.setText(fileData);
                        edtInput.setTextColor(Color.MAGENTA);
                        Toast.makeText(ctx, "Load saved cache data from file " + cacheFileName, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ctx, "Not load any cache data.", Toast.LENGTH_SHORT).show();
                    }
                } catch (FileNotFoundException ex) {
                    Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
                }
            }
        });
    }

    //getting views by its id
    private void getViewsById() {

        edtInput = findViewById(R.id.edt_input);
        btnWriteToFile = findViewById(R.id.btn_write_to_file);
        btnReadFromFile = findViewById(R.id.btn_read_from_file);
        btnCreateCachedFile = findViewById(R.id.btn_create_cached_file);
        btnReadCachedFile = findViewById(R.id.btn_read_cached_file);
    }

    // This method will write data to file.
    private void writeDataToFile(File file, String data) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            this.writeDataToFile(fileOutputStream, data);
            fileOutputStream.close();
        } catch (FileNotFoundException ex) {
            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
        } catch (IOException ex) {
            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
        }
    }

    // This method will write data to FileOutputStream.
    private void writeDataToFile(FileOutputStream fileOutputStream, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStreamWriter.close();
        } catch (IOException ex) {
            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
        }

    }

    //This method will read data from FileInputStream
    private String readDataFromFile(FileInputStream fileInputStream) {
        StringBuffer retBuf = new StringBuffer();
        try {
            if (fileInputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String lineData = bufferedReader.readLine();
                while (lineData != null) {
                    retBuf.append(lineData);
                    lineData = bufferedReader.readLine();
                }
            }
        } catch (IOException ex) {
            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
        } catch (Exception ex) {
            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
        } finally {
            return retBuf.toString();
        }
    }
}
