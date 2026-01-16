package SeleniumTest;

import java.util.Scanner;

public class TestViva {

	public static void main(String[] args) {
//		int[] arr = {11, 12, 15, 16, 19};
//		int n= arr.length +1;
//		
//		int sum = (n* (n+1)/2);
//		int arrsum = 0;
//		
//		for (int num:arr) {
//			arrsum += num;
//			int missingNumber = sum -arrsum;
//			System.out.println("Missing Number: " + missingNumber);
//		}
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter a Number: ");
		int n= sc.nextInt();
		
		boolean isPrime= true;
		
		if (n<=1) {
			isPrime =false;
		}
		else {
			for (int i=2; i<=n/2; i++)
			{
				if (n% i==0) {
					isPrime= false;
					break;
				}
			}
		}
		if (isPrime) {
			System.out.println(n + " Number is Prime Number.");
		}
		else {
			System.out.println(n + "Number is Not a Prime Number.");
		}
	sc.close();
	}
	
}
