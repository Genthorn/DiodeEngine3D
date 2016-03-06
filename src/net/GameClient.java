package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import engineTester.MainGameLoop;
import entities.Player;
import entities.PlayerMP;
import net.packets.Packet;
import net.packets.Packet00Login;
import sun.applet.Main;

public class GameClient extends Thread {

	private InetAddress ipAddress;
	private DatagramSocket socket;

	MainGameLoop mainGameLoop;

	public GameClient(MainGameLoop game, String ipAddress) {
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
			this.mainGameLoop = game;
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

				PlayerMP player = new PlayerMP(null, null, 0, 0, 0,
						0, ((Packet00Login)packet).getUsername(), address, port);
				//mainGameLoop.
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
