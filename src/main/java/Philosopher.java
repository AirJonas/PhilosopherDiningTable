/**
 * this class represents a thread which holds a philosopher
 *
 * @author Aironas
 */
public class Philosopher implements Runnable {
  private int myId;
  private int timesToEat;
  private Monitor mon;
  private Thread t;
  private int sleepLenght;

  Philosopher(int id, int numToEat, Monitor m){
    this.myId = id;
    this.timesToEat = numToEat;
    this.mon = m;
    sleepLenght = 10;
    t = new Thread(this);
    t.start();
  }

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

  void eat(int count){
    System.out.format("Philosopher %d eats (%d times)\n", myId+1, count);
    try {
      Thread.sleep(sleepLenght);
    }
    catch (InterruptedException e) {}
  }

  public static void main(String[] args) {
    int numOfPhilosophers = 5;
    int timesToEat = 5;

    Monitor mon = new Monitor(numOfPhilosophers);
    Philosopher [] p = new Philosopher[numOfPhilosophers];

    System.out.println("Dinner is starting!");
    System.out.println("");

    // Start philosopher threads.
    for(int i = 0; i < numOfPhilosophers; i++)
      p[i] = new Philosopher(i, timesToEat, mon);

    // Suspend the current thread until the philosophers have not done.
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

