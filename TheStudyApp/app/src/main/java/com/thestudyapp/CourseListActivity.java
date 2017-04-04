package com.thestudyapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CourseListActivity extends AppCompatActivity {
    ArrayList<String> spinnerArray;
    ArrayAdapter<String> spinnerArrayAdapter;
    ListView listOfCoursesLV;
    ArrayList<String> listOfCourses = new ArrayList<String>();
    ArrayList<String> listOfUserDBSavedCourses = new ArrayList<String>();
    ArrayAdapter<String> listOfCoursesAdaptor;
    String userUID,currentSubject,selectedCourseToAdd;
    Button addCourseBtn ;
    final DatabaseReference rootDBRef = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference subjectsDBRef = rootDBRef.child("subjects");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        userUID = bundle.getString("uid");

        setContentView(R.layout.activity_course_list);
        addCourseBtn  = (Button) findViewById(R.id.addCourseButton);
        initalizeSpinner();
        initalizeListOfCourses();


    }

    @Override
    protected void onStart() {
        super.onStart();

        subjectsDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Iterator<DataSnapshot> x = snapshot.getChildren().iterator();
                while(x.hasNext()) {
                    spinnerArray.add(x.next().getKey());
                }
                spinnerArrayAdapter.notifyDataSetChanged();
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

      /*  */

        DatabaseReference courseDBRef = rootDBRef.child("users").child(userUID).child("courses");
        courseDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> x = dataSnapshot.getChildren().iterator();
                while(x.hasNext()) {
                    listOfUserDBSavedCourses.add(x.next().getValue().toString());
                }

                //notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }

    public void initalizeSpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.subjectsSpinner);
        spinnerArray = new ArrayList<String>();
        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentSubject =parentView.getSelectedItem().toString();
                 updateListView(currentSubject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }
    public void initalizeListOfCourses(){
        listOfCoursesLV =  ((ListView) findViewById(R.id.coursesLV));
        listOfCoursesAdaptor = new ArrayAdapter<String>(this, R.layout.list_view , listOfCourses);

        listOfCoursesLV.setSelector( R.drawable.list_selector);
        listOfCoursesLV.setAdapter(listOfCoursesAdaptor);

        listOfCoursesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(!listOfUserDBSavedCourses.contains((currentSubject+"."+parent.getItemAtPosition(position).toString()))) {
                    selectedCourseToAdd = currentSubject + "." + parent.getItemAtPosition(position).toString();
                    view.setSelected(true);
                    addCourseBtn.setEnabled(true);
                }
                else {
                    Toast.makeText(CourseListActivity.this, "Already in your courses", Toast.LENGTH_LONG).show();
                   addCourseBtn.setEnabled(false);
                }


            }
        });
    }

    public void addSelectedCourse(View v){

        rootDBRef.child("users").child(userUID).child("courses").push().setValue(selectedCourseToAdd);
        Toast.makeText(CourseListActivity.this, "Course Added", Toast.LENGTH_LONG).show();

        addCourseBtn.setEnabled(false);
    }

    public void updateListView(String subject){


        subjectsDBRef.child(subject).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                Iterator<DataSnapshot> courses = dataSnapshot.getChildren().iterator();
                listOfCourses.clear();
                while(courses.hasNext()) {
                    listOfCourses.add(courses.next().getKey().toString());

                }

                listOfCoursesAdaptor.notifyDataSetChanged();
             }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listOfCoursesAdaptor.notifyDataSetChanged();

    }
}