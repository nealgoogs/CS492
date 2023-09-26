package Homework2;

import java.util.*;

public class blockCipherEncryption {

	public static int[] substitute(int[] four) {

		int[] newFour = new int[4];
		String input = "" + four[0] + four[1] + four[2] + four[3];

		switch (input) {
		case "0000":
			newFour = new int[] { 1, 0, 0, 1 };
			break;
		case "0001":
			newFour = new int[] { 1, 0, 1, 0 };
			break;
		case "0010":
			newFour = new int[] { 0, 1, 1, 0 };
			break;
		case "0011":
			newFour = new int[] { 0, 0, 1, 1 };
			break;
		case "0100":
			newFour = new int[] { 1, 1, 0, 0 };
			break;
		case "0101":
			newFour = new int[] { 0, 1, 0, 1 };
			break;
		case "0110":
			newFour = new int[] { 1, 1, 1, 0 };
			break;
		case "0111":
			newFour = new int[] { 0, 1, 1, 1 };
			break;
		case "1000":
			newFour = new int[] { 1, 0, 1, 1 };
			break;
		case "1001":
			newFour = new int[] { 1, 1, 0, 1 };
			break;
		case "1010":
			newFour = new int[] { 0, 0, 0, 1 };
			break;
		case "1011":
			newFour = new int[] { 0, 1, 0, 0 };
			break;
		case "1100":
			newFour = new int[] { 0, 0, 1, 0 };
			break;
		case "1101":
			newFour = new int[] { 1, 1, 1, 1 };
			break;
		case "1110":
			newFour = new int[] { 0, 0, 0, 0 };
			break;
		case "1111":
			newFour = new int[] { 1, 0, 0, 0 };
			break;
		}

		return newFour;
	}// Substitute method

	public static int[] ecbEncrypt(int[] input) {

		int[] output = new int[input.length];
		int[] temp = new int[4];

		for (int i = 0; i < input.length; i += 4) {
			System.arraycopy(input, i, temp, 0, 4);
			int[] newBlock = substitute(temp);
			System.arraycopy(newBlock, 0, output, i, 4);
		}

		return output;
	}// ecbEncrypt

	public static int[] ctrEncrypt(int[] input, int counter) {
		int[] output = new int[input.length];
		int[] temp = new int[4];

		for (int i = 0, initCounter = counter; i < input.length; i += 4, counter++) {
			int[] counterBits = { (initCounter >> 3) & 1, (initCounter >> 2) & 1, (initCounter >> 1) & 1,
					initCounter & 1 };

			int[] encrypted = substitute(counterBits);

			System.arraycopy(input, i, temp, 0, 4);
			for (int j = 0; j < 4; j++) {
				output[i + j] = temp[j] ^ encrypted[j];
			}
		}

		return output;
	}// ctrEncrypt

	public static int[] cbcEncrypt(int[] input, int[] IV) {

		int[] output = new int[input.length];
		int[] temp = new int[4];
		int[] last = IV;

		for (int i = 0; i < input.length; i += 4) {
			System.arraycopy(input, i, temp, 0, 4);
			for (int j = 0; j < 4; j++) {
				temp[j] ^= last[j];
			}

			int[] encrypted = substitute(temp);

			System.arraycopy(encrypted, 0, output, i, 4);
			last = encrypted;
		}

		return output;
	}// cbcEncrypt

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter plaintext: ");
		String plaintext = scanner.next();


		System.out.print("Enter IV as a number: ");
		int ivNumber = scanner.nextInt();

		int[] input = convertToBinaryArray(plaintext);
		int[] IV = convertToBinaryArray(ivNumber);

		int remainder = input.length % 4;
		if (remainder != 0) {
			int paddingLength = 4 - remainder;
			int[] paddedInput = new int[input.length + paddingLength];
			System.arraycopy(input, 0, paddedInput, 0, input.length);
			input = paddedInput;
		} // Pad plaintext for conversion to binary

		System.out.println("ECB Encryption: " + formatBinaryArray(ecbEncrypt(input)));
		System.out.println("CTR Encryption: " + formatBinaryArray(ctrEncrypt(input, ivNumber)));
		System.out.println("CBC Encryption: " + formatBinaryArray(cbcEncrypt(input, IV)));

		scanner.close();
	}

	public static int[] convertToBinaryArray(String s) {
		StringBuilder binaryString = new StringBuilder();
		for (char c : s.toCharArray()) {
			binaryString.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
		}

		int[] binaryArray = new int[binaryString.length()];
		for (int i = 0; i < binaryString.length(); i++) {
			binaryArray[i] = binaryString.charAt(i) == '1' ? 1 : 0;
		}
		return binaryArray;
	}

	public static int[] convertToBinaryArray(int number) {
		String binaryString = Integer.toBinaryString(number);
		int length = binaryString.length();
		int[] binaryArray = new int[Math.max(4, length)]; // At least 4 bits, or more if needed
		for (int i = 0; i < length; i++) {
			binaryArray[i + (binaryArray.length - length)] = binaryString.charAt(i) == '1' ? 1 : 0;
		}
		return binaryArray;
	}

	public static String formatBinaryArray(int[] binaryArray) {
	    StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < binaryArray.length; i++) {
	        builder.append(binaryArray[i]);
	        if ((i + 1) % 4 == 0 && i + 1 < binaryArray.length) {
	            builder.append(' ');
	        }
	    }
	    return builder.toString();
	}

}
