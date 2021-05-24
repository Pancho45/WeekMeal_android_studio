package app.android.weekmeal;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MyList extends AppCompatActivity {
    ArrayList<String> arrayList;
    listviewAdapter arrayAdapter;
    private ListView list_recipe;

    ArrayList <String> temp_array=null;

    FloatingActionButton button_add;
    Button button_refresh;
    EditText text_add;
    SearchView searchView;

    private boolean isRemoving=false;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        list_recipe = findViewById(R.id.list_Recipe);
        arrayList = new ArrayList<>();
        getData("List_meal.txt"); //Name of the doc to open
        arrayAdapter = new listviewAdapter(this, R.layout.listviewlayout, arrayList);
        list_recipe.setAdapter(arrayAdapter);
        list_recipe.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);//allow multiple selection
        list_recipe.setMultiChoiceModeListener(new ModeCallback());

        text_add= findViewById(R.id.text_add);
        button_add= findViewById(R.id.button_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                if(button_add.getColorFilter()==null){
                    button_add.animate().rotationBy(180);
                    button_add.setColorFilter(Color.WHITE);
                    Animation animation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anime_fade_in);
                    text_add.startAnimation(animation);
                    text_add.setVisibility(View.VISIBLE);
                    text_add.requestFocus();
                    imm.showSoftInput(text_add, InputMethodManager.SHOW_IMPLICIT);
                }else{
                    if(!text_add.getText().toString().isEmpty()){
                        arrayList.add(text_add.getText().toString());
                        arrayAdapter.notifyDataSetChanged();
                        text_add.setText("");
                    }
                    Animation animation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anime_fade_out);
                    text_add.startAnimation(animation);
                    text_add.setVisibility(View.INVISIBLE);
                    button_add.animate().rotationBy(180);
                    button_add.setColorFilter(null);
                }
            }
        });
        searchView= findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                arrayAdapter.getFilter().filter(s);
                list_recipe.setAdapter(arrayAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                arrayAdapter.getFilter().filter(s);
                return false;
            }
        });
        button_refresh= findViewById(R.id.button_refresh);
        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.onFinish();
                countDownTimer.cancel();
                arrayList.addAll(temp_array);
                arrayAdapter.notifyDataSetChanged();
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append("Modification(s) annulée(s) :\n");
                for (String deleted:temp_array)
                {
                    stringBuilder.append(deleted);
                     stringBuilder.append("\n");
                }
                Toast.makeText(getApplicationContext(),stringBuilder.toString(),Toast.LENGTH_LONG).show();
                temp_array.clear();
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
            switch (item.getItemId()){
                case R.id.remove:
                    isRemoving=true;
                    SparseBooleanArray selectedItems=arrayAdapter.getSelectedIds();
                    temp_array=new ArrayList<>(selectedItems.size());
                    for (int i=selectedItems.size()-1; i>=0; i--){
                        if(selectedItems.valueAt(i)){
                            String items=arrayAdapter.getItem(selectedItems.keyAt(i));
                            temp_array.add(items);
                            arrayAdapter.remove(items);
                        }
                    }
                    selectedItems.clear();
                    mode.finish();
                    return true;
                case R.id.selectAll:
                    int count =list_recipe.getCount();
                    arrayAdapter.removeSelection();
                    for(int i=0; i<count; i++)
                        list_recipe.setItemChecked(i, true);
                    return true;
                default:
                    return false;
            }
        }

        public void onDestroyActionMode(ActionMode mode) {
            button_add.setVisibility(View.VISIBLE);
            if (isRemoving) {
                isRemoving=false;
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_in);
                button_refresh.startAnimation(animation);
                button_refresh.setVisibility(View.VISIBLE);
                button_refresh.setClickable(true);
                countDownTimer=new CountDownTimer(4000, 1000) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_out);
                        button_refresh.startAnimation(animation);
                        button_refresh.setVisibility(View.INVISIBLE);
                        button_refresh.setClickable(false);
                    }
                }.start();
            }
        }

        //****************count checked item number****************
        public void onItemCheckedStateChanged(ActionMode mode,
                                              int position, long id, boolean checked) {
            final int checkedCount = list_recipe.getCheckedItemCount();

            mode.setTitle(checkedCount + " sélectionnée(s)");
            arrayAdapter.toggleSelection(position);
        }

    }

    @Override
    public void onBackPressed() {
        sendData("List_meal.txt");
        super.onBackPressed();
    }
    public void sendData(String file){
       OutputStreamWriter outputStreamWriter =null;
       try{
            File outFile = new File(getExternalFilesDir(null), file);
            outputStreamWriter=new OutputStreamWriter(new FileOutputStream(outFile));
            for(int i=0; i<arrayList.size(); i++){
                outputStreamWriter.write(arrayList.get(i));
                outputStreamWriter.write("\n");
            }

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), R.string.error_refresh, Toast.LENGTH_LONG).show();
        }
        finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                    Toast.makeText(getApplicationContext(), R.string.file_saved, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), R.string.error_closing, Toast.LENGTH_LONG).show();
                }
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