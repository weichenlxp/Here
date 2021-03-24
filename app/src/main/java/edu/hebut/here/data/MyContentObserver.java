package edu.hebut.here.data;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;

public class MyContentObserver extends ContentObserver {
    private Cursor cursor = null;
    private Context mContext;

    public MyContentObserver(Handler handler) {
        super(handler);
    }

    public MyContentObserver(Handler handler, Context context) {
        super(handler);
        mContext = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

    }
}
