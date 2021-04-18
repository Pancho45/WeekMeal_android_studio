package app.android.weekmeal;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MyList extends AppCompatActivity {
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;
    private ListView list_recipe;

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
//*****************need to refresh .txt with the new list**************************
                refreshListRecipe();
                mode.finish();
            } else {
                Toast.makeText(getApplicationContext(), "Clicked " + item.getTitle(),
                        Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
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