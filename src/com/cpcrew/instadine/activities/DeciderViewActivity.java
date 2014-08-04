package com.cpcrew.instadine.activities;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpcrew.instadine.R;
import com.cpcrew.instadine.models.Rest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DeciderViewActivity extends Activity {
	Rest rest;
	String restId;
	private static final int NOTIFICATION_ID=30;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		NotificationManager mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(NOTIFICATION_ID);

		if (restId == null)
			restId = getIntent().getStringExtra("rest");
		setContentView(R.layout.activity_decider);
		getRestDetail(restId);
	}
	
	
	public void getRestDetail(String restId){
		
			final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
			final String TYPE_DETAILS = "/details";
			final String OUT_JSON = "/json";
			final String API_KEY = com.cpcrew.instadine.utils.Constants.GOOGLE_PLACES_API_KEY;
			StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS
					+ OUT_JSON);
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("key", API_KEY);
			params.put("placeid", restId);
			client.get(sb.toString(), params, new JsonHttpResponseHandler() {

				public void onSuccess(JSONObject response) {

					try {
						Rest r = new Rest();
						JSONObject jsonObject = response
								.getJSONObject("result");
						r = Rest.fromDetailJson(jsonObject);
						Log.d("debug",
								"Business Object in Decider: " + r.toString());
						updateRestView(r);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};

				@Override
				public void onFailure(Throwable e, String s) {
					Log.d("ERROR", e.toString());
				}
			});
		
		
	}

	private void updateRestView(Rest rest) {

		Typeface SaginawBold = Typeface.createFromAsset(getAssets(), "fonts/SaginawBold.ttf");
		
		// Message ( TODO check if voting in progress or complete )
		TextView tvMessage = (TextView)findViewById(R.id.tvMessage);
		tvMessage.setTypeface(SaginawBold);
		tvMessage.setText("You are going to \n" + rest.getName() + "\n with your group");
		
		
		// Rest image Big
		ImageView ivRest = (ImageView) findViewById(R.id.ivRestImage);
		String photoFile = rest.getImageUrl();
		ImageLoader imageLoader = ImageLoader.getInstance();
		if (photoFile != null && !(photoFile.equals(""))) {
			imageLoader.displayImage(photoFile, ivRest);
		}
		
		
		
	}

}
