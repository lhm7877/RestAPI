package com.hoomin.restapi;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Hooo on 2017-01-26.
 */

public class Provider extends ContentProvider {
    static final Uri CONTENT_URI = Uri.parse("content://com.hoomin.Alarm/ResponseBody");
    static final int ALLWORD = 1;
    static final int ONEWORD = 2;
    static final UriMatcher Matcher;
    private Realm realm;
    static final String[] sColumns = new String[]{"id","name","full_name"};


    static {
        Matcher = new UriMatcher(UriMatcher.NO_MATCH);
        Matcher.addURI("com.hoomin.Alarm", "ResponseBody", ALLWORD);
        Matcher.addURI("com.hoomin.Alarm", "ResponseBody/*", ONEWORD);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        realm.beginTransaction();
        try {
            for(ContentValues value : values){
                ResponseBody responseBody = realm.createObject(ResponseBody.class);
                responseBody.setId(value.getAsInteger(sColumns[0]));
                responseBody.setName(value.getAsString(sColumns[1]));
                responseBody.setFull_name(value.getAsString(sColumns[2]));
            }
        }finally {
            realm.commitTransaction();
        }
        return values.length;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        realm = Realm.getDefaultInstance();
        RealmQuery<ResponseBody> query = realm.where(ResponseBody.class);
        RealmResults<ResponseBody> results = query.findAll();
        MatrixCursor matrixCursor = new MatrixCursor(sColumns);
        for(ResponseBody item : results){
            Object[] rowData= new Object[]{item.getId(),item.getName(),item.getFull_name()};
            matrixCursor.addRow(rowData);
        }

        return matrixCursor;
    }


    //TODO: getType이 왜 필요한지
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        realm.beginTransaction();
        ResponseBody responseBody = realm.createObject(ResponseBody.class);
        responseBody.setId(values.getAsInteger(sColumns[0]));
        responseBody.setName(values.getAsString(sColumns[1]));
        responseBody.setFull_name(values.getAsString(sColumns[2]));
        realm.commitTransaction();
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.withAppendedPath(uri, String.valueOf(responseBody.getId()));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
//        switch (Matcher.match(uri)){
//            case ALLWORD:
////                realm.
//                realm.deleteAll();
//                count = 1;
//                break;
//            case ONEWORD:
//
//                String where = "eng = '" + uri.getPathSegments().get(1) + "'";
//                if (TextUtils.isEmpty(selection) == false){
//                    where += " AND" + selection;
//                }
//                count = db.deelte("dic", where, selectionArgs);
//                realm.delete();
//                break;
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
