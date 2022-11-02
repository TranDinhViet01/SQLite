package com.example.sqlitebt10;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase database;
    Button CT, CP,InsertCP,ShowCP;
    EditText txt,txt2;
    ListView lv;
    ArrayList<String> ds;
    ArrayAdapter adapter;
    int stop = 0;
    Cursor c;
    String table = "Computer";
    String row ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setList();
        doCreateDb();
        doLoadCP();

        CT = findViewById(R.id.CreateCT);
        CP = findViewById(R.id.CreateCP);
        InsertCP = findViewById(R.id.InsertCP);
        ShowCP = findViewById(R.id.ShowCP);
        txt = findViewById(R.id.txtEdit);
        txt2 = findViewById(R.id.txtEdit2);

        CT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table = "Category";
                doLoadCP();
                txt2.setVisibility(view.GONE);
            }
        });
        CP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table = "Computer";
                doLoadCP();
                txt2.setVisibility(view.VISIBLE);
            }
        });
        InsertCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doInsertCP();
            }
        });
        ShowCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resert();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(stop == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("Thông Báo!");
                    alertDialog.setMessage("Bạn có chắc chắn muốn sửa Items này?");
                    int so = i;
                    alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            doUpdateCP(so);
                        }
                    });
                    alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alertDialog.show();
                }
                else
                    stop = 0;
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                stop = 1;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Thông Báo!");
                alertDialog.setMessage("Bạn có chắc chắn muốn xóa Items này?");
                int so = i;
                alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doDeleteCP(so);
                    }
                });
                alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
                return false;
            }
        });
    }
    public void Resert(){
        doDeleteDb();
        doCreateDb();
        doCreateTableCP();
        doCreateTableCT();
        doLoadCP();
    }
    public void doCreateDb() {
        database = openOrCreateDatabase("qlComputer.db", MODE_PRIVATE,null);
    }
    // Create Database và table
    public void doDeleteDb() {
        String msg;
        if (deleteDatabase("qlComputer.db"))
            msg = "Delete database [qlComputer.db] is successful";
        else
            msg = "Delete database [qlComputer.db] is failed";
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void doCreateTableCP() {
        String sql;
        sql = "CREATE TABLE Computer (" +
                "maCP TEXT primary key," +
                "tenCP TEXT," +
                "maCT TEXT)";
        database.execSQL(sql);
    }

    public void doCreateTableCT() {
        String sql;
        sql = " CREATE TABLE Category (" +
                "maCT TEXT primary key," +
                "tenCT TEXT)";
        database.execSQL(sql);
    }
    public void doDeleteCP(int so) {
        c = database.query(table,null,null,null,null,null,null);
        c.moveToPosition(so);
        String text = c.getString(0);
        if(table.equals("Computer"))
            database.delete(table,"maCP=?",new String[]{text});
        else
            database.delete(table,"maCT=?",new String[]{text});
        doLoadCP();
    }

    public void doInsertCP() {
        ContentValues values = new ContentValues();
        if(table.equals("Computer")){
            values.put("maCP", "M0" + row);
            values.put("tenCP", txt.getText().toString());
            values.put("maCT",txt2.getText().toString());
        }
        else{
            values.put("maCT", "m0" + row);
            values.put("tenCT", txt.getText().toString());
        }
        database.insert(table, null, values);
        doLoadCP();
    }

    public void doUpdateCP(int so) {
        ContentValues values = new ContentValues();
        if(table.equals("Computer")){
            values.put("tenCP", txt.getText().toString());
            values.put("maCT",txt2.getText().toString());
        }
        else{
            values.put("tenCT", txt.getText().toString());
        }
        c = database.query(table,null,null,null,null,null,null);
        c.moveToPosition(so);
        String text = c.getString(0);
        if(table.equals("Computer"))
            database.update(table,values,"maCP=?",new String[]{text});
        else
            database.update(table,values,"maCT=?",new String[]{text});
        doLoadCP();
    }
    public void doLoadCP() {
        ds.clear();
        c = database.query(table,null,null,null,null,null,null);
        c.moveToFirst();
        String data;
        while(c.isAfterLast()==false)
        {
            data = "";
            data+= c.getString(0) + " - " + c.getString(1);
            if(table.equals("Computer"))
                data+= " - " +  c.getString(2);
            ds.add(data);
            c.moveToNext();
        }
        row = "" + (ds.size() + 1);
        adapter.notifyDataSetChanged();
        c.close();
    }
    public void setList(){
        lv = findViewById(R.id.ListViewFie);
        ds = new ArrayList<>();
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,ds);
        lv.setAdapter(adapter);
    }
}