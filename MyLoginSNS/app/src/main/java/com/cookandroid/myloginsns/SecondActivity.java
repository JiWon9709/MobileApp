package com.cookandroid.myloginsns;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

public class SecondActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap gMap;
    MapFragment mapFrag;
    GroundOverlayOptions videoMark; //map 위에 띄어지는 형태

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        mapFrag=(MapFragment)getFragmentManager().findFragmentById(R.id.Gmap);
        mapFrag.getMapAsync(this); //비동기화-외부와의 접속을 하는 것은 백그라운드에서 실행하겠다. 지도가 다 준비되면 자동으로 onMapReady 함수로 자동으로 이동한다.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu); //기본형태
        menu.add(0,1,0,"위성지도");
        menu.add(0,2,0,"일반지도"); //두번째 숫자가 제일 중요(메뉴 각각의 아이디를 뜻한다.
        menu.add(0,3,0,"제주도 공항"); //그 위치로 이동해야한다.
        SubMenu sMenu = menu.addSubMenu("유명장소 바로가기 >>");
        sMenu.add(0, 4, 0, "한라산");
        sMenu.add(0, 5, 0, "성산일출봉");
        sMenu.add(0, 6, 0, "한라산 국립공원");
        return true; //기본형태
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1 :
                gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true; //위성사진
            case 2 :
                gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true; //일반사진
            case 3 :
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.510516,126.491380), 15));
                return true;
            case 4:
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(33.362436, 126.529231), 15));
                return true;
            case 5:
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(33.459165, 126.942157), 15));
                return true;
            case 6:
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(33.370692, 126.534923), 15));
                return true;
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap=googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); //그래픽, 위성과 같은 스타일 결정
        //map:cameraTargetLat="37.651683"
        //map:cameraTargetLng="127.016171"
        //map:cameraZoom="15"
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.453216,126.519663), 15)); //줌의 정도를 15로 정한다.
        gMap.getUiSettings().setZoomControlsEnabled(true); //지도에 확대 축소 버튼이 생겼다
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                videoMark=new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.heart))
                .position(latLng, 100f, 100f);
                gMap.addGroundOverlay(videoMark);
            }
        });
    }
}
