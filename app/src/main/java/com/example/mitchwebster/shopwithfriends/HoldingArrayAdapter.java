package com.example.mitchwebster.shopwithfriends;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mitchwebster.shopwithfriends.Models.User;

import java.text.DecimalFormat;
import java.util.List;


public class HoldingArrayAdapter extends ArrayAdapter<User> {
    private final Activity context; //holds context
    private final List<User> users; //holds users

    static class ViewHolder {
        public TextView name; //create a holder for our view
        public TextView email;
        public TextView rating;
        // --Commented out by Inspection (3/24/2015 9:13 PM):public ImageView image;
    }

    /**
     * Construct a new HoldingArrayAdapter
     * @param context, Application context
     * @param us, List of users
     */
    public HoldingArrayAdapter(Activity context, List<User> us) {
        super(context, R.layout.row_layout, us); //get layout
        this.context = context; //take in a list and context
        this.users = us;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.row_layout, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) rowView.findViewById(R.id.user_name_list);
            viewHolder.rating = (TextView) rowView.findViewById(R.id.user_rating_list);
            viewHolder.email = (TextView) rowView.findViewById(R.id.user_email_list);
            //viewHolder.image = (ImageView) rowView.findViewById(R.id.icon);

            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        User u = users.get(position);
        holder.name.setText(u.getFullName());
        holder.email.setText("" + u.getEmail());
        holder.rating.setText(new DecimalFormat("#.##").format(u.getRating()) + "/5");


//        holder.image.setBackgroundColor(0x0000ff);

        return rowView;
    }
}

