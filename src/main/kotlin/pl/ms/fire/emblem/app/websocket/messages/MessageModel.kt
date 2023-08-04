package pl.ms.fire.emblem.app.websocket.messages

interface MessageModel {
    val className: String
        get() = this.javaClass.simpleName
}