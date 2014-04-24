package com.example.smartsdk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class DnaArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
 
	public DnaArrayAdapter(Context context, String[] values) {
		super(context, R.layout.list_display, values);
		this.context = context;
		this.values = values;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.list_display
				, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		textView.setText(values[position]);
 
		String s = values[position];
 
		System.out.println(s);
 
		imageView.setImageResource(R.drawable.img_dna);
 
		return rowView;
	}
}
