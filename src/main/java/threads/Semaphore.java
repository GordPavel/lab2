package threads;

public class Semaphore{
    private boolean canWrite = true;

    synchronized void beginRead() throws InterruptedException{
        while( canWrite )
            wait();
    }

    synchronized void beginWrite() throws InterruptedException{
        while( !canWrite )
            wait();
    }

    synchronized void endRead(){
        canWrite = true;
        notifyAll();
    }

    synchronized void endWrite(){
        canWrite = false;
        notifyAll();
    }
}
