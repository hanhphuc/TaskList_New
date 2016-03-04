package com.example.my.tasklist;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAddapter;
    ListView lvItems;
    Button btn;
    //String itemText;
   // int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItem);
        items = new ArrayList<>();
        readItems();
        itemsAddapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAddapter);
        setupListViewListener();
        btn = (Button)findViewById(R.id.btnAddItem);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
                String itemText = etNewItem.getText().toString();
                itemsAddapter.add(itemText);
                etNewItem.setText("");
                writeItems();

            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showInputBox(items.get(position), position);
            }
        });

    }
    public void showInputBox(String oldItem, final int id){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.input_box);
        TextView txtTask = (TextView)dialog.findViewById(R.id.txtTask);
        txtTask.setText("Edit a new item below");
        txtTask.setTextColor(Color.parseColor("#ff2222"));
        final EditText editText = (EditText)dialog.findViewById(R.id.txtInput);
        editText.setText(oldItem);
        Button btn = (Button)dialog.findViewById(R.id.btndone);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.set(id,editText.getText().toString());
                itemsAddapter.notifyDataSetChanged();
                writeItems();
                dialog.dismiss();


            }
        });
        dialog.show();
    }


    private  void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAddapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
       try {
           items = new ArrayList<String>(FileUtils.readLines(todoFile));
       }catch (IOException e){
           items  = new ArrayList<String>();
       }
    }
    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile,items);
        }catch (IOException e){
           e.printStackTrace();
        }
    }
}
