import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class PhoneStation implements Runnable {
    static Semaphore OPERATOR = new Semaphore(2);
    static Semaphore LINE = new Semaphore(1);
    static int COUNTER = 0;
    private String id;
    static int WAITING = 0;

    public PhoneStation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                do {

                    printStatitonState();

                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }


                } while (COUNTER < 20);

                printStatitonState();
                System.out.println(String.valueOf(PhoneStation.COUNTER + " People Talked"));
                System.out.println("Press Enter for exit");
                new Scanner(System.in).nextLine();
            }
        }).start();


    }


    @Override
    public void run() {
        WAITING++;
        while (true) {


            if (OPERATOR.tryAcquire()) {
                if (LINE.tryAcquire()) {
                    WAITING--;

                    try {
                        Thread.sleep((long) (Math.random() * 2000));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    COUNTER++;
                    OPERATOR.release();
                    LINE.release();

                    break;
                } else {

                    try {
                        Thread.sleep((long) (Math.random() * 500));
                        OPERATOR.release();
                        Thread.sleep((long) (Math.random() * 500));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {


                try {
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }


    }

    private void printStatitonState() {
        clearConsole();


        System.out.print("Line: ");
        printbar(1, LINE.availablePermits(), 5);
        System.out.print("Operators: ");
        printbar(2, OPERATOR.availablePermits(), 0);
        System.out.print("Waiting: ");
        printbar(20, WAITING, 2);
        System.out.print("Done: ");
        printbar(20, COUNTER, 5);


    }

    private void printbar(int range, int data, int offset) {
        for (int i = 0; i < offset; i++) {
            System.out.print(" ");
        }
        System.out.print("[");
        for (int i = 0; i < range; i++) {
            if (data > 0) {
                System.out.print("=");
                data--;
            } else System.out.print(" ");
        }
        System.out.println("]");

    }


    private static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (Exception e) {

        }

    }
}
