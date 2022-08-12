package pl.ms.fire.emblem.app.configuration.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class UserEntity(
    val id: Int,
    username: String,
    password: String,
    val email: String,
    authorities: Collection<GrantedAuthority>,
    val currentPreset: Int
): User(username, password, authorities) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as UserEntity

        if (id != other.id) return false
        if (email != other.email) return false
        if (currentPreset != other.currentPreset) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + id
        result = 31 * result + email.hashCode()
        result = 31 * result + currentPreset
        return result
    }
}