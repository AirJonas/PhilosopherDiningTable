import java.util.concurrent.locks.Lock;

public class Monitor {
  private int numOfPhilosophers;
  final Lock lock;

  public Monitor() {
  }

  private enum States {hungry, thinking, eating}


}
