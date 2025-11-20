# readmate

## 🚀 Overview
**ReadMate** is a comprehensive eBook and downloadable book store app designed to provide users with a seamless reading experience. With features like book browsing, purchasing, reading, and downloading, ReadMate leverages Firebase for user authentication, data storage, and notifications. The app also includes an admin module for managing book collections, making it a powerful tool for both users and administrators.

## Preview
![Preview1](MergedImages(1).png)
![Preview2](MergedImages(2).png)
![Preview3](MergedImages(3).png)
![Preview3](MergedImages(4).png)
![Preview3](MergedImages(5).png)

### Key Features:
- **Book Browsing & Categories:** Explore books from the Library (Firebase database) and the Explore section (downloadable books via API).
- **Library Books:** Purchase and read e-books within the app.
- **Book Reviews & Related Books:** View user reviews and related books.
- **Downloadable Books:** Download free books as PDFs.
- **Payments & Transactions:** Manage payment methods and transactions.
- **Notifications:** Personalized notifications via Firebase Cloud Messaging.
- **User Authentication:** Log in via Google or sign up with email and password.
- **Dark Mode:** A dark theme for users who prefer a dark interface.

### Who This Project Is For:
- **Book Lovers:** Enjoy reading e-books and downloadable books.
- **Administrators:** Manage book collections and user data.
- **Developers:** Contribute to and improve the app.

## ✨ Features
- 📚 **Book Browsing & Categories:** Explore two main categories: Library and Explore.
- 📖 **Library Books:** Purchase and read e-books.
- 📝 **Book Reviews & Related Books:** View user reviews and related books.
- 📄 **Downloadable Books:** Download free books as PDFs.
- 💳 **Payments & Transactions:** Manage payment methods and transactions.
- 🔔 **Notifications:** Personalized notifications via Firebase Cloud Messaging.
- 🔒 **User Authentication:** Log in via Google or sign up with email and password.
- 🌙 **Dark Mode:** A dark theme for users who prefer a dark interface.

## 🛠️ Tech Stack
- **Programming Language:** Kotlin
- **Frameworks & Libraries:**
  - **UI Libraries:** Circular Image, Glide, Navigation Fragment, ViewPager2, ViewBinding
  - **API Libraries:** Retrofit, Gson Converter, OkHttp, Logging Interceptor
  - **Backend:** Firebase (Authentication, Firestore, Storage, Messaging)
- **System Requirements:** Android 10.0 or higher

## 📦 Installation

### Prerequisites
- **Android Studio:** Latest version
- **Gradle:** Version 8.7 or higher
- **Kotlin:** Version 1.9.0 or higher

### Quick Start
1. Clone the repository:
   ```bash
   git clone https://github.com/muhamedamin308/readmate.git
   ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Build and run the app on an emulator or a physical device.

### Alternative Installation Methods
- **Docker:** (if applicable)
  ```bash
  docker run -it --rm -v $(pwd):/app -w /app your-docker-image
  ```

## 🎯 Usage

### Basic Usage
```kotlin
// Example of using Firebase Authentication
val auth = FirebaseAuth.getInstance()
auth.signInWithEmailAndPassword(email, password)
    .addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // Sign in success
            val user = auth.currentUser
            // Update UI with the signed-in user's information
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithEmail:failure", task.exception)
        }
    }
```

### Advanced Usage
- **Customizing Dark Mode:**
  ```kotlin
  // In your theme file
  <style name="Theme.ReadMate" parent="Theme.AppCompat.Light.DarkActionBar">
      <!-- Dark theme customizations -->
  </style>
  ```

## 📁 Project Structure
```
readmate/
├── admin/
│   ├── src/
│   ├── build.gradle.kts
│   └── ...
├── app/
│   ├── src/
│   ├── build.gradle.kts
│   └── ...
├── .gitignore
├── build.gradle.kts
├── gradle.properties
├── gradlew.bat
└── README.md
```

## 🔧 Configuration
- **Environment Variables:** (if applicable)
  ```bash
  export FIREBASE_API_KEY=your_api_key
  export FIREBASE_AUTH_DOMAIN=your_auth_domain
  ```
- **Configuration Files:** (if applicable)
  - `gradle.properties`: Project-wide Gradle settings.
  - `build.gradle.kts`: Project build configuration.

## 🤝 Contributing
- **How to Contribute:**
  1. Fork the repository.
  2. Create a new branch for your feature or bug fix.
  3. Make your changes and commit them.
  4. Push your branch to your fork.
  5. Open a pull request.

- **Development Setup:**
  1. Clone the repository:
     ```bash
     git clone https://github.com/muhamedamin308/readmate.git
     ```
  2. Open the project in Android Studio.
  3. Sync the project with Gradle files.

- **Code Style Guidelines:** Follow Kotlin coding conventions and best practices.
- **Pull Request Process:** Ensure your code is well-documented and follows the project's coding standards.

## 📝 License
This project is licensed under the Apache License, Version 2.0 - see the [LICENSE](LICENSE) file for details.

## 👥 Authors & Contributors
- **Maintainers:** [Muhamed Amin Hassan]
- **Contributors:** [Muhamed Amin Hassan]

## 🐛 Issues & Support
- **Report Issues:** [Open an issue](https://github.com/muhamedamin308/readmate/issues)
- **Get Help:** [Contact us](mailto:mohamed.amin.2290@gmail.com)
- **FAQ:** [Frequently Asked Questions](FAQ.md)

## 🗺️ Roadmap
- **Planned Features:**
  - [ ] Implement dark mode for the admin panel.
  - [ ] Add support for multiple languages.
- **Known Issues:**
  - [ ] Fix issue with book download in low internet conditions.
- **Future Improvements:**
  - [ ] Integrate with more book APIs.
  - [ ] Add offline reading support.

---

**Badges:**
[![Build Status](https://travis-ci.com/muhamedamin308/readmate.svg?branch=main)](https://travis-ci.com/muhamedamin308/readmate)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Version](https://img.shields.io/badge/Version-1.0.0-blue.svg)](https://github.com/muhamedamin308/readmate/releases/tag/v1.0.0)
