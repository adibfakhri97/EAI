package com.detrening.detrening.Maps;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.detrening.detrening.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class infoAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public infoAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void rendowWindowText(Marker marker, View view){

        String title = marker.getTitle();

        String snippet = marker.getSnippet();
        TextView sn = (TextView) view.findViewById(R.id.snippet);
        TextView id = (TextView) view.findViewById(R.id.idPlace);
        if (!title.equals("")){
            id.setText(title);
        }
        if (!snippet.equals("")){
            sn.setText(snippet);
        }


        if (!title.equals("")&& !snippet.equals("")){
            Intent intent = new Intent(mContext, details_tempat.class);
            intent.putExtra("id_pl", title);
            intent.putExtra("snippet", snippet);
            mContext.startActivity(intent);

        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }
}
