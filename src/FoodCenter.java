import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FoodCenter {
    private final List<FoodQueue> queues;
    private final FoodQueue waitingQueue = new FoodQueue(10);
    public int totalBurgerStock = 50;
    public int currentBurgerStock;
    private int firstQueueIncome;
    private int secondQueueIncome;
    private int thirdQueueIncome;
    public FoodCenter() {
        // Create an ArrayList that contains three FoodQueues of different sizes
        this.queues = new ArrayList<>(Arrays.asList(new FoodQueue(2), new FoodQueue(3), new FoodQueue(5)));
        this.currentBurgerStock = 50;
    }

    public void viewAllQueues() {
        System.out.println("*".repeat(18) + "\n" + "**   Cashiers   **" + "\n" + "*".repeat(18));
        // Loop through each row of seats
        for (int i = 0; i < 5; i++) {
            StringBuilder row = new StringBuilder();
            // Loop through each queue
            for (FoodQueue queue : queues) {
                int occupiedSeats = queue.getCustomers().size();
                // Check if the current seat is occupied or empty or out of bounds
                if (i < occupiedSeats) {
                    row.append("O \t\t");
                } else if (i < queue.getMaxCapacity()) {
                    row.append("X \t\t");
                } else {
                    row.append("  \t\t");
                }
            }
            // Print the row with spaces at the beginning
            if(i == 2)
                System.out.println(" \t\t".repeat(i - 1 ) +row.toString().trim());
            else if( i == 3 || i == 4){
                System.out.println(" \t\t".repeat(2) +row.toString().trim());
            }
            else{
                System.out.println(row.toString().trim());
            }
        }
        System.out.println("\n" + "X – Not Occupied    O – Occupied ");
    }


    public void viewAllEmptyQueues() {
        System.out.println("Empty Queues:");
        for (int i = 0; i < queues.size(); i++) {
            FoodQueue queue = queues.get(i);
            if (queue.getCustomers().isEmpty()) {
                System.out.println("Queue " + (i + 1));
            }
        }
    }

    public void addCustomerToQueue(String firstName , String lastName,int burgersRequired) {
        Customer customer = new Customer(firstName,lastName,burgersRequired);
        int minQueueIndex =-1;
        int minQueueLength = Integer.MAX_VALUE;

        for (int i = 0; i < queues.size(); i++) {
            FoodQueue queue = queues.get(i);
            int queueLength = queue.getQueueLength();
            if (queueLength < minQueueLength) {
                minQueueLength = queueLength;
                minQueueIndex = i;
            }
        }

        boolean added = false; // a flag to indicate if the customer is added
        int count = 0; // a counter to keep track of how many queues are checked
        while (!added && count < queues.size()) {
            FoodQueue selectedQueue = queues.get(minQueueIndex);
            if (selectedQueue.addCustomer(customer)) {
                System.out.println("Customer '" + customer.getFirstName() + " " + customer.getLastName() +" "+ "'added to Queue " + (minQueueIndex + 1) + ".");
                added = true;
            } else {
                minQueueIndex = (minQueueIndex + 1) % queues.size(); // move to the next queue in circular order
                count++; // increment the counter
            }
        }

        if (!added) {
            if(!(waitingQueue.isQueueFull())){
                waitingQueue.addCustomer(customer);
                System.out.println("All the cashier queues are full. Customer '" + customer.getFirstName() + " " + customer.getLastName() +" "+ "'added to Waiting List.");
            }else{
                System.out.println("Waiting List is full.");
            }
        }

}

    public void removeCustomerFromQueue(int cashierIdx, int customerIdx) {
        int actualCashierIdx = cashierIdx -1;
        int actualCustomerIdx = customerIdx - 1;
        if(actualCashierIdx >= 0 && actualCashierIdx<queues.size()){
            FoodQueue queue = queues.get(actualCashierIdx);
            Customer customer = queue.removeCustomer(actualCustomerIdx);
            if (customer != null) {
                System.out.println("Customer '" + customer.getFirstName() + " " + customer.getLastName() + "' removed from Queue " + (cashierIdx) + ".");
            } else {
                System.out.println("Invalid customer index in Queue " + (cashierIdx) + ".");
            }
        } else {
            System.out.println("Invalid queue index.");
        }

    }

    public void removeServedCustomerFromQueue(int cashierId) {
        int actualCashierIndex = cashierId - 1;
        if (actualCashierIndex >= 0 && actualCashierIndex < queues.size()) {
            FoodQueue queue = queues.get(actualCashierIndex);
            Customer customer = queue.removeServedCustomer(0);
            if (customer != null) {
                currentBurgerStock -= customer.getBurgersRequired();
                if (currentBurgerStock <= 10) {
                    System.out.println("Warning: Low stock!");
                }
                switch (cashierId) {
                    case 1 -> firstQueueIncome += customer.getBurgersRequired() * 650;
                    case 2 -> secondQueueIncome += customer.getBurgersRequired() * 650;
                    default -> thirdQueueIncome += customer.getBurgersRequired() * 650;
                }
                System.out.println("Customer '" + customer.getFirstName() + " " + customer.getLastName() + "' served and removed from the queue " + cashierId);
                if(!waitingQueue.isQueueEmpty()){
                    Customer nextCustomer = waitingQueue.removeCustomer(0);
                    if (nextCustomer != null){
                        queues.get(actualCashierIndex).addCustomer(nextCustomer);
                        System.out.println("Customer '" + nextCustomer.getFirstName() + " " + nextCustomer.getLastName() +" "+ "'moved from Waiting List to Queue " + cashierId + ".");
                    }else{
                        System.out.println("No customers in Queue " + cashierId + ".");
                    }
                }
            } else {
                System.out.println("No customers to serve.");
            }
        }
    }

    public void viewCustomersSorted() {
        List<Customer> allCustomers = new ArrayList<>();
        for (FoodQueue queue : queues){
            allCustomers.addAll(queue.getCustomers());
        }
        allCustomers.sort(Comparator.comparing(Customer::getFirstName));
        System.out.println("Customers Sorted in alphabetical order:");
        for (Customer customer : allCustomers) {
            System.out.println(customer.getFirstName() + " " + customer.getLastName());
        }
    }

    public void storeProgramData() {
        try{
            FileWriter fileWriter = new FileWriter("foodCenterProgramData.txt");
            int lineCount = 1;
            // Loop through each cashier queue
            for (FoodQueue queue : queues) {
                int index = 1;
                fileWriter.write("Cashier queue: " + lineCount);
                for (Customer customer : queue.getCustomers()) {
                    if (!(customer == null)) {
                        fileWriter.write("\n "+ "\t Queue -> position " + index + " "+customer.getFirstName()+"  " + (customer.getLastName())+ " has ordered " + (customer.getBurgersRequired()) +" burgers.");
                        index++;
                    }
                }
                lineCount++;
                fileWriter.write("\n");
            }


            fileWriter.close();

            System.out.println("Stored all the data successfully.......");

        } catch (IOException exception){
            System.out.println("Something went wrong with storing program data!");
        }

    }

    public void loadProgramData() {
        try{
            File fileInput = new File("foodCenterProgramData.txt");
            //Reading file with scanner object
            Scanner readFile = new Scanner(fileInput);

            String fileLine;
            while (readFile.hasNext()){
                fileLine = readFile.nextLine();
                System.out.println( fileLine);

            }
            readFile.close();
        }
        catch (Exception exception){
            System.out.println("Something went wrong with loading program data!");
        }
    }

    public void viewRemainingBurgerStock() {
        System.out.println("Remaining burgers in stock: " + (currentBurgerStock));
        if (currentBurgerStock <= 10) {
            System.out.println("Warning: Low stock!");
        }
    }

    public void addBurgersToStock(int quantity) {
        currentBurgerStock += quantity;
        System.out.println(quantity + " burgers added to stock.");
    }

    public void printBurgerIncome() {
        int[] income = {firstQueueIncome,secondQueueIncome,thirdQueueIncome};
        for (int i =0; i < income.length; i++) System.out.println("Income from the Queue "+ (i + 1) +" : Rs."  + income[i]);
        System.out.println("\n total income from all the queues : Rs." + (firstQueueIncome+secondQueueIncome +thirdQueueIncome));
    }
}

