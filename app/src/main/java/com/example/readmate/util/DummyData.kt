package com.example.readmate.util

import com.example.readmate.data.model.local.HelpAndSupport
import com.example.readmate.data.model.local.PromoCode

/**
 * @author Muhamed Amin Hassan on 16,September,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

val famousCategories = listOf(
    "Computer Science",
    "Science & Mathematics",
    "Economics & Finance",
    "Business & Management",
    "Politics & Government",
    "History",
    "Philosophy",
    "Action",
    "Adventure",
    "Comedy",
    "Drama",
    "Horror"
)

val availablePromoCodes = listOf(
    PromoCode("SAVE20", 0.20f),
    PromoCode("SAVE30", 0.30f),
    PromoCode("SAVE50", 0.50f)
)

val helpAndSupportList = listOf(
    HelpAndSupport(
        "How do I search for books?",
        "To search for books, navigate to the \"Explore\" section and use the search bar at the top. You can type in the title, author, or genre, and ReadMate will show relevant results."
    ),
    HelpAndSupport(
        "How do I add books to my library?",
        "When you find a book you like, click on it to view details. You can then click the \"Add to My Library\" button to save it in your library for easy access later."
    ),
    HelpAndSupport(
        "How can I track books I've read?",
        "In the \"My Books\" section, you'll see a list of books you’ve added. You can mark books as read, and they will be tracked under your completed books list."
    ),
    HelpAndSupport(
        "Can I download books for offline reading?",
        "Currently, ReadMate does not support offline downloads. However, all books added to your library are available for reading whenever you have internet access."
    ),
    HelpAndSupport(
        "How do I get recommendations for new books?",
        "ReadMate offers personalized book recommendations based on your reading history. You can find these suggestions in the \"Explore\" section under the \"Recommended Books\" tab."
    ),
    HelpAndSupport(
        "What should I do if I encounter issues with the app?",
        "If you face any technical issues, try restarting the app. If the problem persists, you can contact us directly through the \"Support\" section available in the app settings."
    ),
    HelpAndSupport(
        "How do I manage my account settings?",
        "Go to the \"Settings\" tab in the app. From there, you can update your profile, manage notifications, or log out of your account."
    ),
    HelpAndSupport(
        "Can I change my username or email?",
        "Yes, you can update your username and email in the \"Settings\" section under \"Account Details.\""
    ),
    HelpAndSupport(
        "Is there a limit to how many books I can add to my library?",
        "No, you can add as many books as you'd like to your library without any limits."
    )
)