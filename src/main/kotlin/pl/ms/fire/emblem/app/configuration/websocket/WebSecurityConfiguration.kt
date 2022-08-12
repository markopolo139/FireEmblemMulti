package pl.ms.fire.emblem.app.configuration.websocket

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.converter.MessageConverter
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.DefaultCsrfToken
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import pl.ms.fire.emblem.app.exceptions.InvalidWebSocketTokenException

@EnableWebSocketMessageBroker
@Configuration
class WebSecurityConfiguration: WebSocketMessageBrokerConfigurer {

    @Autowired
    private lateinit var webSocketService: WebSocketService

    companion object {
        private val logger = LogManager.getLogger()
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/fireEmblemMulti").setAllowedOrigins("*")
        registry.addEndpoint("/fireEmblemMulti").setAllowedOrigins("*").withSockJS()
    }

    override fun configureMessageConverters(messageConverters: MutableList<MessageConverter>): Boolean {
        return true
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/app/v1")
        registry.setUserDestinationPrefix("/user/")
        registry.enableSimpleBroker("/queue", "/topic")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(
            object: ChannelInterceptor {
                override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
                    val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
                    if (accessor?.command == StompCommand.SEND) {

                        val token = accessor.getFirstNativeHeader("Authorization") ?: ""

                        if (token.isNotEmpty()) {
                            val sessionAttributes = SimpMessageHeaderAccessor.getSessionAttributes(message.headers)
                            sessionAttributes?.set(
                                CsrfToken::class.java.name,
                                DefaultCsrfToken("Authorization", "Authorization", token)
                            )

                            val auth = webSocketService.authenticate(token)

                            SecurityContextHolder.getContext().authentication = auth
                            accessor.user = auth

                        }
                        else {
                            throw InvalidWebSocketTokenException()
                        }
                    }

                    return message
                }
            }
        )
    }
}