package stu.cn.ua.lab3;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Locale;

public class SettingsFragment extends Fragment {

    public interface SettingsFragmentListener {
        void onSaveClicked(String firstName, String lastName, String birthDate, boolean isMale, boolean isFemale);
        void onBackToMainClicked();
    }

    private SettingsFragmentListener listener;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private TextView birthDateTextView;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;

    private SharedPreferences sharedPreferences;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Перевірка чи активність реалізує інтерфейс SettingsFragmentListener
        if (getActivity() instanceof SettingsFragmentListener) {
            listener = (SettingsFragmentListener) getActivity();
        } else {
            throw new RuntimeException(getActivity().toString()
                    + " must implement SettingsFragmentListener");
        }

        // Ініціалізація SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("user_settings", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        firstNameEditText = view.findViewById(R.id.firstNameEditText);
        lastNameEditText = view.findViewById(R.id.lastNameEditText);
        birthDateTextView = view.findViewById(R.id.birthDateTextView);
        maleRadioButton = view.findViewById(R.id.maleRadioButton);
        femaleRadioButton = view.findViewById(R.id.femaleRadioButton);
        Button saveButton = view.findViewById(R.id.saveButton);
        Button backToMainButton = view.findViewById(R.id.backToMainButton);
        Button selectDateButton = view.findViewById(R.id.selectDateButton);

        // Відновлення збережених даних користувача
        firstNameEditText.setText(sharedPreferences.getString("first_name", ""));
        lastNameEditText.setText(sharedPreferences.getString("last_name", ""));
        setBirthDate(sharedPreferences.getString("birth_date", ""));
        maleRadioButton.setChecked(sharedPreferences.getBoolean("is_male", false));
        femaleRadioButton.setChecked(sharedPreferences.getBoolean("is_female", false));

        // Логіка вибору дати
        selectDateButton.setOnClickListener(v -> showDatePickerDialog());

        // Логіка для правильного вибору статі
                maleRadioButton.setOnClickListener(v -> {
                    maleRadioButton.setChecked(true);
                    femaleRadioButton.setChecked(false);
                });

                femaleRadioButton.setOnClickListener(v -> {
                    femaleRadioButton.setChecked(true);
                    maleRadioButton.setChecked(false);
                });


        saveButton.setOnClickListener(v -> {
            // Отримання і збереження даних користувача
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String birthDate = birthDateTextView.getText().toString();
            boolean isMale = maleRadioButton.isChecked();
            boolean isFemale = femaleRadioButton.isChecked();

            // Збереження даних в SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("first_name", firstName);
            editor.putString("last_name", lastName);
            editor.putString("birth_date", birthDate);
            editor.putBoolean("is_male", isMale);
            editor.putBoolean("is_female", isFemale);
            editor.apply();

            listener.onSaveClicked(firstName, lastName, birthDate, isMale, isFemale);
        });

        backToMainButton.setOnClickListener(v -> listener.onBackToMainClicked());

        return view;
    }


    private void setBirthDate(String date) {
        birthDateTextView.setText(date);
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireActivity(),
                (view, selectedYear, monthOfYear, dayOfMonth) -> {
                    calendar.set(selectedYear, monthOfYear, dayOfMonth);
                    setBirthDate(formatDate(selectedYear, monthOfYear, dayOfMonth));
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private String formatDate(int year, int month, int day) {
        return String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, day);
    }
}