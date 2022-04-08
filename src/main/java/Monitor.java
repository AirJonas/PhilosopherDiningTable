import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implements monitor , that keep tracks of philosophers state.
 * Uses ReentrantLock to keep track if chopstick is picked up or not
 * to allowed philosopher to change state.
 *
 * @author  Aironas
 */
public class Monitor {
  private int numOfPhilosophers;
  final Lock lock;
  private enum States {hungry, thinking, eating};
  private States [] state;
  private Condition [] cond;

  /**
   * Constructor for monitor.
   * sets initial state and condition of philosopher.
   * @param numOfPhil number pf philosophers at the table
   */
  Monitor(int numOfPhil){
    this.numOfPhilosophers = numOfPhil;
    lock = new ReentrantLock();
    state = new States[numOfPhilosophers];
    cond = new Condition[numOfPhilosophers];
    for(int i = 0; i < numOfPhilosophers; i++){
      state[i] = States.thinking;
      cond[i] = lock.newCondition();
    }
  }

  /**
   * Method that makes Philosopher pick up two chopstick at the same time if
   * the chopsticks is available, if not waits until signal.
   * @param i philosopher number
   */
  public void PickUp(int i){
    lock.lock();
    try {
      state[i] = States.hungry;
      if((state[(i-1+numOfPhilosophers)%numOfPhilosophers] != States.eating)
      && (state[(i+1)%numOfPhilosophers] != States.eating) ){
        System.out.format("Philosopher %d picks up left chopstick\n", i+1);
        System.out.format("Philosopher %d picks up right chopstick\n", i+1);
        state[i] = States.eating;
      } else {
        try {
          cond[i].await();
          System.out.format("Philosopher %d picks up left chopstick\n", i+1);
          System.out.format("Philosopher %d picks up right chopstick\n", i+1);
          state[i] = States.eating;
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    } finally {
      lock.unlock();
    }
  }

  /**
   * Method that makes philosopher put down chopsticks at the same time and
   * checks right and left philosophers is hungry and if chopstick is available to them
   * signal them to puck up chopsticks
   * @param i
   */
  public void PutDown(int i){
    lock.lock();
    try{
      System.out.format("Philosopher %d puts down right chopstick\n", i+1);
      System.out.format("Philosopher %d puts down left chopstick\n", i+1);
      state[i] = States.thinking;
      int left = (i - 1 + numOfPhilosophers)%numOfPhilosophers;
      int left2 = (i - 2 + numOfPhilosophers)%numOfPhilosophers;
      if( (state[left] == States.hungry) &&
          (state[left2] != States.eating) ){
        cond[left].signal();
      }
      int right = (i+1)%numOfPhilosophers;
      int rihgt2 = (i+2)%numOfPhilosophers;
      if( (state[right] == States.hungry) &&
          (state[rihgt2] != States.eating) ){
        cond[right].signal();
      }
    } finally {
      lock.unlock();
    }
  }
}
