package SOAPTask;

import javax.xml.ws.Endpoint;

public class GameWebServicePublisher {

    public static void main(String... args) {
        Endpoint.publish("http://localhost:1986/wss/game", new GameWebServiceImpl());
    }
}
