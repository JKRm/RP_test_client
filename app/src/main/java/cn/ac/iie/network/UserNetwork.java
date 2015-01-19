package cn.ac.iie.network;

import android.util.Log;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;



import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserNetwork {

    private static UserNetwork mInstance;
    private final OkHttpClient client = new OkHttpClient();


    private UserNetwork() {

	}

	public static UserNetwork getInstance() {
		if (mInstance == null) {
            mInstance = new UserNetwork();
        }
		return mInstance;
	}
	public String bind(final String username, final String password) throws IOException {

		Map<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("password", password);

        RequestBody formBody = new FormEncodingBuilder()
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url("http://10.10.11.102:8000/bind")
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//        System.out.println(response.body().string());

        return response.body().string();
	}

    public String uploadBindResponse(final String res) throws IOException {


        Request request = new Request.Builder()
                .url("http://10.10.11.102:8000/bind")
                .post(RequestBody.create(MediaType.parse("application/json"), res))
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//        System.out.println(response.body().string());

        return response.body().string();
    }


    public String authenticate() throws IOException {

        Request request = new Request.Builder()
                .url("http://10.10.11.102:8000/authenticate")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

//        Headers responseHeaders = response.headers();
//        for (int i = 0; i < responseHeaders.size(); i++) {
//            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//        }

//        System.out.println(response.body().string());
        return response.body().string();
	}

	public void getTrustApps(String appid) {

		if(appid == null || appid.length() == 0){
			appid = "http://127.0.0.1:8000/trustedapps";
		}

		Map<String, String> params = new HashMap<String, String>();

	}
	


}
