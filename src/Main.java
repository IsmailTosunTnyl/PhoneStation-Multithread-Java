import java.util.ArrayList;



public class Main {

    public static void main(String[] args) throws InterruptedException {

        PhoneStation phoneStation = new PhoneStation();   // initialize a phone station
        ArrayList<Thread> threadArrayList = new ArrayList<>();  // initialize an array list for threads

        for (int i = 0; i < 20; i++) {
            threadArrayList.add(new Thread(phoneStation));  //creating threads
        }
        for (Thread t : threadArrayList //starting threads
        ) {
            t.start();
        }

        for (Thread t : threadArrayList //waiting threads to join main thread
        ) {
            t.join();
        }





    }
}