package com.byted.camp.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.byted.camp.todolist.db.TodoContract.FeedEntry.TABLE_NAME;
import static com.byted.camp.todolist.db.TodoContract.SQL_CREATE_ENTRIES;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public class TodoDbHelper extends SQLiteOpenHelper {

    // TODO 定义数据库名、版本；创建数据库

    public TodoDbHelper(Context context) {
        super(context, "todolist", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(int i = oldVersion;i < newVersion;i++){
            switch(i){
                case 1:
                    try{
                        db.execSQL("ALTER TABLE " + TABLE_NAME );
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:break;
            }
        }
    }

}
