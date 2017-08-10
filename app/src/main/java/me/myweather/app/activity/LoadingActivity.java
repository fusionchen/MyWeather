package me.myweather.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.victor.loading.book.BookLoading;

import me.myweather.app.R;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        BookLoading bookLoading = (BookLoading) findViewById(R.id.bookloading);
        Button button = (Button) findViewById(R.id.do_loading);
        button.setOnClickListener(view -> {
            if(bookLoading.isStart())
                bookLoading.stop();
            else
                bookLoading.start();
        });
    }
}
