package com.global.news;

public class GlobalData extends android.app.Application {

	private String serverURL, websiteURL;
	
	public void setserverURL(String  url)
	{
		serverURL = url;
	}
	public String getserverURL()
	{
		return serverURL;
	}
	
	
	public void setwebsiteURL(String  url)
	{
		websiteURL = url;
	}
	public String getwebsiteURL()
	{
		return websiteURL;
	}

}
