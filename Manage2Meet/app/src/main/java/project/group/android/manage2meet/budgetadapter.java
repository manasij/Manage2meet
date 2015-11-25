package project.group.android.manage2meet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;

import java.util.ArrayList;

/**
 * Created by Manasi on 24-11-2015.
 */
public class budgetadapter extends BaseAdapter implements ListAdapter {
    private final Context context;
    private ArrayList<String> values=new ArrayList<String>(); ;


    public budgetadapter(Context context, ArrayList<String> values) {
        this.context = context;
        this.values=values;

    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int pos) {
        return values.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item, parent, false);
        ImageButton button=(ImageButton)rowView.findViewById(R.id.delete);
        EditText username= (EditText)rowView.findViewById(R.id.username);
        EditText amount= (EditText)rowView.findViewById(R.id.amount);
        EditText description= (EditText)rowView.findViewById(R.id.description);
        username.setHint(values.get(position));
        amount.setHint(" Enter amount");
        description.setHint("Enter the description");


        return rowView;
    }



}
