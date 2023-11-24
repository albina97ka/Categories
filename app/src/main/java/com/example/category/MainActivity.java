package com.example.category;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.Arrays;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_view, ContentFragment.class, null)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    /**
     * refresh получает список файлов из папки и подставляет их в ListView
     */
    void refresh() {
        ListView list = findViewById(R.id.listOfTitles);
        try {
            // Если есть папка notes в /data/data, получаю оттуда список файлов. Если нет, создаю.
            File notes = new File(getFilesDir(), "notes");
            if (!notes.exists()) notes.mkdir();
            // Убираю .txt у названий файлов
            String[] titles = Arrays.stream(notes.list()).map(filename -> filename.replaceFirst("[.][^.]+$", "")).toArray(String[]::new);
            list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles));
            list.setOnItemClickListener((parent, v, position, id) -> {
                // При выборе элемента списка, вызываетсыя InfoActivity с параметром - имя выбранного файла
                startActivity(new Intent(MainActivity.this, InfoActivity.class).putExtra("name", titles[position]));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * edit(view) вызывается при нажатии на кнопку добавления новой категории.
     * Вызываю EditActivity с пустым заголовком и текстом
     */
    public void edit(View view) {
        startActivity(new Intent(MainActivity.this, EditActivity.class).putExtra("title", "").putExtra("content", ""));
    }
}