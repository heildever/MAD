package com.example.mad.lab2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    ListView ListView;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    //Stiben
    Button CloSes;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////////////Stiben Abr29
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_group);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Funciona", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(view.getContext(), new_group_activity.class);
                //i.putExtra("group_name",GroupID);
                startActivity(i);

            }
        });
        ////////////


        Bundle bundle = getIntent().getExtras();
        final ArrayList group_list=bundle.getStringArrayList("group_list");
        /*Log.d("AndroidBash", group_list.toString());*/
        //////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////USAR GROUP LIST PARA SACAR LOS GRUPOS Q SON Y NO TODOS, SE INTENTO CON EQUALTO PERO ESOLO E SPARA UNO///////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////
        //Stiben
        CloSes = (Button)findViewById(R.id.clos_ses);
        CloSes.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        });
        //

        //daniel1

        final ArrayList<groups_class> data_groups2 = new ArrayList<groups_class>();
        final groups_adapter adapter = new groups_adapter(this, R.layout.listview_groups_row, data_groups2);

        Firebase.setAndroidContext(this);
        Firebase firebase = new Firebase(Config.FIREBASE_URL).child("Groups");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                data_groups2.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {


                    // PRUEBAS PARA RECIBIR DATOS
                    //Toast.makeText(getApplicationContext(), postSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), postSnapshot.getKey().toString(), Toast.LENGTH_SHORT).show();
                    /// PRUEBAS PARA RECIBIR DATOS FIN
                    //http://stackoverflow.com/questions/30744224/how-to-retrieve-a-list-object-from-the-firebase-in-android

                    if (group_list.contains(postSnapshot.getKey().toString())) {

                        HashMap<String, Object> Items_2 = (HashMap<String, Object>) postSnapshot.child("Items").getValue();
                        String noti = postSnapshot.child("noti").getValue().toString();
                   /* int icon = 0;
                    icon = Integer.parseInt(postSnapshot.child("icon").toString());*/
                        int icon = postSnapshot.child("icon").getValue(int.class);

                        String title = postSnapshot.child("title").getValue().toString();
                        groups_class group2 = new groups_class(noti, icon, title, Items_2);


                   /* groups_class group= (groups_class) postSnapshot.getValue();
                    Log.d("AndroidBash", "ON START2:" + postSnapshot.getValue(groups_class.class).toString());
                    group.setIcon(2130837589);   //HABIA ERROR POR EL NUMERO ENVIADO EN LA IMAGEN*/
                        data_groups2.add(group2);
                        //data_items2.add(new items_class("zzzzz", "5", "$",R.drawable.bills));
                        ListView = (ListView) findViewById(R.id.lista1);
                        ListView.setAdapter(adapter);
                    }

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

            //daniel1 fin


        //Array of type groups_class
        //In this case we cant use ArrayAdapter, we must create our own adapter
        /*groups_class data_groups[] = new groups_class[]{
                new groups_class(R.drawable.house,      "Home",   "16".concat(" US")),
                new groups_class(R.drawable.birthday,      "Daniel's birthday",   "5".concat(" EUR")),
                new groups_class(R.mipmap.ic_launcher,      "Group3",   "9".concat(" EUR")),
                new groups_class(R.mipmap.ic_launcher,    "Group4",   "35".concat(" EUR")),
                new groups_class(R.mipmap.ic_launcher,      "Group5",   "67".concat(" EUR")),
                new groups_class(R.mipmap.ic_launcher,      "Group6",   "43".concat(" EUR")),
                new groups_class(R.mipmap.ic_launcher,      "Group7",   "56".concat(" EUR")),
                new groups_class(R.mipmap.ic_launcher,      "Group8",   "98".concat(" EUR")),
                new groups_class(R.mipmap.ic_launcher,      "Group9",   "22".concat(" EUR")),
        };*/

        //Creating the adapter
        //groups_adapter adapter = new groups_adapter(this, R.layout.listview_groups_row, data_groups);
        ListView = (ListView) findViewById(R.id.lista1);

        View header = (View) getLayoutInflater().inflate(R.layout.list_header_row,null);
        //Add the header to the list
        ListView.addHeaderView(header);
        ListView.setAdapter(adapter);

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView v = (TextView)view.findViewById(R.id.text1);
                //Toast.makeText(getApplicationContext(),v.getText(), Toast.LENGTH_SHORT).show();


                Intent i = new Intent(view.getContext(), Items_activity.class);
                i.putExtra("GroupID",v.getText());
                startActivity(i);
            }
        });


        // MOSTRAR EL USERID

        Firebase.setAndroidContext(this);
        //daniel2 fin
        //Stiben
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        TextView userID =(TextView) findViewById(R.id.userID);
        userID.setText("welcome: "+mAuth.getCurrentUser().getEmail());
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items_class to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

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
