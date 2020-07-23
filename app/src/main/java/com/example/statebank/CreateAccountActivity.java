package com.example.statebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Calendar;

public class CreateAccountActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 100;
    private TextInputLayout textInputName;
    private TextInputLayout textInputBalance;
    private TextInputLayout textInputPIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setTitle("State Bank");
        textInputName = findViewById(R.id.text_input_new_name);
        textInputBalance = findViewById(R.id.text_input_new_balance);
        textInputPIN = findViewById(R.id.text_input_new_pin);

        Button button = findViewById(R.id.button_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                if (validateName() && validateBalance() && validatePIN()) {
                    if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)) {
                        String[] new_data = new String[4];
                        new_data[0] = "637100500";
                        new_data[1] = textInputName.getEditText().getText().toString().trim();
                        new_data[2] = textInputBalance.getEditText().getText().toString().trim();
                        new_data[3] = textInputPIN.getEditText().getText().toString().trim();

                        if (fileExists("Accounts.txt")) {
                            String[] arr = readFile("Accounts.txt");
                            if (!arr[0].equals("0")) {
                                new_data[0] = arr[0];
                                arr[0] = Integer.toString(Integer.parseInt(arr[0]) + 1);
                                writeFile("Accounts.txt", arr);

                                String FILE_NAME = new_data[0] + ".txt";
                                writeFile(FILE_NAME, new_data);
                                writeResFile(new_data[0], c, "Opening", Integer.parseInt(new_data[2]), new_data[2]);
                                Toast.makeText(CreateAccountActivity.this, "Acc No : " + new_data[0], Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String[] arr = new String[1];
                            arr[0] = Integer.toString(Integer.parseInt(new_data[0]) + 1);
                            writeFile("Accounts.txt", arr);

                            String FILE_NAME = new_data[0] + ".txt";
                            writeFile(FILE_NAME, new_data);
                            writeResFile(new_data[0], c, "Opening", Integer.parseInt(new_data[2]), new_data[2]);
                            Toast.makeText(CreateAccountActivity.this, "Acc No : " + new_data[0], Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateName() {
        String name = textInputName.getEditText().getText().toString().trim();
        if (name.isEmpty()) {
            textInputName.setError("Field can't be empty");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateBalance() {
        String bal = textInputBalance.getEditText().getText().toString().trim();
        if (bal.isEmpty()) {
            textInputBalance.setError("Field can't be empty");
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePIN() {
        String pin = textInputPIN.getEditText().getText().toString().trim();
        if (pin.isEmpty()) {
            textInputPIN.setError("Field can't be empty");
            return false;
        } else if (pin.length() != 4) {
            textInputPIN.setError("Field must have 4 digits");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(CreateAccountActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(CreateAccountActivity.this, new String[] { permission }, requestCode);
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
                Toast.makeText(CreateAccountActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateAccountActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
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

    private void writeFile(String filename, String[] nam) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myFile = new File(path, filename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myFile);
            PrintWriter pw = new PrintWriter(fos);
            int i = 0;
            while (i < nam.length) {
                pw.println(nam[i]);
                i += 1;
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void writeResFile(String f, Calendar c, String string, int bal, String totalBal) {
        String filename = f + "-res.txt";
        String date = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
        String time = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
        String data = date + " " + time + " " + string + " " + bal + " " + totalBal;

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myFile = new File(path, filename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myFile, true);
            PrintWriter wrt = new PrintWriter(fos);
            wrt.println(data);
            wrt.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}