package com.example.statebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 101;
    private TextInputLayout textInputAccNo;
    private TextInputLayout textInputPIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("State Bank");
        textInputAccNo = findViewById(R.id.text_input_acc_no);
        textInputPIN = findViewById(R.id.text_input_pin);

        Button button_l = findViewById(R.id.button_login);
        button_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateAcc() | !validatePIN()) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                } else {
                    if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)) {
                        String acc = textInputAccNo.getEditText().getText().toString().trim();
                        String FILE_NAME = acc + ".txt";
                        if (fileExists(FILE_NAME)) {
                            String[] arr = readFile(FILE_NAME);

                            if (textInputPIN.getEditText().getText().toString().trim().equals(arr[3])) {
                                textInputAccNo.getEditText().setText("");
                                textInputPIN.getEditText().setText("");

                                Intent intent = new Intent(getApplicationContext(), LoginAccountActivity.class);
                                intent.putExtra("AccNo", acc);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Wrong PIN" + arr[3], Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "No Account Found", Toast.LENGTH_LONG).show();
                            textInputAccNo.getEditText().setText("");
                            textInputPIN.getEditText().setText("");
                        }
                    }
                }
            }
        });

        Button button_c = findViewById(R.id.button_create_account);
        button_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private Boolean validateAcc() {
        String accInput = textInputAccNo.getEditText().getText().toString().trim();
        if (accInput.isEmpty()) {
            textInputAccNo.setError("Field can't be Empty");
            return false;
        } else if (accInput.length() != 9) {
            textInputAccNo.setError("Account Number must be of 9 digits");
            return false;
        } else {
            return true;
        }
    }

    private Boolean validatePIN() {
        String pinInput = textInputPIN.getEditText().getText().toString().trim();
        if (pinInput.isEmpty()) {
            textInputPIN.setError("Field can't be Empty");
            return false;
        } else if (pinInput.length() != 4) {
            textInputPIN.setError("PIN must be of 4 digits");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean fileExists(String filename) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, filename);
        return file.exists();
    }

    private String[] readFile(String filename) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myFile = new File(path, filename);
        String[] arr = {"0","0","0","0"};
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(myFile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                arr[i] = line;
                i += 1;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return arr;
    }
}