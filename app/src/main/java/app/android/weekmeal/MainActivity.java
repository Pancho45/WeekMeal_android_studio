package app.android.weekmeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button_recipe, button_list, button_leave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//***********************Button leave: leave the app*******************************
        button_leave=(Button) findViewById(R.id.button_leave);
        button_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { System.exit(0);}});
//***********************Button leave: leave the app*******************************
//***********************Button list: list setting (add, remove, modify)*******************************
        button_list=(Button) findViewById(R.id.button_myList);
        button_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), MyList.class);
                startActivity(intent);
            }
        });
//***********************Button list: list setting (add, remove, modify)*******************************
//***********************Button recipe: choose recipes (random number)*******************************
        button_recipe=(Button) findViewById(R.id.button_myRecipe);
        button_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), MyRecipe.class);
                startActivity(intent);
            }
        });
//***********************Button list: list setting (add, remove, modify)*******************************
    }
}