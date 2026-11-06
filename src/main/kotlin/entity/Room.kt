package entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "room")
class Room(
    @Id
    @GeneratedValue
    var id: UUID? = null,
    @ManyToOne
    @JoinColumn(name = "dorm_id")
    var dorm: Dorm,
    @Column(name = "room_number", nullable = false)
    var roomNumber: String,
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant? = null,
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant? = null,
)
