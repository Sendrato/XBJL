/**
 * Copyright 2017, Digi International Inc.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES 
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR 
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES 
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN 
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF 
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package com.digi.xbee.api.discoverdevices;

import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.XBeeNetwork;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.listeners.IPacketReceiveListener;
import com.digi.xbee.api.models.XBeeMessage;
import com.digi.xbee.api.packet.XBeePacket;

/**
 * XBee Java Library Discover Devices sample application.
 * 
 * <p>This example retrieves the XBee network from the local XBee device and 
 * performs a remote device discovery.</p>
 * 
 * <p>For a complete description on the example, refer to the 'ReadMe.txt' file
 * included in the root directory.</p>
 */
public class MainApp {

	static int count = 0;

	/* Constants */
	
	// TODO Replace with the serial port where your module is connected to.
	private static final String PORT = "/dev/cu.usbserial-A5056ZT6";
	// TODO Replace with the baud rate of your module.
	private static final int BAUD_RATE = 230400;
	
	/**
	 * Application main method.
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		System.out.println(" +-------------------------------------------+");
		System.out.println(" | XBee Java Library Discover Devices Sample |");
		System.out.println(" +-------------------------------------------+\n");
		
		XBeeDevice myDevice = new XBeeDevice(PORT, BAUD_RATE);
		
		try {
			myDevice.open();
			
			myDevice.addDataListener(new IDataReceiveListener() {
				
				@Override
				public void dataReceived(XBeeMessage xbeeMessage) {
					System.err.println((count++) + ": received: " + xbeeMessage.getDataString());
				}
			});
			System.err.println("Opened device: " + myDevice);
			
//			XBeeNetwork myXBeeNetwork = myDevice.getNetwork();
//			
//			myXBeeNetwork.setDiscoveryTimeout(15000);
//			
//			myXBeeNetwork.addDiscoveryListener(new MyDiscoveryListener());
//			
//			myXBeeNetwork.startDiscoveryProcess();
//			
//			System.out.println("\n>> Discovering remote XBee devices...");
			
		} catch (XBeeException e) {
			e.printStackTrace();
			myDevice.close();
			System.exit(1);
		}
	}
}
