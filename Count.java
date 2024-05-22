public class Count {
	
	public static int count(int[] array, int element) {
		
		int total = 0;

		try {
			for (int i : array) {
				if (i == element) {
					total += 1;
				}
			}
		} catch (NullPointerException e) {
			System.out.println("Null");
		}
		return total;
	}
	
	public static void main(String[] args) {
		
		int[] array = {1, 1, 5, 6, 5, 3, 8, 1, 9, 2, 8};
		int result = count(array, 1); //3

	}
	
}