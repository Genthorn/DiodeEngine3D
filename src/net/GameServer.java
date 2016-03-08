package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import engineTester.MainGameLoop;
import entities.PlayerMP;
import models.TexturedModel;
import net.packets.Packet;
import net.packets.Packet00Login;
import org.lwjgl.util.vector.Vector3f;

public class GameServer extends Thread {

	private DatagramSocket socket;

	private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();

	public GameServer() {
		try {
			this.socket = new DatagramSocket(1331);
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
				System.out.println("["+address.getHostAddress()+ " : " + port +"]" + ((Packet00Login)packet).getUsername() + " has Connected");

				PlayerMP player = new PlayerMP(null, null, 0, 0, 0,
				0, ((Packet00Login)packet).getUsername(), address, port);
				this.addConnection(player, ((Packet00Login)packet));
				break;
			case DISCONNECT:
				break;
		}

	}

	public void addConnection(PlayerMP player, Packet00Login packet) {
		boolean alreadyConnected = false;
		for(PlayerMP p : this.connectedPlayers) {
			if(player.getUsername().equalsIgnoreCase(player.getUsername())) {
				if(p.getIPAddress() == null) {
					p.setIpAddress(player.getIPAddress());
				}

				if(p.getPort() == -1) {
					p.setPort(player.getPort());
				}

				alreadyConnected = true;
			} else {
				sendData(packet.getData(), p.getIPAddress(), p.getPort());
			}
		}

		if(!alreadyConnected) {
			this.connectedPlayers.add(player);
			packet.writeData(this);
		}
	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		for(PlayerMP p : connectedPlayers) {
			sendData(data, p.getIPAddress(), p.getPort());
		}
	}
}
