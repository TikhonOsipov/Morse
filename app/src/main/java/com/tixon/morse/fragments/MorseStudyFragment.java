package com.tixon.morse.fragments;

import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tixon.morse.Morse;
import com.tixon.morse.R;
import com.tixon.morse.databinding.MorseStudyFragmentBinding;

/**
 * Created by tikhon.osipov on 06.04.2016.
 */
public class MorseStudyFragment extends BaseFragment {
    private MorseStudyFragmentBinding binding;

    public static MorseStudyFragment newInstance() {
        MorseStudyFragment fragment = new MorseStudyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    View.OnTouchListener onTouchPressurePanel;

    Morse morse;
    Handler handler = new Handler();
    Runnable nextLetterRunnable, repeateLetterRunnable;

    //String[] morseAbc = getResources().getStringArray(R.array.russian_abc);
    int index = 0;

    String[] morseAbc, morseCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        morseAbc = getActivity().getResources().getStringArray(R.array.russian_abc);
        morseCode = getActivity().getResources().getStringArray(R.array.russian_morse);

        onTouchPressurePanel = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("myLogs", "touch: " + event.getAction());
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
                    binding.setCode("");
                    binding.setCodePreview(morseCode[index]);
                    binding.pressurePanel.setOnTouchListener(onTouchPressurePanel);
                    binding.pressurePanel.setClickable(true);
                }
            }
        };

        repeateLetterRunnable = new Runnable() {
            @Override
            public void run() {
                binding.textCorrect.setVisibility(View.INVISIBLE);
                binding.setCode("");
                binding.pressurePanel.setOnTouchListener(onTouchPressurePanel);
                binding.pressurePanel.setClickable(true);
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
                if(!binding.getCorrect()) {
                    handler.postDelayed(repeateLetterRunnable, 500);
                }
                else {
                    handler.postDelayed(nextLetterRunnable, 2000);
                }
            }

            @Override
            public void onGenerateCode(String code) {
                binding.setCode(code);
            }

            @Override
            public void onNewCode() {
                binding.textCorrect.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPress() {
                if(shouldVibrate) {
                    vibrator.vibrate(50);
                }
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.morse_study_fragment, container, false);
        binding.textCorrect.setVisibility(View.INVISIBLE);
        binding.setLetter(morseAbc[index]);
        binding.setCodePreview(morseCode[index]);
        binding.setProgress(String.valueOf(index + 1) + " / " + String.valueOf(morseAbc.length));

        binding.pressurePanel.setOnTouchListener(onTouchPressurePanel);
        binding.pressurePanel.setClickable(true);
        return binding.getRoot();
    }
}
