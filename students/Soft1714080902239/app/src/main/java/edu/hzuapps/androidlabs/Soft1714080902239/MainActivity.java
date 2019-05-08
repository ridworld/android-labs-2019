package edu.hzuapps.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static edu.hzuapps.myapplication.DatabaseHelper.DB_NAME;
import static edu.hzuapps.myapplication.DatabaseHelper.TABLE_NAME;
import static edu.hzuapps.myapplication.DatabaseHelper.VERSION;

public class MainActivity extends AppCompatActivity {

    public static DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private List<String> diary=new ArrayList<>();
    public static final int TAG_INSERT=1;
    public static final int TAG_UPDATE=0;
    private String select_item;
    private int Id;
    ListView listView;
    ArrayAdapter<String> adapter;

    private SwipeRefreshLayout swipeRefresh;

    public static DatabaseHelper getDbHelper(){
        return dbHelper;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button add=(Button)findViewById(R.id.add);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout
                .OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        dbHelper=new DatabaseHelper(MainActivity.this,DB_NAME,null,VERSION);
        dbHelper.getWritableDatabase();
        init();
        //添加笔记
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Detail.class);
                intent.putExtra("TAG",TAG_INSERT);
                startActivity(intent);
            }
        });

        //设置列表项目点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent=new Intent(MainActivity.this,Detail.class);
                Id=getDiaryId(position);
                //  Log.d("MainActivity",""+id);
                intent.putExtra("ID",Id);
                intent.putExtra("TAG",TAG_UPDATE);
                startActivity(intent);
            }
        });
    }

    private void init(){
        db=dbHelper.getWritableDatabase();
        diary.clear();
        //查询数据库，将title一列添加到列表项目中
        Cursor cursor=db.query(TABLE_NAME,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            String diary_item;
            do{
                diary_item=cursor.getString(cursor.getColumnIndex("title"));
                diary.add(diary_item);
            }while(cursor.moveToNext());
        }
        cursor.close();
        adapter=new ArrayAdapter<String>(
                MainActivity.this,android.R.layout.simple_list_item_1,diary);
        listView=(ListView)findViewById(R.id.list_item);
        listView.setAdapter(adapter);
    }

    private void refresh(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private int getDiaryId(int position){
        //获取所点击的日记的title
        int Id;
        select_item=diary.get(position);
        //获取id
        db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query(TABLE_NAME,new String[]{"id"},"title=?",
                new String[]{select_item},null,null,null);
        cursor.moveToFirst();
        Id=cursor.getInt(cursor.getColumnIndex("id"));
        return Id;


    }
}
