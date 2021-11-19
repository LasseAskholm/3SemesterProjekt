import Data.DataMain;
import Simulation.SimulationFacade;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class JavaMain {

    private static boolean simRunning;
    private static DataMain db;
    private static SimulationFacade simulationFacade;

    public static void initialize() throws UaException, ExecutionException, InterruptedException, SQLException {
        db=DataMain.getInstance();
        simulationFacade = new SimulationFacade();
        simRunning=false;
    }


    public static void main(String[] args) throws InterruptedException, ExecutionException, SQLException, UaException {
        initialize();
        while(true){
            if(simRunning){
                String command = db.getCommand();
                if(command != null){
                    if(command.equals("3")){ // stop the simulation
                        simRunning=false;

                        simulationFacade.write(new Variant(3),NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));

                    }else{
                        simulationFacade.write(new Variant(Integer.parseInt(command)),NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));
                    }

                }
                // update live batch table
            }else{
                String command= db.getCommand();
                if(command.equals("2")){ //Start simulation
                    System.out.println("Starting machine");
                    simRunning=true;
                    simulationFacade.write(new Variant(2),NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));

                }else{
                    simulationFacade.write(new Variant(Integer.parseInt(command)),NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));
                }

                // wait for sim to start running
            }
            Thread.sleep(5000);
        }


    }

}
