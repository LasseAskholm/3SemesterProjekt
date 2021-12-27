package Simulation;

import Data.DataMain;
import Simulation.Template.Read;
import Simulation.Template.Subscription;
import Simulation.Template.Write;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

import java.lang.invoke.VarHandle;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SimulationFacade {

    public static OpcUaClient client;
    private final Write write;
    private final Subscription subscription;
    private final Read read;

    public SimulationFacade() throws UaException, ExecutionException, InterruptedException {
        write= new Write();
        subscription=new Subscription();
        read=new Read();
        connect();

    }

    public static void connect() throws UaException, ExecutionException, InterruptedException {
        //List<EndpointDescription> endpoints = DiscoveryClient.getEndpoints("opc.tcp://127.0.0.1:4840").get();
        List<EndpointDescription> machineEndpoints = DiscoveryClient.getEndpoints("opc.tcp://192.168.0.122:4840").get();
        //EndpointDescription epd = EndpointUtil.updateUrl(endpoints.get(0), "127.0.0.1", 4840);
        EndpointDescription machineEpd = EndpointUtil.updateUrl(machineEndpoints.get(0), "192.168.0.122", 4840);


        OpcUaClientConfigBuilder cfg = new OpcUaClientConfigBuilder();

        cfg.setEndpoint(machineEpd);

        client = OpcUaClient.create(cfg.build());
        client.connect().get();

    }

    public void write(Variant variant, NodeId nodeId) throws ExecutionException, InterruptedException {
        write.write(variant,nodeId);
        sendChangeRequest();

    }
    public void subscribe(NodeId nodeId){
        subscription.subscribe(nodeId);
    }

    public String read(NodeId nodeId) throws ExecutionException, InterruptedException {

        return read.read(nodeId);


    }

    public Map<String, String> liveRead() throws ExecutionException, InterruptedException {

        /*
        -id
        created_at
        updated_at
        int, prod_processed_count    ns=6;s=::Program:Cube.Admin.ProdProcessedCount.Value
        int, prod_defective_count    ns=6;s=::Program:Cube.Status.ProdDefectiveCount.Value
        flo, mach_speed              ns=6;s=::Program:Cube.Status.MachSpeed.Value
        flo, humidity                ns=6;s=::Program:Cube.Status.Parameter[2].Value
        flo, temperature             ns=6;s=::Program:Cube.Status.Parameter[3].Value
        flo, vibration               ns=6;s=::Program:Cube.Status.Parameter[4].Value
        */


        HashMap<String, String> map = new HashMap<>();

        String state = read(NodeId.parse("ns=6;s=::Program:Cube.Status.StateCurrent"));

        map.put("stateID", state);
        map.put("prod_processed_count", read(NodeId.parse("ns=6;s=::Program:Cube.Admin.ProdProcessedCount")));
        map.put("prod_defective_count", read(NodeId.parse("ns=6;s=::Program:Cube.Admin.ProdDefectiveCount")));
        map.put("mach_speed", read(NodeId.parse("ns=6;s=::Program:Cube.Status.MachSpeed")));
        map.put("batchID", read(NodeId.parse("ns=6;s=::Program:Cube.Status.Parameter[0].Value")));
        map.put("humidity", read(NodeId.parse("ns=6;s=::Program:Cube.Status.Parameter[2].Value")));
        map.put("temperature", read(NodeId.parse("ns=6;s=::Program:Cube.Status.Parameter[3].Value")));
        map.put("vibration", read(NodeId.parse("ns=6;s=::Program:Cube.Status.Parameter[4].Value")));
        map.put("productID",read(NodeId.parse("ns=6;s=::Program:Cube.Admin.Parameter[0].Value")));


        float humidity = Float.parseFloat(map.get("humidity"));
        float temperature = Float.parseFloat(map.get("temperature"));
        float vibration = Float.parseFloat(map.get("vibration"));

        /*if(humidity < -1 || humidity > 10 || temperature < -1 || temperature > 10 || vibration < -1 || vibration > 10){
            //stop
            write(new Variant(Integer.parseInt("3")), NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));
        }*/


        return map;
    }


    public void sendChangeRequest() throws ExecutionException, InterruptedException {
        NodeId nodeIdInt = NodeId.parse("ns=6;s=::Program:Cube.Command.CmdChangeRequest");
        client.writeValue(nodeIdInt, DataValue.valueOnly(new Variant(true))).get();
    }


    public Map<String, Float> getInventory() throws ExecutionException, InterruptedException {

        HashMap<String, Float> map = new HashMap<>();

        map.put("Barley", Float.parseFloat(read(NodeId.parse("ns=6;s=::Program:Inventory.Barley"))));
        map.put("Hops", Float.parseFloat(read(NodeId.parse("ns=6;s=::Program:Inventory.Hops"))));
        map.put("Malt", Float.parseFloat(read(NodeId.parse("ns=6;s=::Program:Inventory.Malt"))));
        map.put("Wheat", Float.parseFloat(read(NodeId.parse("ns=6;s=::Program:Inventory.Wheat"))));
        map.put("Yeast", Float.parseFloat(read(NodeId.parse("ns=6;s=::Program:Inventory.Yeast"))));


        return map;
    }
    public void setSimValues(Map<String, Integer> data) throws ExecutionException, InterruptedException {
        Map<String, Integer> map = data;
        //Speed
        write(new Variant(map.get("speed").floatValue()), NodeId.parse("ns=6;s=::Program:Cube.Command.MachSpeed"));


        //product
        write(new Variant(map.get("product_id").floatValue()), NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[1].Value"));


        //amount
        write(new Variant(map.get("amount").floatValue()), NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[2].Value"));


        //batch ID
        write(new Variant(map.get("batchID").floatValue()), NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[0].Value"));



    }

    public void resetSim() throws ExecutionException, InterruptedException {

        System.out.println("resetting");
        write(new Variant(Integer.parseInt("3")), NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));
        Thread.sleep(3000);

        System.out.println("Waiting");
        write(new Variant(Integer.parseInt("1")), NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));
        Thread.sleep(3000);

    }

    public void startSim(Float product, Float speed) throws ExecutionException, InterruptedException {

        String prod = product.toString();

        //Speed
        write(new Variant(speed), NodeId.parse("ns=6;s=::Program:Cube.Command.MachSpeed"));
        //batch ID
        write(new Variant(Float.parseFloat("batchID")), NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[0].Value"));
        //product
        write(new Variant(Float.parseFloat(prod)), NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[1].Value"));
        //amount
        write(new Variant(Float.parseFloat("200")), NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[2].Value"));

        //start
        write(new Variant(Integer.parseInt("2")), NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd"));

    }


    public Map<String, String> getBatchDetails() throws ExecutionException, InterruptedException {

        String batchID = read(NodeId.parse("ns=6;s=::Program:Cube.Status.Parameter[0].Value"));

        String amount = read(NodeId.parse("ns=6;s=::Program:Cube.Status.Parameter[1].Value"));
        //machspeed
        String machspeed = read(NodeId.parse("ns=6;s=::Program:Cube.Status.MachSpeed"));
        //productID
        String productID = read(NodeId.parse("ns=6;s=::Program:Cube.Admin.Parameter[0].Value"));

        Map<String, String> map = new HashMap<>();
        map.put("batchID", batchID);
        map.put("amount", amount);
        map.put("machspeed", machspeed);
        map.put("productID", productID);
        return map;
    }


    public String getCurrentState() throws ExecutionException, InterruptedException {
        return read(NodeId.parse("ns=6;s=::Program:Cube.Status.StateCurrent"));
    }
}
