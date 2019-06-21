package com.cookandroid.myloginsns;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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


public class Song extends AppCompatActivity {

    EditText etBus;
    EditText edtName;
    Button btSearch;
    TextView tvID; //tv
    ListView listview;
    ListViewAdapter adapter;
    String tag = "song";
    TextView dlgX, dlgMin, dlgSec, dlgCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song);

        btSearch = (Button) findViewById(R.id.btnSearch);
        listview=(ListView)findViewById(R.id.listview1) ;
        String srvUrl="http://www.jeju.go.kr/rest/JejuFolksongService/getJejuFolksongList";
        Log.d(tag, srvUrl);
        new DownloadWebpageTask().execute(srvUrl);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { //세번쨰 매개변수가 몇번째 아이템이 클릭되었는지 알려준다.
                SongItem item=(SongItem)adapterView.getItemAtPosition(i); // 전달받은 값
                String x=item.getGpsX();
                String min=item.getMin();
                String sec=item.getSec();
                String con=item.getCon();
                AlertDialog.Builder dlg=new AlertDialog.Builder(Song.this);
                View dlgView=(View)View.inflate(
                        Song.this,
                        R.layout.dialog1, null);
                dlg.setView(dlgView);
                dlg.setTitle("민요정보");
                dlgX=(TextView)dlgView.findViewById(R.id.textViewX);
                dlgX.setText(x);
                dlgMin=(TextView)dlgView.findViewById(R.id.textViewMin);
                dlgMin.setText(min);
                dlgSec=(TextView)dlgView.findViewById(R.id.textViewSec);
                dlgSec.setText(sec);
                dlgCon=(TextView)dlgView.findViewById(R.id.textViewCon);
                dlgCon.setText(con);
                dlg.setNegativeButton("닫기",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss(); //종료한다.
                            }
                        });
                dlg.show(); //클릭했을 때 화면에 보이게 된다.
            }
        });

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
                finish();
            }
        });
    } // onCreate
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return (String)downloadUrl((String)urls[0]);
            } catch (IOException e) {
                return "==>다운로드 실패";
            }
        }

        protected void onPostExecute(String result) {
            Log.d(tag, result);
            //tv.append(result + "\n");
            //tv.append("========== 파싱 결과 ==========\n");

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                adapter=new ListViewAdapter();
                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();
                boolean bGpsX=false, bMin=false, bSec=false, bCon=false;
                boolean bNo=false;
                String gpsX="", min="", sec="", con="";
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if(eventType == XmlPullParser.START_DOCUMENT) {
                        ;
                    } else if(eventType == XmlPullParser.START_TAG) {
                        String tag_name = xpp.getName();
                        if (tag_name.equals("name"))
                            bGpsX = true;
                        if (tag_name.equals("minute"))
                            bMin = true;
                        if (tag_name.equals("second"))
                            bSec = true;
                        if (tag_name.equals("contents"))
                            bCon = true;
                    } else if(eventType == XmlPullParser.TEXT) {
                        if (bGpsX) {
                            Log.d(tag, gpsX);
                            gpsX=xpp.getText();
                            bGpsX = false;
                        }
                        if (bMin) {
                            //Log.d(tag, gpsX);
                            //adapter.addItem(gpsX, min, sec, con);
                            min=xpp.getText()+"분";
                            bMin = false;
                        }
                        if (bSec) {
                            //Log.d(tag, gpsX);
                            //adapter.addItem(gpsX, min, sec, con);
                            sec=xpp.getText()+"초";
                            bSec = false;
                        }
                        if (bCon) {
                            //Log.d(tag, gpsX);
                            con=xpp.getText();
                            adapter.addItem(gpsX, min, sec, con);
                            bCon = false;
                        }
                    } else if(eventType == XmlPullParser.END_TAG) {
                        ;
                    }
                    eventType = xpp.next();
                } // while
                listview.setAdapter(adapter);
            } catch (Exception e) {
                //tv.setText("\n"+e.getMessage());
            }
        }

        private String downloadUrl(String myurl) throws IOException {

            HttpURLConnection conn = null;
            try {
                Log.d(tag, "downloadUrl : "+  myurl);
                URL url = new URL(myurl);
                conn = (HttpURLConnection) url.openConnection();
                BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf, "utf-8"));
                String line = null;
                String page = "";
                while((line = bufreader.readLine()) != null) {
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
