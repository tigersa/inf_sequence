package inf_sequence;

import java.io.File;
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
	
	public static void main(String args[]){	
		ArrayList<ArrayList<Byte>> a = new ArrayList<ArrayList<Byte>>();
		readDataFromFile(a);
				
		for (int i = 0; i < a.size(); i++)
			System.out.println("Number: " + a.get(i) + ", Index in sequence: " + findIndex(a.get(i)));
	}
	
	private static long findIndex(ArrayList<Byte> number) {
		
		for(int digit = 1; digit <= number.size(); digit++){
			for(int start_index = 0; start_index <= number.size() - digit; start_index++){//выделяем из искомой последовательности часть цифр и считаем их числом
				long index = try_to_find_sequence(number, digit, start_index);  //пытаемся найти слева числа на единицу меньше и справа на единицу больше пока последовательность не законичится или число не будет найдено
				if (index != -1){
					return index;
				}
			}
		}
		return -1;
	}

	private static long try_to_find_sequence(ArrayList<Byte> number, int digit, int start_index) {
		ArrayList<Byte> value = new ArrayList<Byte>();
		for(int i = start_index + digit - 1; i >= start_index; i--) value.add(number.get(i));

		int left_i = start_index - 1, right_i = start_index + digit;
		
		ArrayList<Byte> prev_value = value;
		while(left_i >= 0){
			prev_value = decrement(prev_value);
			int count = Math.min(prev_value.size() - 1, left_i) + 1;
			for(int i = 0; i < count; i++){
				if(number.get(left_i - i) != prev_value.get(i)){
					return -1;
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
					return -1;
				}
			}
			right_i += count;
		}
		
		//последовательность найдена, считаем индекс
		long n = 0;
		for(int i = 0; i < value.size(); i++){
			n += value.get(i) * Math.pow(10, i);
		}
		long index = 0;
		for(int i = 1; i < value.size(); i++){
			index += i * (Math.pow(10, i) - Math.pow(10, i - 1));
		}
		index += value.size() * (n - Math.pow(10, value.size() - 1)) + 1;
		return index - start_index;
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

}
