package com.xuanyuan.license.tool;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 验证IP和MAC
 */
public class ListNets {

	public static boolean validateMacAddress(String macAddress) throws SocketException {
		macAddress = macAddress.replaceAll(":", "-");
		boolean returnFlag = false;
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		for (NetworkInterface netint : Collections.list(nets)) {
			byte[] mac = netint.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			if (mac != null) {
				for (int i = 0; i < mac.length; i++) {
					sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
				}
			}
			String macaddr = sb.toString().replace(" ", "");
			if (macaddr != null && macaddr != "" && macaddr.length() > 1 && macAddress.indexOf(macaddr) >= 0) {
				returnFlag = true;
				break;
			}
		}
		return returnFlag;

	}
	public static List<String> getMacAddress() throws SocketException {
		List<String> list = new ArrayList<String>();
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		for (NetworkInterface netint : Collections.list(nets)) {
			byte[] mac = netint.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			if (mac != null) {
				for (int i = 0; i < mac.length; i++) {
					sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
				}
			}
			String macaddr = sb.toString().replace(" ", "");
			list.add(macaddr);
		}
		return list;
		
	}

	public static boolean validatoIpAndMacAddress(String ipAddress, String macAddress) throws SocketException {
		boolean returnFlag = false;
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		for (NetworkInterface netint : Collections.list(nets)) {
			byte[] mac = netint.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			if (mac != null) {
				for (int i = 0; i < mac.length; i++) {
					sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
				}
			}
			String macaddr = sb.toString();
			if (macaddr != null && macaddr != "" && macAddress.indexOf(macaddr) >= 0) {
				Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
				String ip = "";
				for (InetAddress inetAddress : Collections.list(inetAddresses)) {
					ip = inetAddress.getHostAddress();
					if (ipAddress.indexOf(ip) >= 0) {
						returnFlag = true;
					}
				}
			}
		}
		return returnFlag;

	}
}