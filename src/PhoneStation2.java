import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class PhoneStation2 implements Runnable {
    static Semaphore OPERATOR = new Semaphore(2);   //control access to operators
    static Semaphore LINE = new Semaphore(1);       //control access to line
    static int COUNTER = 0;
    static int WAITING_OP = 0;  //keep operator queue
    static int WAITING_L = 0;   //keep line queue

    public PhoneStation2() {
        new Thread(new Runnable() {   // updating console
            @Override
            public void run() {
                do {

                    printStatitonState();

                    try {
                        Thread.sleep(40);   //update every .. milliseconds
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }


                } while (COUNTER < 20); // update until 20 people talked

                printStatitonState();
                System.out.println(COUNTER + " People Talked");
                System.out.println("Press Enter for exit");
                new Scanner(System.in).nextLine();
            }
        }).start();


    }


    @Override
    public void run() {
        WAITING_OP++;   // adding every thread to operator waiting queue
        try {
            getOperator();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    private void getOperator() throws InterruptedException {
        if (OPERATOR.tryAcquire()) {
            Thread.sleep((long) (Math.random() * 2000)); //communication with operator
            WAITING_OP--;
            WAITING_L++;
            getLine();

        } else {
            Thread.sleep((long) (Math.random() * 2000)); //communication with operator
            getOperator();

        }
    }

    private synchronized void getLine() throws InterruptedException {
        if (!LINE.tryAcquire()) {
            wait();
        }
        Thread.sleep((long) (Math.random() * 2000)); //simulating talking time

        COUNTER++;              //updating static variables for visual check
        WAITING_L--;
        LINE.release();         //releasing semaphore for line
        OPERATOR.release();      //releasing semaphore for operator
        notify();               //notify thread which waiting for line


    }

    private void printStatitonState() {
        clearConsole();                //clear consol before write updated info

        // print status bars with tags
        System.out.print("Line: ");
        printBar(1, LINE.availablePermits(), 6);
        System.out.print("Operators: ");
        printBar(2, OPERATOR.availablePermits(), 1);
        System.out.print("Waiting_L: ");
        printBar(20, WAITING_L, 1);
        System.out.print("Waiting_op: ");
        printBar(20, WAITING_OP, 0);
        System.out.print("Done: ");
        printBar(20, COUNTER, 6);


    }

    private void printBar(int range, int data, int offset) {
        for (int i = 0; i < offset; i++) {      //print offset for different tag
            System.out.print(" ");
        }
        System.out.print("[");
        for (int i = 0; i < range; i++) {       //print every status bar
            if (data > 0) {
                System.out.print("=");
                data--;
            } else System.out.print(" ");
        }
        System.out.println("]");

    }


    private static void clearConsole() {
        //clear console depends on operating system
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
