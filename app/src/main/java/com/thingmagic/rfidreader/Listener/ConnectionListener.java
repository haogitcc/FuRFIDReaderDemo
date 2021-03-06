package com.thingmagic.rfidreader.Listener;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.thingmagic.R;
import com.thingmagic.Reader;
import com.thingmagic.SerialTransportTCP;
import com.thingmagic.rfidreader.activities.ReaderActivity;
import com.thingmagic.util.ReaderConfigure;
import com.thingmagic.rfidreader.services.UsbService;
import com.thingmagic.util.LoggerUtil;
import com.thingmagic.util.ReaderConnect;
import com.thingmagic.util.Utilities;

import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;

public class ConnectionListener implements View.OnClickListener {

	private static EditText ntReaderField;
	private static EditText customReaderField;
	private static ReaderActivity mReaderActivity;
	private static Reader reader = null;
	private static ReaderConfigure rconfig = null;
	private static LinearLayout servicelayout;
	private static Spinner serialList = null;
	private static RadioGroup readerRadioGroup = null;
	private static RadioButton serialReaderRadioButton = null;
	private static RadioButton networkReaderRadioButton = null;
	private static RadioButton customReaderRadioButton = null;
	private static TableLayout table = null;
	private static TextView validationField;
	private static TextView searchResultCount = null;
	private static TextView totalTagCountView = null;
	private static Button connectButton;
	private static Button readButton = null;
	private static ProgressDialog pDialog = null;

	private static LinearLayout antLayout = null;
	private static CheckBox ant1CheckBox = null;
	private static CheckBox ant2CheckBox = null;
	private static CheckBox ant3CheckBox = null;
	private static CheckBox ant4CheckBox = null;

	private static Spinner regionSpinner = null;
	private static Spinner baudrateSpinner = null;

	private static EditText readpowerEdittext = null;
	private static EditText writepowerEdittext = null;

	private static String TAG = "ConnectionListener";
	private static String readerName = null;
	private static String readerChecked;

	public ConnectionListener(ReaderActivity readerActivity) {
		mReaderActivity = readerActivity;
		pDialog = new ProgressDialog(readerActivity);
		pDialog.setCancelable(false);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		findAllViewsById();
	}

	private void findAllViewsById() {
		ntReaderField = (EditText) mReaderActivity.findViewById(R.id.search_edit_text);
		customReaderField = (EditText) mReaderActivity.findViewById(R.id.custom_reader_field);
		connectButton = (Button) mReaderActivity.findViewById(R.id.Connect_button);
		servicelayout = (LinearLayout) mReaderActivity.findViewById(R.id.ServiceLayout);
		validationField = (TextView) mReaderActivity.findViewById(R.id.ValidationField);
		serialList = (Spinner) mReaderActivity.findViewById(R.id.SerialList);
		readerRadioGroup = (RadioGroup) mReaderActivity.findViewById(R.id.Reader_radio_group);
		serialReaderRadioButton = (RadioButton) mReaderActivity.findViewById(R.id.SerialReader_radio_button);
		networkReaderRadioButton = (RadioButton) mReaderActivity.findViewById(R.id.NetworkReader_radio_button);
		customReaderRadioButton = (RadioButton) mReaderActivity.findViewById(R.id.CustomReader_radio_button);
		table = (TableLayout) mReaderActivity.findViewById(R.id.tablelayout);
		searchResultCount = (TextView) mReaderActivity.findViewById(R.id.search_result_view);
		totalTagCountView = (TextView) mReaderActivity.findViewById(R.id.totalTagCount_view);

		readButton = (Button) mReaderActivity.findViewById(R.id.Read_button);

		antLayout = (LinearLayout) mReaderActivity.findViewById(R.id.ant_layout);
		ant1CheckBox = (CheckBox) mReaderActivity.findViewById(R.id.ant1_cbx);
		ant2CheckBox = (CheckBox) mReaderActivity.findViewById(R.id.ant2_cbx);
		ant3CheckBox = (CheckBox) mReaderActivity.findViewById(R.id.ant3_cbx);
		ant4CheckBox = (CheckBox) mReaderActivity.findViewById(R.id.ant4_cbx);

		regionSpinner = (Spinner) mReaderActivity.findViewById(R.id.regionSpinner);
		baudrateSpinner = (Spinner) mReaderActivity.findViewById(R.id.baudrateSpinner);

		readpowerEdittext = (EditText) mReaderActivity.findViewById(R.id.readpowerEdittext);
		writepowerEdittext = (EditText) mReaderActivity.findViewById(R.id.writepowerEdittext);

	}

