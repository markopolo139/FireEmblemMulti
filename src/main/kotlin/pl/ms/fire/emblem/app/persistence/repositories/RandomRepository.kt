package pl.ms.fire.emblem.app.persistence.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.ms.fire.emblem.app.persistence.entities.RandomPlayer

interface RandomRepository: JpaRepository<RandomPlayer, Int>
