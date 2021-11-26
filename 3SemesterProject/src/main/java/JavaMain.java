import Data.DataMain;
import Simulation.SimulationFacade;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.sql.SQLException;
import java.util.Map;
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

            String command= db.getCommand();

            if(command != null) {
                if (simRunning) {
                    if (command.equals("3")) { // stop the simulation
                        simRunning = false;
                        simulationFacade.write(new Variant(3), NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));

                    } else {
                        simulationFacade.write(new Variant(Integer.parseInt(command)), NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));
                    }

                } else {
                    if (command.equals("2")) { //Start simulation
                        System.out.println("Starting machine");
                        simRunning = true;
                        simulationFacade.write(new Variant(2), NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));

                    } else {
                        simulationFacade.write(new Variant(Integer.parseInt(command)), NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));
                    }

                    // wait for sim to start running
                }
            }else if(simRunning){
                //update live batch
                /*
                -id
                created_at
                updated_at
                prod_processed_count
                prod_defective_count
                mach_speed
                humidity
                temperature
                vibration
                */

                Map<String, String> map = simulationFacade.liveRead();

                map.forEach((k, v) -> System.out.println("k: "+ k + "v: " + v));

                db.liveUpdate(map);
            }
            Thread.sleep(5000);
        }


    }

}
