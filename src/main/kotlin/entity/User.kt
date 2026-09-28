package entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "\"user\"")
class User(
    @Column(name = "clerk_id", nullable = false)
    var clerkId: String,
    @Column(name = "email", nullable = false)
    var email: String,
    @Column(name = "username", nullable = true)
    var username: String? = null,
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant? = null,
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant? = null,
    @Id
    @GeneratedValue
    var id: UUID? = null,
)
