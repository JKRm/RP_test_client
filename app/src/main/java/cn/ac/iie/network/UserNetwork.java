package cn.ac.iie.network;

import android.util.Log;
import cn.ac.iie.interfaces.INetworkHandler;
import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class UserNetwork {
	private static UserNetwork mInstance;
	private Response.Listener<String> mSuccessListener;
	private Response.ErrorListener mErrorListener;
	private INetworkHandler callbackHandler;
	private int requestTag;
	
	private UserNetwork() {
		mSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Volley", "response got------");
                Log.d("Volley", response);
				callbackHandler.networkCallback(response, HttpStatus.SC_OK, requestTag);
            }
        };
		mErrorListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				NetworkResponse networkResponse = error.networkResponse;
				int httpCode = 0;
				if (networkResponse == null) {
					Log.e("Volley", "Did not get any response from Internet.");
				} else {
					httpCode = networkResponse.statusCode;
				}

				Log.e("Volley", error.getMessage(), error);

				callbackHandler.networkCallback(null, httpCode, requestTag);
			}
		}; // end error listener
	}

	public static UserNetwork getInstance(INetworkHandler callbackHandler, int requestTag) {
		if (mInstance == null) mInstance = new UserNetwork();
		mInstance.callbackHandler = callbackHandler;
		mInstance.requestTag = requestTag;
		return mInstance;
	}
	public void bind(final String username, final String password) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("password", password);

		FidoStringRequest request = new FidoStringRequest(
				Method.POST,
				"http://192.168.112.29:8000/bind",
                params,
				mInstance.mSuccessListener,
                mInstance.mErrorListener);
		
		VolleySingleton.getInstance().addToRequestQueue(request);
	}

	public void authenticate() {

		Map<String, String> params = new HashMap<String, String>();

		FidoStringRequest request = new FidoStringRequest(
				Method.POST,
				"http://192.168.112.29:8000/authenticate",
				params,
				mInstance.mSuccessListener,
				mInstance.mErrorListener);

		VolleySingleton.getInstance().addToRequestQueue(request);
	}

	public void getTrustApps(String appid) {

		if(appid == null || appid.length() == 0){
			appid = "http://127.0.0.1:8000/trustedapps";
		}

		Map<String, String> params = new HashMap<String, String>();

		FidoStringRequest request = new FidoStringRequest(
				Method.GET,
				appid,
				params,
				mInstance.mSuccessListener,
				mInstance.mErrorListener);

		VolleySingleton.getInstance().addToRequestQueue(request);
	}
	


}
