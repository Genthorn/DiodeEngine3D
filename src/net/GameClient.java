package net;

import entities.PlayerMP;
import net.packets.Packet;
import net.packets.Packet00Login;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import terrains.World;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class GameClient extends Thread {

	private InetAddress ipAddress;
	private DatagramSocket socket;

	World world = null;

	Loader loader = null;

	public GameClient(World world, Loader loader, String ipAddress) {
		try {
			this.world = world;
			this.loader = loader;
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}

			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

		}
	}

	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		Packet.PacketTypes packetType = Packet.lookupPacket(message.substring(0, 2));
		Packet packet = null;

		switch(packetType) {
			default:
			case INVALID:
				break;
			case LOGIN:
				packet = new Packet00Login(data);
				System.out.println("["+address.getHostAddress()+ " : " + port +"]" + ((Packet00Login)packet).getUsername() + " has Joined The Game");

				PlayerMP player = new PlayerMP(null,
						new Vector3f(153, 5, -274),  0, 100, 0, 0.4f, ((Packet00Login)packet).getUsername(), address, port);
				world.addEntity(player);
				break;
			case DISCONNECT:
				break;
		}

	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