	@Override
	public void onClick(View arg0) {
		validationField.setText("");
		validationField.setVisibility(8);
		searchResultCount.setText("");
		totalTagCountView.setText("");
		table.removeAllViews();
		int id = readerRadioGroup.getCheckedRadioButtonId();
		RadioButton readerRadioButton = (RadioButton) mReaderActivity.findViewById(id);
		readerChecked = readerRadioButton.getText().toString();
		Utilities utilities = new Utilities();
		String query = "";
		boolean validPort = true;
		if(connectButton.getText().toString().equalsIgnoreCase("Connect"))
		{
			if (readerChecked.equalsIgnoreCase("SerialReader")) {
	//			if(!Utilities.checkIfBluetoothEnabled(mReaderActivity)){
	//				return;
	//			}
				if(serialList.getCount()<1){
				  return;
			    }
				query = serialList.getSelectedItem().toString();
				readerName = query.split(" \n ")[0];
				LoggerUtil.debug(TAG, "reader name  after split : "+readerName);
				if(query.startsWith("/dev")){
					LoggerUtil.debug(TAG, "in if condition : ");
					UsbService usbService = new UsbService();
					usbService.setUsbManager(query, mReaderActivity);
				   query = "tmr:///"+ query.split("/")[1];
				}else{
					 query = "tmr:///"+ query.split(" \n ")[1];
				}
				System.out.println("query :"+ query);
			}
			else if (readerChecked.equalsIgnoreCase("NetworkReader")) {
	//			if(!utilities.checkIfWiFiEnabled(mReaderActivity)){
	//				return;
	//			}
				query = ntReaderField.getText().toString().trim();
				readerName = query;
	//			query="172.16.16.121";
				System.out.println("readerName :"+ readerName);
				 if(!query.equalsIgnoreCase("")){
					 Scanner input = new Scanner(query);
					 if(!Character.isDigit( query.charAt(0)) && (ntReaderField.getTag() != null)){
						 query = ntReaderField.getTag().toString(); 
					 }
	//			 query="172.16.16.24";
				 }
				 System.out.println("query :"+ query);
	
				validPort = Utilities.validateIPAddress(validationField, query);
				if (validPort) {
					query = "tmr://" + query;
					validationField.setVisibility(8);
					// Closing keyPad manually
					InputMethodManager imm = (InputMethodManager) mReaderActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(ntReaderField.getWindowToken(), 0);
				} else {
					validationField.setVisibility(0);
					return;
				}
			}
			else if (readerChecked.equalsIgnoreCase("CustomReader"))
	        {
				try {
					
					query = customReaderField.getText().toString().trim();
					if (query.length() == 0) {
						throw new Exception("* Field can not be empty.");
					}

					URI uri = new URI(query);
					String scheme = uri.getScheme();

					if (scheme == null) {
						throw new Exception("Blank URI scheme.");
					}

				   Reader.setSerialTransport("tcp", new SerialTransportTCP.Factory());
				} catch (Exception ex) {
					validationField.setText(ex.getMessage());
					validationField.setVisibility(0);
					return;
				}
	        }
		}

		ReaderConnectionThread readerConnectionThread = new ReaderConnectionThread(query, connectButton.getText().toString());
		readerConnectionThread.execute();
	}



