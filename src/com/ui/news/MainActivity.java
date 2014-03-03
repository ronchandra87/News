package com.ui.news;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.global.news.*;
import com.server.connector.NewsHTTPSClient;

public class MainActivity extends Activity {

	GlobalData TheGlobalData ;
	ArrayList<ClsNews> arraylist = new ArrayList<ClsNews>();
	LazyAdapter adapter;
	ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		InternetConnection con = new InternetConnection(this);
		boolean b= con.isOnline();

		if(b == true)
		{
			setContentView(R.layout.activity_main);
			TheGlobalData	= (GlobalData)this.getApplication();
			setURL();
			list=(ListView)findViewById(R.id.list);

			/** Customized ActionBar */
			View customActionBar = getLayoutInflater().inflate(R.layout.action_barlayout,
					new LinearLayout(this), false);
			getActionBar().setCustomView(customActionBar);

			View refreshActionView = customActionBar.findViewById(R.id.action_refresh);   
			refreshActionView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					/** Handling click operation on Refresh button */
					ProgressTask task = new ProgressTask(MainActivity.this, TheGlobalData);
					task.execute();
				}
			});   

			try
			{
				ProgressTask task = new ProgressTask(this, TheGlobalData);
				task.execute();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();

			}

			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub

					/**Fetching the website url and navigating to the new Activity  */
					System.out.println("size " + arraylist.size());
					ClsNews obj_news= (ClsNews) arraylist.get(arg2);
					System.out.println("Main Activity " + obj_news.tinyUrl);
					TheGlobalData.setwebsiteURL(obj_news.tinyUrl);
					Intent intent = new Intent(MainActivity.this, ShowWebView.class);
					startActivity(intent);
				}
			});
		}
		else
		{
			 AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		        builder.setMessage("Internet Connection not available!").setIcon(R.drawable.no).setTitle("Connection Error") .setCancelable(
		                false).setPositiveButton("Quit",
		                        new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int id) { 
		                        Intent startMain = new Intent(Intent.ACTION_MAIN);
		                        startMain.addCategory(Intent.CATEGORY_HOME);
		                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		                        startActivity(startMain);
		                    }
		                });
		        AlertDialog alert = builder.create();
		        alert.show();
		}
	}

	public void loadData()
	{
		// Getting adapter by passing ArrayList
		 adapter=new LazyAdapter(this, arraylist);        
	     list.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	
	private void setURL()
	{
		// This function is used to set the URL of the web service		
		TheGlobalData.setserverURL("http://mobilatr.mob.f2.com.au/services/views/9.json");
	}

	
	@SuppressLint("NewApi")
	private void ConnectToServer()
	{
		try
		{
			TheGlobalData	= (GlobalData)this.getApplication();
			HttpClient httpclient = new NewsHTTPSClient();
			HttpGet httpget = new HttpGet(TheGlobalData.getserverURL()); 
			
			httpclient	= ((NewsHTTPSClient)httpclient).sslClient(httpclient);
			
			// Execute the request
			HttpResponse response;
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release
	
			if (entity != null) {
	
				// A Simple JSON Response Read
				InputStream instream  = entity.getContent();
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
				
				String str_data= reader.readLine();
	
				dataFromServer(str_data);
									
				// Closing the input stream will trigger connection release
				instream.close();
		}
		}catch (ClientProtocolException e)
		{
			e.printStackTrace();
		} catch (IOException e1)
		{
			e1.printStackTrace();
		} 
	}
	
	private void dataFromServer(String str_serverData)
	{
		ClsNews news_obj[]= null;
		try
		{
			  // CONVERT RESPONSE STRING TO JSON ARRAY
			
			JSONObject jObject = new JSONObject(str_serverData);
			JSONArray ja = jObject.getJSONArray("items");
           
            // ITERATE THROUGH AND RETRIEVE CLUB FIELDS
            int n = ja.length();
            
            news_obj = new ClsNews[n];
            
           
            for (int i = 0; i < n; i++) {
            	
            	news_obj[i] = new ClsNews();
                // GET INDIVIDUAL JSON OBJECT FROM JSON ARRAY
                JSONObject jo = ja.getJSONObject(i);
                 
                news_obj[i].identifier = jo.getInt("identifier");
                news_obj[i].type = jo.getString("type");
                news_obj[i].headLine = jo.getString("headLine");
                news_obj[i].slugLine = jo.getString("slugLine");
                 
                String thumbnail = jo.getString("thumbnailImageHref");
                if(thumbnail.isEmpty() || thumbnail.equals("null") )
                {
                	news_obj[i].thumbnailImageHref="!";
                }
                else{
                	news_obj[i].thumbnailImageHref = jo.getString("thumbnailImageHref");
                	
                }
              
                news_obj[i].webHref = jo.getString("webHref");
                System.out.println("Tiny url " + jo.getString("tinyUrl"));
                news_obj[i].tinyUrl = jo.getString("tinyUrl");
                news_obj[i].dateLine=jo.getString("dateLine");
             
                arraylist.add(news_obj[i]);
            }
           
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	/** Async Task for loading data from the server i.e not on UI Thread */
	class ProgressTask extends AsyncTask<String, Void, Boolean> {
		
		
	    public ProgressTask(MainActivity activity, GlobalData data) {
	        this.activity = activity;
	        context = activity;
	        this.data = data;
	        dialog = new ProgressDialog(context);
	    }

	    /** progress dialog to show user that the backup is processing. */
	    private ProgressDialog dialog;
	    /** application context. */
	    private MainActivity activity;
	    private Context context;
	    private GlobalData data;
	    
	    protected void onPreExecute() {
	        this.dialog.setMessage("Loading Data...");
	        this.dialog.show();
	    }

	    protected Boolean doInBackground(final String... args) {
	       try{   
	    	  activity.ConnectToServer();
	          return true;
	       } catch (Exception e){
	          Log.e("tag", "error", e);
	          return false;
	       }
	    }
	    @Override
	    protected void onPostExecute(final Boolean success) {
	        if (dialog.isShowing()) {
	            dialog.dismiss();       
	        }
	   activity.loadData();
	    }
	}
}
