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

    public String read(NodeId nodeId){
        try
        {
            DataValue dataValue = SimulationFacade.client.readValue(0, TimestampsToReturn.Both, nodeId)
                    .get();
            //System.out.println("DataValue= " + dataValue + " id: " + nodeId);


            if(dataValue.getValue().getValue() == null){
                return "0";
            }


            return String.valueOf(dataValue.getValue().getValue());

        }
        catch(Throwable ex)
        {
            ex.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) {


    }
}
