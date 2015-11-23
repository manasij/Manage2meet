package project.group.android.manage2meet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class GroupDisplay extends Activity{

    private Button createButton,addButton;
    private EditText groupNameField;
    private LinearLayout addLayout;
    public static final String GROUP_URL = "http://i.cs.hku.hk/~kasliwal/Android/group.php";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_groups);
        createButton = (Button) findViewById(R.id.createGroup);
        addButton = (Button) findViewById(R.id.addGroup);
        groupNameField = (EditText) findViewById(R.id.groupname);
        addLayout = (LinearLayout) findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createButton.setVisibility(View.VISIBLE);
                addLayout.setVisibility(View.VISIBLE);
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup();
                groupNameField.setText("");
                createButton.setVisibility(View.INVISIBLE);
                addLayout.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void createGroup() {
        String group_name = groupNameField.getText().toString().trim().toLowerCase();
        create(group_name, LoginActivity.username);
    }
    private void create(String group_name, String admin) {
        class Groups extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(GroupDisplay.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                if('s'==s.charAt(0)) {
                    Intent intent = new Intent(getBaseContext(), GroupDisplay.class);
                    startActivity(intent);
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap();
                data.put("group_name",params[0]);
                data.put("admin",params[1]);

                String result = sendPostRequest(GROUP_URL, data);

                return  result;
            }
        }

        Groups user = new Groups();
        user.execute(group_name, admin);
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
