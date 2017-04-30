package com.example.mad.lab2;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class Items_activity extends AppCompatActivity {

    android.widget.ListView ListView;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private String GroupID;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_activity);


        Bundle bundle = getIntent().getExtras();
        GroupID =bundle.getString("GroupID");

        final ArrayList<items_class> data_items = new ArrayList<items_class>();
        final items_adapter adapter = new items_adapter(this, R.layout.listview_items_row, data_items);
        //The items_class are set in the list view progressively, not all of them at the same time




        ////////////////////////////////FIREBASE ATTEMPT///////////
        Firebase.setAndroidContext(this);
        Firebase firebase = new Firebase(Config.FIREBASE_URL).child("Groups").child(GroupID).child("Items");
        //firebase = new Firebase(FIREBASE_URL).child(FIREBASE_CHILD);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                data_items.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {


                    //Log.d("AndroidBash", "item_activity: " + postSnapshot.toString());
                    Log.d("AndroidBash", "item_activity: " + postSnapshot.getValue().toString());

                    items_class item= postSnapshot.getValue(items_class.class);
                    item.setIcon(2130837589);   //HABIA ERROR POR EL NUMERO ENVIADO EN LA IMAGEN
                    data_items.add(item);
                    //data_items2.add(new items_class("zzzzz", "5", "$",R.drawable.bills));

                    ListView = (ListView) findViewById(R.id.lista2);
                    ListView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });


        ///////////////////////////////////////
        //Creating the adapter
        //items_adapter adapter = new items_adapter(this, R.layout.listview_items_row, data_items2);
        ListView = (ListView) findViewById(R.id.lista2);

        View header = (View) getLayoutInflater().inflate(R.layout.list_header_row,null);

        //Add the header to the list
        ListView.addHeaderView(header);
        ListView.setAdapter(adapter);

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView v = (TextView)view.findViewById(R.id.items_name);
                Toast.makeText(getApplicationContext(),v.getText(), Toast.LENGTH_SHORT).show();
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Funciona", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(view.getContext(), new_item_activity.class);
                i.putExtra("group_name",GroupID);
                startActivity(i);

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
        if (id == R.id.action_seeIndividuals) {
            //return true;
            //Toast.makeText(getApplicationContext(),item.getTitle(), Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, members_activity.class);
            i.putExtra("group_name",this.getTitle());
            startActivity(i);
            return true;

        }
        if (id == R.id.action_invitePeople) {
            Intent i = new Intent(this, Invite_Activity.class);
            i.putExtra("GroupID",GroupID);
            startActivity(i);
            return true;

        }

        if (id == R.id.action_leaveGroup) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Leaving Group")
                    .setMessage("Are you sure you want to leave this group?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"pues estoy embarazada", Toast.LENGTH_SHORT).show();
                            //finish();



                            ////////delete from group members
                            final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            //Firebase firebase = new Firebase(Config.FIREBASE_URL).child("Groups").child(GroupID).child("members");

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            //ref.child("Groups").child("members").orderByChild("userID").equalTo(mAuth.getCurrentUser().getUid());
                            ref.child("Groups").child(GroupID).child("members").orderByChild("userID").equalTo(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                //
                                @Override
                                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                                    for (com.google.firebase.database.DataSnapshot ref: dataSnapshot.getChildren()) {
                                        ref.getRef().removeValue();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            ////////delete from group members-end


                            ///delete from user groups
                            //DatabaseReference ref_userg = FirebaseDatabase.getInstance().getReference();
                            ref.child("Users").child(mAuth.getCurrentUser().getUid()).child("groups").orderByChild("group").equalTo(GroupID).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                //



                                @Override
                                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                                    for (com.google.firebase.database.DataSnapshot ref: dataSnapshot.getChildren()) {
                                        ref.getRef().removeValue();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            ///delete from user groups-end



                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

        }
        return super.onOptionsItemSelected(item);
        //Toast.makeText(getApplicationContext(),"perro", Toast.LENGTH_SHORT).show();
        //return true;
    }



}


class member_class{

    public String userID;
    public boolean paid;
    public member_class(String userID) {
        this.userID=userID;
        paid=false;
    }

}