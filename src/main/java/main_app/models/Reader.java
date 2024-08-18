package main_app.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import main_app.utils.*;

public class Reader extends User
{
    private String phoneNumber, address, paymentMethod;
    private ShoppingCart shoppingCart;
    private ArrayList<Order> previousOrders;

    public Reader(String username, String email, String password, String phoneNumber,
            String address, String paymentMethod)
    {
        super(username, email, password);

        this.phoneNumber = phoneNumber;
        this.address = address;
        this.paymentMethod = paymentMethod;

        this.shoppingCart = new ShoppingCart(this);

        this.previousOrders = new ArrayList<Order>();
        ArrayList<String> orders = FileManager.readFile(Constants.ORDERS_FILE_PATH);
        
        for (int i = 0; i < orders.size(); i++)
        {
            String[] orderDetails = SecurityService.decrypt(orders.get(i)).split(",");
            if (orderDetails[2].equals(this.email))
            {
                try
                {
                    this.previousOrders.add(new Order(Integer.parseInt(orderDetails[0]),
                            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(orderDetails[1]),
                            this, Double.parseDouble(orderDetails[3])));
                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<Order> getPreviousOrders()
    {
        return previousOrders;
    }

    public ShoppingCart getShoppingCart()
    {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart)
    {
        this.shoppingCart = shoppingCart;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPaymentMethod()
    {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod)
    {
        this.paymentMethod = paymentMethod;
    }

    public void updateReaderInformation()
    {
        ArrayList<String> readersinfo = FileManager.readFile(Constants.READERS_FILE_PATH);

        int index;

        for (index = 0; index < readersinfo.size(); index++)
        {
            if (SecurityService.decrypt(readersinfo.get(index)).contains(email))
            {
                break;
            }
        }

        readersinfo.set(index, SecurityService.encrypt(email + "," + username + "," + password
                + "," + phoneNumber + "," + address + "," + paymentMethod));

        FileManager.writeFile(Constants.READERS_FILE_PATH, readersinfo);
    }

    public static boolean register(String email, String username, String password, String phoneNumber, String address,
            String paymentMethod)
    {
        for (String user : FileManager.readFile(Constants.READERS_FILE_PATH))
        {
            if (SecurityService.decrypt(user).contains(email))
            {
                return false;
            }
        }

        FileManager.appendFile(Constants.READERS_FILE_PATH, SecurityService.encrypt(email + "," + username + "," +
                SecurityService.hash(password) + "," + phoneNumber + "," + address + "," + paymentMethod));

        return true;
    }
}
