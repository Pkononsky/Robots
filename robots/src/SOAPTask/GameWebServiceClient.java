package SOAPTask;

import javax.swing.*;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class GameWebServiceClient {

    public static void main(String... args) throws MalformedURLException {
        URL url = new URL("http://localhost:1986/wss/game?wsdl");
        QName qname = new QName("http://SOAPTask/", "GameWebServiceImplService");
        Service service = Service.create(url, qname);
        GameWebService gameWebService = service.getPort(GameWebService.class);

        JFrame jFrame = new JFrame();
        Visualizer visualizer = new Visualizer(gameWebService);

        jFrame.setContentPane(new JDesktopPane());

        JInternalFrame jInternalFrame = new JInternalFrame("Игровое поле", true, true, true, true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        jInternalFrame.getContentPane().add(panel);
        jInternalFrame.setSize(400, 600);

        jFrame.add(jInternalFrame);
        jInternalFrame.setVisible(true);
        jFrame.setSize(400, 600);
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

    }
}
