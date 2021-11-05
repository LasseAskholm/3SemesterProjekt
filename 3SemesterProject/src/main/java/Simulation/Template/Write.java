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
            List<EndpointDescription> endpoints = DiscoveryClient.getEndpoints("opc.tcp://127.0.0.1:4840").get();

            EndpointDescription epd = EndpointUtil.updateUrl(endpoints.get(0), "127.0.0.1", 4840);

            OpcUaClientConfigBuilder cfg = new OpcUaClientConfigBuilder();


            cfg.setEndpoint(epd);

            OpcUaClient client = OpcUaClient.create(cfg.build());
            client.connect().get();

            NodeId nodeIdInt = NodeId.parse("ns=6;s=::Program:Cube.Command.CmdChangeRequest");
            client.writeValue(nodeIdInt, DataValue.valueOnly(new Variant(true))).get();

            // 0 <= Value <= 65535
            NodeId nodeIdP0 = NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[0].Value");
            client.writeValue(nodeIdP0, new DataValue(new Variant(18.0))).get();

            // 0->5
            NodeId nodeIdP1 = NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[1].Value");
            client.writeValue(nodeIdP1, new DataValue(new Variant(1.0))).get();

            // 0 <= Value <= 65535
            NodeId nodeIdP2 = NodeId.parse("ns=6;s=::Program:Cube.Command.Parameter[2].Value");
            client.writeValue(nodeIdP2, new DataValue(new Variant(1000))).get();


            NodeId nodeIdRun = NodeId.parse("ns=6;s=::Program:Cube.Command.CntrlCmd");
            client.writeValue(nodeIdRun, DataValue.valueOnly(new Variant(1))).get();



        }
        catch(Throwable ex)
        {
            ex.printStackTrace();
        }

    }
}
