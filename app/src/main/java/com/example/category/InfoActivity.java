package com.example.category;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class InfoActivity extends AppCompatActivity {
    StringBuilder data;
    String title;
    private ActivityResultLauncher<Intent> activityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new StringBuilder();
        // Получаю имя файла из intent'a, устанавливаю его как название
        title = getIntent().getStringExtra("name");
        setContentView(R.layout.activity_info);
        ((TextView)findViewById(R.id.title)).setText(title);
        activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // После будущего запуска EditActivity, нужно обновить поля, значения которых будут переданы в результат.
                        ((TextView)findViewById(R.id.title)).setText(result.getData().getStringExtra("title"));
                        ((TextView)findViewById(R.id.descripton)).setText(result.getData().getStringExtra("content"));
                    };
                }
        );
        // Считываю содержимое файла, устанавливаю его как текст заметки.
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(getFilesDir(), "notes/"+title+".txt"))))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                data.append(line).append("\n");
            }
            ((TextView)findViewById(R.id.descripton)).setText(data.toString());
        } catch (IOException e) {
            ((TextView)findViewById(R.id.descripton)).setText("Не удалось прочитать информацию из файла");
        }

    }

    // При нажатии на кнопку редактирования, вызываю EditActivity и передаю туда название и текст заметки
    public void edit(View view) {
        activityLauncher.launch(new Intent(InfoActivity.this, EditActivity.class).putExtra("content", data.toString()).putExtra("title", title));
    }
}