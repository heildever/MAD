package com.example.mad.lab2;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    ListView ListView;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    //Stiben
    Button CloSes;

    final ArrayList group_list = new ArrayList();
    int update_count=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////////////Stiben Abr29
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_group);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), new_group_activity.class);
                startActivity(i);

            }
        });



       //Stiben
        final FloatingActionButton update = (FloatingActionButton) findViewById(R.id.update_button);

        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                finish();
                startActivity(getIntent());

            }
        });



       //Stiben
        CloSes = (Button) findViewById(R.id.clos_ses);
        CloSes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        });
        //

        //daniel1

        ////PENDIENTES
        ///SACAR LA LISTA DE GRUPOS Q SE TIENEN

        Firebase.setAndroidContext(this);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final String userID=mAuth.getCurrentUser().getUid();

        Firebase firebase = new Firebase(Config.FIREBASE_URL).child("Users").child(userID).child("groups");

        final HashMap<String,HashMap<String,String>> data_items = new HashMap<>();
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                group_list.clear();
                String group_name;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    group_name=postSnapshot.child("group").getValue().toString();

                    group_list.add(group_name);

                }
                update_count=update_count+1; //
                if (update_count>1) {

                    update.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext(),"your Group list has changed. \nPUSH THE BUTTON FOR UPDATE THE GROUP LIST", Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onCancelled(FirebaseError error) {
            }

        });


        final ArrayList<groups_class> data_groups2 = new ArrayList<groups_class>();
        final ArrayList<doubts_class> data_doubts= new ArrayList<doubts_class>();

        final groups_adapter adapter = new groups_adapter(this, R.layout.listview_groups_row, data_groups2,data_doubts);


        Firebase.setAndroidContext(this);
        firebase = new Firebase(Config.FIREBASE_URL).child("Groups");


        //daniel1

        firebase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                data_groups2.clear();
                data_doubts.clear();

                float all_doubts=0;

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    if (group_list.contains(postSnapshot.getKey().toString())) {

                        HashMap<String, Object> Items_2 = (HashMap<String, Object>) postSnapshot.child("Items").getValue();
                        String noti = postSnapshot.child("noti").getValue().toString();
                        int icon = postSnapshot.child("icon").getValue(int.class);

                        String title = postSnapshot.child("title").getValue().toString();
                        groups_class group2 = new groups_class(noti, icon, title, Items_2);

                        //data_groups2.add(group2);

                        //ListView = (ListView) findViewById(R.id.lista1);
                        //ListView.setAdapter(adapter);


                        //TOTAL PRICE OF GROUP
                        Log.d("GRUPO : ",title);
                        HashMap<String,String> items_price = (HashMap<String,String>) postSnapshot.child("Items").getValue();
                        float total_price=0;

                        boolean same_currency=false;
                        float contador_currency=0;
                        String temporal_currency=""; //voy a contar todos los iguales, 1==2, 2==3, 4==5 ... y cada acierto se suma en contador currency, si es igual a contador items entonces todos tienen el mismo currency
                        float contador_items=0;

                        for (DataSnapshot postSnapshot2 : postSnapshot.child("Items").getChildren()) {
                            total_price=total_price+Float.parseFloat(postSnapshot2.child("price").getValue().toString());

                            if(Objects.equals(temporal_currency, postSnapshot2.child("currency").getValue().toString())){
                                contador_currency=contador_currency+1;
                            }
                            temporal_currency=postSnapshot2.child("currency").getValue().toString();
                            contador_items=contador_items+1;
                        }

                        if((contador_items-1)==contador_currency){same_currency=true;}

                        Log.d("total spenditure : ", String.valueOf(total_price));


                        //TOTAL MEMBERS

                        float total_members=postSnapshot.child("members").getChildrenCount();;
                        Log.d("total members : ", String.valueOf(total_members));

                        float divided_price=total_price/total_members;
                        Log.d("divided prie : ", String.valueOf(divided_price));

                        if (same_currency){Log.d("currency : ", temporal_currency);}

                        //check if I had paid

                        boolean paid;
                        paid= Boolean.parseBoolean(postSnapshot.child("members").child(userID).child("paid").getValue().toString());
                        Log.d("paid : ", String.valueOf(paid));

                        if(paid==false){all_doubts=all_doubts+divided_price;}




                        data_groups2.add(group2);
                        data_doubts.add(new doubts_class(total_price,divided_price));

                        ListView = (ListView) findViewById(R.id.lista1);
                        ListView.setAdapter(adapter);


                    }



                }
                Log.d("total to paid all groups : ", String.valueOf(all_doubts));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        //daniel1 fin


            //Creating the adapter
        ListView = (ListView) findViewById(R.id.lista1);

        View header = (View) getLayoutInflater().inflate(R.layout.list_header_row, null);
        ListView.addHeaderView(header);
        ListView.setAdapter(adapter);

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView v = (TextView) view.findViewById(R.id.text1);


                Intent i = new Intent(view.getContext(), Items_activity.class);
                i.putExtra("GroupID", v.getText());
                startActivity(i);
            }
        });



        // MOSTRAR EL USERID

        Firebase.setAndroidContext(this);
        //daniel2 fin
        //Stiben
        mAuth = FirebaseAuth.getInstance();

        TextView userID_tv = (TextView) findViewById(R.id.userID);
        userID_tv.setText("welcome: " + mAuth.getCurrentUser().getEmail());
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


class doubts_class {
    //SE TUBO QUE HACER ESTA CLASE PARA ENVIAR LOS PARAMETROS DE DEUDAS TOTALES Y DIVIDIDOS, PQ SI
    // SE PONIAN EN LA CLASE GRUPOS SE SUBIRIAN TAMBIEN CON ESOS VALORES A LA
    // BASE DE DATOS Y NO QUEREMOS ESO

    public float total;
    public float divided;

    public doubts_class(){
        super();
    }

    public doubts_class( float total,float divided){
        super();
        this.total = total;
        this.divided = divided;
    }

    public void setTotal( float total){this.total=total;}
    public String getTotal(){return String.valueOf(this.total);}

    public void setDivided( float d){this.divided=d;}
    public String getDivided(){return String.valueOf(this.divided);}

}
