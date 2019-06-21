package com.cookandroid.myloginsns;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Culture extends AppCompatActivity {
    String host, info, location, tel, title;
    String tag = "JEJU";
    TextView dlgX, dlgY, dlgA, dlgB, dlgC;
    ListView listView; // 위젯
    ListViewAdapter_culture adapter; // 클래스 -> 객체 생성
    Button Btreturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.culture);
        setTitle("제주 문화");

        listView = (ListView) findViewById(R.id.listview1);

        String srvUrl = "http://210.99.248.79/rest/FestivalInquiryService/getFestivalList"; // 띄어쓰기 있으면 x, 주소
        String srvKey = "O1eRMhYRV%2FmwW1rCavaxORY44We%2FrqFL%2FBPUR9iB9%2FuPzH42fKkJ9oHRD58hxsvT5WYAWXaLXj51Y4%2Bgluldsg%3D%3D"; // 인증키
        String strUrl = srvUrl+"?authApiKey="+srvKey+"&startPage=1"+"&pageSize=10";
        Log.d(tag, strUrl);

        Button Btreturn=(Button)findViewById(R.id.btnReturning);
        Btreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
                finish();
            }
        });

        new DownloadWebpageTask().execute(strUrl);
        // 누르면 다이얼로그 뜨게 하기
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 몇번째 아이템인가,,,,
                @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Culitem item = (Culitem) adapterView.getItemAtPosition(i); // i번째 해당하는 아이템
                String x = item.gethost();
                String y = item.getinfo();
                String a = item.getlocation();
                String b = item.gettel();
                String c = item.gettitle();
                // 다이얼로그 상자 만들기
                AlertDialog.Builder dlg = new AlertDialog.Builder(Culture.this);
                View dlgView = (View) View.inflate(Culture.this, R.layout.dialog2, null);
                dlg.setView(dlgView);
                dlg.setTitle("제주 문화정보");
                dlgX = (TextView)dlgView.findViewById(R.id.tvhost); //// dlgView에 있는 textViewX를 가져와라
                dlgY = (TextView)dlgView.findViewById(R.id.tvinfo);
                dlgA = (TextView)dlgView.findViewById(R.id.tvlocation);
                dlgB = (TextView)dlgView.findViewById(R.id.tvtel);
                dlgC = (TextView)dlgView.findViewById(R.id.tvtitle);
                dlgX.setText(x);
                dlgY.setText(y);
                dlgA.setText(a);
                dlgB.setText(b);
                dlgC.setText(c);
                dlg.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dlg.show(); // 화면에 보이기
            }
        });

    } // oncreate

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> { // json,,,,, execute 함수를 부름(=doInbackground를 부르는 것임)
        @Override
        protected String doInBackground(String... urls) {
            try {
                return (String)downloadUrl((String)urls[0]); // xml을 전달받음
            } catch (IOException e) {
                return "==>다운로드 실패";
            }
        }

        protected void onPostExecute(String result) { // doInBackground 함수가 끝나면 자동으로 onpostexcute가 실행됨.
            Log.d(tag, result);
            //tv.append(result + "\n"); // setText와는 다름, append는 붙이는것
            //tv.append("========== 파싱 결과 ==========\n");

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                adapter = new ListViewAdapter_culture(); // adapter 객체 생성
                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();
                boolean bHost = false, binfo = false, blocation = false;
                boolean btel = false, btitle = false;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if(eventType == XmlPullParser.START_DOCUMENT) {
                        ;
                    } else if(eventType == XmlPullParser.START_TAG) { //// <> -> start_tag
                        String tag_name = xpp.getName();
                        if (tag_name.equals("host")) // tag 이름만 바꿔주면 됨, 내가 원하는 부분만 가져옴
                            bHost = true;
                        if (tag_name.equals("info"))
                            binfo = true;
                        if (tag_name.equals("location"))
                            blocation = true;
                        if (tag_name.equals("tel"))
                            btel = true;
                        if (tag_name.equals("title"))
                            btitle = true;
                    } else if(eventType == XmlPullParser.TEXT) { //// start_tag가 true이면 text 가져온다
                        if (bHost) {
                            //String content = xpp.getText();
                            host = xpp.getText();
                            bHost = false;
                        }
                        if (binfo) {
                            //String content = xpp.getText();
                            //tv.append(xpp.getText() + "\n");
                            info =xpp.getText();
                            binfo = false;
                        }
                        if (blocation) {
                            //String content = xpp.getText();
                            //tv.append(xpp.getText()+ "\n");
                            location = xpp.getText();
                            blocation = false;
                        }
                        if (btel) {
                            tel = xpp.getText();
                            btel = false;
                        }
                        if (btitle) {
                            //String content = xpp.getText();
                            //tv.append(xpp.getText()+ "\n");
                            title = xpp.getText();
                            adapter.addItem(host, info, location, tel, title);
                            btitle = false;
                        }
                    } else if(eventType == XmlPullParser.END_TAG) {
                        ;
                    }
                    eventType = xpp.next();
                } //while
                listView.setAdapter(adapter);
            } catch (Exception e) {
                //tv.setText("\n"+e.getMessage());
            }
        }

        private String downloadUrl(String myurl) throws IOException { //
            HttpURLConnection conn = null;
            try {
                Log.d(tag, "downloadUrl : "+  myurl);
                URL url = new URL(myurl); // string url을 개체인 url로 바꿈
                conn = (HttpURLConnection) url.openConnection();
                BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf, "utf-8")); // 데이터를 받음
                String line = null;
                String page = "";
                while((line = bufreader.readLine()) != null) { // bufferedReader로 받은 string으로 바꿈
                    page += line;
                }

                return page;
            } catch(Exception e){
                return " ";
            }
            finally {
                conn.disconnect();
            }
        }
    }
}