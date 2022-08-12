package pl.ms.fire.emblem.app.persistence

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import pl.ms.fire.emblem.app.configuration.security.UserEntity
import pl.ms.fire.emblem.app.persistence.entities.PlayerEntity

fun UserDetails.toUserEntity(id: Int, email: String, preset: Int) =
    UserEntity(id, username, password, email, authorities, preset)

fun PlayerEntity.toUserEntity() =
    User.builder()
        .username(username)
        .password(password)
        .roles(*roles.toTypedArray())
        .build().toUserEntity(id, email, currentPreset)