package com.tixon.morse.fragments;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
public class MorseStudyFragment extends Fragment {
    private MorseStudyFragmentBinding binding;

    public static MorseStudyFragment newInstance() {
        MorseStudyFragment fragment = new MorseStudyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    Morse morse;

    int index = 0;
    //String[] morseAbc = getResources().getStringArray(R.array.russian_abc);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        morse = new Morse(getActivity()) {
            @Override
            public void onGenerateLetter(String letter) {
                String code = binding.getCode();
                binding.textCorrect.setVisibility(View.VISIBLE);
                //binding.setCorrect(morse.checkCorrect(code, morseAbc[index]));
            }

            @Override
            public void onGenerateCode(String code) {
                binding.setCode(code);
            }

            @Override
            public void onNewCode() {
                binding.textCorrect.setVisibility(View.INVISIBLE);
                index++;
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.morse_study_fragment, container, false);
        binding.pressurePanel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return morse.onTouch(v, event);
            }
        });
        return binding.getRoot();
    }
}
