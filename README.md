# **ReadMate App**

## Overview

**ReadMate** is an app designed for reading e-books and exploring downloadable books from APIs. It allows users to browse, purchase, and read e-books while providing a seamless experience with Firebase for user authentication, data storage, notifications, and more. The app also includes an admin module for managing book collections.

## Preview
![Preview1](MergedImages(1).png)
![Preview2](MergedImages(2).png)
![Preview3](MergedImages(3).png)
![Preview3](MergedImages(4).png)
![Preview3](MergedImages(5).png)


## Features

1. **Book Browsing & Categories:**
   - Explore two main categories:  
     **Library** (Firebase database books) and **Explore** (downloadable books via API).
   - Search for any book by title or category in the Explore section.
   
2. **Library Books:**
   - Users can buy books from the Library and add them to their **MyBooks** screen.  
     Purchased books are e-books that can be read within the app.
   - Add books to the **Bookcase** for later reference (save for buying or recommending).
   - Delete books from the Bookcase when no longer needed.

3. **Book Reviews & Related Books:**
   - View reviews from other users for each book in the Library.
   - See related books based on the same category.

4. **Downloadable Books:**
   - Books in the Explore section can be downloaded as PDFs.
   - These books are free and can be printed or read outside the app.

5. **Payments & Transactions:**
   - Track payment methods and credit card information for book purchases.
   - Manage transactions directly within the app.

6. **Notifications:**
   - Personalized notifications for each user, provided via Firebase Cloud Messaging.

7. **User Authentication:**
   - Log in via Google or sign up with email and password, using Firebase Authentication.

8. **Additional Features:**
   - Terms & Conditions and Help & Support screens.
   - Admin module for adding and deleting books in the Firebase Library.
   - Upcoming: **Dark Mode** for users who prefer a dark theme.

## Tech Stack

### UI Libraries:
- **Circular Image**: For circular image views.
- **Glide**: Efficient image loading.
- **Navigation Fragment**: For fragment navigation.
- **ViewPager2**: Used for the onboarding screen.
- **ViewBinding**: For binding UI elements efficiently.

### API Libraries:
- **Retrofit**: For handling network requests.
- **Gson Converter**: For JSON parsing.
- **OkHttp**: For managing HTTP calls.
- **Logging Interceptor**: For logging network requests.

To include the information about the API used in your application in the README file, you can add a section that describes the external API source. Below is an example of how you can incorporate this:

## API Used
The application utilizes the [dBooks API](https://www.dbooks.org/api/) to fetch book data, including book details and other relevant information. The API provides a comprehensive set of resources to support the functionality of the application.

**Base URL**: `https://www.dbooks.org/api/`

This API is used to:
- Retrieve book details.
- Retrieve recent books and provide a download link for each book.
- Search for any book you want and you are looking for.

For more information on how to interact with the API, refer to the official website at [dbooks official website](https://www.dbooks.org/).


### Remote Database:
- **Firebase**:
  - **Authentication**: User sign-in and sign-up.
  - **Firestore**: Storing and retrieving book data.
  - **Cloud Messaging**: For sending notifications.
  - **Storage**: For saving profile images and Firebase Books images.

### Dependency Injection:
- **Koin**: To manage dependency injection throughout the app.

### Architecture:
- **MVVM** (Model-View-ViewModel):
  - **Model**: Data layer (APIs, Firebase, Room).
  - **Repository**: Manages data sources.
  - **ViewModel**: Handles UI logic.
  - **View**: UI components.

## Installation & Setup

1. Clone the repository.
2. Add your **Firebase project credentials** in the app.
3. Set up necessary dependencies (Koin, Retrofit, etc.).
4. Build and run the app on an Android device or emulator.

## How to Use

1. **Explore Books**: Navigate to the **Explore** section to search for and download free books.
2. **Library**: Purchase books from the Library and view them in your **MyBooks** section.
3. **Notifications**: Stay updated with personalized alerts.
4. **Admin Module**: Manage book collections if you have admin access.

## Future Enhancements

- **Dark Mode**: Provide an alternate theme for users who prefer darker colours.
