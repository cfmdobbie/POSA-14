package edu.vuum.mocca;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;

/**
 * @class ThreadPoolDownloadService
 *
 * @brief This Service handles downloading several files concurrently
 *        within a pool of Threads.  When it is created, it creates a
 *        ThreadPoolExecutor using the newFixedThreadPool() method of
 *        the Executors class.
 * 
 *        When this Service is started, it should be supplied with a
 *        URI for download and a Messenger.  It downloads the URI
 *        supplied, stores it on the Android file system, then returns
 *        the pathname of the downloaded file using the supplied
 *        Messenger.
 * 
 *        This class implements the Synchronous Service layer of the
 *        Half-Sync/Half-Async pattern.  It also implements a variant
 *        of the Factory Method pattern.
 */
public class ThreadPoolDownloadService extends Service {
    /**
     * A class constant that determines the maximum number of threads
     * used to service download requests.
     */
    static final int MAX_THREADS = 4;
	
    /**
     * The ExecutorService that references a ThreadPool.
     */
    ExecutorService mExecutor;

    /**
     * Hook method called when the Service is created.
     */
    @Override
    public void onCreate() {
        // TODO - You fill in here to replace null with a new
        // FixedThreadPool Executor that's configured to use
        // MAX_THREADS. Use a factory method in the Executors class.

        // NOTE: Lots of factory methods exist in the Executors class, but
        // the one we want just creates a fixed pool of threads:
        mExecutor = Executors.newFixedThreadPool(MAX_THREADS);
    }

    /**
     * Make an intent that will start this service if supplied to
     * startService() as a parameter.
     * 
     * @param context		The context of the calling component.
     * @param handler		The handler that the service should
     *                          use to respond with a result  
     * @param uri               The web URL of a file to download
     * 
     * This method utilizes the Factory Method makeMessengerIntent()
     * from the DownloadUtils class.  The returned intent is a Command
     * in the Command Processor Pattern. The intent contains a
     * messenger, which plays the role of Proxy in the Active Object
     * Pattern.
     */
    public static Intent makeIntent(Context context,
                                    Handler handler,
                                    String uri) {
    	// TODO - You fill in here, by replacing null with an
        // invocation of the appropriate factory method in
        // DownloadUtils that makes a MessengerIntent.
        
        // NOTE: A factory method exists in DownloadUtils to make a suitable
        // Intent that contains a Messenger and the passed URI, so we just
        // need to call it to obtain the Intent:
        final Intent newIntent = DownloadUtils.makeMessengerIntent(context, ThreadPoolDownloadService.class, handler, uri);
        // NOTE: And return it!
        return newIntent;
    }

    /**
     * Hook method called when a component calls startService() with
     * the proper Intent.
     */
    @Override
    public int onStartCommand(final Intent intent,
                              int flags,
                              int startId) {
        // TODO - You fill in here to replace null with a new Runnable
        // that the ThreadPoolExecutor will execute to download the
        // image and respond to the client.  The Runnable's run()
        // method implementation should forward to the appropriate
        // helper method from the DownloadUtils class that downloads
        // the uri in the intent and returns the file's pathname using
        // a Messenger who's Bundle key is defined by DownloadUtils.MESSENGER_KEY.

        Runnable downloadRunnable = new Runnable() {
            @Override
            public void run() {
                // NOTE: First extract the Messenger from the Intent's extras Bundle
                final Messenger messenger = (Messenger)(intent.getExtras().get(DownloadUtils.MESSENGER_KEY));
                // NOTE: And the URI from the Intent's data field:
                final Uri uri = intent.getData();
                // NOTE: Then call the helper method in DownloadUtils that will perform
                // the actual download and use the Messenger to send a message back to
                // the calling Activity's MessageHandler containing the path of the
                // downloaded file.
                DownloadUtils.downloadAndRespond(ThreadPoolDownloadService.this, uri, messenger);
            }
        };

        mExecutor.execute(downloadRunnable);
      
        // Tell the Android framework how to behave if this service is
        // interrupted.  In our case, we want to restart the service
        // then re-deliver the intent so that all files are eventually
        // downloaded.
        return START_REDELIVER_INTENT;
    }

    /**
     * Called when the service is destroyed, which is the last call
     * the Service receives informing it to clean up any resources it
     * holds.
     */
    @Override
	public void onDestroy() {
    	// Ensure that the threads used by the ThreadPoolExecutor
    	// complete and are reclaimed by the system.

        mExecutor.shutdown();
    }

    /**
     * Return null since this class does not implement a Bound
     * Service.
     */
    @Override
	public IBinder onBind (Intent intent) {
        return null;
    }
}
