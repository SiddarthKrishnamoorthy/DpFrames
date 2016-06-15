package in.ac.iitk.dpf_download;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by siddarth on 9/6/16.
 */
public class TemperatureFragment extends Fragment {

    TextView test;
    String download_url = "http://api.openweathermap.org/data/2.5/weather?q=Kanpur&appid=d2c631d5dea854eb3b95317435053147";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup content, Bundle savedInstanceState){
        return inflater.inflate(R.layout.temperature_fragment, content, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        test = (TextView) getActivity().findViewById(R.id.textBox);

        StringRequest stringRequest = new StringRequest(download_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject temperature = null;
                try {
                    temperature = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    test.setText("City : " + temperature.getString("name"));
                    test.append("\n");
                    test.append("Temperature : " + temperature.getJSONObject("main").getString("temp"));
                    test.append("\n" + "Pressure : " + temperature.getJSONObject("main").getString("pressure"));
                    test.append("\n" + "Humidity : " + temperature.getJSONObject("main").getString("humidity"));
                    test.append("\n" + "Weather : " + temperature.getJSONArray("weather").getJSONObject(0).getString("description"));
                } catch (JSONException e) {
                    Log.e("JSON", "JSON");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleyRequestQueue.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }
}
