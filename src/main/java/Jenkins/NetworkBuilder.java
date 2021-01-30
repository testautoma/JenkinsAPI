package Jenkins;

public class NetworkBuilder {
    private String basePath;
    private String user;
    private String password;

    public NetworkBuilder() {}

    public static NetworkBuilder defaultValues() {
        return new NetworkBuilder();
    }

    public NetworkBuilder setBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public NetworkBuilder setUser(String user) {
        this.user = user;
        return this;
    }

    public NetworkBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public Network build() {
        return new Network(basePath, user, password);
    }
}
