package SeleniumTest;

public class ShiftZerosRight {
    public static void main(String[] args) {

        int[] arr = {1, 0, 5, 0, 3, 0, 2, 4};

        int index = 0; // Position to place non-zero elements

        // Move all non-zero elements to the front
        for (int num : arr) {
            if (num != 0) {
                arr[index] = num;
                index++;
            }
        }

        // Fill remaining positions with zeros
        while (index < arr.length) {
            arr[index] = 0;
            index++;
        }

        System.out.print("Array after shifting zeros to right: ");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
}
