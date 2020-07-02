package com.tixon.morse.fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
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
                binding.codeView.clear();
                viewCode(code, binding.codeView);
            }

            @Override
            public void onNewCode() {
            }

            @Override
            public void onPress() {
                //binding.dotProgressBar.setVisibility(View.VISIBLE);
                //binding.progressBarBackground.setVisibility(View.VISIBLE);
                animator.start();
                if(shouldVibrate) {
                    vibrator.vibrate(50);
                }
            }

            @Override
            public void onRelease() {
                animator.cancel();
                binding.dotProgressBar.setVisibility(View.INVISIBLE);
                //binding.progressBarBackground.setVisibility(View.INVISIBLE);
                binding.dashAlert.setVisibility(View.INVISIBLE);
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

    @Override
    public void onResume() {
        super.onResume();
        morse.updateSavedValues();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer degree = (Integer) animation.getAnimatedValue();
                binding.dotProgressBar.updateDegree(degree);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("myLogs", "animation ended!");
                binding.dotProgressBar.setVisibility(View.VISIBLE);
                binding.dashAlert.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
