package cn.ac.iie.network;

import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class FidoStringRequest extends StringRequest {
	private Map<String, String> mParams;
	
	public FidoStringRequest(int method, String url, Map<String, String> params, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
		super(method, url, successListener, errorListener);
		mParams = params;
	}
	
	@Override
    public Map<String, String> getParams() throws AuthFailureError {
		if (mParams != null) {
			for (String key : mParams.keySet()) {
				Log.d("Volley", key + ": " + mParams.get(key));
			}
		}
        return mParams;
    }
}
