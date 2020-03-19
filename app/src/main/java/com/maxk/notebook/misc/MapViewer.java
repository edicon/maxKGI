/* 
 */

package com.maxk.notebook.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.maxk.notebook.maxkgi.R;
/*
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
*/

/**
 * Google Geocoding API
 * 	@link: https://developers.google.com/maps/documentation/geocoding/
 * 
 * Naver: 도로명 주소에대한 GeoCode Query 적용할 수 없음.
 */
public class MapViewer extends Activity { // NMapActivity {
	private static final String LOG_TAG = "NMapViewer";
	private static final boolean DEBUG = false;

	// set your API key which is registered for NMapViewer library.
	private static final String API_KEY = "c342e9f8a7a956b1c5e1b50acd1e0efe";

	// private NMapView mMapView;
	// private NMapController mMapController;

	public static String queryAddr;
	// private static final NGeoPoint NMAP_LOCATION_DEFAULT	= new NGeoPoint(126.978371, 37.5666091);
	private static final int NMAP_ZOOMLEVEL_DEFAULT 		= 11;
	// private static final int NMAP_VIEW_MODE_DEFAULT 		= NMapView.VIEW_MODE_VECTOR;
	private static final String KEY_ZOOM_LEVEL 				= "NMapViewer.zoomLevel";
	private static final String KEY_VIEW_MODE 				= "NMapViewer.viewMode";
	private SharedPreferences mPreferences;

	// private NMapLocationManager mMapLocationManager;
	// private NMapPOIdataOverlay mFloatingPOIdataOverlay;
	// private NMapPOIitem mFloatingPOIitem;
	
