package io.github.movo.nettygateway.router;

import java.util.List;

public interface HttpEndpointRouter {
    String route(List<String> endpoints);
}