	private static class ReaderConnectionThread extends
			AsyncTask<Void, Void, String> {
		private String uriString = "";
		private String operation;
		private boolean operationStatus = true;

		public ReaderConnectionThread(String requestedQuery, String operation) {
			this.uriString = requestedQuery;
			this.operation = operation;
			LoggerUtil.debug(TAG, "query="+requestedQuery+", operation="+operation);
		}

		@Override
		protected void onPreExecute() {
			LoggerUtil.debug(TAG, "** onPreExecute **");			
			if (operation.equalsIgnoreCase("Connect")) {
				disableEdit();
				pDialog.setMessage("Connecting. Please wait...");
			} else {
				readButton.setClickable(false);
				disableEdit();
				pDialog.setMessage("Disconnecting. Please wait...");
			}
			pDialog.show();
			searchResultCount.setText("");
		}

		@Override
		protected String doInBackground(Void... params) {
			String exception = "Exception :";
			try {
				if (operation.equalsIgnoreCase("Connect")) {
					reader = ReaderConnect.connect(uriString);
					rconfig = new ReaderConfigure(reader);
					rconfig.loadConfigure();
					LoggerUtil.debug(TAG, "Reader Connected");
				} else {
					reader.destroy();
					LoggerUtil.debug(TAG, "Reader Disconnected");
				}

			} catch (Exception ex) {
				operationStatus = false;
				if (ex.getMessage().contains("Connection is not created")
						|| ex.getMessage().startsWith("Failed to connect")) {
					exception += "Failed to connect to " + readerName;
				} else {
					exception += ex.getMessage();
				}
				LoggerUtil.error(TAG, "Exception while Connecting :", ex);
			}
			return exception;
		}

		@Override
		protected void onPostExecute(String exception) {
			pDialog.dismiss();
			LoggerUtil.debug(TAG, "** onPostExecute **");
			if (!operationStatus) {
				validationField.setText(exception);
				validationField.setVisibility(0);
				totalTagCountView.setText("");
				if (operation.equalsIgnoreCase("Connect")) {
					connectButton.setText("Connect");
					enableEdit();
					mReaderActivity.reader = null;
				}
			} else {
				validationField.setText("");
				validationField.setVisibility(8);
				if (operation.equalsIgnoreCase("Connect")) {
					connectButton.setText("Disconnect");
					servicelayout.setVisibility(0);
					readerRadioGroup.setVisibility(8);
					disableEdit();
					connectButton.setClickable(true);
					readButton.setClickable(true);
					mReaderActivity.reader = reader;

//					InitRegion();
					ConfigureAntennaBoxes(reader);


				} else {
					connectButton.setText("Connect");
					servicelayout.setVisibility(8);
					readerRadioGroup.setVisibility(0);
					enableEdit();
					totalTagCountView.setText("");
					mReaderActivity.reader = null;
					System.gc();
				}
			}
		}

		private void InitRegion() {
			ArrayAdapter<Reader.Region> adapter = new ArrayAdapter<>(mReaderActivity, android.R.layout.simple_spinner_dropdown_item);
			for (Reader.Region region : rconfig.getSupportedRegions())
			{
				adapter.add(region);
			}
			regionSpinner.setAdapter(adapter);
		}

		/// <summary>
		/// Configure antennas
		/// </summary>
		/// <param name="objReader"></param>
		public void ConfigureAntennaBoxes(Reader objReader)
		{
			// Cast int[] return values to IList<int> instead of int[] to get Contains method
			ArrayList<Integer> existingAntennas = null;
			ArrayList<Integer> detectedAntennas = null;
			ArrayList<Integer> validAntennas = null;
			boolean checkPort = false;
			if (null == objReader)
			{
				ArrayList<Integer> empty = new ArrayList<>();
				existingAntennas = detectedAntennas = validAntennas = empty;
			}
			else
			{
				switch (rconfig.getModel())
				{
					case "Astra":
						checkPort = true;
						break;
					default:
						checkPort = rconfig.isCheckPort();
						break;
				}
				existingAntennas = ArrayToList(rconfig.getPortList());
				detectedAntennas = ArrayToList(rconfig.getConnectedPortList());
				validAntennas = checkPort ? detectedAntennas : existingAntennas;
			}
//			chkbxAntennaDetection.IsChecked = checkPort;
			CheckBox[] antennaBoxes = { ant1CheckBox, ant2CheckBox, ant3CheckBox, ant4CheckBox };
//			TextBox[] readPowerTextBoxes = { txtReadPowerAnt1, txtReadPowerAnt2, txtReadPowerAnt3, txtReadPowerAnt4 };
//			TextBox[] writePowerTextBoxes = { txtWritePowerAnt1, txtWritePowerAnt2, txtWritePowerAnt3, txtWritePowerAnt4 };
//			Label[] powerLabel = { lblPowerAnt1, lblPowerAnt2, lblPowerAnt3, lblPowerAnt4 };
			int antNum = 1;
			for (CheckBox cb : antennaBoxes)
			{
				if (existingAntennas.contains(antNum))
				{
					cb.setVisibility(View.VISIBLE);
//					readPowerTextBoxes[antNum - 1].Visibility = writePowerTextBoxes[antNum - 1].Visibility = powerLabel[antNum - 1].Visibility = Visibility.Visible;
				}
				else
				{
					cb.setVisibility(View.GONE);
//					readPowerTextBoxes[antNum - 1].Visibility = writePowerTextBoxes[antNum - 1].Visibility = powerLabel[antNum - 1].Visibility = Visibility.Collapsed;
				}
				if (validAntennas.contains(antNum))
				{
					cb.setEnabled(true);
//					readPowerTextBoxes[antNum - 1].IsEnabled = writePowerTextBoxes[antNum - 1].IsEnabled = powerLabel[antNum - 1].IsEnabled = true;
				}
				else
				{
					cb.setEnabled(false);
//					readPowerTextBoxes[antNum - 1].IsEnabled = writePowerTextBoxes[antNum - 1].IsEnabled = powerLabel[antNum - 1].IsEnabled = false;
				}
				if (detectedAntennas.contains(antNum))
				{
					cb.setChecked(true);
				}
				else
				{
					cb.setChecked(false);
				}
				antNum++;
			}
		}

		/// <summary>
		/// Configure Logical antennas
		/// </summary>
		/// <param name="objReader"></param>
		public void ConfigureLogicalAntennaBoxes(Reader objReader)
		{
			// Cast int[] return values to IList<int> instead of int[] to get Contains method
			ArrayList<Integer> existingAntennas = null;
			ArrayList<Integer> detectedAntennas = null;
			ArrayList<Integer> validAntennas = null;
			boolean checkPort = false;
			if (null == objReader)
			{
				ArrayList<Integer> empty = new ArrayList<Integer>();
				existingAntennas = detectedAntennas = validAntennas = empty;
			}
			else
			{

				switch (rconfig.getModel())
				{
					case "Astra":
						checkPort = true;
						break;
					default:
						checkPort = rconfig.isCheckPort();
						break;
				}
				existingAntennas = ArrayToList(rconfig.getPortList());
				detectedAntennas = ArrayToList(rconfig.getConnectedPortList());
				validAntennas = checkPort ? detectedAntennas : existingAntennas;
			}
//			chkbxAntennaDetection.IsChecked = checkPort;
			CheckBox[] antennaBoxes = { ant1CheckBox, ant2CheckBox, ant3CheckBox, ant4CheckBox
//					, ant5CheckBox, ant6CheckBox, ant7CheckBox, ant8CheckBox,
//					ant9CheckBox, ant10CheckBox, ant11CheckBox, ant12CheckBox, ant13CheckBox, ant14CheckBox, ant15CheckBox, ant16CheckBox,
//					ant17CheckBox, ant18CheckBox, ant19CheckBox, ant20CheckBox, ant21CheckBox, ant22CheckBox, ant23CheckBox, ant24CheckBox,
//					ant25CheckBox, ant26CheckBox, ant27CheckBox, ant28CheckBox, ant29CheckBox, ant30CheckBox, ant31CheckBox, ant32CheckBox,
//					ant33CheckBox, ant34CheckBox, ant35CheckBox, ant36CheckBox, ant37CheckBox, ant38CheckBox, ant39CheckBox, ant40CheckBox,
//					ant41CheckBox, ant42CheckBox, ant43CheckBox, ant44CheckBox, ant45CheckBox, ant46CheckBox, ant47CheckBox, ant48CheckBox,
//					ant49CheckBox, ant50CheckBox, ant51CheckBox, ant52CheckBox, ant53CheckBox, ant54CheckBox, ant55CheckBox, ant56CheckBox,
//					ant57CheckBox, ant58CheckBox, ant59CheckBox, ant60CheckBox, ant61CheckBox, ant62CheckBox, ant63CheckBox, ant64CheckBox
			};
			int antNum = 1;
			for (CheckBox cb : antennaBoxes)
			{
				if (existingAntennas.contains(antNum))
				{
					cb.setVisibility(View.VISIBLE);
				}
				else
				{
					cb.setVisibility(View.GONE);
				}
				if (validAntennas.contains(antNum))
				{
					cb.setEnabled(true);
				}
				else
				{
					cb.setEnabled(false);
				}
				if (detectedAntennas.contains(antNum))
				{
					cb.setChecked(true);
				}
				else
				{
					cb.setChecked(false);
				}
				antNum++;
			}
		}

		private ArrayList<Integer> ArrayToList(int[] portList) {
			ArrayList<Integer> list=new ArrayList<Integer>();
			for(int i : portList){
				list.add(i);
			}
			return list;
		}

		/// <summary>
		/// Get selected antenna list
		/// </summary>
		/// <returns></returns>
		private ArrayList<Integer> GetSelectedAntennaList()
		{
			CheckBox[] antennaBoxes = { ant1CheckBox, ant2CheckBox, ant3CheckBox, ant4CheckBox,
//					ant5CheckBox, ant6CheckBox, ant7CheckBox, ant8CheckBox,
//					ant9CheckBox, ant10CheckBox, ant11CheckBox, ant12CheckBox, ant13CheckBox, ant14CheckBox, ant15CheckBox, ant16CheckBox,
//					ant17CheckBox, ant18CheckBox, ant19CheckBox, ant20CheckBox, ant21CheckBox, ant22CheckBox, ant23CheckBox, ant24CheckBox,
//					ant25CheckBox, ant26CheckBox, ant27CheckBox, ant28CheckBox, ant29CheckBox, ant30CheckBox, ant31CheckBox, ant32CheckBox,
//					ant33CheckBox, ant34CheckBox, ant35CheckBox, ant36CheckBox, ant37CheckBox, ant38CheckBox, ant39CheckBox, ant40CheckBox,
//					ant41CheckBox, ant42CheckBox, ant43CheckBox, ant44CheckBox, ant45CheckBox, ant46CheckBox, ant47CheckBox, ant48CheckBox,
//					ant49CheckBox, ant50CheckBox, ant51CheckBox, ant52CheckBox, ant53CheckBox, ant54CheckBox, ant55CheckBox, ant56CheckBox,
//					ant57CheckBox, ant58CheckBox, ant59CheckBox, ant60CheckBox, ant61CheckBox, ant62CheckBox, ant63CheckBox, ant64CheckBox
			};
			ArrayList<Integer> ant = new ArrayList<Integer>();
			boolean flagAntBoxVisibility;
			for (int antIdx = 0; antIdx < antennaBoxes.length; antIdx++)
			{
				CheckBox antBox = antennaBoxes[antIdx];
				int antBoxVisibility = antBox.getVisibility();
				if (antBoxVisibility == View.VISIBLE)
				{
					flagAntBoxVisibility = true;
				}
				else
				{
					flagAntBoxVisibility = false;
				}
				if (flagAntBoxVisibility && (boolean) antBox.isEnabled() && (boolean) antBox.isChecked())
				{
					int antNum = antIdx + 1;
					ant.add(antNum);
				}
			}
			return ant;
		}

		private void disableEdit() {
			connectButton.setClickable(false);
			ntReaderField.setEnabled(false);
			customReaderField.setEnabled(false);
			serialList.setEnabled(false);
			serialReaderRadioButton.setClickable(false);
			networkReaderRadioButton.setClickable(false);
			customReaderRadioButton.setClickable(false);
		}

		private void enableEdit() {
			connectButton.setClickable(true);
			ntReaderField.setEnabled(true);
			serialList.setEnabled(true);
			customReaderField.setEnabled(true);
			serialReaderRadioButton.setClickable(true);
			networkReaderRadioButton.setClickable(true);
			customReaderRadioButton.setClickable(true);
		}
	}
}
