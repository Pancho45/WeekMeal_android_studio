package app.android.weekmeal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class MyRecipe extends AppCompatActivity {

    private Button button_picker;
    private NumberPicker numberPicker;

    private ListView listView;
    private listviewAdapter listviewAdapter;
    private ArrayList<String> list;
    private ArrayList<String> list_temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);
        numberPicker= findViewById(R.id.number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(14);
        listView= findViewById(R.id.listview_picker);

        button_picker= findViewById(R.id.button_picker);
        button_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list = new ArrayList<>();
                list_temp = new ArrayList<>();
                getData("List_meal.txt"); //Name of the doc to open
                Random randomizer = new Random();
                for(int i =0; i<numberPicker.getValue(); i++)
                    list.add(list_temp.get(randomizer.nextInt(list_temp.size())));
                listviewAdapter = new listviewAdapter(getApplicationContext(), R.layout.listviewlayout, list);
                listView.setAdapter(listviewAdapter);
            }
        });
    }
    private void getData(String file) {
        BufferedReader reader = null;
        File outFile = new File(getExternalFilesDir(null), file);
        FileReader fileReader;
        try {
            fileReader=new FileReader(outFile);
            reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null)
                list_temp.add(line);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.error_reading, Toast.LENGTH_LONG).show();
            e.getMessage();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.error_closing, Toast.LENGTH_LONG).show();
                    e.getMessage();
                }
            }

        }
    }
}