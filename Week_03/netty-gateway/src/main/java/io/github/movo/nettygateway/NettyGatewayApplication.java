package io.github.movo.nettygateway;

import io.github.movo.nettygateway.inbound.HttpInboundServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class NettyGatewayApplication {

    public static void main(String[] args) {
        String proxyPort = System.getProperty("proxyPort", "8890");
        String proxyServers = System.getProperty("proxyServers","http://10.0.3.15:" + proxyPort);
        int port = Integer.parseInt(proxyPort);
        HttpInboundServer server = new HttpInboundServer(port, Arrays.asList(proxyServers.split(",")));
        server.run();
    }

}
