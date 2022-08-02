package com.parzivail.stardust;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.UUID;

import no.nordicsemi.android.ble.BleManager;

public class StardustBleManager extends BleManager
{
	private static final String TAG = "StardustBleManager";
	private static final UUID STARDUST_SERVICE_UUID = UUID.fromString("12345678-1234-5678-1234-56789abcdef0");
	private static final UUID STARDUST_CHAR_UUID = UUID.fromString("12345678-1234-5678-1234-56789abcdef1");

	private BluetoothGattCharacteristic characteristic;

	public StardustBleManager(@NonNull final Context context)
	{
		super(context);
	}

	@Override
	public int getMinLogPriority()
	{
		// Use to return minimal desired logging priority.
		return Log.VERBOSE;
	}

	@Override
	public void log(int priority, @NonNull String message)
	{
		// Log from here.
		Log.println(priority, TAG, message);
	}

	@NonNull
	@Override
	protected BleManagerGattCallback getGattCallback()
	{
		return new GattCallback();
	}

	private class GattCallback extends BleManagerGattCallback
	{
		@Override
		protected boolean isRequiredServiceSupported(@NonNull BluetoothGatt gatt)
		{
			BluetoothGattService service = gatt.getService(STARDUST_SERVICE_UUID);
			if (service != null)
				characteristic = service.getCharacteristic(STARDUST_CHAR_UUID);
			return characteristic != null;
		}

		@Override
		protected void initialize()
		{
			setNotificationCallback(characteristic)
					.with((device, data) -> {
						System.out.printf("Got %s bytes from device%n", data.size());
						System.out.printf("String: %s%n", data.getStringValue(0));
					});
			enableNotifications(characteristic)
					.enqueue();
		}

		@Override
		protected void onServicesInvalidated()
		{
			characteristic = null;
		}
	}
}
