package main_app.models;

import java.util.ArrayList;

import main_app.services.*;
import main_app.utils.*;

public class Admin extends User implements AdminService
{
    public Admin(String username, String email, String password)
    {
        super(username, email, password);
    }

    public void addBook(Book book)
    {
        FileManager.appendFile(Constants.BOOKS_FILE_PATH, book.getName() + "," + book.getAuthor() + "," + book.getPrice() + "," + book.getStock() + "," +
        book.getCategory());
    }

    public void editBook(Book book, Book updatedBook)
    {
        ArrayList<String> booksinfo = FileManager.readFile(Constants.BOOKS_FILE_PATH);
        int i;

        for (i = 0; i < booksinfo.size(); i++)
        {
            if (booksinfo.get(i).contains(book.getName()))
            {
                break;
            }
        }
        booksinfo.remove(i);
        booksinfo.add(i, updatedBook.getName() + "," + updatedBook.getAuthor() + "," + updatedBook.getPrice() + "," + updatedBook.getStock() + "," + updatedBook.getCategory());

        FileManager.writeFile(Constants.BOOKS_FILE_PATH, booksinfo);
    }

    public void deleteBook(Book book)
    {
        ArrayList<String> books = FileManager.readFile(Constants.BOOKS_FILE_PATH);

        for (int i = 0; i < books.size(); i++)
        {
            if (books.get(i).contains(book.getName()))
            {
                books.remove(i);
                break;
            }
        }

        FileManager.writeFile(Constants.BOOKS_FILE_PATH, books);
    }
}
