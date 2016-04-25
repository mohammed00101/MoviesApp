package mno.mohamed_youssef.movie1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Map;
import java.util.zip.InflaterInputStream;

/**
 * Created by Mohamed Yossif on 21/04/2016.
 */
public class ReviewsAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater inflater;
    private String [] authordata , contentdata;

    public ReviewsAdapter(Context c,Map<String ,String> reviews) {
       this.mContext = c;
        this.authordata = reviews.keySet().toArray(new String [reviews.size()]);
        this.contentdata = reviews.values().toArray(new String [reviews.size()]);
       this.inflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return authordata.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        TextView author , content;
        LinearLayout reviews;
        if (convertView == null) {


            convertView = inflater.inflate(R.layout.reviews,parent,false);



        }

        author= (TextView)convertView.findViewById(R.id.author);
        content =(TextView)convertView.findViewById(R.id.content);
        author.setText(authordata[position]);
        content.setText(contentdata[position]);

        return convertView ;
    }
}
