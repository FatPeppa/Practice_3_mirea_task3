package com.example.practice_3_mirea_task3;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.practice_3_mirea_task3.FirstFragment.isNumeric;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class SecondFragment extends Fragment {
    public SecondFragment() {
        super(R.layout.fragment_second);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText input_the_amount_edit_text = (EditText) view.findViewById(R.id.input_the_amount_edit_text);
        TextView chosen_good = (TextView) view.findViewById(R.id.chosen_good);
        Button choose_the_amount_button = (Button) view.findViewById(R.id.choose_the_amount_button);

        ArrayList<String> inputs = null;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            inputs = bundle.getStringArrayList("bundleKey");
        }

        if (inputs != null && checkInputs(inputs)) {
            String good_name = parseInputsGoods(inputs);

            chosen_good.setText(good_name);
        } else {
            Toast.makeText(getActivity(), "An intent error occurred", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Intent error occurred!!!");
        }

        choose_the_amount_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input_amount = input_the_amount_edit_text.getText().toString();
                if (input_amount.length() != 0 && isNumeric(input_amount)) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(input_amount);
                    arrayList.add(chosen_good.getText().toString());

                    Bundle result = new Bundle();
                    result.putStringArrayList("goods", (ArrayList<String>) arrayList);
                    getParentFragmentManager().setFragmentResult("requestKey", result);

                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                } else {
                    input_the_amount_edit_text.setText("");
                    input_the_amount_edit_text.setHint(R.string.str_input_the_amount_hint);
                    input_the_amount_edit_text.setHintTextColor(view.getContext().getColor(R.color.red));
                }
            }
        });

        Toast.makeText(getActivity(), "onResume", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onViewCreated");
    }

    private String parseInputsGoods(ArrayList<String> array) {
        return array.get(0);
    }

    private boolean checkInputs(ArrayList<String> array) {
        if (array.size() != 1) return false;

        String g_n = array.get(0);

        if (isNumeric(g_n) || g_n.length() == 0) return false;
        return true;
    }
}
