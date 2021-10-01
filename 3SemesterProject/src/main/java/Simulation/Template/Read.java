package Simulation.Template;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

import java.util.List;

public class Read {


    public static void main(String[] args) {
        try
        {
            List<EndpointDescription> endpoints = DiscoveryClient.getEndpoints("opc.tcp://127.0.0.1:4840").get();

            OpcUaClientConfigBuilder cfg = new OpcUaClientConfigBuilder();
            cfg.setEndpoint(endpoints.get(0));

            OpcUaClient client = OpcUaClient.create(cfg.build());
            client.connect().get();

            NodeId nodeId = NodeId.parse("ns=6;s=::Program:Data.Value.RelHumidity");

            DataValue dataValue = client.readValue(0, TimestampsToReturn.Both, nodeId)
                    .get();
            System.out.println("DataValue= " + dataValue);

            Variant variant = dataValue.getValue();

            System.out.println("Variant= " + variant);

            short random =  (short) variant.getValue();
            System.out.println("myVariable= " + random);

        }
        catch(Throwable ex)
        {
            ex.printStackTrace();
        }

    }
}
