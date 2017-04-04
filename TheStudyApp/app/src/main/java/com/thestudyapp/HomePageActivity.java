package com.thestudyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;




public class HomePageActivity extends AppCompatActivity {
    //TODO: Get list of subjects from Database
    ArrayList<String> listOfSubjects = new ArrayList<String>();
    ArrayList<String> listOfUserDBSavedCourses = new ArrayList<String>();
    HashMap<String, String> coursesHashMap = new HashMap<>();
    ArrayAdapter<String> listOfSubjectsAdaptor;
    ListView listOfSubjectsLV;
    String userName, userUID, userEmail;
    String selectedCourse;

    final DatabaseReference rootDBRef = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference userDBRef = rootDBRef.child("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("displayName");
        userUID = bundle.getString("uid");
        userEmail = bundle.getString("email");
        setContentView(R.layout.activity_home_page);
        ((TextView) findViewById(R.id.users_name)).setText(userName);
        initalizeListOfSubjects();
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference coursesDBRef = userDBRef.child(userUID).child("courses");

        userDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild(userUID)) {
                    List<String> userCourses = new ArrayList<>();
                    Student newStudent = new Student(userUID, userName, userEmail, userCourses);
                    snapshot.child(userUID);
                    DatabaseReference newUser = userDBRef.child(userUID);
                    newUser.setValue(newStudent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference courseDBRef = rootDBRef.child("users").child(userUID).child("courses");
        courseDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> x = dataSnapshot.getChildren().iterator();
                listOfUserDBSavedCourses.clear();
                while(x.hasNext()) {
                    DataSnapshot val = x.next();
                    coursesHashMap.put(val.getValue().toString(),val.getKey());
                    listOfUserDBSavedCourses.add(val.getValue().toString());
                }
                listOfSubjectsAdaptor.notifyDataSetChanged();
                //notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    public void initalizeVariables() {

    }

    public void initalizeListOfSubjects() {
        listOfSubjectsLV = ((ListView) findViewById(R.id.usersSubjectsLV));
        listOfSubjectsAdaptor = new ArrayAdapter<String>(this, R.layout.list_view, listOfUserDBSavedCourses);

        listOfSubjectsLV.setSelector(R.drawable.list_selector);
        listOfSubjectsLV.setAdapter(listOfSubjectsAdaptor);

        listOfSubjectsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                 selectedCourse   = parent.getItemAtPosition(position).toString();
                view.setSelected(true);
            }
        });
    }
    public void viewCourse(View v) {
    }
    public void deleteCourse(View v) {

        Toast.makeText(this, coursesHashMap.get(selectedCourse.replace("> ",".")).toString(), Toast.LENGTH_LONG).show();

        userDBRef.child(userUID).child("courses").child(coursesHashMap.get(selectedCourse.replace("> ",".")).toString()).removeValue();
    }
        public void addCourse(View v) {
        Intent intent = new Intent(HomePageActivity.this, CourseListActivity.class);
        intent.putExtra("uid",userUID);
        startActivity(intent);
    }

}
