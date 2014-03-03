package com.ui.news;


import com.global.news.GlobalData;
import com.global.news.InternetConnection;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class ShowWebView extends Activity {
	WebView webview;
	int count;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		InternetConnection con = new InternetConnection(this);
		boolean b = con.isOnline();
		if(b== true)
		{
			getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_show_web_view);

			webview = (WebView)findViewById(R.id.webView1);
			GlobalData TheGlobal = (GlobalData) this.getApplication();
			System.out.println("The Global " + TheGlobal.getwebsiteURL());
			startWebView1(TheGlobal.getwebsiteURL()); 

		}
		else
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(ShowWebView.this);
			builder.setMessage("Internet Connection not available!").setIcon(R.drawable.no).setTitle("Connection Error") .setCancelable(
					false).setPositiveButton("Quit",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) { 
							Intent startMain = new Intent(Intent.ACTION_MAIN);
							startMain.addCategory(Intent.CATEGORY_HOME);
							startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(startMain);
						}
					}).setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							onBackPressed();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_show_web_view, menu);
		return true;
	}
	 
	private void startWebView1(String url) {

		//Create new webview Client to show progress dialog
		//When opening a url or click on link
		

		webview.setWebViewClient(new WebViewClient() {      
	            ProgressDialog progressDialog;
	          
	            //If you will not use this method url links are opeen in new brower not in webview
	      /*      public boolean shouldOverrideUrlLoading(WebView view, String url) {              
	                view.loadUrl(url);
	                return true;
	            }
	        
	            //Show loader on url load
	            public void onLoadResource (WebView view, String url) {
	                if (progressDialog == null) {
	                    // in standard case YourActivity.this
	                    progressDialog = new ProgressDialog(ShowWebView.this);
	                    progressDialog.setMessage("Loading...");
	                    progressDialog.show();
	                }
	            }
	            public void onPageFinished(WebView view, String url) {
	                try{
	                if (progressDialog.isShowing()) {
	                    progressDialog.dismiss();
	                    progressDialog = null;
	                }
	                }catch(Exception exception){
	                    exception.printStackTrace();
	                }
	            }*/
	             
	        }); 

		// Javascript inabled on webview  x4xzrfv
		webview.getSettings().setJavaScriptEnabled(true); 

		//Load url in webview 

		webview.loadUrl(url);
		 
	}

	@Override
	// Detect when the back button is pressed
	public void onBackPressed() {
		super.onBackPressed();
	}




}
