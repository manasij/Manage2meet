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

public class GroupDisplay extends Activity {

    public static String group_name;
    private Button addButton;
    public static final String GROUPLIST_URL = "http://i.cs.hku.hk/~kasliwal/Android/grouplist.php";
    ArrayList<String> list_items = new ArrayList<>();
    ArrayAdapter<String> groupListAdapter;
    ListView list;
    AdapterView.OnItemClickListener goToGroup;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_groups);
        displayList();
        addButton = (Button) findViewById(R.id.addGroup);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), GroupForm.class);
                startActivity(intent);
            }
        });
        groupListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list_items);
        goToGroup = new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView parent,
                                            View v,
                                            int position,
                                            long id) {
                        group_name = list_items.get(position);
                        Intent intent = new Intent(getBaseContext(), GroupPage.class);
                        startActivity(intent);
                    }
        };

    }
    private void displayList() {
        create(LoginActivity.username);
    }
    private void create(String username) {
        class Groups extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(GroupDisplay.this, "Please Wait",null, true, true);
            }

            String message;
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("Groups");
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jobject = jsonArray.getJSONObject(i);
                        message = jobject.getString("group_name");
                        list_items.add(message);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                list = (ListView) findViewById(R.id.listView);
                list.setAdapter(groupListAdapter);
                list.setOnItemClickListener(goToGroup);
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap();
                data.put("username",params[0]);
                String result = sendPostRequest(GROUPLIST_URL, data);

                return  result;
            }
        }

        Groups user = new Groups();
        user.execute(username);
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