package com.example.readmate.data.repo.remote.firebase.user

import com.example.readmate.data.model.firebase.CreditCard
import com.example.readmate.data.model.firebase.Notification
import com.example.readmate.data.service.remote.firebase.FirebaseUserService
import com.example.readmate.data.service.remote.firebase.FirestorePaymentService

/**
 * @author Muhamed Amin Hassan on 11,October,2024
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class UserServicesRepositoryImpl(
    private val userService: FirebaseUserService,
    private val paymentService: FirestorePaymentService,
) : UserServicesRepository {

    override fun getNotifications(onAction: (List<Notification>?, Exception?) -> Unit) =
        userService.getUserNotifications(onAction)

    override fun saveNotification(notification: Notification) =
        userService.saveUserNotification(notification)

    override fun addCreditCard(
        creditCard: CreditCard,
        onAction: (CreditCard?, Exception?) -> Unit,
    ) = paymentService.addPaymentMethod(creditCard, onAction)

    override fun getAllCreditCards(onAction: (List<CreditCard>?, Exception?) -> Unit) =
        paymentService.getUserCreditCards(onAction)

    override fun deleteCreditCard(
        creditCard: CreditCard,
        onAction: (Boolean, Exception?) -> Unit,
    ) = paymentService.removePaymentMethod(creditCard, onAction)

}