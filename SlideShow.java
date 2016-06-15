package in.ac.iitk.dpf_download;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class SlideShow extends FragmentActivity {

    ViewPager mViewPager;
    CustomPagerAdapter mCustomPagerAdapter;
    public int[] mResources = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c
    };
    Timer swipeTimer = new Timer();
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);

        mCustomPagerAdapter = new CustomPagerAdapter(this);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mCustomPagerAdapter);

        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count == mResources.length){
                            count = 0;
                        }
                        mViewPager.setCurrentItem(count++, true);
                    }
                });
            }
        }, 500, 1000);
    }

    public class CustomPagerAdapter extends PagerAdapter {

        Context ctx;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context){
            ctx = context;
            mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {

            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public void destroyItem(ViewGroup container, int pos, Object object){
            container.removeView((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Integer i = position%2 + 1;

            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            String diskState = Environment.getExternalStorageState();


            if (diskState.equals("mounted")){
                File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File pictureView = new File("/storage/emulated/0/Android/data/in.ac.iitk.dpf_download/files/Pictures" + "/logo-" + i.toString() + ".png");

//            Bitmap bitmapDiaplay = null;

                if (pictureView.exists()){

//                Toast.makeText(MainActivity.this, "Atleast it somewhat works", Toast.LENGTH_LONG).show();

                    Bitmap bitmapDisplay = BitmapFactory.decodeFile(pictureView.toString());
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bitmapDisplay);
                } else {
                    Toast.makeText(SlideShow.this, pictureFolder.toString(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SlideShow.this, "Disk not mounted", Toast.LENGTH_SHORT).show();
            }


            container.addView(itemView);

            return itemView;
        }
    }



}
