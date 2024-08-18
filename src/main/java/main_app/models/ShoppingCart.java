package main_app.models;

import java.util.ArrayList;

import main_app.utils.*;

public class ShoppingCart
{
    private ArrayList<Book> addedBooks;

    public ShoppingCart(Reader reader)
    {
        addedBooks = new ArrayList<Book>();
    }

    public ArrayList<Book> getAddedBooks()
    {
        return addedBooks;
    }

    public boolean isShoppingCartEmpty()
    {
        return addedBooks.isEmpty();
    }

    public void addToCart(Book book)
    {
        for (String line : FileManager.readFile(Constants.BOOKS_FILE_PATH))
        {
            if (line.split(",")[0].equals(book.getName()))
            {
                addedBooks.add(book);
                break;
            }
        }
    }

    public void removeFromCart(Book bookToRemove)
    {
        int i;
        for (i = 0; i < addedBooks.size(); i++)
        {
            if (addedBooks.get(i) == bookToRemove)
            {
                break;
            }
        }
        addedBooks.remove(i);
    }
}
