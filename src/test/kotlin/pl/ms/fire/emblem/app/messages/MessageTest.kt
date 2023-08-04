package pl.ms.fire.emblem.app.messages

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.ms.fire.emblem.app.websocket.messages.MessageModel
import pl.ms.fire.emblem.app.websocket.messages.board.WaitMessageModel
import pl.ms.fire.emblem.business.values.board.Position

class MessageTest {

    @Test
    fun `test getting classname`() {
        val message1 = WaitMessageModel(Position(1,2))
        Assertions.assertEquals("WaitMessageModel", message1.className)
    }
}