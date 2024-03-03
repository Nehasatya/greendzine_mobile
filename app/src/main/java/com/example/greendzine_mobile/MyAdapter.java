package com.example.greendzine_mobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MyAdapter extends BaseAdapter implements Filterable {
    private ArrayList OriginalValues;
    private ArrayList DisplayedValue;

    public MyAdapter(ArrayList<HashMap<String, String>> a) {
        OriginalValues = a;
        DisplayedValue = a;
    }

    @Override
    public int getCount() {
        return DisplayedValue.size();
    }

    @Override
    public HashMap<String, String> getItem(int position) {
        return (HashMap<String, String>) DisplayedValue.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_adapter_item, parent, false);
        } else {
            result = convertView;
        }

        HashMap<String, String> item = getItem(position);

//        Setting emp.json in adapter
        ((TextView) result.findViewById(R.id.name)).setText("Name: "+item.get("Name"));
        ((TextView) result.findViewById(R.id.dob)).setText("DOB: "+item.get("DOB"));
        ((TextView) result.findViewById(R.id.role)).setText("Role: "+item.get("Role"));
        ((TextView) result.findViewById(R.id.emp_id)).setText("EMP ID: "+item.get("EMP ID"));

        return result;
    }

    @Override
//    For search feature
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                DisplayedValue = (ArrayList<HashMap<String,String>>)results.values;
                notifyDataSetChanged();
            }@Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<HashMap<String,String>> FilteredArrList = new ArrayList<>();

                if (OriginalValues == null) {
                    OriginalValues = new ArrayList<>(DisplayedValue);
                }

                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = OriginalValues.size();
                    results.values = OriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < OriginalValues.size(); i++) {
                        String name = ((HashMap<String,String>)OriginalValues.get(i)).get("Name");
                        if (name.toLowerCase().equals(constraint.toString())) {
                            FilteredArrList.add((HashMap<String,String>)OriginalValues.get(i));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}