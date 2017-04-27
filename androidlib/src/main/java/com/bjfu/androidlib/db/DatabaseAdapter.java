package com.bjfu.androidlib.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11827 on 2016/6/15.
 */
public abstract class DatabaseAdapter<T extends IHasId> {
    private String tableName;
    private final SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(SQLiteOpenHelper openHelper, String tableName) {
        this.openHelper = openHelper;
        this.tableName = tableName;
    }

    /**
     * 插入t，并返回id
     *
     * @param t
     * @return
     */
    public final long insert(T t) {
        ContentValues values = new ContentValues();
        insert2ContentValues(t, values);
        database = openHelper.getWritableDatabase();
        long id = database.insert(tableName, null, values);
        database.close();
        database = null;
        return id;
    }
    
    /**
     * 根据id更新t
     *
     * @param t
     * @return 更新数量
     */
    public final int update(T t) {
        ContentValues values = new ContentValues();
        insert2ContentValues(t, values);
        database = openHelper.getWritableDatabase();
        int num = database.update(tableName, values, BaseColumns._ID + "=?", new String[]{"" + t.getId()});
        database.close();
        database = null;
        return num;
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return 删除条数
     */
    public final int delete(long id) {
        database = openHelper.getWritableDatabase();
        int num = database.delete(tableName, BaseColumns._ID + "=?", new String[]{"" + id});
        database.close();
        database = null;
        return num;
    }

    public final int deleteByParent(long parentId) {
        database = openHelper.getWritableDatabase();
        int num = 0;
        if (parentId < 0) {
            num = database.delete(tableName, null, null);
        } else {
            num = database.delete(tableName, IParentIdColumns._PARENT_ID + "=?", new String[]{"" + parentId});
        }
        database.close();
        database = null;
        return num;
    }

    /**
     * 获取所有元素
     *
     * @return
     */
    public final List<T> retrieveByParent(long parentId) {
        List<T> list = new ArrayList<T>();
        database = openHelper.getWritableDatabase();
        Cursor cursor;
        if (parentId < 0) {
            cursor = database.query(tableName, null, null, null, null, null, null);
        } else {
            cursor = database.query(tableName, null, IParentIdColumns._PARENT_ID + "=?", new String[]{"" + parentId}, null, null, null);
        }
        parseCursor(cursor, list);
        database.close();
        database = null;
        return list;
    }

    /**
     * 解析cursor中所有元素
     *
     * @param cursor
     * @param list
     */
    private final void parseCursor(Cursor cursor, List<T> list) {
        while (cursor.moveToNext()) {
            list.add(getCurrentCursorElement(cursor));
        }
    }

    /**
     * 将t中的元素装载入values中
     * 不存储id
     *
     * @param t      要装载元素
     * @param values
     */
    protected abstract void insert2ContentValues(T t, ContentValues values);

    /**
     * 获取当前Cursor指向行的对象
     *
     * @param cursor
     * @return
     */
    protected abstract T getCurrentCursorElement(Cursor cursor);

}
