import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    public static void main(String[] args) throws InterruptedException {
	// write your code here
      ArrayList<Thread> mythreads = new ArrayList<>();
      for (int i = 0; i <20 ; i++) {
         mythreads.add(new Thread(new PhoneStation()));

      }
        for (Thread t: mythreads
        ) {
            t.start();
        }
      for (Thread t: mythreads
           ) {
          t.join();
      }

      System.out.print(String.valueOf(PhoneStation.COUNTER));


    }
}
