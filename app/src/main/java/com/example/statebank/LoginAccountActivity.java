package com.example.statebank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Calendar;

public class LoginAccountActivity extends AppCompatActivity implements WithdrawFragment.WithdrawFragmentListener {
    private String acc;
    private String FILE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account);
        setTitle("State Bank");
        acc = getIntent().getStringExtra("AccNo");
        FILE_NAME  = acc + ".txt";

        Button button_wd = findViewById(R.id.button_withdraw);
        button_wd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag1 = new WithdrawFragment();
                replaceFragment(frag1);
            }
        });

        Button button_cb = findViewById(R.id.button_check_balance);
        button_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] cb = readFile(FILE_NAME);
                Toast.makeText(getApplicationContext(), "Your Balance is " + cb[2], Toast.LENGTH_LONG).show();
            }
        });

        Button button_th = findViewById(R.id.button_transaction_history);
        button_th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("AccNumber", acc);
                TransactionFragment tf = new TransactionFragment();
                tf.setArguments(bundle);
                Fragment frag2 = tf;
                replaceFragment(frag2);
            }
        });

        Button button_lg = findViewById(R.id.button_logout);
        button_lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void replaceFragment(Fragment frag) {
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();
        ft.replace(R.id.frame_layout, frag);
        ft.commit();
    }

    private String[] readFile(String filename) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myFile = new File(path, filename);
        String[] arr = new String[4];
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
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myFile = new File(folder, filename);
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

    @Override
    public void onWithdraw(CharSequence input, Calendar c) {
        int bal = Integer.parseInt(input.toString());
        String[] arr = readFile(FILE_NAME);
        if (bal > Integer.parseInt(arr[2])) {
            Toast.makeText(this, "Insufficient Amount", Toast.LENGTH_SHORT).show();
        } else {
            arr[2] = Integer.toString(Integer.parseInt(arr[2]) - bal);
            writeResFile(acc, c, "Withdraw", bal, arr[2]);
            writeFile(FILE_NAME, arr);
            Toast.makeText(this, "Amount Withdraw : " + bal, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeposit(CharSequence input, Calendar c) {
        int bal = Integer.parseInt(input.toString());
        String[] arr = readFile(FILE_NAME);
        arr[2] = Integer.toString(Integer.parseInt(arr[2]) + bal);
        writeResFile(acc, c, "Deposit", bal, arr[2]);
        writeFile(FILE_NAME, arr);
        Toast.makeText(this, "Amount Deposited : " + bal, Toast.LENGTH_SHORT).show();
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
            PrintWriter pw = new PrintWriter(fos);
            pw.print(data);
            pw.println("");
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
}