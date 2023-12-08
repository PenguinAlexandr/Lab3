package stu.cn.ua.lab3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class QuestionFragment extends Fragment {

    public interface QuestionFragmentListener {
        void onAskQuestionClicked(String userQuestion);
        void onBackToMainClicked();
    }

    private QuestionFragmentListener listener;

    private EditText questionEditText;
    private TextView answerTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Перевірка чи активність реалізує інтерфейс QuestionFragmentListener
        if (getActivity() instanceof QuestionFragmentListener) {
            listener = (QuestionFragmentListener) getActivity();
        } else {
            throw new RuntimeException(getActivity().toString()
                    + " must implement QuestionFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        questionEditText = view.findViewById(R.id.questionEditText);
        Button askButton = view.findViewById(R.id.askButton);
        answerTextView = view.findViewById(R.id.answerTextView);
        Button backToMainButton = view.findViewById(R.id.backToMainButton);

        askButton.setOnClickListener(v -> {
            String userQuestion = questionEditText.getText().toString();
            listener.onAskQuestionClicked(userQuestion);
        });

        backToMainButton.setOnClickListener(v -> listener.onBackToMainClicked());

        return view;
    }
}
