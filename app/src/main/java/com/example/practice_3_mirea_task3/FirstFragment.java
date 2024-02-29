package com.example.practice_3_mirea_task3;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import java.util.ArrayList;

enum GoodParameterType {
    GOOD_AMOUNT,
    GOOD_NAME
}

public class FirstFragment extends Fragment {
    TextView order_congratulations_warning_view;
    TextView order_list;
    EditText customer_introduction_input_text;
    Button choose_the_amount_button;
    Button clean_button;

    public FirstFragment() {
        super(R.layout.fragment_first);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("requestKey",
                this, new FragmentResultListener() {
        @Override
        public void onFragmentResult(@NonNull String requestKey,
                @NonNull Bundle bundle) {

                ArrayList<String> inputs = bundle.getStringArrayList("goods");

                if (inputs != null && checkInputs(inputs)){
                    String goods_amount = parseInputsGoods(inputs, GoodParameterType.GOOD_AMOUNT);
                    String good_name = parseInputsGoods(inputs, GoodParameterType.GOOD_NAME);

                    order_list.setText(goods_amount);
                    customer_introduction_input_text.setText(good_name);
                } else {
                    Toast.makeText(getActivity(), "A fragment data transition error occurred",
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "A fragment data transition error occurred!!!");
                }
            }

        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        order_congratulations_warning_view = (TextView) view.findViewById(R.id.order_congratulations_warning);
        order_list = (TextView) view.findViewById(R.id.order_list);
        customer_introduction_input_text = (EditText) view.findViewById(R.id.customer_introduction_input);
        choose_the_amount_button = (Button) view.findViewById(R.id.choose_the_amount_button);
        clean_button = (Button) view.findViewById(R.id.clean_button);

        clean_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_list.setVisibility(View.INVISIBLE);

                customer_introduction_input_text.setText("");
                customer_introduction_input_text.setHint(R.string.str_customer_introduction_hint);
            }
        });

        choose_the_amount_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customer_introduction_input_text.getText().toString().length() > 0
                        && !isNumeric(customer_introduction_input_text.getText().toString())) {


                    Bundle bundle = new Bundle();
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(customer_introduction_input_text.getText().toString());
                    bundle.putStringArrayList("bundleKey", arrayList);

                    //Fragment childFragment = new SecondFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.fragment_container_view1, SecondFragment.class, bundle, "secondFragment")
                            .addToBackStack(null)
                            .commit();
                } else {
                    customer_introduction_input_text.setText("");
                    customer_introduction_input_text.setHint(R.string.str_customer_introduction_hint);
                    customer_introduction_input_text.setHintTextColor(view.getContext().getColor(R.color.red));
                }
            }
        });

        Toast.makeText(getActivity(), "onViewCreated", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onViewCreated");
    }

    private String parseInputsGoods(ArrayList<String> array, GoodParameterType type) {
        switch (type) {
            case GOOD_AMOUNT:
                return array.get(0);
            case GOOD_NAME:
                return array.get(1);
            default:
                return null;
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private boolean checkInputs(ArrayList<String> array) {
        if (array.size() != 2) return false;

        String g_a = array.get(0);
        String g_n = array.get(1);

        return isNumeric(g_a) && !isNumeric(g_n) && g_n.length() != 0 && g_a.length() != 0;
    }
}


