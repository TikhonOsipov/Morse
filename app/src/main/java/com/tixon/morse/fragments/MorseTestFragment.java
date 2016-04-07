package com.tixon.morse.fragments;

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
public class MorseTestFragment extends BaseFragment {

    MorseTestFragmentBinding binding;

    public static MorseTestFragment newInstance() {
        MorseTestFragment fragment = new MorseTestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    Morse morse;

    int index = 0;
    String inputCode = "";
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
                if(index < morseAbc.length - 1) {
                    index++;
                    binding.setProgress(String.valueOf(index + 1) + " / " + String.valueOf(morseAbc.length));
                    binding.setLetter(morseAbc[index]);
                    inputCode = "";
                    binding.codeView.clear();
                    binding.pressurePanel.setOnTouchListener(onTouchPressurePanel);
                    binding.pressurePanel.setClickable(true);
                }
            }
        };

        morse = new Morse(getActivity()) {
            @Override
            public void onGenerateLetter(String letter) {
                binding.setCorrect(morse.checkCorrect(inputCode, morseAbc[index]));
                binding.textCorrect.setVisibility(View.VISIBLE);
                binding.pressurePanel.setOnTouchListener(null);
                binding.pressurePanel.setClickable(false);
                //через 2 секунды переключить на новую букву, очистить код и убрать correct
                handler.postDelayed(nextLetterRunnable, 2000);
            }

            @Override
            public void onGenerateCode(String code) {
                inputCode = code;
                binding.codeView.clear();
                viewCode(code, binding.codeView);
            }

            @Override
            public void onNewCode() {

            }

            @Override
            public void onPress() {
                if(shouldVibrate) {
                    vibrator.vibrate(50);
                }
            }

            @Override
            public void onRelease() {

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

    @Override
    public void onResume() {
        super.onResume();
        morse.updateSavedValues();
    }
}
