package mno.mohamed_youssef.movie1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class PostersAdapter extends BaseAdapter {
    private Context mContext;
    private String[] posters;
    private LayoutInflater inflater;


    public PostersAdapter(Context c, String[] posters) {
        mContext = c;
        this.posters = posters;
        this.inflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return posters.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter



    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {


            convertView = inflater.inflate(R.layout.poster,parent,false);

        }

        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w130" + posters[position]).into((ImageView)convertView);


        return convertView;
    }


}