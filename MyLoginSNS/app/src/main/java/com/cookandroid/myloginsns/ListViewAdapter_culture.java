package com.cookandroid.myloginsns;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter_culture extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Culitem> listViewItemList = new ArrayList<Culitem>(); // busitem을 배열로 갖는 arraylist 선언
    //  String tag = "ListViewAdapter";

    // ListViewAdapter의 생성자
    public ListViewAdapter_culture() { }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // 고정되어있지 않은 데이터를 다룰때 inflater 사용
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textViewX) ;

        Culitem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(listViewItem.gettitle());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String icon, String gpsX, String gpsY, String g, String gp) {
        Culitem item = new Culitem();

        item.sethost(icon);
        item.setinfo(gpsX);
        item.setlocation(gpsY);
        item.settel(g);
        item.settitle(gp);
        listViewItemList.add(item);
        //     Log.d(tag,gpsX+", "+gpsY);
    }
}