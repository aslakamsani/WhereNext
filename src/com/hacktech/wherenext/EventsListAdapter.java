package com.hacktech.wherenext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventsListAdapter extends ArrayAdapter<PlaceEvent>{
	private final Context context;
	private final ArrayList<PlaceEvent> events;
	private TextView tv;
 
	public EventsListAdapter(Context context, ArrayList<PlaceEvent> events) {
		super(context, R.layout.events_adapter, events);
		this.context = context;
		this.events = events;
	}
 
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View view = inflater.inflate(R.layout.events_adapter, parent, false);
		tv = (TextView) view.findViewById(R.id.text);
		PlaceEvent event = events.get(position);
		
		// write out each event
		tv.append(event.getTime()+"\n");
		tv.append(event.getSummary()+"\n\n");
		tv.append(event.getUrl());
		
		return tv;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return events.size();
	}

	@Override
	public PlaceEvent getItem(int arg0) {
		// TODO Auto-generated method stub
		return events.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
}
