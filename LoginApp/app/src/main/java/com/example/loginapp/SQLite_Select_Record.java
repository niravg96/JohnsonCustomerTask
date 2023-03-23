package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SQLite_Select_Record extends BaseActivity {

    RecyclerView recyclerView;
     SQLite_Database_Helper databaseHelper;
    List<SQLiteEmployeeData> employeelist;
    SQLite_RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_select_record);

        recyclerView =(RecyclerView)findViewById(R.id.ShowEmpData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        employeelist =new ArrayList<>();
        databaseHelper =new SQLite_Database_Helper(this);
        employeelist=databaseHelper.getEmployeeData();
        adapter = new SQLite_RecyclerAdapter(employeelist, new SQLite_RecyclerAdapter.ItemClickListener() {
            @Override
            public void onDeleteClicked(int position) {

                    databaseHelper.DeleteRecord(employeelist.get(position).id);
                    showToast("Employee Details Deleted");
                    employeelist.remove(position);
                    adapter.notifyDataSetChanged();
            }
            @Override
            public void onUpdateClicked(int position) {
                String id2 = employeelist.get(position).id;
                Intent i =new Intent(SQLite_Select_Record.this,SQLite_Update_Activity.class);
                i.putExtra("myid2",id2);
                startActivity(i);
                finish();
            }

            @Override
            public void onShowClicked(int position) {
                String id = employeelist.get(position).id;
                Intent i =new Intent(SQLite_Select_Record.this,SQLite_Select_All_Records.class);
                i.putExtra("myid",id);
                startActivity(i);
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    public boolean onSupportNavigateUp() {

        Intent i =new Intent(SQLite_Select_Record.this, SQLite_Insert_Activity.class);
        startActivity(i);
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Intent i =new Intent(SQLite_Select_Record.this, SQLite_Insert_Activity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

}