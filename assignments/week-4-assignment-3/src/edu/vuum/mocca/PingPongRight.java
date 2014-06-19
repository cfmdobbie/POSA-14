package edu.vuum.mocca;

// Import the necessary Java synchronization and scheduling classes.
import java.util.concurrent.CountDownLatch;

/**
 * @class PingPongRight
 * 
 * @brief This class implements a Java program that creates two
 *        instances of the PlayPingPongThread and start these thread
 *        instances to correctly alternate printing "Ping" and "Pong",
 *        respectively, on the console display.
 */
public class PingPongRight {
    /**
     * Number of iterations to run the test program.
     */
    public final static int mMaxIterations = 10;

    /**
     * Latch that will be decremented each time a thread exits.
     */
    public static CountDownLatch mLatch = null;

    /**
     * @class PlayPingPongThread
     * 
     * @brief This class implements the ping/pong processing algorithm
     *        using the SimpleSemaphore to alternate printing "ping"
     *        and "pong" to the console display.
     */
    public static class PlayPingPongThread extends Thread {

        /**
         * Constants to distinguish between ping and pong
         * SimpleSemaphores, if you choose to use an array of
         * SimpleSemaphores.  If you don't use this implementation
         * feel free to remove these constants.
         */
    	// NOTE: Am using two SimpleSemaphore variables, so don't need these constants
        //private final static int FIRST_SEMA = 0;
        //private final static int SECOND_SEMA = 1;

        /**
         * Maximum number of loop iterations.
         */
    	// NOTE: Default assignment removed so am able to declare member variable final
        private final int mMaxLoopIterations;

        /**
         * String to print (either "ping!" or "pong"!) for each
         * iteration.
         */
        // TODO - You fill in here.
        private final String mStringToPrint;

        /**
         * Two SimpleSemaphores use to alternate pings and pongs.  You
         * can use an array of SimpleSemaphores or just define them as
         * two data members.
         */
        // TODO - You fill in here.
        private final SimpleSemaphore mSemaphoreToAcquire;
        private final SimpleSemaphore mSemaphoreToRelease;

        /**
         * Constructor initializes the data member(s).
         */
        public PlayPingPongThread(String stringToPrint,
                                  SimpleSemaphore semaphoreToAcquire,
                                  SimpleSemaphore semaphoreToRelease,
                                  int maxIterations) {
            // TODO - You fill in here.
        	this.mStringToPrint = stringToPrint;
        	this.mSemaphoreToAcquire = semaphoreToAcquire;
        	this.mSemaphoreToRelease = semaphoreToRelease;
        	this.mMaxLoopIterations = maxIterations;
        }

        /**
         * Main event loop that runs in a separate thread of control
         * and performs the ping/pong algorithm using the
         * SimpleSemaphores.
         */
        public void run() {
            /**
             * This method runs in a separate thread of control and
             * implements the core ping/pong algorithm.
             */

            // TODO - You fill in here.
        	try {
	        	for(int i = 0 ; i < mMaxLoopIterations ; i++) {
	        		// NOTE: Expected output is one-based , not zero-based.  Using non-standard
	        		// looping variables is a common source of error, so am using a standard
	        		// zero-based "for" loop and calculating the display value separately.
	        		final int displayedIteration = i + 1;
	        		try {
	        			acquire();
	        			System.out.println(mStringToPrint + "(" + displayedIteration + ")");
	        		} finally {
	        			release();
	        		}
	        	}
        	} finally {
            	// NOTE: This thread has completed, decrement the latch by one.
        		// This is done in a finally block to ensure that whatever happens,
        		// the latch knows the thread has exited.
            	mLatch.countDown();
        	}
        }

        /**
         * Method for acquiring the appropriate SimpleSemaphore.
         */
        private void acquire() {
            // TODO fill in here
        	mSemaphoreToAcquire.acquireUninterruptibly();
        }

        /**
         * Method for releasing the appropriate SimpleSemaphore.
         */
        private void release() {
            // TODO fill in here
        	mSemaphoreToRelease.release();
        }
    }

    /**
     * The method that actually runs the ping/pong program.
     */
    public static void process(String startString, 
                               String pingString,
                               String pongString, 
                               String finishString, 
                               int maxIterations) throws InterruptedException {

        // TODO initialize this by replacing null with the appropriate
        // constructor call.
        mLatch = new CountDownLatch(2);

        // Create the ping and pong SimpleSemaphores that control
        // alternation between threads.

        // TODO - You fill in here, make pingSema start out unlocked.
        final boolean fair = true;
        SimpleSemaphore pingSema = new SimpleSemaphore(1, fair);
        // TODO - You fill in here, make pongSema start out locked.
        SimpleSemaphore pongSema = new SimpleSemaphore(0, fair);

        System.out.println(startString);

        // Create the ping and pong threads, passing in the string to
        // print and the appropriate SimpleSemaphores.
        PlayPingPongThread ping = new PlayPingPongThread(
        		pingString, pingSema, pongSema, maxIterations
        );
        PlayPingPongThread pong = new PlayPingPongThread(
        		pongString, pongSema, pingSema, maxIterations
        );

        // TODO - Initiate the ping and pong threads, which will call
        // the run() hook method.
        ping.start();
        pong.start();

        // TODO - replace the following line with a barrier
        // synchronizer call to mLatch that waits for both threads to
        // finish.
        // NOTE: Could just use join() on the two threads here, but latches are cool, so... :-)
        mLatch.await();

        System.out.println(finishString);
    }

    /**
     * The main() entry point method into PingPongRight program.
     * 
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        process("Ready...Set...Go!", 
                "Ping!  ",
                " Pong! ",
                "Done!",
                mMaxIterations);
    }
}

