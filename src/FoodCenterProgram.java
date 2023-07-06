import java.util.InputMismatchException;
import java.util.Scanner;

public class FoodCenterProgram {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FoodCenter foodCenter = new FoodCenter();

        while (true) {
            System.out.println("-".repeat(35));
            System.out.println("Welcome to Foodies Fave Food Center");
            System.out.println("-".repeat(35));
            System.out.println("*".repeat(26) + "\n" + "****   Menu Options   ****" + "\n" + "*".repeat(26));
            System.out.println("""
                100 or VAQ: view all Queues.
                101 or VEQ: View all Empty Queues.
                102 or ACQ: Add customer to a Queue.
                103 or RCQ: Remove a customer from a Queue.
                104 or PCQ: Remove a served customer.
                105 or VCS: View Customers Sorted in alphabetical order.
                106 or SPD: Store Program Data into file.
                107 or LPD: Load Program Data from file.
                108 or STK: View Remaining burgers Stock.
                109 or AFS: Add burgers to Stock.
                110 0r IFQ: print the income of each queue.
                999 or EXT: Exit the Program.
                """);
            System.out.println("*".repeat(26));
            System.out.print("Enter your choice here: ");
            String choice = scanner.next().toUpperCase();
            scanner.nextLine();

            switch (choice) {
                case "100", "VFQ" -> foodCenter.viewAllQueues();
                case "101", "VEQ" -> foodCenter.viewAllEmptyQueues();
                case "102", "ACQ" -> {
                    System.out.println("Enter customer's first name:  ");
                    try {
                        // Reference 01 :https://www.geeksforgeeks.org/how-to-validate-a-username-using-regular-expressions-in-java/
                        // Reference 02 : https://stackoverflow.com/questions/15805555/java-regex-to-validate-full-name-allow-only-spaces-and-letters
                        // regular expression to validate the customer name
                        String regex = "^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0}$"; // Unicode-based regex
                        // String regex = "^[A-Za-z\\s]+[\\.\\-\\']{0,1}[A-Za-z\\s]*$"; // ASCII-based regex
                        String firstName = scanner.nextLine().toLowerCase();
                        if (firstName.matches(regex)) {
                            System.out.println("Enter customer's second name: ");
                            String lastName = scanner.nextLine().toLowerCase();
                            if (lastName.matches(regex)) {
                                System.out.print("Enter the number of burgers required: ");
                                int burgersRequired = scanner.nextInt();
                                scanner.nextLine();
                                if(burgersRequired <= foodCenter.currentBurgerStock && foodCenter.currentBurgerStock > 0)
                                    foodCenter.addCustomerToQueue(firstName, lastName, burgersRequired);
                                else{
                                    System.out.println("Invalid input. only " + foodCenter.currentBurgerStock +" are Remaining.Refill burgers before adding customers.");
                                }
                            } else {
                                System.out.println("Invalid input. Please enter valid customer's last name.");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter valid customer's first name.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid input.");
                        scanner.nextLine(); // clear the buffer
                    }
                }
                case "103", "RCQ" -> {
                    System.out.print("Enter the cashier queue number (1-3): ");
                    try {
                        int cashierIdx = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        if (cashierIdx >= 1 && cashierIdx <= 3) {
                            System.out.print("Enter the customer position in the queue:");
                            try {
                                int customerIdx = scanner.nextInt();
                                scanner.nextLine(); // Consume the newline character
                                if (customerIdx >= 0 && customerIdx <= 5) {
                                    foodCenter.removeCustomerFromQueue(cashierIdx, customerIdx);
                                } else {
                                    System.out.println("Invalid input. Please enter a valid customer position in the queue.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please enter a numeric value for the customer index.");
                                scanner.nextLine(); // clear the buffer
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a cashier index between 1 and 3.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a numeric value for the cashier index.");
                        scanner.nextLine(); // clear the buffer
                    }
                }
                case "104", "PCQ" -> {
                    System.out.print("Enter the cashier queue number (1-3): ");
                    try {
                        int cashierId = scanner.nextInt();
                        if (cashierId >= 1 && cashierId <= 3) {
                            if(foodCenter.currentBurgerStock > 0) {
                                foodCenter.removeServedCustomerFromQueue(cashierId);
                            }else System.out.println("Burgers are out of stock. refill burger stock before serving customers.");
                        } else {
                            System.out.println("Invalid input. Please enter a cashier index between 1 and 3.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a numeric value for the cashier index.");
                        scanner.nextLine(); // clear the buffer
                    }
                }
                case "105", "VCS" -> foodCenter.viewCustomersSorted();
                case "106", "SPD" -> foodCenter.storeProgramData();
                case "107", "LPD" -> foodCenter.loadProgramData();
                case "108", "STK" -> foodCenter.viewRemainingBurgerStock();
                case "109", "AFS" -> {
                    try {
                        int burgerStock = foodCenter.totalBurgerStock - foodCenter.currentBurgerStock;
                        if (foodCenter.currentBurgerStock < 50) {
                            System.out.println("There are " + (burgerStock) + " empty spaces to store burgers");
                            System.out.print("Enter the quantity of burgers to add: ");
                            int quantity = scanner.nextInt();
                            if (quantity > foodCenter.currentBurgerStock) {
                                foodCenter.addBurgersToStock(quantity);
                            } else {
                                System.out.println("Enter a valid burger count between 1 to " + burgerStock);
                            }

                        } else {
                            System.out.println("The burgers stock is full !");
                        }
                    } catch (Exception exception) {
                        System.out.println("Something went Wrong with adding burgers");
                    }
                }
                case "110","IFQ" ->foodCenter.printBurgerIncome();
                case "999", "EXT" -> {
                    scanner.close();
                    System.out.println("Exiting the program...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        }
    }
}