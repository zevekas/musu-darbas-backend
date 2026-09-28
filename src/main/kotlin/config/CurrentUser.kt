package config

import entity.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope
import repository.UserRepository
import service.UserService

@Component
@RequestScope
class CurrentUser(
    private val userRepository: UserRepository,
    private val userService: UserService,
) {
    private val jwt: Jwt by lazy {
        SecurityContextHolder.getContext().authentication?.principal as Jwt
    }

    val clerkId: String by lazy {
        jwt.subject!!
    }

    val email: String by lazy {
        jwt.claims["email"] as String
    }

    val user: User by lazy {
        val existing = userRepository.findByClerkId(clerkId)
        when {
            existing == null -> userService.createUser(clerkId, email)
            existing.email != email -> userService.updateEmail(existing, email)
            else -> existing
        }
    }
}
