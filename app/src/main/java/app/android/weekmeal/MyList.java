package app.android.weekmeal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MyList extends AppCompatActivity {
    private ListView list_recipe;
    private FloatingActionButton button_add, button_remove;
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;
    boolean isDeletable=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
//*************List initialization********************
        list_recipe = (ListView) findViewById(R.id.list_Recipe);
        arrayList = new ArrayList<>();
        getData("List_meal.txt"); //Name of the doc to open
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, arrayList);
        list_recipe.setAdapter(arrayAdapter);
        list_recipe.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                list_recipe.setItemChecked(i, !list_recipe.isItemChecked(i));

                if(list_recipe.getCheckedItemCount()>0)
                    button_remove.setVisibility(View.VISIBLE);
                else
                    button_remove.setVisibility(View.INVISIBLE);
                return true;
            }
        });
//*************button*********************************
        button_add=(FloatingActionButton)findViewById(R.id.button_add);
        button_remove=(FloatingActionButton)findViewById(R.id.button_remove);

        button_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(button_add.getColorFilter()==null){
                    if(button_remove.getColorFilter()!=null){
                    }
                    button_add.animate().rotationXBy(180);
                    button_add.setColorFilter(Color.GREEN);
                }else{
                    button_add.animate().rotationBy(180);
                    button_add.setColorFilter(null);
                }
            }
        });
    }
//****************Open the document, transform line into itemlist****************
    private void getData(String file){
        BufferedReader reader=null;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open(file)));
            String line;
            while ((line = reader.readLine()) != null)
                arrayList.add(line);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Erreur lecture du fichier", Toast.LENGTH_LONG).show();
            e.getMessage();
        }finally {
            if (reader != null)
            {
                try {
                    reader.close();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Erreur fermeture du fichier", Toast.LENGTH_LONG).show();
                    e.getMessage();
                }
            }

        }
    }
    /*private class ModeCallback implements ListView.MultiChoiceModeListener {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.list_select_menu, menu);
            mode.setTitle("Recettes sélectionées");
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.remove:
                    Toast.makeText(getApplicationContext(), list_recipe.getCheckedItemCount() +
                            " recette(s)" + " Supprimée(s) ", Toast.LENGTH_SHORT).show();
//****************delete checked items after clicking****************
                    SparseBooleanArray checkedItemPositions = list_recipe.getCheckedItemPositions();
                    for(int i=checkedItemPositions.size(); i >= 0; i--){
                        if(checkedItemPositions.get(i)){
                            arrayAdapter.remove(arrayList.get(i));
                        }
                    }
                    checkedItemPositions.clear();
                    arrayAdapter.notifyDataSetChanged();
                    mode.finish();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Clicked " + item.getTitle(),
                            Toast.LENGTH_SHORT).show();
                    break;
            }

            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
            //*****************need to refresh .txt with the new list**************************
            refreshListRecipe("List_meal.txt");
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

    }*/
    public void refreshListRecipe(String file){


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