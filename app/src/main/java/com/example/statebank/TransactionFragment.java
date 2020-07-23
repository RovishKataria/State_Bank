package com.example.statebank;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment {

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_transaction, container, false);
        if (getArguments() != null) {
            String[] arr = readFile(getArguments().getString("AccNumber") + "-res.txt");
            TableLayout tableLayout = v.findViewById(R.id.main_table);

            for (int i = 0; i < arr.length; i++) {
                String[] up = arr[i].split(" ");
                TableRow tr = new TableRow(getContext());

                TextView tv = new TextView(getContext());
                tv.setText(up[0]);
                tv.setWidth(150);

                TextView tv1 = new TextView(getContext());
                tv1.setText(up[1]);
                tv1.setWidth(150);

                TextView tv2 = new TextView(getContext());
                tv2.setText(up[2]);
                tv2.setWidth(150);

                TextView tv3 = new TextView(getContext());
                tv3.setText(up[3]);
                tv3.setWidth(150);

                TextView tv4 = new TextView(getContext());
                tv4.setText(up[4]);
                tv4.setWidth(150);

                tr.addView(tv);
                tr.addView(tv1);
                tr.addView(tv2);
                tr.addView(tv3);
                tr.addView(tv4);
                tableLayout.addView(tr);
            }
        }
        return v;
    }

    private String[] readFile(String filename) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myFile = new File(path, filename);
        String[] arr = {"0"};
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(myFile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;

            arr[0] = br.readLine();
            ArrayList<String> myList = new ArrayList<String>(Arrays.asList(arr));
            while ((line = br.readLine()) != null) {
                myList.add(line);
            }
            br.close();
            arr = myList.toArray(arr);
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
