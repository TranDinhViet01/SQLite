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
    EditText txt_357,txt2_357,txt3_357;
    TextView tittle_357;
    ListView lv_357;
    ArrayList<String> ds_357;
    ArrayAdapter adapter_357;
    int stop = 0;
    Cursor c;
    String table_357 = "SinhVien";
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
        txt_357 = findViewById(R.id.txtEdit);
        txt2_357 = findViewById(R.id.txtEdit2);
        txt3_357 = findViewById(R.id.txtEdit3);
        tittle_357 = findViewById(R.id.title);

        if(Hastable_357())
            txt_357.setText("Has Not table_357");
        doLoadCP();

        CT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table_357 = "SinhVien";
                doLoadCP();
                tittle_357.setText("MaSV - Ten SV");
                txt3_357.setHint("Tên Sinh Viên");
                txt2_357.setVisibility(view.GONE);
                txt_357.setHint("Mã Sinh Viên");
            }
        });
        CP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table_357 = "Lop";
                doLoadCP();
                tittle_357.setText("MaL - Khoa - TenGV - MaSV");
                txt3_357.setHint("Tên Giáo Viên");
                txt2_357.setVisibility(view.VISIBLE);
                txt_357.setHint("Mã Sinh Viên");
            }
        });
        InsertCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(doInsertCP()== false)
                    Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_LONG).show();
            }
        });
        ShowCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resert();
            }
        });
        lv_357.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        lv_357.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
    public boolean Hastable_357(){
        try{
            doCreatetable_357CT();
            doCreatetable_357CP();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void Resert(){
        doDeleteDb();
        doCreateDb();
        doCreatetable_357CP();
        doCreatetable_357CT();
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


    // Create table_357
    public void doCreatetable_357CP() {
        String sql;
        sql = "CREATE table Lop (" +
                "maL TEXT primary key," +
                "khoa TEXT," +
                "tenGV TEXT," +
                "maSV TEXT)";
        database.execSQL(sql);
    }
    public void doCreatetable_357CT() {
        String sql;
        sql = " CREATE table SinhVien (" +
                "maSV TEXT primary key," +
                "tenSV TEXT)";
        database.execSQL(sql);
    }

    // Insert - Update - Delete table_357

    public boolean doInsertCP() {
        try{
            ContentValues values = new ContentValues();
            if(table_357.equals("Lop")){
                values.put("maL", "M0" + row);
                values.put("khoa", txt2_357.getText().toString());
                values.put("tenGV",txt3_357.getText().toString());
                values.put("maSV", txt_357.getText().toString());
            }
            else{
                values.put("maSV", txt_357.getText().toString());
                values.put("tenSV",txt3_357.getText().toString());
            }
            database.insert(table_357, null, values);
            txt_357.setText("");
            txt2_357.setText("");
            txt3_357.setText("");
            doLoadCP();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void doUpdateCP(int so) {
        ContentValues values = new ContentValues();
        if(table_357.equals("Lop")){
            values.put("khoa", txt2_357.getText().toString());
            values.put("tenGV",txt3_357.getText().toString());
            values.put("maSV",txt_357.getText().toString());
        }
        else{
            values.put("tenSV",txt3_357.getText().toString());
        }
        String text = getMa(so);
        if(table_357.equals("Lop"))
            database.update(table_357,values,"maL=?",new String[]{text});
        else
            database.update(table_357,values,"maSV=?",new String[]{text});
        doLoadCP();
    }
    public void doDeleteCP(int so) {
        c = database.query(table_357,null,null,null,null,null,null);
        c.moveToPosition(so);
        String text = c.getString(0);
        if(table_357.equals("Lop"))
            database.delete(table_357,"maL=?",new String[]{text});
        else
            database.delete(table_357,"maSV=?",new String[]{text});
        doLoadCP();
    }

    // Select table_357

    public String getMa(int so){
        c = database.query(table_357,null,null,null,null,null,null);
        c.moveToPosition(so);
        String text = c.getString(0);
        return text;
    }

    public void doLoadCP() {
        ds_357.clear();
        c = database.query(table_357,null,null,null,null,null,null);
        c.moveToFirst();
        String data;
        while(c.isAfterLast()==false)
        {
            data = "";
            data+= c.getString(0) + " - " + c.getString(1);
            if(table_357.equals("Lop"))
                data+= " - " +  c.getString(2)
                        + " - " + c.getString(3);
            ds_357.add(data);
            c.moveToNext();
        }
        row = "" + (ds_357.size() + 1);
        adapter_357.notifyDataSetChanged();
        c.close();
    }
    public void setList(){
        lv_357 = findViewById(R.id.ListViewFie);
        ds_357 = new ArrayList<>();
        adapter_357 = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,ds_357);
        lv_357.setAdapter(adapter_357);
    }
}