import java.util.ArrayList;
import java.util.List;

public class FoodQueue {

    private final List<Customer> customers;
    private final int maxCapacity;
    public FoodQueue(int maxCapacity){
        this.customers = new ArrayList<>();
        this.maxCapacity = maxCapacity;
    }

    public boolean isQueueEmpty(){
        return customers.size() == 0;
    }

    public boolean isQueueFull(){
        return customers.size() == maxCapacity;
    }

    public boolean addCustomer(Customer customer) {
        if(!isQueueFull()){
            customers.add(customer);
            return true;
        }
        return false;
    }


    public int getQueueLength() {
        return customers.size();
    }

    public List<Customer> getCustomers(){
        return new ArrayList<>(customers);
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public Customer removeServedCustomer(int actualId){
        if(actualId >= 0 && actualId < customers.size()){
            return customers.remove(0);
        }
        return null;
    }

    public Customer removeCustomer(int actualCustomerIdx) {
        if(actualCustomerIdx >= 0 && actualCustomerIdx < customers.size()){
            return customers.remove(actualCustomerIdx);
        }
        return null;
    }
    
}
