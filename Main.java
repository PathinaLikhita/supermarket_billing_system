import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Supermarket Billing System ---");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. View Products");
            System.out.println("4. Delete Product");
            System.out.println("5. Generate Bill");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    ProductModule.addProduct();
                    break;
                case 2:
                    ProductModule.updateProduct();
                    break;
                case 3:
                    ProductModule.viewProducts();
                    break;
                case 4:
                    ProductModule.deleteProduct();
                    break;
                case 5:
                    ProductModule.generateBill();
                    break;
                case 6:
                    ProductModule.clearCart();
                    System.out.println("\nTHANK YOU for shopping!");
                    System.out.println("Have a nice day ahead.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}


