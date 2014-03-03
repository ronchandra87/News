package com.ui.news;

import java.util.ArrayList;

import com.global.news.ClsNews;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<ClsNews> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, ArrayList<ClsNews> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        ClsNews o = new ClsNews();
        o = data.get(position);
        // Setting all values in listview
        if(convertView==null){
        	 
              if(o.thumbnailImageHref.equals("!"))
              {
            	  vi = inflater.inflate(R.layout.list_row_noimg, null);
            	  TextView headline = (TextView)vi.findViewById(R.id.headline); // headline
                  TextView slugLine = (TextView)vi.findViewById(R.id.slugLine); // slugline    
                  
                  headline.setText(o.headLine);
                  slugLine.setText(o.slugLine);
              }
              else{
            	  vi = inflater.inflate(R.layout.list_row, null);
            	  TextView headline = (TextView)vi.findViewById(R.id.headline); // headline
                  TextView slugLine = (TextView)vi.findViewById(R.id.slugLine); // slugline    
                  ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
                  headline.setText(o.headLine);
                  slugLine.setText(o.slugLine);
                  
                  imageLoader.DisplayImage(o.thumbnailImageHref, thumb_image);
              }
              		
        }
        else
        {
        	if(o.thumbnailImageHref.equals("!"))
            {
        		// vi = inflater.inflate(R.layout.list_row_noimg, null);
          	  TextView headline = (TextView)vi.findViewById(R.id.headline); // headline
              TextView slugLine = (TextView)vi.findViewById(R.id.slugLine); // slugline    
                
              headline.setText(o.headLine);
              slugLine.setText(o.slugLine);
              
             
            }
            else{
            	// vi = inflater.inflate(R.layout.list_row, null);
          	  TextView headline = (TextView)vi.findViewById(R.id.headline); // headline
              TextView slugLine = (TextView)vi.findViewById(R.id.slugLine); // slugline    
              ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
              headline.setText(o.headLine);
              slugLine.setText(o.slugLine);
              if(thumb_image != null)
            	  imageLoader.DisplayImage(o.thumbnailImageHref, thumb_image);
             
            }
      
        }
       
        return vi;
    }
}