package men.brakh.server.handlers;

import men.brakh.chat.Message;
import men.brakh.chat.UsersTypes;
import men.brakh.server.handlers.impl.AgentsHandler;
import men.brakh.server.handlers.impl.CustomersHandler;
import men.brakh.server.senders.Sender;
import men.brakh.server.Server;
import men.brakh.server.senders.impl.WebSender;

import javax.websocket.Session;

/**
 * Обработка сообщений в отдельном потоке (для вебсоектов)
 */
public class HandlerThread extends Thread {
    private Message message;

    private AgentsHandler agentsHandler;
    private CustomersHandler customersHandler;

    public HandlerThread(Message message, Session session, Server server) {
        this.message = message;
        Sender sender = new WebSender(session);
        agentsHandler = new AgentsHandler(server, sender);
        customersHandler = new CustomersHandler(server, sender);
        start();
    }

    @Override
    public void run() {
        if (message.getUser().getUserType() == UsersTypes.CUSTOMER) { // На сервер написал клиент
            customersHandler.handle(message);
        } else if (message.getUser().getUserType() == UsersTypes.AGENT) { // На сервер написал агент
            agentsHandler.handle(message);
        }
    }

}