package SOAPTask;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface GameWebService {

    @WebMethod
    public String getNextBugPositions(String prevPositions, String targets, int screenWidth, int screenHeight);

}
