package Tests;

import Jenkins.Network;
import Jenkins.NetworkBuilder;

public class BaseTest {

    Network network = NetworkBuilder.defaultValues()
            .setBasePath("http://localhost:8080/")
            .setUser("admin")
            .setPassword("e992de6197004c76bf3eb44bf43444b1")
            .build();
}
