/**
 * This class represents a thread which holds philosopher
 *
 * @author Aironas
 */
public class Philosopher implements Runnable {
  private int myId;
  private int timesToEat;
  private Monitor mon;
  private Thread t;
  private int sleepLenght;

  /**
   * Constructor for Philosopher
   *
   * @param id index of philosopher
   * @param numToEat how many times a philosopher must eat
   * @param m monitor
   */
  Philosopher(int id, int numToEat, Monitor m){
    this.myId = id;
    this.timesToEat = numToEat;
    this.mon = m;
    sleepLenght = 4000; //how long philosopher eats in ms
    t = new Thread(this);
    t.start();
  }

  /**
   * Methods that start running in the thread
   * when the thread starts.
   */
  @Override
  public void run() {
    int count = 1;
    while(count <= timesToEat ){
      mon.PickUp(myId);
      eat(count);
      mon.PutDown(myId);
      ++count;
    }
  }

  /**
   * Method that makes philosopher to eat.
   * Prints out state of philosopher.
   * @param count how many times philosopher has eaten
   */
  void eat(int count){
    System.out.format("Philosopher %d eats (%d times)\n", myId+1, count);
    try {
      Thread.sleep(sleepLenght);
    }
    catch (InterruptedException e) {}
  }

  public static void main(String[] args) {
    int numOfPhilosophers = 5;
    int timesToEat = 1;

    Monitor mon = new Monitor(numOfPhilosophers);
    Philosopher [] p = new Philosopher[numOfPhilosophers];

    System.out.println("Dinner is starting!");
    System.out.println("");

    // Start philosopher threads.
    for(int i = 0; i < numOfPhilosophers; i++)
      p[i] = new Philosopher(i, timesToEat, mon);

    // Suspend the current thread until the philosophers have not finished eating.
    for(int i = 0; i < numOfPhilosophers; i++)
      try {
        p[i].t.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    System.out.println("");
    System.out.println("Dinner is over!");
  }
}

