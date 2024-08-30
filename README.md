# Online Book Store Management System

## UML Diagram

<p align="center">
  <img src="Media/UML.jpeg" alt="UML Diagram" width="70%">
</p>
<p align="center">Figure 1: UML Diagram</p>

## Overview of the System

<p align="center">
  <img src="Media/Main Page.jpg" alt="Main Menu" width="70%">
</p>
<p align="center">Figure 2: Main Menu</p>

<p align="center">
  <img src="Media/Login.png" alt="Login Page" width="50%">
</p>
<p align="center">Figure 3: Login Page</p>

## Admin Functions

<p align="center">
  <img src="Media/admin.png" alt="Admin Menu" width="70%">
</p>
<p align="center">Figure 4: Admin Menu</p>

<p align="center">
  <img src="Media/Admin Functions.png" alt="Admin Functions Overview" width="50%">
</p>
<p align="center">Figure 5: Admin Functions Overview</p>

### Add a Book
The admin can add new books to the store's inventory using the "Add New Book" dialog. Once the admin fills in the book's data, pressing save will call the `addBook(Book book)` method.

<p align="center">
  <img src="Media/Add New Book.png" alt="Add New Book Dialog" width="25%">
</p>
<p align="center">Figure 6: Add New Book Dialog</p>

<p align="center">
  <img src="Media/addBook.png" alt="addBook Implementation" width="50%">
</p>
<p align="center">Figure 7: addBook Implementation</p>

### Edit a Book
The admin can edit the details of an existing book using the "Edit Book" dialog. Once the admin modifies the book's data, pressing save will call the `editBook(Book book, Book updatedBook)` method.

<p align="center">
  <img src="Media/Edit Book.png" alt="Edit Book Dialog" width="25%">
</p>
<p align="center">Figure 8: Edit Book Dialog</p>

<p align="center">
  <img src="Media/editBook.png" alt="editBook Implementation" width="50%">
</p>
<p align="center">Figure 9: editBook Implementation</p>

### Delete a Book
The admin can remove a book from the store's inventory using the `deleteBook(Book book)` method.

<p align="center">
  <img src="Media/Delete Book.png" alt="Delete Book Button" width="25%">
</p>
<p align="center">Figure 10: Delete Book Button</p>

<p align="center">
  <img src="Media/deleteBook.png" alt="deleteBook Implementation" width="75%">
</p>
<p align="center">Figure 11: deleteBook Implementation</p>

## Reader Functions
New users can register as readers. Once the user fills in the required data, pressing Register will call the `register(String email, String username, String password, String phoneNumber, String address, String paymentMethod)` method.

<p align="center">
  <img src="Media/Register Form.png" alt="Registration Form" width="45%">
</p>
<p align="center">Figure 12: Registration Form</p>

<p align="center">
  <img src="Media/register.png" alt="register Implementation" width="75%">
</p>
<p align="center">Figure 13: register Implementation</p>

<p align="center">
  <img src="Media/Reader Menu.png" alt="Reader Menu" width="60%">
</p>
<p align="center">Figure 14: Reader Menu</p>

### Edit Account Information
The reader can edit their account information using the Edit Account Information page. Once the reader has modified their data, pressing yes will call the `updateReaderInformation()` method, which will update the user's data in the bookstore's database.

<p align="center">
  <img src="Media/Edit Information.png" alt="Edit Information Page" width="55%">
</p>
<p align="center">Figure 15: Edit Information Page</p>

<p align="center">
  <img src="Media/updateReaderInformation.png" alt="updateReaderInformation Implementation" width="60%">
</p>
<p align="center">Figure 16: updateReaderInformation Implementation</p>

### Show Previous Orders
The reader can view all their previous orders on this page.

<p align="center">
  <img src="Media/Show Previous Orders.png" alt="Show Previous Orders Page" width="50%">
</p>
<p align="center">Figure 17: Show Previous Orders Page</p>

<p align="center">
  <img src="Media/previousOrders.png" alt="Initialization of the previousOrders ArrayList" width="75%">
</p>
<p align="center">Figure 18: Initialization of the previousOrders ArrayList</p>

### Display Books
The reader can view all books *with stock more than 0* and can search for a specific book. The reader can select a book and add it to the cart.

<p align="center">
  <img src="Media/Display Books.png" alt="Display Books Page" width="75%">
</p>
<p align="center">Figure 19: Display Books Page</p>

Adding a book to the cart with the Add to Cart button will call the `addToCart(Book book)` method.

<p align="center">
  <img src="Media/addToCart.png" alt="addToCart Implementation" width="60%">
</p>
<p align="center">Figure 20: addToCart Implementation</p>

### Display Cart
The reader can view the books currently in their shopping cart on this page.

<p align="center">
  <img src="Media/Your Cart.png" alt="Your Cart Page" width="75%">
</p>
<p align="center">Figure 21: Your Cart Page</p>

The reader can also remove books from the cart, which will call the `removeFromCart(Book bookToRemove)` method.

<p align="center">
  <img src="Media/removeFromCart.png" alt="removeFromCart Implementation" width="50%">
</p>
<p align="center">Figure 22: removeFromCart Implementation</p>

### Go To Receipt
In the receipt page, the user can use the Pay button, which will call the `generateOrder(ArrayList<Book> books)` method.

<p align="center">
  <img src="Media/Receipt.png" alt="Receipt page" width="75%">
</p>
<p align="center">Figure 23: Receipt page</p>

<p align="center">
  <img src="Media/generateOrder.png" alt="generateOrder Implementation" width="75%">
</p>
<p align="center">Figure 24: generateOrder Implementation</p>
