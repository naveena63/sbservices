package com.app.sb.sbservices.Payment;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.sb.sbservices.BottomNavActivity;
import com.app.sb.sbservices.R;

public class StatusPageActivity extends AppCompatActivity {
Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_page);

        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StatusPageActivity.this,BottomNavActivity.class);

                startActivity(i);

            }
        });
    }
}
