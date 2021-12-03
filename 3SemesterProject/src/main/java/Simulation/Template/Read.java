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
            DataValue dataValue = SimulationFacade.client.readValue(10, TimestampsToReturn.Both, nodeId)
                    .get();
           // System.out.println("DataValue= " + dataValue + " id: " + nodeId);


            Variant variant = dataValue.getValue();

           /* if(dataValue.getValue().getValue() == null){
                return "0";
            }*/

            String s = String.valueOf(variant.getValue());


            return s;

        }
        catch(Throwable ex)
        {

            ex.printStackTrace();
            return "0";
        }


    }


    public static void main(String[] args) {


    }
}
