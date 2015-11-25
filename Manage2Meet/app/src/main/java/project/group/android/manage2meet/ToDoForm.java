package project.group.android.manage2meet;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ToDoForm extends Activity{


    private Button add;
    private EditText todoField,deadlineField,memberField,statusField;
    //TODO_FORM
    public static final String TODO_FORM = "http://i.cs.hku.hk/~kasliwal/Android/todo.php";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_form_layout);
        add = (Button) findViewById(R.id.add);
        todoField = (EditText) findViewById(R.id.todoEdit);
        deadlineField = (EditText) findViewById(R.id.deadlineEdit);
        memberField = (EditText) findViewById(R.id.memberEdit);
        statusField = (EditText) findViewById(R.id.statusEdit);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createToDo();
            }
        });
    }
    private void createToDo() {

        create(todoField.getText().toString().trim().toLowerCase(),
                deadlineField.getText().toString().trim(),
                    memberField.getText().toString().trim().toLowerCase(),
                         statusField.getText().toString().trim().toLowerCase());
    }


    private void create(String todo, String deadline, String member,String status) {
        class Groups extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ToDoForm.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                if('s'==s.charAt(0)) {
                    Intent intent = new Intent(getBaseContext(), tab_main.class);
                    startActivity(intent);
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap();
                data.put("todoField",params[0]);
                data.put("deadlineField",params[1]);
                data.put("memberField",params[2]);
                data.put("statusField",params[3]);
                String result = sendPostRequest(TODO_FORM, data);

                return  result;
            }
        }//end of Async Task

        //Groups class extends AsyncTask
        Groups user = new Groups();
        user.execute(todo,deadline,member,status);
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



}//end of ToDoForm