	private boolean USE_NAVER_MAP 		= false;
	private boolean USE_NAVER_GEOCODE 	= false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.map_viewer);

		if( USE_NAVER_MAP == false ) {
			getGeoLocation( queryAddr );
		} else {
			/*
			mMapView = (NMapView)findViewById(R.id.mapView);
	
			// set a registered API key for Open MapViewer Library
			mMapView.setApiKey(API_KEY);
	
			// initialize map view
			mMapView.setClickable(true);
			mMapView.setEnabled(true);
			mMapView.setFocusable(true);
			mMapView.setFocusableInTouchMode(true);
			mMapView.requestFocus();
	
			// register listener for map state changes
			mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
			mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);
			// mMapView.setOnMapViewDelegate(onMapViewTouchDelegate);
	
			// use map controller to zoom in/out, pan and set map center, zoom level etc.
			mMapController = mMapView.getMapController();
	
			// use built in zoom controls
			NMapView.LayoutParams lp = new NMapView.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, NMapView.LayoutParams.BOTTOM_RIGHT);
			mMapView.setBuiltInZoomControls(true, lp);
	
			// create resource provider
			// mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
	
			// set data provider listener
			super.setMapDataProviderListener(onDataProviderListener);
	
			// create overlay manager
			// mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
			// register callout overlay listener to customize it.
			// mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);
			// register callout overlay view listener to customize it.
			// mOverlayManager.setOnCalloutOverlayViewListener(onCalloutOverlayViewListener);
	
			// location manager
			mMapLocationManager = new NMapLocationManager(this);
			mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
	
			// compass manager
			// mMapCompassManager = new NMapCompassManager(this);
	
			// create my location overlay
			// mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);
			 */
		}
	}

	/* NMapDataProvider Listener 
	private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {

		@Override
		public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {

			if (DEBUG) {
				Log.i(LOG_TAG, "onReverseGeocoderResponse: placeMark="
					+ ((placeMark != null) ? placeMark.toString() : null));
			}

			if (errInfo != null) {
				Log.e(LOG_TAG, "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());

				Toast.makeText(MapViewer.this, errInfo.toString(), Toast.LENGTH_LONG).show();
				return;
			}

			if (mFloatingPOIitem != null && mFloatingPOIdataOverlay != null) {
				mFloatingPOIdataOverlay.deselectFocusedPOIitem();

				if (placeMark != null) {
					mFloatingPOIitem.setTitle(placeMark.toString());
				}
				mFloatingPOIdataOverlay.selectPOIitemBy(mFloatingPOIitem.getId(), false);
			}
		}

	};

	/* MyLocation Listener
	private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

		@Override
		public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {

			if (mMapController != null) {
				mMapController.animateTo(myLocation);
			}

			return true;
		}

		@Override
		public void onLocationUpdateTimeout(NMapLocationManager locationManager) {

			Toast.makeText(MapViewer.this, "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {

			Toast.makeText(MapViewer.this, "Your current location is unavailable area.", Toast.LENGTH_LONG).show();

			// stopMyLocation();
		}

	};

	// MapView State Change Listener 
	private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {

		@Override
		public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {

			if (errorInfo == null) { // success
				// restore map view state such as map center position and zoom level.
				getGeoLocation( queryAddr );
				// restoreInstanceState();

			} else { // fail
				Log.e(LOG_TAG, "onFailedToInitializeWithError: " + errorInfo.toString());
				Toast.makeText(MapViewer.this, errorInfo.toString(), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onAnimationStateChange(NMapView mapView, int animType, int animState) {
			if (DEBUG) {
				Log.i(LOG_TAG, "onAnimationStateChange: animType=" + animType + ", animState=" + animState);
			}
		}

		@Override
		public void onMapCenterChange(NMapView mapView, NGeoPoint center) {
			if (DEBUG) {
				Log.i(LOG_TAG, "onMapCenterChange: center=" + center.toString());
			}
		}

		@Override
		public void onZoomLevelChange(NMapView mapView, int level) {
			if (DEBUG) {
				Log.i(LOG_TAG, "onZoomLevelChange: level=" + level);
			}
		}

		@Override
		public void onMapCenterChangeFine(NMapView mapView) {
		}
	};

	private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {

		@Override
		public void onLongPress(NMapView mapView, MotionEvent ev) {
		}
		@Override
		public void onLongPressCanceled(NMapView mapView) {
		}
		@Override
		public void onSingleTapUp(NMapView mapView, MotionEvent ev) {
		}
		@Override
		public void onTouchDown(NMapView mapView, MotionEvent ev) {
		}
		@Override
		public void onScroll(NMapView mapView, MotionEvent e1, MotionEvent e2) {
		}
		@Override
		public void onTouchUp(NMapView mapView, MotionEvent ev) {
		}
	};
	*/

	// Local Functions
	private void restoreInstanceState( String geoX, String geoY) {
		
		/*
		Double lon = Double.parseDouble(geoX);
		Double lat = Double.parseDouble(geoY);
		mPreferences = getPreferences(MODE_PRIVATE);

		int level = mPreferences.getInt(KEY_ZOOM_LEVEL, NMAP_ZOOMLEVEL_DEFAULT);
		int viewMode = mPreferences.getInt(KEY_VIEW_MODE, NMAP_VIEW_MODE_DEFAULT);

		mMapController.setMapViewMode(viewMode);
		mMapController.setMapCenter(new NGeoPoint(lon, lat), level);
		*/
	}

	
	private void getGeoLocation( String addr ) {
		AddressTask addressTask = new AddressTask();
		addressTask.execute(addr);
	}
	
	public class AddressTask extends AsyncTask<String, Boolean, Boolean> {
		 
		String query;
		String geoX, geoY;
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            if( USE_NAVER_GEOCODE )
	    		query = "http://openapi.map.naver.com/api/geocode.php?"
						+ "key=" + API_KEY
						+ "&encoding=utf-8"
						+ "&coord=latlng"	// latlng or tm128
						+ "&query=";
            else	    		
	    		query = "http://maps.googleapis.com/maps/api/geocode/xml?"
	    				+ "language=ko&sensor=false&address=";
        }
         
        @Override
        protected Boolean doInBackground(String... params) {
             
            String addr = params[0];
            
            try {
            	query = query + Uri.encode(addr);
            	
				HttpGet request = new HttpGet( query );
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse response = httpClient.execute(request);
				HttpEntity httpEntity = response.getEntity();

				int status = response.getStatusLine().getStatusCode();

				if (status == HttpStatus.SC_OK) {
					InputStream is = httpEntity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 1024);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					
					parseGeoXml( sb.toString() );
					
					is.close();
					return true;
				} else 
					return false;
							
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} 
        }
         
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            
            if( USE_NAVER_MAP && result ) 
            	restoreInstanceState( geoX, geoY);
            else {
            	String format;
				// https://www.google.co.kr/maps/place/서울특별시+종로구+사직동+1-30
            	if( geoX == null || geoY == null ) {
            		// https://www.google.co.kr/maps/search/사직공원/
            		format = "https://www.google.co.kr/maps/search/" + queryAddr;
            	} else {
	            	// https://www.google.co.kr/maps/@37.5650957,126.9895447,18z
					format = "https://www.google.co.kr/maps/@" + geoY + "," + geoX + "," + "18z"; // 37.5650957,126.9895447,18z
            	}
            	
	    		Uri uri = Uri.parse(format);
	    		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		startActivity(intent);
	    		
	    		finish();	// Exit Naver Map
            }
        }
         
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
        
		private void parseGeoXml(String xml) {
			
			String text = null;
			
			XmlPullParserFactory factory;
			try {
				factory = XmlPullParserFactory.newInstance();

				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();

				xpp.setInput(new StringReader(xml));
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_DOCUMENT) {
						Log.d("XML", "Start document");
					} else if (eventType == XmlPullParser.START_TAG) {
						Log.d("XML", "Start tag " + xpp.getName());
					} else if (eventType == XmlPullParser.END_TAG) {
						Log.d("XML", "End tag " + xpp.getName());
						if( USE_NAVER_MAP ) {
							String tag = xpp.getName();
							if( tag!= null && tag.equals("total")) {
								if( "0".equals(text))
									return;
							} else if( tag.equals("x")) {
								geoX =  text;
								Log.d("XML", "X " + geoX);							
							} else if( tag.equals("y")) {
								geoY =  text;
								Log.d("XML", "Y " + geoY);
								return;
							}
						} else {
							String tag = xpp.getName();
							if( tag!= null && tag.equals("status")) {
								if( !"OK".equals(text))
									return;
							} else if( tag.equals("lng")) {
								geoX =  text;
								Log.d("XML", "X " + geoX);		
								return;	// lat --> lng 순서 유의
							} else if( tag.equals("lat")) {
								geoY =  text;
								Log.d("XML", "Y " + geoY);
							}
						}
					} else if (eventType == XmlPullParser.TEXT) {
						text = xpp.getText();
						Log.d("XML", "Text " + xpp.getText());
					}					
					eventType = xpp.next();
				}
				Log.d("XML", "End document");
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {	// xpp.next();
				e.printStackTrace();
			}
		}
    }
}
