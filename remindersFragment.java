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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by siddarth on 14/6/16.
 */
public class remindersFragment extends Fragment {
    TextView reminder;
    String ip = "http://10.42.0.157";
    String download_url = ip + "/uploading/getReminder.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup content, Bundle savedInstanceState){
        return inflater.inflate(R.layout.rem_fragment, content, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        reminder = (TextView) getActivity().findViewById(R.id.textView);

//        Toast.makeText(getActivity(), "does it work", Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(download_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getActivity(), "abcdefh", Toast.LENGTH_LONG).show();
                JSONObject remind = null;
                try {
                    remind = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error", "error");
                }

                try {
                    JSONArray arr = remind.getJSONArray("Reminder");
                    Integer size = arr.length();
                    JSONObject temp;

                    temp = arr.getJSONObject(0);
                    reminder.setText(temp.getString("note"));

                    for (int i = 1; i<size; i++) {
                        reminder.append("\n");
                        temp = arr.getJSONObject(i);
                        reminder.append(temp.getString("note"));
                    }

                } catch (JSONException e) {
                    Log.e("JSON", "JSON");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley", "ball");
            }
        });

        VolleyRequestQueue.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }

}
