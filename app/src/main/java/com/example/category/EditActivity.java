package com.example.category;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        // Получаю из intent'a название и текст категории, устанавливаю
        ((TextView) findViewById(R.id.title)).setText(getIntent().getStringExtra("title").toString());
        ((TextView) findViewById(R.id.content)).setText(getIntent().getStringExtra("content").toString());
    }

    // Edit вызывается при нажатии на кнопку сохранения
    public void edit(View view) {
        // Если название изменилось, удаляю старый файл и создаю новый с новым названием
        String oldTitle = getIntent().getStringExtra("title").toString();
        String newTitle = ((TextView) findViewById(R.id.title)).getText().toString();
        if (!oldTitle.equals(newTitle)) {
            new File(getFilesDir(), "notes/" + oldTitle + ".txt").delete();
        }
        // Записываю в файл новый текст заметки
        try {
            File newNote = new File(getFilesDir(), "notes/"+newTitle+".txt");
            if (!newNote.exists()) {
                Log.e("IOException", String.valueOf(newNote.createNewFile()));
            }
            FileWriter w = new FileWriter(newNote);
            w.write(((TextView) findViewById(R.id.content)).getText().toString());
            w.close();
        } catch (IOException e) {
            Log.e("IOException", Arrays.toString(e.getStackTrace()));
        }
        // Перед возвратом на InfoActivity, передаю в Intent новый заголовок и текст заметки.
        setResult(Activity.RESULT_OK, new Intent().putExtra("title", newTitle).putExtra("content", ((TextView) findViewById(R.id.content)).getText().toString()));
        finish();
    }
}