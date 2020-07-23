package com.example.statebank;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class WithdrawFragment extends Fragment {
    private WithdrawFragmentListener listener;
    private TextInputLayout textInputAmount;

    public WithdrawFragment() {
        // Required empty public constructor
    }

    public interface WithdrawFragmentListener {
        void onWithdraw(CharSequence input, Calendar c);
        void onDeposit(CharSequence input, Calendar c);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_withdraw, container, false);
        textInputAmount = v.findViewById(R.id.text_input_amount);

        Button button_wd = v.findViewById(R.id.button_withdraw_amount);
        button_wd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                CharSequence input = textInputAmount.getEditText().getText().toString().trim();
                listener.onWithdraw(input, c);
                textInputAmount.getEditText().setText("");
            }
        });

        Button button_dp = v.findViewById(R.id.button_deposit_amount);
        button_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                CharSequence input = textInputAmount.getEditText().getText().toString().trim();
                listener.onDeposit(input, c);
                textInputAmount.getEditText().setText("");
            }
        });
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof WithdrawFragmentListener) {
            listener =(WithdrawFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement WithdrawFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
