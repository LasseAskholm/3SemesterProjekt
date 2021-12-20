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
        simulationFacade.resetSim();
        db.resetTable("live_batches");
        db.resetTable("batch_report");

        Thread.sleep(1000);

        simulationFacade.startSim(0f, 600f);

        Thread.sleep(1000);
        simRunning = true;

        while(true){


            String command = db.getCommand();
            int state = Integer.parseInt(simulationFacade.getCurrentState());
            db.setState(state);

            System.out.println("command: " + command);
            if(command != null) {
                if (simRunning) {
                    if (command.equals("3")) { // stop the simulation
                        //simRunning = false;
                        System.out.println("stopped");
                        simulationFacade.write(new Variant(3), NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));

                    } else {
                        simulationFacade.write(new Variant(Integer.parseInt(command)), NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));
                        System.out.println("stop else");
                    }

                } else {
                    if (command.equals("2")) { //Start simulation
                        System.out.println("Starting machine");
                        simulationFacade.setSimValues(db.sendValues());
                        simRunning = true;

                        simulationFacade.write(new Variant(2), NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));

                    } else {
                        simulationFacade.write(new Variant(Integer.parseInt(command)), NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));
                        System.out.println("start else");
                    }

                    // wait for sim to start running
                }
            }else if(simRunning){
                System.out.println("live update. State :" + state);
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

                db.liveUpdate(map);

                Map<String, Float> inventory = simulationFacade.getInventory();

                db.updateInventory(inventory);



                if (state != 6){
                    //simRunning = false;

                    if(state == 17){
                        System.out.println("Done. Making report");
                        //batch report
                        Map<String, String> batchDetails = simulationFacade.getBatchDetails();
                        db.makeBatchReport(batchDetails);
                        //reset
                        //db.resetTable("live_batches");
                        System.out.println("Report done!!");
                        simulationFacade.resetSim();

                    }

                }


            }else{
                System.out.println("Current state: " + state);

                Map<String, String> map = simulationFacade.liveRead();

               // db.liveUpdate(map);
            }

            Thread.sleep(2000);
        }


    }




}
