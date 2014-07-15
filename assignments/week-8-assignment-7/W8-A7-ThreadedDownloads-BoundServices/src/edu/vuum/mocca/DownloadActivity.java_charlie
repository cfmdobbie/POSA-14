package edu.vuum.mocca;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

/**
 * This is the main Activity that the program uses to start the
 * ThreadedDownloads application.  It allows the user to input the URL
 * of an image and download that image using one of two different
 * Android Bound Service implementations: synchronous and
 * asynchronous.  The Activity starts the Service using bindService().
 * After the Service is started, its onBind() hook method returns an
 * implementation of an AIDL interface to the Activity by
 * asynchronously calling the onServiceConnected() hook method in the
 * Activity.  The AIDL interface object that's returned can then be
 * used to interact with the Service either synchronously or
 * asynchronously, depending on the type of AIDL interface requested.
 * 
 * Starting Bound Services to run synchronously in background Threads
 * from the asynchronous UI Thread is an example of the
 * Half-Sync/Half-Async Pattern.  Starting Bound Services using
 * Intents is an example of the Activator and Command Processor
 * patterns.  The DownloadActivity plays the role of the Creator and
 * creates a Command in the form of an Intent.  The Intent is received
 * by the Service process, which plays the role of the Executor.
 * 
 * The use of AIDL interfaces to pass information between two
 * different processes is an example of the Broker Pattern, in which
 * all communication-related functionality is encapsulated in the AIDL
 * interface and the underlying Android Binder framework, shielding
 * applications from tedious and error-prone aspects of inter-process
 * communication.
 */
public class DownloadActivity extends DownloadBase {
    /**
     * Used for debugging.
     */
    private final String TAG = this.getClass().getSimpleName(); 
    
    /**
     * The AIDL Interface that's used to make twoway calls to the
     * DownloadServiceSync Service.  This object plays the role of
     * Requestor in the Broker Pattern.  If it's null then there's no
     * connection to the Service.
     */
    DownloadCall mDownloadCall;
     
    /**
     * The AIDL Interface that we will use to make oneway calls to the
     * DownloadServiceAsync Service.  This plays the role of Requestor
     * in the Broker Pattern.  If it's null then there's no connection
     * to the Service.
     */
    DownloadRequest mDownloadRequest;
     
    /** 
     * This ServiceConnection is used to receive results after binding
     * to the DownloadServiceSync Service using bindService().
     */
    ServiceConnection mServiceConnectionSync = new ServiceConnection() {
            /**
             * Cast the returned IBinder object to the DownloadCall
             * AIDL Interface and store it for later use in
             * mDownloadCall.
             */
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
            	Log.d(TAG, "ComponentName: " + name);
                // TODO You fill in here to replace null with a call
                // to a generated stub method that converts the
                // service parameter into an interface that can be
                // used to make RPC calls to the Service.

                mDownloadCall = DownloadCall.Stub.asInterface(service);
            }

