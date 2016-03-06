package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

import java.net.InetAddress;

public class PlayerMP extends Player {

    private String username;
    private InetAddress ipAddress;
    private int port;

    public PlayerMP(TexturedModel model, Vector3f position,
                    float rotX, float rotY, float rotZ,
                    float scale, String username, InetAddress ipAddress, int port) {
        super(model, position, rotX, rotY, rotZ, scale);

        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void update() {

    }

    public InetAddress getIPAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }
}
