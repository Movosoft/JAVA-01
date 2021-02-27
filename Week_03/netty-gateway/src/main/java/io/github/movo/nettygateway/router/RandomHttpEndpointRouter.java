package io.github.movo.nettygateway.router;

import java.util.List;
import java.util.Random;

/**
 * @Description
 * @auther Movo
 * @create 2021/1/25 11:01
 */
public class RandomHttpEndpointRouter implements HttpEndpointRouter {
    @Override
    public String route(List<String> endpoints) {
        int size = endpoints.size();
        Random random = new Random(System.currentTimeMillis());
        return endpoints.get(random.nextInt(size));
    }
}
