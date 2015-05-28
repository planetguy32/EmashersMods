package emasher.sockets.packethandling;


public class NetworkUtilities {

	public static void toByte( byte[] out, int in, int start ) {
		out[start++] = ( byte ) ( in >> 24 );
		out[start++] = ( byte ) ( in >> 16 );
		out[start++] = ( byte ) ( in >> 8 );
		out[start++] = ( byte ) in;
	}

	public static int toInteger( byte[] in, int start ) {
		int value = 0;
		for( int i = start; i < start + 4; i++ ) {
			value = ( value << 8 ) + ( in[i] & 0xFF );
		}
		return value;
	}

	public static byte boolToByte( boolean b ) {
		if( b ) return ( byte ) 1;
		return ( byte ) 0;
	}

	public static boolean byteToBool( byte b ) {
		if( b == 0 ) return false;
		return true;
	}
}
