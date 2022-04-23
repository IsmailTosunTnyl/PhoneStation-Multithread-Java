import java.util.concurrent.Semaphore;

public class PhoneStation implements Runnable {
    static Semaphore OPERATOR = new Semaphore(2);
    static Semaphore LINE = new Semaphore(1);
    static int COUNTER = 0;
    private String id;
    static int WAITING = 0;




    @Override
    public void run() {
        WAITING++;
        while (true) {
            printStatitonState(OPERATOR.availablePermits(), WAITING, COUNTER,LINE.availablePermits());

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
        printStatitonState(OPERATOR.availablePermits(), WAITING, COUNTER,LINE.availablePermits());

    }

    private void printStatitonState(int operators, int waiting, int done,int line) {
        System.out.print("\033[H\033[2J");
        System.out.flush();


        System.out.print("Line: ");
        printbar(1,line,5);
        System.out.print("Operators: ");
        printbar(2,operators,0);
        System.out.print("Waiting: ");
        printbar(20,waiting,2);
        System.out.print("Done: ");
        printbar(20,done,5);


    }
    private void printbar(int range, int data , int offset){
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
}
