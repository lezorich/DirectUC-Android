package com.directuc.android.com.directuc.android.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;

import java.util.Queue;

/**
 * Created by lukas on 02-04-14.
 */
public class WebCursosDownloader implements DownloadListener {

    private long enqueue;
    private DownloadManager mDownloadManager;
    private Activity mContext;


    public WebCursosDownloader(Activity context) {

        mContext = context;

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor cursor = mDownloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);

                        if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                            String nombre = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                            String descr = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));
                            String mimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));

                            Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(DownloadManager
                                    .COLUMN_LOCAL_URI)));
                            String path = uri.getPath();
                            long length = Long.getLong(cursor.getString(cursor.getColumnIndex(DownloadManager
                                    .COLUMN_TOTAL_SIZE_BYTES)), -1);

                            Log.d("nombre", nombre);
                            Log.d("mimetype", mimeType);
                            Log.d("path", path);
                            Log.d("length", "" + length);
                            Log.d("descr", descr);

                            mDownloadManager.addCompletedDownload(nombre, " ", true, mimeType, path, 10, true);
                            /*String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager
                                    .COLUMN_LOCAL_URI));

                            Uri uri = Uri.parse(uriString);
                            Intent i = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            mContext.startActivity(i);*/
                        }
                    }
                }
            }
        };

        mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                long contentLength) {

        mDownloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        CookieManager cookieManager = CookieManager.getInstance();
        String cookie = cookieManager.getCookie(url);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.addRequestHeader("Cookie", cookie);
        enqueue = mDownloadManager.enqueue(request);
    }
}
