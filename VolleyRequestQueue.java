package in.ac.iitk.dpf_download;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by siddarth on 23/5/16.
 */
public class VolleyRequestQueue {
    private static VolleyRequestQueue instanceOfClass;
    private static Context ctx;
    private RequestQueue myRequestQueue;
    private ImageLoader imageLoading;

    private VolleyRequestQueue(Context context){
        ctx = context;
        myRequestQueue = getRequestQueue();

        imageLoading = new ImageLoader(myRequestQueue,
                new ImageLoader.ImageCache() {

                    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                }
        );
    }

    private RequestQueue getRequestQueue() {
        if(myRequestQueue == null){
            Cache cache = new DiskBasedCache(ctx.getCacheDir(), 10*1024*1024);
            Network network = new BasicNetwork(new HurlStack());
            myRequestQueue = new RequestQueue(cache, network);

            myRequestQueue.start();
        }
        
        return myRequestQueue;
    }


    public static synchronized VolleyRequestQueue getInstance(Context applicationContext) {
        if(instanceOfClass == null){
            instanceOfClass = new VolleyRequestQueue(applicationContext);
        }
        return instanceOfClass;
    }

    public ImageLoader getImageLoader() {
        return imageLoading;
    }


    public <T> void addToRequestQueue(Request<T> req) {
//        req.setTag(TAG);
        getRequestQueue().add(req);
    }
}
