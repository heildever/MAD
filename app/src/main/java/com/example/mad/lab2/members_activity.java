package com.example.mad.lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

public class members_activity extends AppCompatActivity {

    android.widget.ListView ListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //The items_class are set in the list view progressively, not all of them at the same time


        Bundle bundle = getIntent().getExtras();
        String group_name=bundle.getString("group_name");
        Toast.makeText(getApplicationContext(),group_name, Toast.LENGTH_SHORT).show();


        //Array of type members_class
        //In this case we cant use ArrayAdapter, we must create our own adapter
        members_class data_members[] = new members_class[]{
                new members_class(R.drawable.man,      "Pepe",   "1".concat(" EUR")),
                new members_class(R.drawable.woman,      "Maria",   "5".concat(" EUR")),
                new members_class(R.drawable.woman,      "Juan",   "3".concat(" EUR")),
        };

        //Creating the adapter
        members_adapter adapter = new members_adapter(this, R.layout.listview_groups_row, data_members);
        ListView = (android.widget.ListView) findViewById(R.id.lista1);

        View header = (View) getLayoutInflater().inflate(R.layout.list_header_row,null);

        //Add the header to the list
        ListView.addHeaderView(header);
        ListView.setAdapter(adapter);

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView v = (TextView)view.findViewById(R.id.text1);
                Toast.makeText(getApplicationContext(),v.getText(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items_class to the action bar if it is present.
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


}