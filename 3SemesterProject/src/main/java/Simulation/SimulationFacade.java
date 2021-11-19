package Simulation;

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
import java.util.List;
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
        List<EndpointDescription> endpoints = DiscoveryClient.getEndpoints("opc.tcp://127.0.0.1:4840").get();

        EndpointDescription epd = EndpointUtil.updateUrl(endpoints.get(0), "127.0.0.1", 4840);

        OpcUaClientConfigBuilder cfg = new OpcUaClientConfigBuilder();

        cfg.setEndpoint(epd);

        client = OpcUaClient.create(cfg.build());
        client.connect().get();

    }

    public void write(Variant variant,NodeId nodeId) throws ExecutionException, InterruptedException {
        write.write(variant,nodeId);
        sendChangeRequest();

    }
    public void subscribe(NodeId nodeId){
        subscription.subscribe(nodeId);
    }
    public void read(NodeId nodeId) throws ExecutionException, InterruptedException {
        read.read(nodeId);
        sendChangeRequest();
    }
    public void sendChangeRequest() throws ExecutionException, InterruptedException {
        NodeId nodeIdInt = NodeId.parse("ns=6;s=::Program:Cube.Command.CmdChangeRequest");
        client.writeValue(nodeIdInt, DataValue.valueOnly(new Variant(true))).get();
    }


}
