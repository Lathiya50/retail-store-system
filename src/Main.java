Here is the Java code formatted with proper indentation and spacing to make it more readable:

```java
import java.io.*;
import java.util.*;

public class Main {

    // Convert char array to String
    public static String toString(char[] a) {
        return new String(a);
    }

    // Read password
    private static String readPassword() {
        Console console;
        if ((console = System.console()) != null) {
            char[] password = console.readPassword();
            for (int i = 0; i < password.length; i++) {
                System.out.print("*");
            }
            System.out.println();
            return toString(password);
        }
        return null;
    }

    // User login
    public static RegisteredUsers Login(String curruname, String currupwd, ArrayList<RegisteredUsers> registeredUsers) {
        ListIterator<RegisteredUsers> iterate = registeredUsers.listIterator();
        while (iterate.hasNext()) {
            RegisteredUsers u = iterate.next();
            if (u.getuName().equals(curruname) && u.getuPwd().equals(currupwd)) {
                System.out.println("Successfully Logged in.........");
                System.out.println();
                return u;
            }
        }
        return null;
    }

    // Function to validate password
    public static boolean isValidPassword(String regupwd) {
        try {
            boolean hasLetter = false;
            boolean hasUpperCase = false;
            boolean hasLowerCase = false;
            boolean hasDigit = false;

            for (int i = 0; i < regupwd.length(); i++) {
                char c = regupwd.charAt(i);

                // checking if character is letter
                if (Character.isLetter(c)) {
                    hasLetter = true;
                }

                // checking if character is uppercase
                if (Character.isUpperCase(c)) {
                    hasUpperCase = true;
                }

                // checking if character is lowercase
                if (Character.isLowerCase(c)) {
                    hasLowerCase = true;
                }

                // checking if character is digit
                if (Character.isDigit(c)) {
                    hasDigit = true;
                }
            }
            return hasLetter && hasUpperCase && hasLowerCase && hasDigit && regupwd.length() >= 6;

        } catch (Exception e) {
            System.out.println("Something went wrong: " + e);
            return false;
        }
    }

    // Forgot password
    public static boolean forgotPassword(String findUser, ArrayList<RegisteredUsers> registeredUsers) throws IOException {
        int found = 0;
        ListIterator<RegisteredUsers> iterate = registeredUsers.listIterator();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (iterate.hasNext()) {
            RegisteredUsers u = iterate.next();
            if (u.getuName().equals(findUser)) {
                System.out.println("Enter new password: ");
                String pwd = br.readLine();
                u.setPassword(pwd);
                return true;
            }
        }
        return false;
    }

    // Display product catalog
    public static void displayProductCatalog(ArrayList<Product> productArrayList, Boolean isMember) {
        System.out.println("--------------------------------- Product Catalog ---------------------------------");
        System.out.println();
        System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s%n", "Product ID", "Product Name", "Product Qty", "Availability", "Product Price", "Return limit");
        System.out.println();
        for (Product p : productArrayList) {
            String avail = (p.getpQty() > 0) ? "In Stock" : "Out of Stock";
            System.out.printf("%-20d %-20s %-20d %-20s %-20d %-20d%n", p.getpId(), p.getpName(), p.getpQty(), avail, (isMember ? p.getDiscoutPrice() : p.getSellPrice()), p.getLimit());
        }
        System.out.println("-----------------------------------------------------------------------------------");
    }

    // Check if username is taken
    private static boolean isUsernameTaken(ArrayList<RegisteredUsers> registeredUsers, String username) {
        for (RegisteredUsers regusr : registeredUsers) {
            if (regusr.getuName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    // Find product by ID
    public static Product findProduct(int proId, ArrayList<Product> productArrayList) {
        ListIterator<Product> iterate = productArrayList.listIterator();
        while (iterate.hasNext()) {
            Product buyprod = iterate.next();
            if (buyprod.getpId() == proId) {
                return buyprod;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Console cnl = System.console();
        Scanner scan = new Scanner(System.in);

        // Store Books data
        ArrayList<Product> productArrayList = new ArrayList<>();
        ArrayList<RegisteredUsers> regUsers;

        int ch = 0;
        System.out.println("----------------------------- Welcome to the book store ---------------------------");
        while (true) {
            try {
                System.out.println();
                System.out.println("------------------------------------- Main Menu -----------------------------------");
                System.out.println("1--->User Login");
                System.out.println("2--->Admin Login");
                System.out.println("3--->Register");
                System.out.println("4--->Exit");
                System.out.println("Enter your choice: ");
                ch = scan.nextInt();
                RegisteredUsers currUser = null;

                switch (ch) {
                    case 1:
                        System.out.println("------------------------------------- Log In --------------------------------------");
                        System.out.println("Enter Username: ");
                        String curruname = scan.next();
                        System.out.println("Enter Password: ");
                        String currupwd = readPassword();

                        // Log in and get the current user's object
                        if (fileIO.readUsers("users.txt") != null) {
                            regUsers = fileIO.readUsers("users.txt");
                            currUser = Login(curruname, currupwd, regUsers);
                            if (currUser == null) {
                                System.out.println("-----------------------------------------------------------------------------------");
                                System.out.println("1-->Forgot Password");
                                System.out.println("2-->Back to Main Menu");
                                System.out.println("-----------------------------------------------------------------------------------");

                                ch = scan.nextInt();

                                // Retry , i.e start from the beginning
                                if (ch == 2) continue;

                                // Forgot password
                                System.out.println("Enter the username: ");
                                String findUser = scan.next();
                                boolean status = forgotPassword(findUser, regUsers);

                                if (status) {
                                    fileIO.writeUsers("users.txt", regUsers);
                                    System.out.println("Password updated successfully");
                                } else {
                                    System.out.println("Password not updated or Username not found");
                                }
                            }
                        } else {
                            System.out.println("User Not Registered");
                        }
                        break;

                    case 2:
                        System.out.println("Username: ");
                        curruname = scan.next();
                        System.out.println("Password: ");
                        currupwd = readPassword();

                        // Admin LogIn
                        if (curruname.equals("admin") && currupwd.equals("admin")) {
                            Admin admin = new Admin(curruname, currupwd);
                            System.out.println("Admin Login successful!!");
                            Admin.AdminLogin(admin);
                        } else {
                            System.out.println("Admin Login Failed");
                        }
                        break;

                    case 3:
                        System.out.println("------------------------------------ Registration ---------------------------------");

                        char[] regupwd;
                        scan.nextLine();
                        System.out.println("Enter Full name: ");
                        String regufullname = scan.nextLine();
                        while (regufullname.length() < 1) {
                            System.out.println("-----------------------------------");
                            System.out.println("| Error: Please Enter the Name    |");
                            System.out.println("-----------------------------------");
                            System.out.println("Enter Full name: ");
                            regufullname = scan.nextLine();
                        }

                        System.out.println("Enter Username: ");
                        String reguuname = scan.nextLine();
                        regUsers = new ArrayList<>();
                        if (fileIO.readUsers("users.txt") != null) {
                            regUsers = fileIO.readUsers("users.txt");
                            if (isUsernameTaken(regUsers, reguuname)) {
                                System.out.println("-->Error: Username is already taken, please try again with a different username");
                            } else {
                                regupwd = cnl.readPassword("Enter Password: ");
                                while (!isValidPassword(new String(regupwd))) {
                                    System.out.println("-------------------------------------------------------------------------------------------------------");
                                    System.out.println("Password must contain 6 characters long, one lowercase letter, one uppercase letter, and one digit.");
                                    System.out.println("-------------------------------------------------------------------------------------------------------");
                                    regupwd = cnl.readPassword("Enter password: ");
                                }

                                System.out.println("Want to be a member(1-->yes/0-->No): ");
                                int ismem = scan.nextInt();
                                while (ismem > 1) {
                                    System.out.println("Please enter 0 or 1");
                                    System.out.println("Want to be a member(1-->yes/0-->No): ");
                                    ismem = scan.nextInt();
                                }

                                boolean member = (ismem == 1);
                                RegisteredUsers newuser = new RegisteredUsers(reguuname, new String(regupwd), regufullname, member);
                                regUsers.add(newuser);
                                fileIO.writeUsers("users.txt", regUsers);
                                System.out.println("User Successfully registered.........");
                            }
                        } else {
                            regupwd = cnl.readPassword("Enter Password: ");

                            while (!isValidPassword(new String(regupwd))) {
                                System.out.println("-------------------------------------------------------------------------------------------------------");
                                System.out.println("Password must contain 6 characters long, one lowercase letter, one uppercase letter, and one
