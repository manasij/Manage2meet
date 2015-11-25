package project.group.android.manage2meet;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class ToDoList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_form_layout);
        Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();


    }
    public void add(View v){
      finish();

    }

    public void DatePickerDialog(View v)
    {
        //Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();
        DialogFragment newFragment = new DatePickerFrag();
        newFragment.show(getFragmentManager(), "datePicker");

    }

}
