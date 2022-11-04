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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase database;
    Button CT, CP,InsertCP,ShowCP;
    EditText txt,txt3,txt2;
    TextView tittle;
    ListView lv;
    ArrayList<String> ds;
    ArrayAdapter adapter;
    int stop = 0;
    Cursor c;
    String table = "SinhVien";
    String row ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setList();
        doCreateDb();

        CT = findViewById(R.id.CreateCT);
        CP = findViewById(R.id.CreateCP);
        InsertCP = findViewById(R.id.InsertCP);
        ShowCP = findViewById(R.id.ShowCP);
        txt = findViewById(R.id.txtEdit);
        txt2 = findViewById(R.id.txtEdit2);
        txt3 = findViewById(R.id.txtEdit3);
        tittle = findViewById(R.id.title);

        if(HasTable())
            txt.setText("Has Not Table");
        doLoadCP();

        CT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table = "SinhVien";
                doLoadCP();
                tittle.setText("MaSV - Ten SV");
                txt.setHint("Tên Sinh Viên");
                txt2.setVisibility(view.GONE);
                txt3.setVisibility(view.GONE);
            }
        });
        CP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table = "Lop";
                doLoadCP();
                tittle.setText("MaL - Khoa - TenGV - MaSV");
                txt.setHint("Ma Sinh Viên");
                txt2.setVisibility(view.VISIBLE);
                txt3.setVisibility(view.VISIBLE);
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
    public boolean HasTable(){
        try{
            doCreateTableCT();
            doCreateTableCP();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void Resert(){
        doDeleteDb();
        doCreateDb();
        doCreateTableCP();
        doCreateTableCT();
        doLoadCP();
    }


    //Database
    public void doCreateDb() {
        database = openOrCreateDatabase("qlSinhVien.db", MODE_PRIVATE,null);
    }

    public void doDeleteDb() {
        String msg;
        if (deleteDatabase("qlSinhVien.db"))
            msg = "Delete database [qlSinhVien.db] is successful";
        else
            msg = "Delete database [qlSinhVien.db] is failed";
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    // Create table
    public void doCreateTableCP() {
        String sql;
        sql = "CREATE TABLE Lop (" +
                "maL TEXT primary key," +
                "khoa TEXT," +
                "tenGV TEXT," +
                "maSV TEXT)";
        database.execSQL(sql);
    }
    public void doCreateTableCT() {
        String sql;
        sql = " CREATE TABLE SinhVien (" +
                "maSV TEXT primary key," +
                "tenSV TEXT)";
        database.execSQL(sql);
    }

    // Insert - Update - Delete table

    public void doInsertCP() {
        ContentValues values = new ContentValues();
        if(table.equals("Lop")){
            values.put("maL", "M0" + row);
            values.put("khoa", txt.getText().toString());
            values.put("tenGV",txt2.getText().toString());
            values.put("maSV", txt.getText().toString());
        }
        else{
            values.put("maSV", "m0" + row);
            values.put("tenSV", txt.getText().toString());
        }
        database.insert(table, null, values);
        txt.setText("");
        txt2.setText("");
        txt3.setText("");
        doLoadCP();
    }

    public void doUpdateCP(int so) {
        ContentValues values = new ContentValues();
        if(table.equals("Lop")){
            values.put("khoa", txt2.getText().toString());
            values.put("tenGV",txt3.getText().toString());
            values.put("maSV",txt.getText().toString());
        }
        else{
            values.put("tenSV",txt.getText().toString());
        }
        String text = getMa(so);
        if(table.equals("Lop"))
            database.update(table,values,"maL=?",new String[]{text});
        else
            database.update(table,values,"maSV=?",new String[]{text});
        doLoadCP();
    }
    public void doDeleteCP(int so) {
        c = database.query(table,null,null,null,null,null,null);
        c.moveToPosition(so);
        String text = c.getString(0);
        if(table.equals("Lop"))
            database.delete(table,"maL=?",new String[]{text});
        else
            database.delete(table,"maSV=?",new String[]{text});
        doLoadCP();
    }

    // Select Table

    public String getMa(int so){
        c = database.query(table,null,null,null,null,null,null);
        c.moveToPosition(so);
        String text = c.getString(0);
        return text;
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
            if(table.equals("Lop"))
                data+= " - " +  c.getString(2)
                        + " - " + c.getString(3);
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