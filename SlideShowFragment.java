package in.ac.iitk.dpf_download;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by siddarth on 9/6/16.
 */
public class SlideShowFragment extends Fragment{
    ViewPager mViewPager;
    CustomPagerAdapter mCustomPagerAdapter;
    public int[] mResources = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c
    };
    Timer swipeTimer = new Timer();
    int count = 0;

    MainActivity testing;

    JSONObject obj = null;

    Integer arrSize;
    static Bundle s;

    public static SlideShowFragment newInstance(Bundle b){
        SlideShowFragment f = new SlideShowFragment();
        f.setArguments(b);

        s = b;

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_slide_show, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if (s!=null){
            String f = s.getString("json");
            try {
                obj = new JSONObject(f);
//                Toast.makeText(getActivity(), obj.toString(), Toast.LENGTH_SHORT).show();
                arrSize = obj.getJSONArray("file").length();
//                Toast.makeText(getActivity(), arrSize.toString(), Toast.LENGTH_SHORT).show();

                mCustomPagerAdapter = new CustomPagerAdapter(getActivity());
                mViewPager = (ViewPager) getActivity().findViewById(R.id.view_pager);
                mViewPager.setAdapter(mCustomPagerAdapter);

                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (count == arrSize){
                                    count = 0;
                                }
                                mViewPager.setCurrentItem(count++, true);
                            }
                        });
                    }
                }, 500, 1000);

            } catch (JSONException e) {
//                Toast.makeText(getActivity(), "abcdefghijk", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public class CustomPagerAdapter extends PagerAdapter {

        Context ctx;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            ctx = context;
            mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return arrSize;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public void destroyItem(ViewGroup container, int pos, Object object) {
            container.removeView((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Integer i = null;
            try {
                JSONArray j = obj.getJSONArray("file");
                i = j.getJSONObject(position).getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            String diskState = Environment.getExternalStorageState();


            if (diskState.equals("mounted")) {
                File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File pictureView = new File("/storage/emulated/0/Android/data/in.ac.iitk.dpf_download/files/Pictures/"  + i.toString() + ".png");

//            Bitmap bitmapDiaplay = null;

                if (pictureView.exists()) {

//                Toast.makeText(MainActivity.this, "Atleast it somewhat works", Toast.LENGTH_LONG).show();

                    Bitmap bitmapDisplay = BitmapFactory.decodeFile(pictureView.toString());
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bitmapDisplay);
                } else {
                    Toast.makeText(getActivity(), pictureFolder.toString(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Disk not mounted", Toast.LENGTH_SHORT).show();
            }


            container.addView(itemView);

            return itemView;
        }
    }



}

