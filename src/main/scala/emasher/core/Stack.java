package emasher.core;

public class Stack<E> {
	private int count;
	private SNode<E> head;
	
	public Stack() {
		head = null;
		count = 0;
	}
	
	public void push( E data ) {
		head = new SNode<E>( data, head );
		count++;
	}
	
	public E pop() {
		E result = null;
		
		if( head != null ) {
			result = head.data;
			head = head.next;
			count--;
		}
		
		return result;
	}
	
	public E peek() {
		E result = null;
		
		if( head != null ) {
			result = head.data;
		}
		
		return result;
	}
	
	public int getCount() {
		return count;
	}
	
	private class SNode<T> {
		public SNode<T> next;
		public T data;
		
		public SNode( T data, SNode<T> next ) {
			this.data = data;
			this.next = next;
		}
	}
}
