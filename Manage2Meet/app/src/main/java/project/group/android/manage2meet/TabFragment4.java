package project.group.android.manage2meet;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;


public class TabFragment4 extends ListFragment {
    ArrayList<String> listItems;
    budgetadapter adapter;
    View inflatedView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflatedView=inflater.inflate(R.layout.fragment_tab_fragment4, container, false);
        listItems= new ArrayList<>();
        listItems.add("Enter your username");
        adapter= new budgetadapter(getActivity(),listItems);
        setListAdapter(adapter);
        Button newrow= (Button)inflatedView.findViewById(R.id.newrow);
        newrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItems.add("Enter your username");
                adapter.notifyDataSetChanged();

            }
        });

       return inflatedView;
    }
}