import Data.DataMain;
import Simulation.SimulationFacade;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class SimTest {

    private static boolean simRunning;
    private static SimulationFacade sim;
    private static File file;

    public static void initialize() throws UaException, ExecutionException, InterruptedException, SQLException {
        sim = new SimulationFacade();

        simRunning=true;
    }


    private static Float product = 0f;

    public static void main(String[] args) throws SQLException, UaException, ExecutionException, InterruptedException, IOException {
        initialize();

        //+/documents on laptop
        file = new File("C:/Users/Aksel/Documents/GitHub/3SemesterProjekt/data.txt");

        //print headers
        FileWriter fw = new FileWriter(file, true);
        fw.write("Product" + ";");
        fw.write("machSpeed;");
        fw.write("curSpeed;");
        fw.write("defective;");
        fw.write("processed\n");
        fw.close();
        //reset
        resetSim();

        for (float i = 5; i <= 5; i++) {


            product = i;

            float maxSpeed = 0;
            float start = 0;

            if (product == 0) {
                maxSpeed = 600;
            } else if (product == 1) {
                maxSpeed = 300;
            } else if (product == 2) {
                start = 50;
                maxSpeed = 150;
            } else if (product == 3) {
                continue;
               // maxSpeed = 200;
            } else if (product == 4) {
                start = 50;
                maxSpeed = 100;
            } else if (product == 5) {

                maxSpeed = 125;
                start = maxSpeed/10;
            }

            float add = (maxSpeed - start) / 10;

            for (float j = start; j <= maxSpeed; j = j + add) {

                startSim(product, j);

                System.out.println("Started: " + product + " speed: " + j);
                storeData();

                resetSim();

            }
        }

        //resetSim();

    }

    private static void startSim(Float product, Float speed) throws ExecutionException, InterruptedException {

        simRunning = true;

        String prod = product.toString();

        //Speed
        sim.write(new Variant(speed), NodeId.parse("ns=6;s=::Program:Cube.Command.MachSpeed"));
        //batch ID
        sim.write(new Variant(Float.parseFloat("100")), NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[0].Value"));
        //product
        sim.write(new Variant(Float.parseFloat(prod)), NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[1].Value"));
        //amount
        sim.write(new Variant(Float.parseFloat("200")), NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[2].Value"));

        //start
        sim.write(new Variant(Integer.parseInt("2")), NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));

    }

    private static void resetSim() throws ExecutionException, InterruptedException {

        System.out.println("resetting");
        sim.write(new Variant(Integer.parseInt("3")), NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));
        Thread.sleep(3000);

        System.out.println("Waiting");
        sim.write(new Variant(Integer.parseInt("1")), NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));
        Thread.sleep(3000);

    }

    private static void storeData() throws ExecutionException, InterruptedException, IOException {

        System.out.println("Sleeping Store");
        while(!sim.read(NodeId.parse("ns=6;s=::Program:Cube.Status.StateCurrent")).equals("17")){
            Thread.sleep(3000);
        }

        System.out.println("Printing Data");

        //ns=6;s=::Program:Cube.Command.MachSpeed
        String machSpeed = sim.read(NodeId.parse("ns=6;s=::Program:Cube.Status.MachSpeed"));
        //ns=6;s=::Program:Cube.Admin.ProdDefectiveCount
        String defective = sim.read(NodeId.parse("ns=6;s=::Program:Cube.Admin.ProdDefectiveCount"));
        //ns=6;s=::Program:Cube.Admin.ProdProcessedCount
        String processed = sim.read(NodeId.parse("ns=6;s=::Program:Cube.Admin.ProdProcessedCount"));
        //ns=6;s=::Program:Cube.Command.CurMachSpeed
        String curSpeed = sim.read(NodeId.parse("ns=6;s=::Program:Cube.Status.CurMachSpeed"));

        FileWriter fw = new FileWriter(file, true);

        fw.write(product + ";");
        fw.write(machSpeed + ";");
        fw.write(curSpeed + ";");
        fw.write(defective + ";");
        fw.write(processed + "\n");

        fw.close();

    }

}
