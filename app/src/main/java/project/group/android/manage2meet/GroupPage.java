package project.group.android.manage2meet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GroupPage extends Activity {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Button members = (Button) findViewById(R.id.members);
        members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Members.class);
                startActivity(intent);
            }
        });
        Button todo = (Button) findViewById(R.id.todo);
        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Todo.class);
                startActivity(intent);
            }
        });
        Button budget = (Button) findViewById(R.id.budget);
        budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Budget.class);
                startActivity(intent);
            }
        });
        Button loctime = (Button) findViewById(R.id.locationTime);
        loctime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LocationTime.class);
                startActivity(intent);
            }
        });
    }
}
