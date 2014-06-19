package edu.vuum.mocca;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

import android.app.Activity;
import android.widget.TextView;
import android.util.Log;

/**
 * @class AndroidPlatformStrategy
 * 
 * @brief Provides methods that define a platform-independent API for
 *        output data to Android UI thread and synchronizing on thread
 *        completion in the ping/pong game.  It plays the role of the
 *        "Concrete Strategy" in the Strategy pattern.
 */
public class AndroidPlatformStrategy extends PlatformStrategy
{	
    /** TextViewVariable. */
    private TextView mTextViewOutput;
	
    /** Activity variable finds gui widgets by view. */
    private WeakReference<Activity> mActivity;

    public AndroidPlatformStrategy(Object output,
                                   final Object activityParam)
    {
        /**
         * A textview output which displays calculations and
         * expression trees.
         */
        mTextViewOutput = (TextView) output;

        /** The current activity window (succinct or verbose). */
        mActivity = new WeakReference<Activity>((Activity) activityParam);
    }

    /**
     * Latch to decrement each time a thread exits to control when the
     * play() method returns.
     */
    private static CountDownLatch mLatch = null;

    /** Do any initialization needed to start a new game. */
    public void begin()
    {
        /** (Re)initialize the CountDownLatch. */
        // TODO - You fill in here.
        mLatch = new CountDownLatch(NUMBER_OF_THREADS);
    }

    /** Print the outputString to the display. */
    public void print(final String outputString)
    {
        /** 
         * Create a Runnable that's posted to the UI looper thread
         * and appends the outputString to a TextView. 
         */
        // TODO - You fill in here.
        final Activity activity = mActivity.get();
        // NOTE: Must check the return from a WeakReference before using it!
        if(activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    mTextViewOutput.append(outputString + "\n");
                }
            });
        }
    }

    /** Indicate that a game thread has finished running. */
    public void done()
    {	
        // TODO - You fill in here.
        final Activity activity = mActivity.get();
        // NOTE: Must check the return from a WeakReference before using it!
        if(activity != null) {
            // NOTE: This is run on the UI thread to ensure that is gets queued after
            // any pending TextView updates.  Don't want the mLtch.await() call in
            // awaitDone() to un-block until all TextView updates have been done.
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    mLatch.countDown();
                }
            });
        }
    }

    /** Barrier that waits for all the game threads to finish. */
    public void awaitDone()
    {
        // TODO - You fill in here.
        try {
            mLatch.await();
        } catch(InterruptedException e) {
            // NOTE: Our contract does not allow us to throw this exception, but
            // there's no suitable way to notify the app that something went wrong.
            // Just log it with Android and move on:
            errorLog(AndroidPlatformStrategy.class.getName(), "InterruptedException: " + e.getMessage());
        }
    }

    /** 
     * Error log formats the message and displays it for the
     * debugging purposes.
     */
    public void errorLog(String javaFile, String errorMessage) 
    {
       Log.e(javaFile, errorMessage);
    }
}