            /**
             * Called if the remote service crashes and is no longer
             * available.  The ServiceConnection will remain bound,
             * but the service will not respond to any requests.
             */
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mDownloadCall = null;
            }
    	 
        };
     
    /** 
     * This ServiceConnection is used to receive results after binding
     * to the DownloadServiceAsync Service using bindService().
     */
    ServiceConnection mServiceConnectionAsync = new ServiceConnection() {
            /**
             * Cast the returned IBinder object to the DownloadRequest
             * AIDL Interface and store it for later use in
             * mDownloadRequest.
             */
            @Override
		public void onServiceConnected(ComponentName name,
                                               IBinder service) {
                // TODO You fill in here to replace null with a call
                // to a generated stub method that converts the
                // service parameter into an interface that can be
                // used to make RPC calls to the Service.

                mDownloadRequest = DownloadRequest.Stub.asInterface(service);
            }

            /**
             * Called if the remote service crashes and is no longer
             * available.  The ServiceConnection will remain bound,
             * but the service will not respond to any requests.
             */
            @Override
		public void onServiceDisconnected(ComponentName name) {
                mDownloadRequest = null;
            }
        };
     
    /**
     * The implementation of the DownloadCallback AIDL
     * Interface. Should be passed to the DownloadBoundServiceAsync
     * Service using the DownloadRequest.downloadImage() method.
     * 
     * This implementation of DownloadCallback.Stub plays the role of
     * Invoker in the Broker Pattern.
     */
    DownloadCallback.Stub mDownloadCallback = new DownloadCallback.Stub() {
            /**
             * Called when the DownloadServiceAsync finishes obtaining
             * the results from the GeoNames Web service.  Use the
             * provided String to display the results in a TextView.
             */
            @Override
            public void sendPath(final String imagePathname) throws RemoteException {
                // TODO - You fill in here to replace null with a new
                // Runnable whose run() method displays the bitmap
                // image whose pathname is passed as a parameter to
                // sendPath().  Please use displayBitmap() defined in
                // DownloadBase.

                // NOTE: Create a new Runnable that will update the bitmap...
                final Runnable displayRunnable = new Runnable() {
					@Override
					public void run() {
						// NOTE: imagePathname is declared as final in the method
						// signature, so is accessible inside this anonymous class.
						// Don't need to "ghost" it with a final local variable
						displayBitmap(imagePathname);
					}
				};
				// NOTE: ...And post it to the UI thread using Activity.runOnUiThread
				runOnUiThread(displayRunnable);
            }
        };
     
    /**
     * This method is called when a user presses a button (see
     * res/layout/activity_download.xml)
     */
    public void runService(View view) {
        Uri uri = Uri.parse(getUrlString());

        hideKeyboard();

    	switch (view.getId()) {
        case R.id.bound_sync_button:
            // TODO - You fill in here to use mDownloadCall to
            // download the image & then display it.
        	
        	// NOTE: Using an AsyncTask here because it avoids blocking the UI thread (don't
        	// forget the runService(View) method is being called on the UI thread!) and makes
        	// for a very clean and readable solution.
        	final AsyncTask<Uri, Void, String> downloadTask = new AsyncTask<Uri, Void, String>() {
				// NOTE: This task must be executed with at least one parameter which is the
				// URI pointing to the image to download.  Multiple parameters can be submitted,
				// but anything beyond the first will be ignored.  We could check this, but as
        		// the only invocation of this class is directly below, we can control it.
        		protected String doInBackground(Uri... params) {
        			// NOTE: downloadImage can throw RemoteException, so this call must be made
        			// inside a try/catch block.  In the event of an exception being thrown...
        			// we don't really have a good way to handle it in this Activity!  So for
        			// this assignment, we'll just write it out to stderr and continue.
        			// If this were a real app, some feedback to the user should be made.
        			try {
        				return mDownloadCall.downloadImage(params[0]);
        			} catch(RemoteException e) {
        				e.printStackTrace();
            			return null;
        			}
        		};
        		@Override
        		protected void onPostExecute(String result) {
        			// NOTE: onPostExecute runs in the UI thread, so don't need to explicitly
        			// post a runnable here.  Incidentally, this would be a good place to handle
        			// exceptions caught by the doInBackground method, as the result parameter
        			// passed in would be null.
        			displayBitmap(result);
        		}
			};
			// NOTE: Execute the task with the specified URI
			downloadTask.execute(uri);
            break;

        case R.id.bound_async_button:
            // TODO - You fill in here to call downloadImage() on
            // mDownloadRequest, passing in the appropriate Uri and
            // callback.
        	
        	try {
        		// NOTE: Ask the service to download the image but don't wait for a
        		// response.  We supply a callback that the service can use when a
        		// result is available, however.
        		mDownloadRequest.downloadImage(uri, mDownloadCallback);
        	} catch(RemoteException e) {
        		// NOTE: Again, no good way to handle failures in this app.
        		// Don't do this in production code!
        		e.printStackTrace();
        	}
            break;
        }
    }

    /**
     * Hook method called when the DownloadActivity becomes visible to
     * bind the Activity to the Services.
     */
    @Override
    public void onStart () {
    	super.onStart();
    	
    	// Bind this activity to the DownloadBoundService* Services if
    	// they aren't already bound Use mBoundSync/mBoundAsync
    	if (mDownloadCall == null) 
            bindService(DownloadBoundServiceSync.makeIntent(this), 
                        mServiceConnectionSync, 
                        BIND_AUTO_CREATE);
    	if (mDownloadRequest == null)
            bindService(DownloadBoundServiceAsync.makeIntent(this), 
                        mServiceConnectionAsync, 
                        BIND_AUTO_CREATE);
    }
    
    /**
     * Hook method called when the DownloadActivity becomes completely
     * hidden to unbind the Activity from the Services.
     */
    @Override
    public void onStop () {
    	super.onStop();
    	
    	// Unbind the Sync/Async Services if they are bound. Use
    	// mBoundSync/mBoundAsync
    	if (mDownloadCall != null) 
            unbindService(mServiceConnectionSync);
    	if (mDownloadRequest != null) 
            unbindService(mServiceConnectionAsync);
    }
    
    // Public accessor method for testing purposes
    public DownloadCall getDownloadCall () {
    	return mDownloadCall;
    }
    
    // Public accessor method for testing purposes
    public DownloadRequest getDownloadRequest () {
    	return mDownloadRequest;
    }
    
    // Public accessor method for testing purposes
    public DownloadCallback getDownloadCallback () {
    	return mDownloadCallback;
    }
    
    // Public accessor method for testing purposes
    public boolean isBoundToSync () {
    	return mDownloadCall != null;
    }
    
    // Public accessor method for testing purposes
    public boolean isBoundToAsync () {
    	return mDownloadRequest != null;
    }     
    
}
