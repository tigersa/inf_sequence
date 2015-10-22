package inf_sequence;

import java.io.File;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.ArrayList;	
import javax.swing.JOptionPane;

public class Main {
	
	private static void readDataFromFile(ArrayList<ArrayList<Byte>> array) {
		Scanner scanner;
		try{
			scanner = new Scanner(new File("res//input.txt"));
			while(scanner.hasNext()){
				String value = scanner.next();
				ArrayList<Byte> vector_value = new ArrayList<Byte>();
				for(int i = 0; i < value.length(); i++){
					String s = value.substring(i, i + 1);
					vector_value.add((byte)Integer.parseInt(s));
				}
				array.add(vector_value);
			}
			
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Файл не найден!");
		}
	}
	
	private static ArrayList<Byte> increment(ArrayList<Byte> value) {
		ArrayList<Byte> new_value = new ArrayList<Byte>();
		Byte next_degree = 0;
		for(int i = 0; i < value.size(); i++){
			byte v = (byte)(value.get(i) + ((i == 0) ? 1 : 0) + next_degree);
			next_degree = (byte) (v / 10);
			new_value.add((byte)(v % 10));
		}
		if (next_degree != 0) new_value.add((byte)(1));
		return new_value;
	}
	
	private static ArrayList<Byte> decrement(ArrayList<Byte> value) {
		ArrayList<Byte> new_value = new ArrayList<Byte>();
		Byte next_degree = 0;
		for(int i = 0; i < value.size(); i++){
			byte v = (byte)(value.get(i) - ((i == 0) ? 1 : 0) - next_degree);
			next_degree = (byte) ((v == -1) ? 1 : 0);
			if (i == value.size() - 1 && (byte)((v + 10) % 10) == 0) break;
			new_value.add((byte)((v + 10) % 10));
		}
		return new_value;
	}
	
	private static BigInteger findIndex(ArrayList<Byte> number) {
		
		for(int digit = 1; digit <= number.size(); digit++){
			for(int start_index = 0; start_index <= Math.min(number.size() - digit, digit - 1); start_index++){//выделяем из искомой последовательности часть цифр и считаем их числом
				BigInteger index = try_to_find_sequence(number, digit, start_index);  //пытаемся найти слева числа на единицу меньше и справа на единицу больше пока последовательность не законичится или число не будет найдено
				if (index.compareTo(BigInteger.valueOf(-1)) != 0){
					return index;
				}
			}
		}
		return BigInteger.valueOf(-1);
	}

	private static BigInteger try_to_find_sequence(ArrayList<Byte> number, int digit, int start_index) {
		ArrayList<Byte> value = new ArrayList<Byte>();
		if (number.get(start_index) == 0) return BigInteger.valueOf(-1); //число не может начинаться с нуля
		for(int i = start_index + digit - 1; i >= start_index; i--) value.add(number.get(i));
		
		
		int left_i = start_index - 1, right_i = start_index + digit;
		
		ArrayList<Byte> prev_value = value;
		while(left_i >= 0){
			prev_value = decrement(prev_value);
			int count = Math.min(prev_value.size() - 1, left_i) + 1;
			for(int i = 0; i < count; i++){
				if(number.get(left_i - i) != prev_value.get(i)){
					return BigInteger.valueOf(-1);
				}
			}
			left_i -= count;
		}
		
		ArrayList<Byte> next_value = value;
		while(right_i < number.size()){
			next_value = increment(next_value);
			int count = Math.min(next_value.size() - 1, number.size() - right_i - 1) + 1;
			for(int i = 0; i < count; i++){
				if(number.get(right_i + i) != next_value.get(next_value.size() - i - 1)){
					return BigInteger.valueOf(-1);
				}
			}
			right_i += count;
		}
		
		//последовательность найдена, считаем индекс
		BigInteger n = BigInteger.valueOf(0);
		for(int i = 0; i < value.size(); i++){
			n = n.add(BigInteger.valueOf(value.get(i)).multiply(BigInteger.valueOf(10).pow(i)));
		}
		BigInteger index = new BigInteger("0");
		for(int i = 1; i < value.size(); i++){
			//index += i * (Math.pow(10, i) - Math.pow(10, i - 1));
			index = index.add(BigInteger.valueOf(i).multiply(BigInteger.valueOf(10).pow(i).subtract(BigInteger.valueOf(10).pow(i - 1))));
		}
		//index += value.size() * (n - Math.pow(10, value.size() - 1)) + 1;
		index = index.add(BigInteger.valueOf(value.size()).multiply(n.subtract(BigInteger.valueOf(10).pow(value.size() - 1)))).add(BigInteger.valueOf(1));
		index = index.subtract(BigInteger.valueOf(start_index));
		return index;
	}
	
	public static void main(String args[]){	
		ArrayList<ArrayList<Byte>> a = new ArrayList<ArrayList<Byte>>();
		readDataFromFile(a);
				
		for (int i = 0; i < a.size(); i++){
			String number = "";
			for (int j = 0; j < a.get(i).size(); j++){
				number += a.get(i).get(j).toString();
			}
			System.out.println("Number: " + number + ", Index in sequence: " + findIndex(a.get(i)));
		}
	}
}
