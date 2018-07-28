package com.example.aditya.rideapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListOfRiderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private DatabaseReference mDtabase,mDatabase2;
    String UID;
    int person,beg;
    double sourceLat,sourceLong;
    List<String> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_rider);
        UID=getIntent().getStringExtra("RequiredId");
        recyclerView=new RecyclerView(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDtabase= FirebaseDatabase.getInstance().getReference().child("TakeLiftRequests").child(UID);

        mDtabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                person= Integer.parseInt(dataSnapshot.child("Persons").getValue().toString());
                beg= Integer.parseInt(dataSnapshot.child("Persons").getValue().toString());
                sourceLat= Integer.parseInt(dataSnapshot.child("SurceLat").getValue().toString());
                sourceLong= Integer.parseInt(dataSnapshot.child("PersonsLong").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"try Again",Toast.LENGTH_LONG).show();
            }
        });
        ids=new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase2=FirebaseDatabase.getInstance().getReference().child("GiveLiftRequests");
        mDatabase2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Iterable<DataSnapshot> iterable=dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator=iterable.iterator();
                while (iterator.hasNext())
                {
                    iterator.next().child("Begs");
                    iterator.next().child("Begs");
                    iterator.next().child("Begs");
                    iterator.next().child("Begs");
                    if(5>4)
                    {
                        ids.add(dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FirebaseRecyclerAdapter<ListOfRiderModel,riderViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<ListOfRiderModel, riderViewHolder>(
                ListOfRiderModel.class,
                R.layout.single_rider,
                riderViewHolder.class,
                mDtabase

        ) {
            @Override
            protected void populateViewHolder(riderViewHolder viewHolder, ListOfRiderModel model, int position) {
                viewHolder.setRiderName(model.getNameRider());
                viewHolder.setSourceRider(model.getSourceText());
                viewHolder.setDestText(model.getDestText());
                viewHolder.setTime(model.getTime());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(ListOfRiderActivity.this,TakerDetailActivity.class);
                        intent.putExtra("Rider","dfb5465f4gb454g5v5df");
                        startActivity(intent);
                    }
                });
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }



    public class riderViewHolder extends RecyclerView.ViewHolder {
        View  mView;
        public riderViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setRiderName(String riderName) {
            TextView rn=mView.findViewById(R.id.namesingletaker);
            rn.setText(riderName);
        }

        public void setDestText(String destText) {
            TextView dt=mView.findViewById(R.id.to);
            dt.setText(destText);
        }

        public void setSourceRider(String sourceRider) {
            TextView sr=mView.findViewById(R.id.from);
            sr.setText(sourceRider);
        }

        public void setTime(String time) {
            TextView tm=mView.findViewById(R.id.time);
            tm.setText(time);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.refresh)
        {

        }
        return super.onOptionsItemSelected(item);
    }
}
