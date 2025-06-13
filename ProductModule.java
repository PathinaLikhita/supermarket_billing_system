import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ProductModule {
    private static final Scanner scanner = new Scanner(System.in);

    // Add Product
    public static void addProduct() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        while(name.trim().isEmpty()) {
        System.out.println("Product name cannot be empty. Please enter a valid name.");
        name = scanner.nextLine();
}


        double price = 0;
        int quantity = 0;

        while (true) {
            System.out.print("Enter price: ");
            try {
                price = Double.parseDouble(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid price.");
            }
        }

        while (true) {
            System.out.print("Enter quantity: ");
            try {
                quantity = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid quantity.");
            }
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Products (product_name, price, quantity) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setInt(3, quantity);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Product added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update Product
    public static void updateProduct() {
        System.out.print("Enter product ID to update: ");
        int productId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter new price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter new quantity: ");
        int quantity = scanner.nextInt();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE Products SET product_name = ?, price = ?, quantity = ? WHERE product_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setInt(3, quantity);
            stmt.setInt(4, productId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Product updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View All Products
    public static void viewProducts() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Products";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n--- Product List ---");
            System.out.printf("%-10s %-30s %-10s %-10s%n", "Product ID", "Product Name", "Price", "Quantity");
            System.out.println("--------------------------------------------------------------");

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                System.out.printf("%-10d %-30s %-10.2f %-10d%n", productId, productName, price, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Product
    public static void deleteProduct() {
        System.out.print("Enter product ID to delete: ");
        int productId = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM Products WHERE product_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Product deleted successfully!");
            } else {
                System.out.println("No product found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

public static void generateBill() {
    try (Connection conn = DatabaseConnection.getConnection()) {
        // 1. Query all products with quantity > 0
        String sql = "SELECT product_id, product_name, price, quantity FROM Products WHERE quantity > 0";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        System.out.println("--- Generating Bill ---");
        System.out.printf("%-10s %-20s %-10s %-10s%n", "Product ID", "Product Name", "Price", "Quantity");
        System.out.println("------------------------------------------------------------");

        double totalAmount = 0.0;

        // Store product info temporarily for later use (you can use a List of objects or arrays)
        java.util.List<int[]> productsToUpdate = new java.util.ArrayList<>();

        while (rs.next()) {
            int productId = rs.getInt("product_id");
            String productName = rs.getString("product_name");
            double price = rs.getDouble("price");
            int quantity = rs.getInt("quantity");

            double productTotal = price * quantity;
            totalAmount += productTotal;

            System.out.printf("%-10d %-20s %-10.2f %-10d%n", productId, productName, price, quantity);

            // Save for later update: product_id, quantity, price (price can be fetched again if needed)
            productsToUpdate.add(new int[]{productId, quantity});
        }

        System.out.println("------------------------------------------------------------");
        System.out.printf("Total Amount: %.2f%n", totalAmount);

        // 2. Insert bill record
        String insertBillSql = "INSERT INTO Bills (bill_date, total_amount) VALUES (NOW(), ?)";
        PreparedStatement billStmt = conn.prepareStatement(insertBillSql, PreparedStatement.RETURN_GENERATED_KEYS);
        billStmt.setDouble(1, totalAmount);
        billStmt.executeUpdate();

        ResultSet generatedKeys = billStmt.getGeneratedKeys();
        int billId = 0;
        if (generatedKeys.next()) {
            billId = generatedKeys.getInt(1);
        }

        // 3. For each product, insert bill items and update quantity to 0
        String updateProductSql = "UPDATE Products SET quantity = 0 WHERE product_id = ?";
        String insertBillItemSql = "INSERT INTO Bill_Items (bill_id, product_id, quantity, price_per_unit) VALUES (?, ?, ?, ?)";

        PreparedStatement updateStmt = conn.prepareStatement(updateProductSql);
        PreparedStatement insertBillItemStmt = conn.prepareStatement(insertBillItemSql);

        for (int[] product : productsToUpdate) {
            int productId = product[0];
            int quantity = product[1];

            // Get price again for Bill_Items insert
            PreparedStatement priceStmt = conn.prepareStatement("SELECT price FROM Products WHERE product_id = ?");
            priceStmt.setInt(1, productId);
            ResultSet priceRs = priceStmt.executeQuery();
            double price = 0.0;
            if (priceRs.next()) {
                price = priceRs.getDouble("price");
            }
            priceRs.close();
            priceStmt.close();

            // Update product quantity to 0
            updateStmt.setInt(1, productId);
            updateStmt.executeUpdate();

            // Insert into Bill_Items
            insertBillItemStmt.setInt(1, billId);
            insertBillItemStmt.setInt(2, productId);
            insertBillItemStmt.setInt(3, quantity);
            insertBillItemStmt.setDouble(4, price);
            insertBillItemStmt.executeUpdate();
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
//clear cart
public static void clearCart() {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "UPDATE Products SET quantity = 0 WHERE quantity > 0";
        PreparedStatement stmt = conn.prepareStatement(sql);
        int rowsAffected = stmt.executeUpdate();

        // Remove or comment out the following message:
        // if (rowsAffected == 0) {
        //     System.out.println("Cart is already empty.");
        // }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



}
