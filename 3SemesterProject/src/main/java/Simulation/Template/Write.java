package Simulation.Template;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author athil
 */

import Simulation.SimulationFacade;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Write {


    public Write() throws UaException, ExecutionException, InterruptedException {

    }

    public void write(Variant variant, NodeId nodeId) throws ExecutionException, InterruptedException {
       SimulationFacade.client.writeValue(nodeId,DataValue.valueOnly(variant)).get();
    }
    public static void main(String[] args) {
     /*   try
        {



            // 0 <= Value <= 65535
            NodeId nodeIdP0 = NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[0].Value");
            client.writeValue(nodeIdP0, DataValue.valueOnly(new Variant(18F))).get();

            // 0->5
            NodeId nodeIdP1 = NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[1].Value");
            client.writeValue(nodeIdP1, DataValue.valueOnly(new Variant(11f))).get();

            // 0 <= Value <= 65535
            NodeId nodeIdP2 = NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[2].Value");
            client.writeValue(nodeIdP2, DataValue.valueOnly(new Variant(100f))).get();



            NodeId nodeIdRun = NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd");
            client.writeValue(nodeIdRun, DataValue.valueOnly(new Variant(2))).get();


            NodeId nodeIdRun = NodeId.parse("ns=6;s=::Program:Cube.Command.MachSpeed");
            client.writeValue(nodeIdRun, DataValue.valueOnly(new Variant(99f))).get();


            NodeId nodeIdInt = NodeId.parse("ns=6;s=::Program:Cube.Command.CmdChangeRequest");
        client.writeValue(nodeIdInt, DataValue.valueOnly(new Variant(true))).get();

        }
        catch(Throwable ex)
        {
            ex.printStackTrace();
        }

   */ }
}
