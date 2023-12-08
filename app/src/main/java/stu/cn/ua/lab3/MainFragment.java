package stu.cn.ua.lab3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {

    public interface MainFragmentListener {
        void onAskQuestionClicked();
        void onSettingsClicked();
        void onExitClicked();
    }

    private MainFragmentListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Перевірка чи активність реалізує інтерфейс MainFragmentListener
        if (getActivity() instanceof MainFragmentListener) {
            listener = (MainFragmentListener) getActivity();
        } else {
            throw new RuntimeException(getActivity().toString()
                    + " must implement MainFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button askQuestionButton = view.findViewById(R.id.askQuestionButton);
        Button settingsButton = view.findViewById(R.id.settingsButton);
        Button exitButton = view.findViewById(R.id.exitButton);

        askQuestionButton.setOnClickListener(v -> listener.onAskQuestionClicked());
        settingsButton.setOnClickListener(v -> listener.onSettingsClicked());
        exitButton.setOnClickListener(v -> listener.onExitClicked());

        return view;
    }
}
