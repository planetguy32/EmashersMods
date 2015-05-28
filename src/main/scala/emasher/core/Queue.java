package emasher.core;

public class Queue<E> {
	private int count;
	private QNode<E> head;
	private QNode<E> foot;
	
	public Queue() {
		count = 0;
		head = null;
		foot = null;
	}
	
	public void enqueue( E data ) {
		QNode<E> temp = new QNode<E>( data );
		
		if( head == null ) {
			head = temp;
			foot = temp;
		} else {
			foot.next = temp;
			foot = temp;
		}
		
		count++;
	}
	
	public E dequeue() {
		E result = null;
		
		if( head != null ) {
			result = head.data;
			head = head.next;

			if( head == null ) {
				foot = null;
			}
			
			count--;
		}
		
		return result;
	}
	
	public int getCount() {
		return count;
	}
	
	private class QNode<T> {
		public T data;
		public QNode<T> next;
		
		public QNode( T data ) {
			this.data = data;
			this.next = null;
		}
	}
}
