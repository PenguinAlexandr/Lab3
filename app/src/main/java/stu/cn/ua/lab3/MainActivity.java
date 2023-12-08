package stu.cn.ua.lab3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements
        MainFragment.MainFragmentListener,
        QuestionFragment.QuestionFragmentListener,
        SettingsFragment.SettingsFragmentListener {

    private AnswerService answerService;
    private boolean isBound = false;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AnswerService.AnswerBinder binder = (AnswerService.AnswerBinder) service;
            answerService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Запуск служби
        Intent intent = new Intent(this, AnswerService.class);
        startService(intent);

        // Зв'язуємося із службою
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        // Перший раз створюємо MainFragment
        if (savedInstanceState == null) {
            MainFragment mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, mainFragment)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Роз'єднуємося від служби при знищенні активності
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

    @Override
    public void onAskQuestionClicked() {
        // Заміна MainFragment на QuestionFragment
        QuestionFragment questionFragment = new QuestionFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, questionFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSettingsClicked() {
        // Заміна MainFragment на SettingsFragment
        SettingsFragment settingsFragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, settingsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onExitClicked() {
        // Вихід з додатку
        finish();
    }

    @Override
    public void onAskQuestionClicked(String userQuestion) {
        // Генерація відповіді
        generateAnswer(userQuestion);
    }

    @Override
    public void onBackToMainClicked() {
        // Повернення на головний екран
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onSaveClicked(String firstName, String lastName, String birthDate, boolean isMale, boolean isFemale) {
        // Збереження даних з SettingsFragment та повернення на головний екран
        getSupportFragmentManager().popBackStack();
    }

    private void generateAnswer(String question) {
        if (isBound) {
            String answer = answerService.generateAnswer(question);

            // Передача відповіді фрагменту AnswerFragment
            AnswerFragment answerFragment = AnswerFragment.newInstance(answer);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, answerFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
