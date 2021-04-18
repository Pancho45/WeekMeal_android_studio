package app.android.weekmeal;

import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class MyList extends AppCompatActivity {
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;
    private ListView list_recipe;

    FloatingActionButton button_add, button_refresh;
    EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        list_recipe = (ListView) findViewById(R.id.list_Recipe);
        arrayList = new ArrayList<>();
        getData("List_meal.txt"); //Name of the doc to open
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, arrayList);
        list_recipe.setAdapter(arrayAdapter);
        list_recipe.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);//allow multiple selection
        list_recipe.setMultiChoiceModeListener(new ModeCallback());

        text=(EditText)findViewById(R.id.text_add);
        button_add=(FloatingActionButton) findViewById(R.id.button_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(button_add.getColorFilter()==null){
                    button_add.animate().rotationBy(180);
                    button_add.setColorFilter(Color.WHITE);
                    Animation animation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anime_fade_in);
                    text.startAnimation(animation);
                    text.setVisibility(View.VISIBLE);
                }else{
                    if(!text.getText().toString().isEmpty()){
                        arrayList.add(text.getText().toString());
                        arrayAdapter.notifyDataSetChanged();
                        button_refresh.setVisibility(View.VISIBLE);
                        text.setText("");
                    }
                    Animation animation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anime_fade_out);
                    text.startAnimation(animation);
                    text.setVisibility(View.INVISIBLE);
                    button_add.animate().rotationBy(180);
                    button_add.setColorFilter(null);
                }
            }
        });
        button_refresh=(FloatingActionButton)findViewById(R.id.button_refresh);
        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
                Toast.makeText(getApplicationContext(),"Modification(s) annulée(s) !!",Toast.LENGTH_LONG).show();
            }
        });
    }

    //****************Open the document, transform line into itemlist****************
    private void getData(String file) {
        BufferedReader reader = null;
        File outFile = new File(getExternalFilesDir(null), file);
        FileReader fileReader;
        try {
            fileReader=new FileReader(outFile);
            reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null)
                arrayList.add(line);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error reading file!", Toast.LENGTH_LONG).show();
            e.getMessage();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error closing file!", Toast.LENGTH_LONG).show();
                    e.getMessage();
                }
            }

        }
    }

    public void refreshListRecipe() {

    }

    private class ModeCallback implements ListView.MultiChoiceModeListener {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.list_select_menu, menu);
            mode.setTitle("Recettes sélectionées");
            button_add.setVisibility(View.INVISIBLE);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.remove) {
                Toast.makeText(getApplicationContext(), list_recipe.getCheckedItemCount() +
                        " recette(s)" + " Supprimée(s) ", Toast.LENGTH_SHORT).show();
//****************delete checked items after clicking****************
                SparseBooleanArray checkedItemPositions = list_recipe.getCheckedItemPositions();
                for (int i = checkedItemPositions.size(); i >= 0; i--) {
                    if (checkedItemPositions.get(i)) {
                        arrayAdapter.remove(arrayList.get(i));
                    }
                }
                checkedItemPositions.clear();
                arrayAdapter.notifyDataSetChanged();
                mode.finish();
                button_refresh.setVisibility(View.VISIBLE);
            }
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
            button_add.setVisibility(View.VISIBLE);
        }

        //****************count checked item number****************
        public void onItemCheckedStateChanged(ActionMode mode,
                                              int position, long id, boolean checked) {
            final int checkedCount = list_recipe.getCheckedItemCount();

            switch (checkedCount) {
                case 0:
                    mode.setSubtitle(null);
                    break;
                case 1:
                    mode.setSubtitle("Une recette sélectionnée");
                    break;
                default:
                    mode.setSubtitle("" + checkedCount + " recettes sélectionnées");
                    break;
            }
        }

    }

}
/* SparseBooleanArray checkedItemPositions = list_recipe.getCheckedItemPositions();

                    for(int i=checkedItemPositions.size(); i >= 0; i--){
                        if(checkedItemPositions.get(i)){
                            arrayAdapter.remove(arrayList.get(i));
                        }
                    }
                    checkedItemPositions.clear();
                    arrayAdapter.notifyDataSetChanged();

 */