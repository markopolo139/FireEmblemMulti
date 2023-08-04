package pl.ms.fire.emblem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

//TODO: create rest password recovery
fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
