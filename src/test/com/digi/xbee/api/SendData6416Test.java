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
package com.digi.xbee.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.digi.xbee.api.connection.serial.SerialPortRxTx;
import com.digi.xbee.api.exceptions.InterfaceNotOpenException;
import com.digi.xbee.api.exceptions.InvalidOperatingModeException;
import com.digi.xbee.api.exceptions.OperationNotSupportedException;
import com.digi.xbee.api.exceptions.TimeoutException;
import com.digi.xbee.api.exceptions.TransmitException;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.models.XBee64BitAddress;
import com.digi.xbee.api.packet.common.TransmitPacket;

@RunWith(PowerMockRunner.class)
@PrepareForTest({XBeeDevice.class})
public class SendData6416Test {
	
	// Constants.
	private static final XBee64BitAddress XBEE_64BIT_ADDRESS = new XBee64BitAddress("0123456789ABCDEF");
	private static final XBee16BitAddress XBEE_16BIT_ADDRESS = new XBee16BitAddress("0123");
	
	private static final String DATA = "data";
	
	// Variables.
	private XBeeDevice xbeeDevice;
	
	private TransmitPacket transmitPacket;
	
	@Before
	public void setup() throws Exception {
		// Instantiate an XBeeDevice object with a mocked interface.
		xbeeDevice = PowerMockito.spy(new XBeeDevice(Mockito.mock(SerialPortRxTx.class)));
		
		// Mock Transmit Request packet.
		transmitPacket = Mockito.mock(TransmitPacket.class);
		
		// Whenever a TransmitPacket class is instantiated, the mocked transmitPacket packet should be returned.
		PowerMockito.whenNew(TransmitPacket.class).withAnyArguments().thenReturn(transmitPacket);
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.XBeeDevice#sendData(XBee64BitAddress, XBee16BitAddress, byte[])}.
	 * 
	 * <p>Verify that data cannot be sent if the 64-Bit address is {@code null}.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 */
	@Test(expected=NullPointerException.class)
	public void testSendData64BitAddrNull() throws TimeoutException, XBeeException {
		xbeeDevice.sendData(null, XBEE_16BIT_ADDRESS, DATA.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.XBeeDevice#sendData(XBee64BitAddress, XBee16BitAddress, byte[])}.
	 * 
	 * <p>Verify that data cannot be sent if the 16-Bit address is {@code null}.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 */
	@Test(expected=NullPointerException.class)
	public void testSendData16BitAddrNull() throws TimeoutException, XBeeException {
		xbeeDevice.sendData(XBEE_64BIT_ADDRESS, null, DATA.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.XBeeDevice#sendData(XBee64BitAddress, XBee16BitAddress, byte[])}.
	 * 
	 * <p>Verify that data cannot be sent if the data is {@code null}.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 */
	@Test(expected=NullPointerException.class)
	public void testSendDataDataNull() throws TimeoutException, XBeeException {
		xbeeDevice.sendData(XBEE_64BIT_ADDRESS, XBEE_16BIT_ADDRESS, null);
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.XBeeDevice#sendData(XBee64BitAddress, XBee16BitAddress, byte[])}.
	 * 
	 * <p>Verify that data cannot be sent if the sender is a remote XBee device.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 */
	@Test(expected=OperationNotSupportedException.class)
	public void testSendDataFromRemoteDevices() throws TimeoutException, XBeeException {
		// Return that the XBee device is remote when asked.
		Mockito.when(xbeeDevice.isRemote()).thenReturn(true);
		
		xbeeDevice.sendData(XBEE_64BIT_ADDRESS, XBEE_16BIT_ADDRESS, DATA.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.XBeeDevice#sendData(XBee64BitAddress, XBee16BitAddress, byte[])}.
	 * 
	 * <p>Verify that data cannot be sent if the device is not open.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 */
	@Test(expected=InterfaceNotOpenException.class)
	public void testSendDataConnectionClosed() throws TimeoutException, XBeeException {
		// Throw an Interface not open exception when sending and checking any Transmit packet.
		Mockito.doThrow(new InterfaceNotOpenException()).when(xbeeDevice).sendAndCheckXBeePacket(Mockito.any(TransmitPacket.class), Mockito.eq(false));
		
		xbeeDevice.sendData(XBEE_64BIT_ADDRESS, XBEE_16BIT_ADDRESS, DATA.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.XBeeDevice#sendData(XBee64BitAddress, XBee16BitAddress, byte[])}.
	 * 
	 * <p>Verify that data cannot be sent if the device has an invalid operating mode.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 */
	@Test(expected=InvalidOperatingModeException.class)
	public void testSendDataInvalidOperatingMode() throws TimeoutException, XBeeException {
		// Throw an invalid operating mode exception when sending and checking any Transmit packet.
		Mockito.doThrow(new InvalidOperatingModeException()).when(xbeeDevice).sendAndCheckXBeePacket(Mockito.any(TransmitPacket.class), Mockito.eq(false));
		
		xbeeDevice.sendData(XBEE_64BIT_ADDRESS, XBEE_16BIT_ADDRESS, DATA.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.XBeeDevice#sendData(XBee64BitAddress, XBee16BitAddress, byte[])}.
	 * 
	 * <p>Verify that data cannot be sent if there is a timeout sending and checking the Transmit packet.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 */
	@Test(expected=TimeoutException.class)
	public void testSendDataTimeout() throws TimeoutException, XBeeException {
		// Throw a timeout exception when sending and checking any Transmit packet.
		Mockito.doThrow(new TimeoutException()).when(xbeeDevice).sendAndCheckXBeePacket(Mockito.any(TransmitPacket.class), Mockito.eq(false));
		
		xbeeDevice.sendData(XBEE_64BIT_ADDRESS, XBEE_16BIT_ADDRESS, DATA.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.XBeeDevice#sendData(XBee64BitAddress, XBee16BitAddress, byte[])}.
	 * 
	 * <p>Verify that data cannot be sent if there is a transmit exception when sending and checking the Transmit packet.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 */
	@Test(expected=TransmitException.class)
	public void testSendDataTransmitException() throws TimeoutException, XBeeException {
		// Throw a transmit exception when sending and checking any Transmit packet.
		Mockito.doThrow(new TransmitException(null)).when(xbeeDevice).sendAndCheckXBeePacket(Mockito.any(TransmitPacket.class), Mockito.eq(false));
		
		xbeeDevice.sendData(XBEE_64BIT_ADDRESS, XBEE_16BIT_ADDRESS, DATA.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.XBeeDevice#sendData(XBee64BitAddress, XBee16BitAddress, byte[])}.
	 * 
	 * <p>Verify that data cannot be sent if there is an IO error when sending and checking the Transmit packet.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 */
	@Test(expected=XBeeException.class)
	public void testSendDataIOException() throws TimeoutException, XBeeException {
		// Throw an XBee exception when sending and checking any Transmit packet.
		Mockito.doThrow(new XBeeException()).when(xbeeDevice).sendAndCheckXBeePacket(Mockito.any(TransmitPacket.class), Mockito.eq(false));
		
		xbeeDevice.sendData(XBEE_64BIT_ADDRESS, XBEE_16BIT_ADDRESS, DATA.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.XBeeDevice#sendData(XBee64BitAddress, XBee16BitAddress, byte[])}.
	 * 
	 * <p>Verify that data is sent successfully if there is not any error when sending and checking the Transmit packet.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 */
	@Test
	public void testSendDataSuccess() throws TimeoutException, XBeeException {
		// Do nothing when sending and checking any Transmit packet.
		Mockito.doNothing().when(xbeeDevice).sendAndCheckXBeePacket(Mockito.any(TransmitPacket.class), Mockito.eq(false));
		
		xbeeDevice.sendData(XBEE_64BIT_ADDRESS, XBEE_16BIT_ADDRESS, DATA.getBytes());
		
		// Verify the sendAndCheckXBeePacket(XBeePacket, boolean) method was called.
		Mockito.verify(xbeeDevice, Mockito.times(1)).sendAndCheckXBeePacket(Mockito.eq(transmitPacket), Mockito.eq(false));
	}
}
