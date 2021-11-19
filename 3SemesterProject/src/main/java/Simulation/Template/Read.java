package Simulation.Template;

import Simulation.SimulationFacade;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

import java.util.List;

public class Read {

    public Read(){

    }

    public void read(NodeId nodeId){
        try
        {
            DataValue dataValue = SimulationFacade.client.readValue(0, TimestampsToReturn.Both, nodeId)
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


    public static void main(String[] args) {


    }
}
