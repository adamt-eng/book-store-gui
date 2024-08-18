package main_app.services;

import main_app.models.Book;

public interface AdminService
{
    public void addBook(Book book);
    public void editBook(Book book, Book updatedBook);
    public void deleteBook(Book book);
}