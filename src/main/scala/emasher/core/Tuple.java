package emasher.core;

public class Tuple {
	int[] values;
	
	public Tuple( int... newValues ) {
		values = newValues;
	}
	
	public int x() {
		int result = 0;
		
		if( values.length >= 1 ) {
			result = values[0];
		}
		
		return result;
	}
	
	public int y() {
		int result = 0;
		
		if( values.length >= 2 ) {
			result = values[1];
		}
		
		return result;
	}
	
	public int z() {
		int result = 0;
		
		if( values.length >= 3 ) {
			result = values[2];
		}
		
		return result;
	}
	
	public int getValue( int index ) {
		int result = 0;
		
		if( values.length >= index + 1 ) {
			result = values[index];
		}
		
		return result;
	}
}
