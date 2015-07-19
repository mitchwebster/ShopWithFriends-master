package com.example.mitchwebster.shopwithfriends;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.example.mitchwebster.shopwithfriends.Models.Tag;

import java.util.ArrayList;
import java.util.List;


public class TagAdapter extends ArrayAdapter<String> implements Filterable {
    private final Activity context; //holds context
    private List<String> tags; //holds users

    static class ViewHolder {
        public TextView tagName;
    }

    /**
     * Construct a new HoldingArrayAdapter
     * @param context, Application context
     * @param us, List of users
     */
    public TagAdapter(Activity context, List<String> us) {
        super(context, R.layout.row_layout, us); //get layout
        this.context = context; //take in a list and context
        this.tags = us;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.tag_layout, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tagName = (TextView) rowView.findViewById(R.id.tagText);

            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        String t = tags.get(position);
        holder.tagName.setText(t);
        return rowView;
    }

    /**
     * method for getting the tag
     * @param p location of tag clicked
     * @return String with tag
     */
    public String get(int p) {
        if (p >= 0 && p < tags.size()) {
            return tags.get(p);
        }
        return null;
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public void remove(String s) {
        tags.remove(s);
        notifyDataSetChanged();
    }

    @Override
    public void add(String s) {
        if (!tags.contains(s)) {
            tags.add(s);
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<String> FilteredArrayNames = new ArrayList<>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (Tag t : Tag.values()) {
                    if (t.toString().toLowerCase().startsWith(constraint.toString()))  {
                        FilteredArrayNames.add(t.toString());
                    }
                }
                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //noinspection unchecked
                tags = (List<String>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}

