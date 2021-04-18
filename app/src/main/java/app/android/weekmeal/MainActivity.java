package app.android.weekmeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    int CODE_REQUEST_READ_PERMISSION =12, CODE_REQUEST_WRITE_PERMISSION =11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//***********************Button leave: leave the app*******************************
        Button button_leave = (Button) findViewById(R.id.button_leave);
        button_leave.setOnClickListener(view -> System.exit(0));
//***********************Button leave: leave the app*******************************
//***********************Button list: list setting (add, remove, modify)*******************************
        Button button_list = (Button) findViewById(R.id.button_myList);
        button_list.setOnClickListener(view -> {
            Intent intent= new Intent(getApplicationContext(), MyList.class);
            startActivity(intent);
        });
//***********************Button list: list setting (add, remove, modify)*******************************
//***********************Button recipe: choose recipes (random number)*******************************
        Button button_recipe = (Button) findViewById(R.id.button_myRecipe);
        button_recipe.setOnClickListener(view -> {
            Intent intent= new Intent(getApplicationContext(), MyRecipe.class);
            startActivity(intent);
        });
//***********************Button list: list setting (add, remove, modify)*******************************
    }

    @Override
    protected void onStart() {
        super.onStart();
        storagePermission();
        copyAssets("List_meal.txt");
    }
    void storagePermission(){
//******************Grant write permission**********************
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(writeExternalStoragePermission!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_REQUEST_WRITE_PERMISSION);
//******************Grant read permission**********************
        int readExternalStoragePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(readExternalStoragePermission!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_REQUEST_READ_PERMISSION);
    }

    /*
    ///copy designed file in asset folder to private app external storage
     */
    private void copyAssets(String file) {
        AssetManager assetManager = getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(file);
            File outFile = new File(getExternalFilesDir(null), file);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
        } catch(IOException e) {
            Toast.makeText(getApplicationContext(), "Error copying file!", Toast.LENGTH_LONG).show();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Error closing asset file!", Toast.LENGTH_LONG).show();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Error closing external file!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

}