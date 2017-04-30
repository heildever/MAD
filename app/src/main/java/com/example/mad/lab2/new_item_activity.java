package com.example.mad.lab2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class new_item_activity extends AppCompatActivity {

    private EditText new_item_name;
    private EditText new_item_price;
    private EditText new_item_currency;
    private EditText new_item_alert;
    private Button new_item_save;
    //private Button but_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item);

        new_item_name = (EditText) findViewById(R.id.new_item_name);
        new_item_price = (EditText) findViewById(R.id.new_item_price);
        //new_item_currency = (EditText) findViewById(R.id.new_item_currency);
        new_item_alert = (EditText) findViewById(R.id.new_item_alert);
        new_item_save = (Button) findViewById(R.id.new_item_save);
        //but_cancel = (Button) findViewById(R.id.new_item_cancell);

        //Spiner
        final Spinner spinner_cur = (Spinner) findViewById(R.id.spin_item_currency);
        ArrayAdapter<CharSequence> adap = ArrayAdapter.createFromResource(this,
                R.array.currencies_array, android.R.layout.simple_spinner_item);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cur.setAdapter(adap);

        //Put it in the xml

        //End spinner

        cancel_new_item();

        Firebase.setAndroidContext(this);


        //access to the group id
        Bundle bundle = getIntent().getExtras();
        final String group_name=bundle.getString("group_name");


        //Click Listener for button
        new_item_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating firebase object
                Firebase ref = new Firebase(Config.FIREBASE_URL).child("Groups").child(group_name).child("Items");

                //Getting values to store
                String name = new_item_name.getText().toString().trim();
                String price = new_item_price.getText().toString();
                //String currency = new_item_currency.getText().toString().trim();
                String currency=    spinner_cur.getSelectedItem().toString().trim();
                String alert = new_item_alert.getText().toString().trim();


                //Creating Person object
                items_class new_item = new items_class();

                //Adding values
                new_item.setName(name);
                new_item.setPrice(price);
                new_item.setCurrency(currency);
                new_item.setAlert(alert);

                //Storing values to firebase
                ref.child(new_item.getName()).setValue(new_item);
                finish();
            }
        });
    }

//Click Listener for button
        public void cancel_new_item () {
            Button but_cancel = (Button) findViewById(R.id.new_item_cancell);
            but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv) {

                Intent i = new Intent(new_item_activity.this, Items_activity.class);
                i.putExtra("group_name", "Cancelled");
                startActivity(i);
            }
        });
        }



}
