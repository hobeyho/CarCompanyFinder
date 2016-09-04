package com.kwontaehoon.carcompanyfinder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btnHelp;
    Button btnPhoto;
    Button btnUpload;
    ImageView imageViewPhoto;
    private String selectedImagePath = "";
    final private int PICK_IMAGE = 1;
    final private int CAPTURE_IMAGE = 2;
    String imgPath = "";
    String imgName = "";
    File file;
    int serverResponseCode = 0;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHelp=(Button) findViewById(R.id.btnHelp);
        btnUpload=(Button) findViewById(R.id.btnUpload);
        btnPhoto=(Button) findViewById(R.id.btnPhoto);
        imageViewPhoto=(ImageView) findViewById(R.id.imageViewPhoto);

        btnUpload.setEnabled(false);


        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHelp();
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                imageCapturing();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                classify();
            }
        });


    }

    public void viewHelp(){
        Activity fromActivity = MainActivity.this;
        Class toActivity = HelpActivity.class;
        Intent intent = new Intent (fromActivity, toActivity);
        startActivity(intent);
    }
    public void imageCapturing() {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
        startActivityForResult(intent, CAPTURE_IMAGE);
    }

    public void classify(){

        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run(){
                try{
                    VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
                    service.setApiKey("a6b43b1903b1deb5557398b4b0a47ffdddc8cbd6");

                    ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
                            .images(file)
                            .classifierIds("carlogo_custom_1959643290")
                            .build();
                    VisualClassification result = service.classify(options).execute();
                    Log.d("debugging","result:"+result);
                    startActivityFromMainThread(result.toString());
                } catch (Exception e){
                    e.getStackTrace();

                }
            }
        });
        thread.start();

    }
    public void startActivityFromMainThread(String result){
        Activity fromActivity = MainActivity.this;
        Class toActivity = ResultActivity.class;
        Intent intent2 = new Intent (fromActivity, toActivity);
        intent2.putExtra("result", result);
        startActivity(intent2);

    }


    public void uploadImage(){
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        try{

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            URL url = new URL("https://gateway-a.watsonplatform.net/visual-recognition/api/v3/classify?api_key=a6b43b1903b1deb5557398b4b0a47ffdddc8cbd6&version=2016-05-20");
            conn= (HttpURLConnection) url.openConnection();
            FileInputStream fileInputStream = new FileInputStream(file);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("images_file", imgPath);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"images_file\"; filename = \"" + imgName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);


            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);


            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG,100,bos);
            byte[] pixels = bos.toByteArray();
            dos.write(pixels);


            if(serverResponseCode == 200){

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) !=null) {
                    sb.append(output);
                }
                Log.d("debugging", sb.toString());

                runOnUiThread(new Runnable() {
                    public void run() {

                        String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                +" http://www.androidexample.com/media/uploads/"
                                +imgName;


                        Toast.makeText(getApplicationContext(), "File Upload Complete.",
                                Toast.LENGTH_SHORT).show();


                    }
                });
            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {

            ex.printStackTrace();

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "MalformedURLException",
                            Toast.LENGTH_SHORT).show();
                }
            });

            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {

            e.printStackTrace();

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Got Exception : see logcat ",
                            Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("Debugging", "Exception : "
                    + e.getMessage(), e);
        }
        Log.d("Debugging", "responseCode:" + serverResponseCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            btnUpload.setEnabled(true);
            if (requestCode == PICK_IMAGE) {
                selectedImagePath = getAbsolutePath(data.getData());
                imageViewPhoto.setImageBitmap(decodeFile(selectedImagePath));
            } else if (requestCode == CAPTURE_IMAGE) {
                selectedImagePath = getImagePath();
                imageViewPhoto.setImageBitmap(decodeFile(selectedImagePath));
                bmp = decodeFile(selectedImagePath);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }


    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 320;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getAbsolutePath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public Uri setImageUri() {
        // Store image in dcim
        imgName = "image" + new Date().getTime() + ".jpg";
        file = new File(Environment.getExternalStorageDirectory() + "/DCIM/" + imgName);
        Uri imgUri = Uri.fromFile(file);
        imgPath = file.getAbsolutePath();
        return imgUri;
    }


    public String getImagePath() {
        return imgPath;
    }
}
