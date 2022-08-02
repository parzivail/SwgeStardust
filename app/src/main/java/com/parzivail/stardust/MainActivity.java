package com.parzivail.stardust;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void tryConnectToDevice(View view)
	{
		TextView tb = (TextView)findViewById(R.id.tbMacAddress);

		var manager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
		var server = manager.getAdapter().getRemoteDevice(tb.getText().toString());

		var ble = new StardustBleManager(this);
		ble.connect(server)
		   .retry(3, 1000)
		   .timeout(5000)
		   .done(device -> {
			   Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
		   })
		   .fail((device, status) -> {
			   Toast.makeText(this, "fail: " + status, Toast.LENGTH_SHORT).show();
		   })
		   .enqueue();
	}
}