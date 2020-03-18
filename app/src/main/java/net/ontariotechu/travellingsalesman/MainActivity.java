package net.ontariotechu.travellingsalesman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button subBtn = (Button) findViewById(R.id.subBtn);

        //when user clicks submit, get the number they entered and send it to GameActivity
        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nodes = (EditText) findViewById(R.id.numNodes);

                int numNodes = Integer.parseInt(nodes.getText().toString());

                Intent startIntent = new Intent(getApplicationContext(), GameActivity.class);
                startIntent.putExtra("nodes", numNodes);
                startActivity(startIntent);
            }
        });

    }
}
