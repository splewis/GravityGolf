package game;

import structures.*;

public final class DataWriter {

	/**
	 * Returns an encoded value based of the input value and unique key.
	 * @param value the input value
	 * @param key the unique input key
	 * @return the encoded value
	 */
	public static long encode(int value, int key) {
		if (CalcHelp.isPrime(key)) {
			return (value * 3) + key * key * key;
		}
		if (key % 6 == 0) {
			return (value + key) * (key + 5);
		}
		if (key % 4 == 0) {
			return -15 * value + key;
		}
		if (key % 5 == 0) {
			return value * value + key;
		}
		if (key % 7 == 0) {
			return value + key * key * key;
		}
		if (key % 2 == 0) {
			return 10 * value - 20 - key * key;
		}
		return 8 * value - 6 * key * key;
	}

}