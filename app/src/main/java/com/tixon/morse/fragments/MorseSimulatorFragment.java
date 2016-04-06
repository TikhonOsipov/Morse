package com.tixon.morse.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tixon.morse.Morse;
import com.tixon.morse.R;
import com.tixon.morse.databinding.MorseSimulatorFragmentBinding;

/**
 * Created by tikhon.osipov on 06.04.2016.
 */
public class MorseSimulatorFragment extends BaseFragment {
    MorseSimulatorFragmentBinding binding;

    Morse morse;

    public static MorseSimulatorFragment newInstance() {
        MorseSimulatorFragment fragment = new MorseSimulatorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        morse = new Morse(getActivity()) {
            @Override
            public void onGenerateLetter(String letter) {
                binding.textLetter.setText(letter);
            }

            @Override
            public void onGenerateCode(String code) {
                binding.textCode.setText(code);
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
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.morse_simulator_fragment, container, false);
        binding.pressurePanel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return morse.onTouch(v, event);
            }
        });
        return binding.getRoot();
    }
}
