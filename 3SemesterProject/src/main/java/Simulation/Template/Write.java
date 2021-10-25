package Simulation.Template;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author athil
 */

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

import java.util.List;

public class Write {
    public static void main(String[] args) {
        try 
        {
            List<EndpointDescription> endpoints = DiscoveryClient.getEndpoints("opc.tcp://192.168.0.122:4840").get();

            EndpointDescription epd = EndpointUtil.updateUrl(endpoints.get(0), "192.168.0.122", 4840);

            OpcUaClientConfigBuilder cfg = new OpcUaClientConfigBuilder();


            cfg.setEndpoint(epd);

            OpcUaClient client = OpcUaClient.create(cfg.build());
            client.connect().get();




            NodeId nodeId0 = NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[0]");
            client.writeValue(nodeId0, DataValue.valueOnly(new Variant(1))).get();

            NodeId nodeId1 = NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[1]");
            client.writeValue(nodeId1, DataValue.valueOnly(new Variant(2))).get();

            NodeId nodeId2 = NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[2]");
            client.writeValue(nodeId2, DataValue.valueOnly(new Variant(100))).get();

            NodeId nodeId = NodeId.parse("ns=6;s=::Program:Cube.Command.MachSpeed");
            client.writeValue(nodeId, DataValue.valueOnly(new Variant(100))).get();

            NodeId nodeIdRun = NodeId.parse("ns=6;s=::Program:Cube.Command.Cntr lCmd");
            client.writeValue(nodeIdRun, DataValue.valueOnly(new Variant(2))).get();

            NodeId nodeIdInt = NodeId.parse("ns=6;s=::Program:Cube.Command.CmdChangeRequest");
            client.writeValue(nodeIdInt, DataValue.valueOnly(new Variant(true))).get();

        }
        catch(Throwable ex)
        {
            ex.printStackTrace();
        }

    }
}
