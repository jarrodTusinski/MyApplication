package com.example.jarek.gps;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DropboxApi extends Activity{
    final static private String APP_KEY = "826jsfbz4tmngbz";
    final static private String APP_SECRET = "es3m71i8sf5ai11";
    AppKeyPair appKeys = new AppKeyPair(APP_KEY,APP_SECRET);
    final static private Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;
    private DropboxAPI<AndroidAuthSession> mDBApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropbox);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TYPE );
        mDBApi = new DropboxAPI<AndroidAuthSession> (session);
        mDBApi.getSession().startOAuth2Authentication(DropboxApi.this);
    }

    protected void onResume() {
        super.onResume();
        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                mDBApi.getSession().finishAuthentication();
                String accessToken = mDBApi.getSession().getOAuth2AccessToken();

            }
            catch (IllegalStateException e) {
                Log.i("DBAuthLog","ErrorAuthenticating",e);
            }
        }
    }

    public void SaveOnDropbox(View v) throws IOException, DropboxException {
        File sdCard = Environment.getExternalStorageDirectory();
        File file = new File(sdCard.getAbsolutePath() + "/MojePliki/mojplik.txt");
        FileInputStream inputStream = new FileInputStream(file);
        DropboxAPI.Entry response = mDBApi.putFile("/mojplik.txt", inputStream, file.length(), null, null);
        Log.i("DbExampleLog", "The uploaded file's rev is: " + response.rev);
    }

    public void SaveOnDevice(View v) throws IOException {
        FileOutputStream outputStream = null;
        try {
            File sdCard = Environment.getExternalStorageDirectory();
//            File file = new File(sdCard.getAbsolutePath() + "/MojePliki/mojplik.txt");
            File dir = new File(sdCard.getAbsolutePath() + "/MojePliki");
            dir.mkdir();
            File file = new File(dir, "mojplik.txt");
            outputStream = new FileOutputStream(file);
            @SuppressWarnings("unused")
            DropboxAPI.DropboxFileInfo info = mDBApi.getFile("/mojplik.txt", null, outputStream, null);
        }
        catch (Exception e) {
            System.out.println("Something went wrong " + e);
        }
        finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                }
                catch (IOException e) {
                    System.out.println("___" + e);
                }
            }
        }
    }
}