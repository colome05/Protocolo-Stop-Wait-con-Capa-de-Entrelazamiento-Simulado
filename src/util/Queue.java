package util;

public interface Queue<E> extends Iterable<E> {

  /**
   * Returns the number of elements in this queue.
   */
  public int size();

  
  
  /**
   * Returns the the space currently available in this queue.
   */
  public int free();

  
  
  /**
   * Returns true if this queue contains no elements. Equivals to {@code size() == 0}.
   */
  public boolean empty();

  
  
  /**
   * Returns true if no space is currently available in this queue (cannot putCnd more elements). Equivals to
   * {@code !hasFree(1)}.
   */
  public boolean full();

  
  
  /**
   * Retrieves, but does not remove, the head (first element) of this queue, or returns null if this queue is empty.
   *
   * @return the head of this queue
   */
  public E peekFirst();

  
  
  /**
   * Retrieves and removes the head of this queue, or throws an exception if this queue is empty.
   *
   * @return the head of this queue
   * @throws IllegalStateException if this queue is empty
   */
  public E get();

  
  
  /**
   * Inserts the specified element at the tail of this queue, or throws an exception if this queue is full.
   *
   * @throws IllegalStateException if this queue is full
   */
  public void put(E e);

}
