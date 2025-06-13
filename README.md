
# Supermarket Billing System

The **Supermarket Billing System** is a Java-based application designed to streamline the billing process in supermarkets. This project includes a user-friendly interface, product management, and dynamic billing with database integration.

---

## Features
- **Product Management:**
  - Add, update, and delete products in the inventory.
  - Manage product details such as name, price, and quantity.

- **Dynamic Billing:**
  - Generate bills based on selected products.
  - Automatically calculate the total cost.
  - Save billing information for future reference.

- **Database Integration:**
  - MySQL database to store product and billing data.
  - Seamless connection with JDBC.

- **Error Handling:**
  - Robust error handling to ensure smooth operations.

---

## Technologies Used
- **Programming Language:** Java
- **Database:** MySQL
- **Frameworks/Libraries:** JDBC
- **Development Environment:** XAMPP

---

## Installation and Setup

### Prerequisites
- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) (Version 11 or later)
- [XAMPP](https://www.apachefriends.org/index.html) (for MySQL)
- A MySQL Connector JAR file (e.g., `mysql-connector-java-8.0.x.jar`)

### Steps
1. **Clone the Repository:**
   ```bash
   git clone https://github.com/PathinaLikhita/supermarket_billing_system.git
   ```
2. **Set Up Database:**
   - Open XAMPP and start the MySQL server.
   - Create a database using the following SQL script:
     ```sql
     CREATE DATABASE supermarket_billing;

     USE supermarket_billing;

     CREATE TABLE Products (
         product_id INT AUTO_INCREMENT PRIMARY KEY,
         product_name VARCHAR(255) NOT NULL,
         price DECIMAL(10, 2) NOT NULL,
         quantity INT NOT NULL
     );

     CREATE TABLE Bills (
         bill_id INT AUTO_INCREMENT PRIMARY KEY,
         bill_date DATETIME NOT NULL,
         total_amount DECIMAL(10, 2) NOT NULL
     );

     CREATE TABLE Bill_Items (
         bill_item_id INT AUTO_INCREMENT PRIMARY KEY,
         bill_id INT NOT NULL,
         product_id INT NOT NULL,
         quantity INT NOT NULL,
         FOREIGN KEY (bill_id) REFERENCES Bills(bill_id),
         FOREIGN KEY (product_id) REFERENCES Products(product_id)
     );
     ```
3. **Configure the Project:**
   - Add the MySQL connector JAR file to your project’s classpath.
   - Update the database connection details in the `DatabaseConnection.java` file:
     ```java
     String url = "jdbc:mysql://localhost:3306/supermarket_billing";
     String username = "root"; // Replace with your MySQL username
     String password = "";     // Replace with your MySQL password
     ```

4. **Compile and Run:**
   ```bash
   javac -cp .;mysql-connector-java-8.0.x.jar *.java
   java -cp .;mysql-connector-java-8.0.x.jar Main
   ```

---

## Usage
1. Launch the application.
2. Manage products in the inventory.
3. Generate bills for customers dynamically.
4. View, update, or delete existing products.

---

## Contributing
Contributions are welcome! If you would like to improve this project, feel free to fork the repository and create a pull request.

---

## Developer
- **Name:** Pathina Likhita
- **Email:** likhithapathina@gmail.com
- **GitHub:** [PathinaLikhita](https://github.com/PathinaLikhita)

---


