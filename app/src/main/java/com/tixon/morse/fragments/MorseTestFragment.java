package com.tixon.morse.fragments;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tixon.morse.Morse;
import com.tixon.morse.R;
import com.tixon.morse.databinding.MorseTestFragmentBinding;

/**
 * Created by tikhon.osipov on 06.04.2016.
 */
public class MorseTestFragment extends Fragment {

    MorseTestFragmentBinding binding;

    public static MorseTestFragment newInstance() {
        MorseTestFragment fragment = new MorseTestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    Morse morse;

    int index = 0;
    String[] morseAbc;

    Handler handler = new Handler();
    Runnable nextLetterRunnable;

    View.OnTouchListener onTouchPressurePanel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        morseAbc = getActivity().getResources().getStringArray(R.array.russian_abc);

        onTouchPressurePanel = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("myLogs", "touch");
                return morse.onTouch(v, event);
            }
        };

        nextLetterRunnable = new Runnable() {
            @Override
            public void run() {
                binding.textCorrect.setVisibility(View.INVISIBLE);
                if(index < morseAbc.length) {
                    index++;
                    binding.setProgress(String.valueOf(index + 1) + " / " + String.valueOf(morseAbc.length));
                    binding.setLetter(morseAbc[index]);
                    binding.setCode("");
                    binding.pressurePanel.setOnTouchListener(onTouchPressurePanel);
                    binding.pressurePanel.setClickable(true);
                }
            }
        };
        morse = new Morse(getActivity()) {
            @Override
            public void onGenerateLetter(String letter) {
                String code = binding.getCode();
                binding.setCorrect(morse.checkCorrect(code, morseAbc[index]));
                binding.textCorrect.setVisibility(View.VISIBLE);
                binding.pressurePanel.setOnTouchListener(null);
                binding.pressurePanel.setClickable(false);
                //через 3 секунды переключить на новую букву, очистить код и убрать correct
                handler.postDelayed(nextLetterRunnable, 3000);
            }

            @Override
            public void onGenerateCode(String code) {
                binding.setCode(code);
            }

            @Override
            public void onNewCode() {
                /*binding.textCorrect.setVisibility(View.INVISIBLE);
                if(index < morseAbc.length) {
                    index++;
                    binding.setProgress(String.valueOf(index + 1) + " / " + String.valueOf(morseAbc.length));
                    binding.setLetter(morseAbc[index]);
                }*/
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.morse_test_fragment, container, false);
        binding.textCorrect.setVisibility(View.INVISIBLE);
        binding.setLetter(morseAbc[index]);
        binding.setProgress(String.valueOf(index + 1) + " / " + String.valueOf(morseAbc.length));

        binding.pressurePanel.setOnTouchListener(onTouchPressurePanel);
        binding.pressurePanel.setClickable(true);
        return binding.getRoot();
    }
}
