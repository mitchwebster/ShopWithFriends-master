package com.example.mitchwebster.shopwithfriends;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mitchwebster.shopwithfriends.Models.Product;
import java.util.List;


public class ProductAdapter extends ArrayAdapter<Product> {
    private final Activity context; //holds context
    private final List<Product> products; //holds users

    static class ViewHolder {
        public TextView title; //create a holder for our view
        public TextView desc;
        public TextView price;
    }

    /**
     * Construct a new HoldingArrayAdapter
     * @param context, Application context
     * @param prods, List of users
     */
    public ProductAdapter(Activity context, List<Product> prods) {
        super(context, R.layout.row_layout, prods); //get layout
        this.context = context; //take in a list and context
        this.products = prods;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.product_layout, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) rowView.findViewById(R.id.product_title);
            viewHolder.desc = (TextView) rowView.findViewById(R.id.product_description);
            viewHolder.price = (TextView) rowView.findViewById(R.id.product_price);

            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        Product p = products.get(position);
        holder.title.setText(p.getTitle());
        holder.desc.setText(p.getDescription());
        holder.price.setText("$" + p.getPrice());


//        holder.image.setBackgroundColor(0x0000ff);

        return rowView;
    }
}

