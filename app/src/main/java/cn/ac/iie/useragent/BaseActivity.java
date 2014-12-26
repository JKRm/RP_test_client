package cn.ac.iie.useragent;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import cn.ac.iie.rpclient.R;

public class BaseActivity extends Activity {

	private Dialog waitDialog;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        String title;
	        try {
	            title = getPackageManager().getActivityInfo(this.getComponentName(), 0).loadLabel(getPackageManager()).toString();
	        } catch (NameNotFoundException e) {
	            e.printStackTrace();
	        }
//	        if (callback != null) {
//	            callback.onCreate(savedInstanceState);
//	        }

	    }
	 
	 @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	 }
	
	 @Override
	    protected void onResume() {
	        super.onResume();
	    }

	    @Override
	    protected void onPause() {
	        super.onPause();
	    }
	    

	    
	    /**
	     * show wait dialog
	     */
	    public void showWaitDialog(String message) {
	        if (isFinishing()) {
	            return;
	        }
	        if (waitDialog != null && waitDialog.isShowing()) {
	            waitDialog.dismiss();
	            waitDialog = null;
	        }
	        waitDialog = new Dialog(this, R.style.dialog );
	        waitDialog.setContentView(R.layout.dialog_progress);
	        ((TextView) waitDialog.findViewById(R.id.message)).setText(message);
	        waitDialog.setCancelable(false);
	        waitDialog.show();
	    }

	    /**
	     * show wait dialog
	     */
	    public void showWaitDialog(String message, boolean cancelable) {
	        if (isFinishing()) {
	            return;
	        }
	        if (waitDialog != null && waitDialog.isShowing()) {
	            waitDialog.dismiss();
	            waitDialog = null;
	        }
	        waitDialog = new Dialog(this, R.style.dialog);
	        waitDialog.setContentView(R.layout.dialog_progress);
	        ((TextView) waitDialog.findViewById(R.id.message)).setText(message);
	        waitDialog.setCancelable(cancelable);
	        waitDialog.show();
	    }

	    /**
	     * show dismiss dialog
	     */
	    public void dismissWaitDialog() {
	        if (waitDialog != null && waitDialog.isShowing() && !this.isFinishing()) {
	            waitDialog.dismiss();
	        }
	        waitDialog = null;
	    }   
	    
	
}
