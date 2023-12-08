package stu.cn.ua.lab3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class AnswerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer, container, false);

        // Отримання відповіді з аргументів фрагмента
        String answer = getArguments().getString("answer", "");

        TextView answerTextView = view.findViewById(R.id.answerTextView);
        answerTextView.setText(answer);

        // Додаємо обробник для кнопки "повернутися на головну"
        view.findViewById(R.id.backToMainButton).setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    // Статичний метод для створення екземпляра AnswerFragment з параметром answer
    public static AnswerFragment newInstance(String answer) {
        AnswerFragment fragment = new AnswerFragment();
        Bundle args = new Bundle();
        args.putString("answer", answer);
        fragment.setArguments(args);
        return fragment;
    }
}
