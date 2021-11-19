import Data.DataMain;
import Simulation.SimulationFacade;
import org.eclipse.milo.opcua.stack.core.UaException;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class JavaMain {

    private static DataMain db;
    private static SimulationFacade simulationFacade;

    public static void initialize() throws UaException, ExecutionException, InterruptedException, SQLException {
        db=DataMain.getInstance();
        simulationFacade = new SimulationFacade();
    }


    public static void main(String[] args) throws InterruptedException, ExecutionException, SQLException, UaException {
        initialize();
        
    }
}
