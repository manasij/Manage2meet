package project.group.android.manage2meet;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Members extends Activity {

    private Button addButton;
    public static final String MEMBERLIST_URL = "http://i.cs.hku.hk/~kasliwal/Android/memberlist.php";
    ArrayList<String> list_items = new ArrayList<>();
    ArrayAdapter<String> memberListAdapter;
    ListView list;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_layout);
        displayList();
        addButton = (Button) findViewById(R.id.addMember);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MembersForm.class);
                startActivity(intent);
            }
        });
        memberListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list_items);

    }
    private void displayList() {
        create(GroupDisplay.group_name);
    }
    private void create(String group_name) {
        class Member extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Members.this, "Please Wait",null, true, true);
            }

            String message;
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("Members");
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jobject = jsonArray.getJSONObject(i);
                        message = jobject.getString("username");
                        list_items.add(message);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                list = (ListView) findViewById(R.id.listView);
                list.setAdapter(memberListAdapter);
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap();
                data.put("group_name",params[0]);
                String result = sendPostRequest(MEMBERLIST_URL, data);

                return  result;
            }
        }

        Member user = new Member();
        user.execute(group_name);
    }
    public String sendPostRequest(String requestURL,
                                  HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = br.readLine();
            }
            else {
                response="Error Registering";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}