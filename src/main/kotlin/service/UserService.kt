package service

import entity.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import repository.UserRepository

@Service
class UserService(
    val userRepository: UserRepository,
) {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun createUser(
        clerkId: String,
        email: String,
    ): User = userRepository.save(User(clerkId, email))

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun updateEmail(
        user: User,
        newEmail: String,
    ): User {
        user.email = newEmail
        return userRepository.save(user)
    }
}
