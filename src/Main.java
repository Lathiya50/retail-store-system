import java.io.*;
import java.io.Console;
import java.util.*;

public class Main {

  // Convert char array to String
  public static String toString(char[] a) {
    String string = new String(a);

    return string;
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
  public static RegisteredUsers Login(
      String curruname,
      String currupwd,
      ArrayList<RegisteredUsers> registeredUsers) {
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

        if (Character.isLowerCase(c)) {
          hasLowerCase = true;
        }
        // checking if character is Number
        if (Character.isDigit(c)) {
          hasDigit = true;
        }
      }
      return hasLetter && hasUpperCase && hasLowerCase && hasDigit && regupwd.length() >= 6;

    } catch (Exception e) {
      System.out.println("Something went wrong" + e);
      return false;
    }
  }

  // Forgot password
  public static boolean forgotPassword(
      String findUser,
      ArrayList<RegisteredUsers> registeredUsers) throws IOException {
    // Searches for the username
    int found = 0;
    ListIterator<RegisteredUsers> iterate = registeredUsers.listIterator();
    InputStreamReader r = new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(r);
    while (iterate.hasNext()) {
      RegisteredUsers u = iterate.next();
      if (u.getuName().equals(findUser)) {
        System.out.println("Enter new password : ");
        Scanner scan = new Scanner(System.in);
        String pwd = br.readLine();
        u.setPassword(pwd);
        return true;
      }
    }
    return false;
  }

  public static void displayProductCatalog(
      ArrayList<Product> productArrayList, Boolean isMember) {
    System.out.println(
        "--------------------------------- Product Catalog ---------------------------------");
    System.out.println();
    System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s%n", "Product ID", "Product Name", "Product Qty",
        "Availability",
        "Product Price", "Return limit");
    System.out.println();
    String avail;
    for (Product p : productArrayList) {
      if (p.getpQty() > 0)
        avail = "In Stock";
      else
        avail = "Out of Stock";
      System.out.printf("%-20d %-20s %-20d %-20s %-20d %-20d %n", p.getpId(), p.getpName(), p.getpQty(), avail,
          (isMember ? p.getDiscoutPrice() : p.getSellPrice()), p.getLimit());
    }
    System.out.println();
    System.out.println(
        "-----------------------------------------------------------------------------------");
  }

  private static boolean isUsernameTaken(
      ArrayList<RegisteredUsers> registeredUsers,
      String username) {
    for (RegisteredUsers regusr : registeredUsers) {
      if (regusr.getuName().equals(username)) {
        return true;
      }
    }
    return false;
  }

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
    Console console;
    Console cnl = System.console();
    Scanner scan = new Scanner(System.in);

    // Store Books data
    ArrayList<Product> productArrayList = new ArrayList<>();
    ArrayList<RegisteredUsers> regUsers;

    // regUsers = fileIO.readUsers("users.txt");
    // if (fileIO.readProduct("products.txt") != null) {
    // productArrayList = fileIO.readProduct("products.txt");
    // }

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
        System.out.println("Enter your choice : ");
        ch = scan.nextInt();
        RegisteredUsers currUser = null;
        switch (ch) {
          case 1:
            System.out.println(
                "------------------------------------- Log In --------------------------------------");
            System.out.println("Enter Username : ");
            String curruname = scan.next();
            System.out.println("Enter Password : ");
            String currupwd = readPassword();

            // Log in and get the current user's object
            if (fileIO.readUsers("users.txt") != null) {
              regUsers = fileIO.readUsers("users.txt");
              currUser = Login(curruname, currupwd, regUsers);
              if (currUser == null) {
                System.out.println(
                    "-----------------------------------------------------------------------------------");
                System.out.println("1-->Forgot Password");
                System.out.println("2-->Back to Main Menu");
                System.out.println(
                    "-----------------------------------------------------------------------------------");

                ch = scan.nextInt();

                // Retry , i.e start from the beginning
                if (ch == 2)
                  continue;

                // Forgot password
                System.out.println("Enter the username : ");
                String findUser = scan.next();
                boolean status = forgotPassword(findUser, regUsers);

                if (status == true) {
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
            System.out.println("Username : ");
            curruname = scan.next();
            System.out.println("Password : ");
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
            System.out.println("Enter Full name : ");
            String regufullname = scan.nextLine();
            while (regufullname.length() < 1) {
              System.out.println("-----------------------------------");
              System.out.println("| Error: Please Enter the Name    |");
              System.out.println("-----------------------------------");
              System.out.println("Enter Full name: ");
              regufullname = scan.nextLine();
            }
            System.out.println("Enter Username : ");
            String reguuname = scan.nextLine();
            regUsers = new ArrayList<>();
            if (fileIO.readUsers("users.txt") != null) {
              regUsers = fileIO.readUsers("users.txt");
              if (isUsernameTaken(regUsers, reguuname)) {
                System.out.println("-->Error: Username is already taken, please try again with a different username");
              } else {

                regupwd = cnl.readPassword("Enter Password : ");
                while (!isValidPassword(new String(regupwd))) {
                  System.out.println(
                      "-------------------------------------------------------------------------------------------------------");
                  System.out.println(
                      "Password must contain 6 characters long, one lowercase letter, one uppercase letter, and one digit.    ");
                  System.out.println(
                      "-------------------------------------------------------------------------------------------------------");
                  regupwd = cnl.readPassword("Enter  password:");
                  System.out
                      .println("-----------------------------------------------------------------------------------");
                }

                System.out.println("Want to be a member(1-->yes/0-->No) : ");
                int ismem = scan.nextInt();
                while (ismem > 1) {
                  System.out.println("Please enter 0 or 1");
                  System.out.println("Want to be a member(1-->yes/0-->No) : ");
                  ismem = scan.nextInt();
                }

                boolean member;
                if (ismem == 1)
                  member = true;
                else
                  member = false;

                RegisteredUsers newuser = new RegisteredUsers(
                    reguuname,
                    new String(regupwd),
                    regufullname,
                    member);
                regUsers.add(newuser);
                fileIO.writeUsers("users.txt", regUsers);
                System.out.println("User Successfully registered.........");
                // ListIterator<RegisteredUsers> iterate = regUsers.listIterator();
                // while (iterate.hasNext()) {
                // RegisteredUsers u = iterate.next();
                // System.out.println(u.getFullName());
                // System.out.println(u.getuName());
                // }
              }
            } else {
              // System.out.println("Enter Password : ");
              regupwd = cnl.readPassword("Enter Password : ");

              while (!isValidPassword(new String(regupwd))) {
                System.out.println(
                    "-------------------------------------------------------------------------------------------------------");
                System.out.println(
                    "Password must contain 6 characters long, one lowercase letter, one uppercase letter, and one digit.    ");
                System.out.println(
                    "-------------------------------------------------------------------------------------------------------");
                regupwd = cnl.readPassword("Enter  password:");
                System.out
                    .println("-----------------------------------------------------------------------------------");
              }

              System.out.println("Want to be a member(1-->yes/0-->No) : ");
              int ismem = scan.nextInt();
              while (ismem > 1) {
                System.out.println("Please enter 0 or 1");
                System.out.println("Want to be a member(1-->yes/0-->No) : ");
                ismem = scan.nextInt();
              }
              boolean member;
              if (ismem == 1)
                member = true;
              else
                member = false;

              RegisteredUsers newuser = new RegisteredUsers(
                  reguuname,
                  new String(regupwd),
                  regufullname,
                  member);
              regUsers.add(newuser);
              fileIO.writeUsers("users.txt", regUsers);
              System.out.println();
              System.out.println("-->User Successfully registered.........");
            }
            break;
          case 4:
            // fileIO.writeUsers("users.txt", regUsers);
            // fileIO.writeProduct("products.txt", productArrayList);
            System.exit(0);
            break;
          default:
            System.out.println("Invalid Choice!!");
            break;
        }

        // Perform the operations untill the user is logged in
        while (currUser != null) {
          // Display the available books in the store
          if (fileIO.readProduct("products.txt") != null) {
            productArrayList = fileIO.readProduct("products.txt");
            displayProductCatalog(productArrayList, currUser.getMember());
          } else {
            System.out.println("-->No products To Display");
          }

          System.out.println(
              "-----------------------------------------------------------------------------------");
          System.out.println("1-->Profile");
          System.out.println("2-->Issue/Purchase Product");
          System.out.println("3-->Return/Cancel Product");
          System.out.println("4-->Logout");
          System.out.println(
              "-----------------------------------------------------------------------------------");
          System.out.println("Enter your choice : ");
          try {
            int logichoice = scan.nextInt();
            System.out.println();
            switch (logichoice) {
              // Show profile
              case 1:
                regUsers = fileIO.readUsers("users.txt");
                for (RegisteredUsers user : regUsers) {
                  if (user.getuName().equals(currUser.getuName())) {
                    user.ShowProfile();
                    break;
                  }
                }
                System.out.println(
                    "-----------------------------------------------------------------------------------");
                System.out.println("1-->Back");
                System.out.println("2-->Logout");
                System.out.println(
                    "-----------------------------------------------------------------------------------");

                System.out.println("Enter your choice : ");
                int profilechoice = scan.nextInt();

                // Logout
                if (profilechoice == 2) {
                  currUser = null;
                  System.out.println("-->Successfully Logged Out!!");
                }

                break;
              // Issue/Purchase Product
              case 2:
                System.out.println(
                    "Enter the product id which you want to purchase - ");
                int proId = scan.nextInt();

                // Find the book and purchase the book
                Product foundproduct = findProduct(proId, productArrayList);

                if (foundproduct == null) {
                  System.out.println();
                  System.out.println("-->please enter valid book id...");
                } else {
                  if (foundproduct.getpQty() > 0) {
                    // currUser.PurchaseProduct(foundproduct);
                    regUsers = fileIO.readUsers("users.txt");
                    for (RegisteredUsers user : regUsers) {
                      if (user.getuName().equals(currUser.getuName())) {
                        user.PurchaseProduct(foundproduct);
                        // user.ShowProfile();
                        fileIO.writeUsers("users.txt", regUsers);
                        break;
                      }
                    }
                    System.out.println();
                    System.out.println("-->Product purchaased successfully!!");
                    foundproduct.decreaseQtyby(1);
                    fileIO.writeProduct("products.txt", productArrayList);
                  } else {
                    System.out.println();
                    System.out.println("-->Out of Stock!!");
                  }
                }
                break;
              // Return/Cancel Product
              case 3:
                regUsers = fileIO.readUsers("users.txt");
                for (RegisteredUsers user : regUsers) {
                  if (user.getuName().equals(currUser.getuName())) {
                    currUser = user;
                    break;
                  }
                }
                if (currUser.boughtProducts.size() == 0) {
                  System.out.println("You haven't bought Any Product yet!!");
                  break;
                }
                System.out.println(
                    "--------------------------------- Purchased Product ---------------------------------");
                System.out.printf("%-20s %-20s %-20s %-20s %n", "Product ID", "Product Name",
                    "Product price", "Return limit");
                System.out.println();
                for (Product product : currUser.boughtProducts) {
                  System.out.printf("%-20d %-20s %-20d %-20d%n", product.getpId(), product.getpName(),
                      (currUser.getMember() ? product.getDiscoutPrice()
                          : product
                              .getSellPrice()),
                      product.getLimit());
                }

                System.out.println("Enter the product id which you want to return ");
                proId = scan.nextInt();
                foundproduct = null;
                // foundproduct = currUser.ReturnProduct(proId);

                for (RegisteredUsers user : regUsers) {
                  if (user.getuName().equals(currUser.getuName())) {
                    foundproduct = user.ReturnProduct(proId);
                    fileIO.writeUsers("users.txt", regUsers);
                    break;
                  }
                }
                if (foundproduct != null) {
                  for (Product product : productArrayList) {
                    if (product.getpId() == proId) {
                      product.increaseQtyby(1);
                      fileIO.writeProduct("products.txt", productArrayList);
                      break;
                    }
                  }

                  System.out.println();
                  System.out.println("--> Product returned successfully!!");
                } else {
                  System.out.println();
                  System.out.println("--> Product not found!!");
                }
                break;
              // Log out
              case 4:
                currUser = null;
                System.out.println();
                System.out.println("Successfully Logged Out!!");
                break;
              default:
                break;
            }
          } catch (Exception e) {
            System.out.println();
            System.out.println("Error Caught--->" + e);
            scan.nextLine();
          }
        }
      } catch (Exception e) {
        System.out.println();
        System.out.println("Exception caught-->" + e);
      }
    }
  }
}
