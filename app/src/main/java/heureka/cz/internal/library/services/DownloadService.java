package heureka.cz.internal.library.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import heureka.cz.internal.library.R;
import heureka.cz.internal.library.helpers.Config;
import heureka.cz.internal.library.helpers.Download;
import heureka.cz.internal.library.rest.interfaces.ApiInterface;
import heureka.cz.internal.library.ui.MainActivity;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Retrofit;

public class DownloadService extends IntentService {

    public static final String TYPE_BOOK ="book";
    public static final String TYPE_INTERNAL ="internal_book";

    public static final String KEY_ID = "book_id";
    public static final String KEY_NAME = "book_name";
    public static final String KEY_TYPE = "book_type";

    public DownloadService() {
        super("Download Service");
    }

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private int totalFileSize;

    private String bookId;
    private String bookName;
    private String type;

    @Override
    protected void onHandleIntent(Intent intent) {

        if(intent.getExtras() == null) {
            return;
        }

        bookId = intent.getExtras().getString(KEY_ID);
        bookName = intent.getExtras().getString(KEY_NAME);
        type = intent.getExtras().getString(KEY_TYPE);

        Log.d("TEST", "handle intent");
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Download")
                .setContentText(getResources().getText(R.string.downloading))
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        Log.d("TEST", "init download");
        initDownload();

    }

    private void initDownload(){

        Log.d("TEST", "init download");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.API_BASE_URL)
                .build();

        ApiInterface retrofitInterface = retrofit.create(ApiInterface.class);

        Call<ResponseBody> request = retrofitInterface.downloadInternalBook(bookId);
        if(TYPE_BOOK.equals(type)) {
            Log.d("TEST", Config.URL_DOWNLOAD_BOOK + ": " + bookId);
            request = retrofitInterface.downloadBook(Integer.parseInt(bookId));
        }

        try {
            downloadFile(request.execute().body());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadFile(ResponseBody body) throws IOException {

        Log.d("TEST", "download");
        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), bookName);
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            Download download = new Download();
            download.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {
                download.setCurrentFileSize((int) current);
                download.setProgress(progress);
                sendNotification(download);
                timeCount++;
            }
            output.write(data, 0, count);
        }
        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();

    }

    private void sendNotification(Download download){
        Log.d("TEST", "send notification");
        sendIntent(download);
        notificationBuilder.setProgress(100,download.getProgress(),false);
        notificationBuilder.setContentText("Downloading file "+ download.getCurrentFileSize() +"/"+totalFileSize +" MB");
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendIntent(Download download){
        Log.d("TEST", "send intent");
        Intent intent = new Intent(MainActivity.MESSAGE_PROGRESS);
        intent.putExtra("download",download);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(){
        Log.d("TEST", "download complet");
        Download download = new Download();
        download.setProgress(100);
        sendIntent(download);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setContentText(getResources().getText(R.string.downloaded) + " " + Environment.DIRECTORY_DOWNLOADS);
        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

}