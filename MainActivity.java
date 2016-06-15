package in.ac.iitk.dpf_download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    public  static  final  String j="json";
    public  String k;

    private Button buttonGetImage;
    private ImageView networkImageView;
    private ImageLoader imageLoader;
    String ip = "http://10.42.0.157";
    String download_url = ip + "/uploading/getData.php";
    String url = "http://172.20.181.98/uploading/upload/3.png";
    DownloadManager downloadManager;
    private long downloadReference;
    private BroadcastReceiver downloadComplete, notificationClicked;
    Button slideShow;
    public JSONObject jsonObject;
    public static String json = "{\"file_id\" : 3}";
    SlideShowFragment f;
    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
    Bundle args;
    String string = "abcd";

    Button tempButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkImageView = (ImageView) findViewById(R.id.imageView);
//        buttonGetImage = (Button) findViewById(R.id.buttonGetImage);
//        tempButton = (Button) findViewById(R.id.testJson);
//        slideShow = (Button) findViewById(R.id.slideShow);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

/*
        slideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSlideShow(v);
            }
        });
*/

        downloadPictures();


        /*buttonGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               downloadPictures();
            }
        });

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);

                request.setDescription("Download Image");
                request.setTitle("MCBC");

                request.setDestinationInExternalFilesDir(MainActivity.this, Environment.DIRECTORY_PICTURES, "0.png");
                request.allowScanningByMediaScanner();

                downloadReference = downloadManager.enqueue(request);

            }
        });*/


    }

    private void getBitmapFromFile() {
        String MEDIA_MOUNTED = "mounted";
        String diskState = Environment.getExternalStorageState();

        if (diskState.equals(MEDIA_MOUNTED)){
            File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File pictureView = new File("/storage/emulated/0/Android/data/in.ac.iitk.dpf_download/files/Pictures" + "/logo-1.png");

//            Bitmap bitmapDiaplay = null;

            if (pictureView.exists()){

//                Toast.makeText(MainActivity.this, "At least it somewhat works", Toast.LENGTH_LONG).show();

                Bitmap bitmapDisplay = BitmapFactory.decodeFile("/storage/emulated/0/Android/data/in.ac.iitk.dpf_download/files/Pictures" + "/logo-1.png");
                networkImageView.setVisibility(View.VISIBLE);
                networkImageView.setImageBitmap(bitmapDisplay);
            } else {
                Toast.makeText(MainActivity.this, pictureFolder.toString(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(MainActivity.this, "Disk not mounted", Toast.LENGTH_SHORT).show();
        }
    }

    public void startSlideShow(View view) {
        Intent intent = new Intent(this, SlideShow.class);
        startActivity(intent);
    }

    private void downloadPictures() {


        StringRequest stringRequest = new StringRequest(download_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.getInt("success") == 0) {
                        Toast.makeText(MainActivity.this, "No entries", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray result = jsonObject.getJSONArray("file");

                        Integer arrSize = result.length();
                        ArrayList<Integer> pic_id = new ArrayList<>(arrSize);
                        JSONObject picId;
                        int s;
                        for (int i = 0; i < arrSize; i++) {
                            picId = result.getJSONObject(i);
                            pic_id.add(picId.getInt("id"));
                            s = pic_id.get(i) - 1;

                            url = ip + "/uploading/upload/" + s + ".png";

                            Uri uri = Uri.parse(url);
                            DownloadManager.Request request = new DownloadManager.Request(uri);

                            request.setDescription("Download Image");
                            request.setTitle("MCBC");

                            request.setDestinationInExternalFilesDir(MainActivity.this, Environment.DIRECTORY_PICTURES, (pic_id.get(i)) + ".png");
                            request.allowScanningByMediaScanner();

                            downloadReference = downloadManager.enqueue(request);
                        }

                        json = jsonObject.toString();
                        args = new Bundle();
                        args.putString(j, json);
                        f = SlideShowFragment.newInstance(args);
                        f.setArguments(args);

//                    Toast.makeText(MainActivity.this, json, Toast.LENGTH_SHORT).show();

                        fm.beginTransaction().add(R.id.slideShowFragment, f).commit();

                    }


                } catch (JSONException e) {
                    Log.e("JSONException", "JSON Not Working");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("onErrorResponse", "you screwed up");
            }
        });

        VolleyRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        final IntentFilter filter_click = new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        notificationClicked = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String Id = DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS;
                long[] references = intent.getLongArrayExtra(Id);
                for (long reference : references){
                    if(reference == downloadReference){
                        //Do something
                    }
                }

            }
        };

        final IntentFilter filter_dwnld = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        downloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (reference == downloadReference) {
                    //do something with the download file
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(reference);
                    Cursor cursor = downloadManager.query(query);

                    cursor.moveToFirst();

                }
            }
        };
    }

}
