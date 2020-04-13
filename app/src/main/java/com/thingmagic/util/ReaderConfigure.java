package com.thingmagic.util;

import com.thingmagic.Gen2;
import com.thingmagic.ReadPlan;
import com.thingmagic.Reader;
import com.thingmagic.ReaderException;
import com.thingmagic.SerialReader;
import com.thingmagic.SimpleReadPlan;
import com.thingmagic.TMConstants;
import com.thingmagic.TagProtocol;

public class ReaderConfigure {
    private static final String TAG = "ReaderConfigure";
    private Reader reader = null;
//reader
    int baudrate = 115200; // 9600, 115200, 921600, 19200, 38400, 57600, 230400, 460800
    int commandTimeout = 1000;
    byte[] licenseKey = null;
    SerialReader.PowerMode powerMode = null; // Controls the power-consumption mode of the reader as a whole.
    int transportTimeout = 1000;
    SerialReader.UserMode userMode = null; // Sets a number of protocol specific parameters for particular usecases.
    String uri = null;
    SerialReader.UserConfigOp userConfig = null; // M6e and Micro
//Antenna
    boolean checkPort = true; // Not Micro or Nano
    int[] connectedPortList = null; // Not Micro or Nano
    int[] portList = null;
    Integer[] portSwitchGpos = null;
    int[][] returnloss = null; // Micro, M6e --> (tx port, return loss)
    int[][] settlingTimeList = null; // 每个传输端口的设置时间值的列表。每个列表元素是一个长度为2的数组; --> ( txRxMap number, ms)
    int[][] txRxMap = null; // [[1,1,1],[2,2,2]]
//gen2
    Gen2.Password accessPassword = null;
    Enum writeMode = Gen2.WriteMode.WORD_ONLY;
    Gen2.LinkFrequency BLF = Gen2.LinkFrequency.LINK250KHZ;
    Gen2.Q q = new Gen2.DynamicQ();
    Gen2.TagEncoding tagEncoding = Gen2.TagEncoding.M4; // MillerM
    Gen2.Session session = Gen2.Session.S0;
    Gen2.Target target = Gen2.Target.A;
    Gen2.Tari tari = null;
    int writeReplyTimeout = 20000; // Max. timeout = 21000us, Min. timeout = 1000us.
    boolean writeEarlyExit = true;
//gpio
    int[] inputList = null;
    int[] outputList = null;
//iso18000-6b
//radio
    boolean enablePowerSave = false; // M6e
    int powerMax = 0;
    int powerMin = 0;
    int[][] portReadPowerList = null;
    int[][] portWritePowerList = null;
    int readPower = 0;
    int writePower = 0;
    int temperature = 0; // Contains the temperature of the reader radio, in degrees C.
//read
    int asyncOffTime = 0;
    int asyncOnTime = 250;
    ReadPlan plan = new SimpleReadPlan();
    int[][] triggerGpi = null; // Defines GPI pins that trigger reading under Autonomous Operation
//region
    Reader.Region region = null;
    Reader.Region[] supportedRegions = null;
    int[] hopTable = null;
    int hopTime = 0;
    boolean lbtEnable = false; // Not all regions support LBT. For the M6e, Micro, and Nano modules, only the JP (Japan) region supports LBT.
//status
    boolean antennaEnable = false; // Enables/disables the antenna field in StatusListener reports.
    boolean frequencyEnable = false; // Enables/disables the frequency field in StatusListener reports.
    boolean temperatureEnable = false; // Enables/disables the temperature field in StatusListener reports.
//tagReadData
    boolean recordHighestRssi = false;
    boolean reportRssiInDbm = false; // M5e
    boolean uniqueByAntenna = false; // Controls whether reads on different antennas are reported separately.
    boolean uniqueByData = false; // Controls whether reads with different data memory values are reported separately when reading tag data.
    int tagopSuccess = 0;
    int tagopFailures = -1;
//tagop
    int antenna = 0; //First element of /reader/antenna/connectedPortList
    TagProtocol protocol = null;
//version
    String hardware = null;
    String model = null;
    String productGroup = null; // Contains the Product group type (“Module”, “Ruggedized Reader”, “USB Reader”) that helps define the physical port settings, allowing Auto Configuration.
    String serial = null;
    String software = null; // Contains a version identifier for the readerʼs software.
    TagProtocol[] supportedProtocols = null;

    public ReaderConfigure(Reader reader) {
        this.reader = reader;
    }

    public void loadConfigure() throws ReaderException {
        baudrate = (int) reader.paramGet(TMConstants.TMR_PARAM_BAUDRATE);

        hardware = (String) reader.paramGet(TMConstants.TMR_PARAM_VERSION_HARDWARE);
        model = (String) reader.paramGet(TMConstants.TMR_PARAM_VERSION_MODEL);
        productGroup = (String) reader.paramGet(TMConstants.TMR_PARAM_READER_PRODUCTGROUP);
        serial = (String) reader.paramGet(TMConstants.TMR_PARAM_VERSION_SERIAL);
        software = (String) reader.paramGet(TMConstants.TMR_PARAM_VERSION_SOFTWARE);
        supportedProtocols = (TagProtocol[]) reader.paramGet(TMConstants.TMR_PARAM_VERSION_SUPPORTEDPROTOCOLS);

        commandTimeout = (int) reader.paramGet(TMConstants.TMR_PARAM_COMMANDTIMEOUT);
        //licenseKey = (byte[]) reader.paramGet(TMConstants.TMR_PARAM_LICENSE_KEY);
        powerMode = (SerialReader.PowerMode) reader.paramGet(TMConstants.TMR_PARAM_POWERMODE);
        transportTimeout = (int) reader.paramGet(TMConstants.TMR_PARAM_TRANSPORTTIMEOUT);
        userMode = (SerialReader.UserMode) reader.paramGet(TMConstants.TMR_PARAM_USERMODE);
        //ToDo: All Serial devices can't run this param
        if(!model.equalsIgnoreCase("M5e"))
        {
//            uri = (String) reader.paramGet(TMConstants.TMR_PARAM_READER_URI);
//            userConfig = (SerialReader.UserConfigOp) reader.paramGet(TMConstants.TMR_PARAM_USER_CONFIG);
        }
        else
        {
//            uri = "M5e";
        }

        checkPort = (boolean) reader.paramGet(TMConstants.TMR_PARAM_ANTENNA_CHECKPORT);
        connectedPortList = (int[]) reader.paramGet(TMConstants.TMR_PARAM_ANTENNA_CONNECTEDPORTLIST);
        portList = (int[]) reader.paramGet(TMConstants.TMR_PARAM_ANTENNA_PORTLIST);
        portSwitchGpos = (Integer[]) reader.paramGet(TMConstants.TMR_PARAM_ANTENNA_PORTSWITCHGPOS);
//        returnloss = (int[][]) reader.paramGet(TMConstants.TMR_PARAM_ANTENNA_RETURNLOSS);
        settlingTimeList = (int[][]) reader.paramGet(TMConstants.TMR_PARAM_ANTENNA_SETTLINGTIMELIST);
        txRxMap = (int[][]) reader.paramGet(TMConstants.TMR_PARAM_ANTENNA_TXRXMAP);
        accessPassword = (Gen2.Password) reader.paramGet(TMConstants.TMR_PARAM_GEN2_ACCESSPASSWORD);
        writeMode = (Enum) reader.paramGet(TMConstants.TMR_PARAM_GEN2_WRITEMODE);
        BLF = (Gen2.LinkFrequency) reader.paramGet(TMConstants.TMR_PARAM_GEN2_BLF);
        q = (Gen2.Q) reader.paramGet(TMConstants.TMR_PARAM_GEN2_Q);
        tagEncoding = (Gen2.TagEncoding) reader.paramGet(TMConstants.TMR_PARAM_GEN2_TAGENCODING);
        session = (Gen2.Session) reader.paramGet(TMConstants.TMR_PARAM_GEN2_SESSION);
        target = (Gen2.Target) reader.paramGet(TMConstants.TMR_PARAM_GEN2_TARGET);
        if(!model.equalsIgnoreCase("M5e"))
        {
            tari = (Gen2.Tari) reader.paramGet(TMConstants.TMR_PARAM_GEN2_TARI);
        }
        else {

        }
        writeReplyTimeout = (int) reader.paramGet(TMConstants.TMR_PARAM_READER_WRITE_REPLY_TIMEOUT);
        writeEarlyExit = (boolean) reader.paramGet(TMConstants.TMR_PARAM_READER_WRITE_EARLY_EXIT);
//        inputList = (int[]) reader.paramGet(TMConstants.TMR_PARAM_GPIO_INPUTLIST);
//        outputList = (int[]) reader.paramGet(TMConstants.TMR_PARAM_GPIO_OUTPUTLIST);
        enablePowerSave = (boolean) reader.paramGet(TMConstants.TMR_PARAM_RADIO_ENABLEPOWERSAVE);
        powerMax = (int) reader.paramGet(TMConstants.TMR_PARAM_RADIO_POWERMAX);
        powerMin = (int) reader.paramGet(TMConstants.TMR_PARAM_RADIO_POWERMIN);
        portReadPowerList = (int[][]) reader.paramGet(TMConstants.TMR_PARAM_RADIO_PORTREADPOWERLIST);
        portWritePowerList = (int[][]) reader.paramGet(TMConstants.TMR_PARAM_RADIO_PORTWRITEPOWERLIST);
        readPower = (int) reader.paramGet(TMConstants.TMR_PARAM_RADIO_READPOWER);
        writePower = (int) reader.paramGet(TMConstants.TMR_PARAM_RADIO_WRITEPOWER);
        temperature = (int) reader.paramGet(TMConstants.TMR_PARAM_RADIO_TEMPERATURE);
        asyncOffTime = (int) reader.paramGet(TMConstants.TMR_PARAM_READ_ASYNCOFFTIME);
        asyncOnTime = (int) reader.paramGet(TMConstants.TMR_PARAM_READ_ASYNCONTIME);
        plan = (ReadPlan) reader.paramGet(TMConstants.TMR_PARAM_READ_PLAN);
//        triggerGpi = (int[][]) reader.paramGet(TMConstants.TMR_PARAM_TRIGGER_READ_GPI);
        region = (Reader.Region) reader.paramGet(TMConstants.TMR_PARAM_REGION_ID);
        supportedRegions = (Reader.Region[]) reader.paramGet(TMConstants.TMR_PARAM_REGION_SUPPORTEDREGIONS);
        hopTable = (int[]) reader.paramGet(TMConstants.TMR_PARAM_REGION_HOPTABLE);
        hopTime = (int) reader.paramGet(TMConstants.TMR_PARAM_REGION_HOPTIME);
//        lbtEnable = (boolean) reader.paramGet(TMConstants.TMR_PARAM_REGION_LBT_ENABLE);
        if(!model.equalsIgnoreCase("M5e"))
        {
            antennaEnable = (boolean) reader.paramGet(TMConstants.TMR_PARAM_READER_STATUS_ANTENNA);
            frequencyEnable = (boolean) reader.paramGet(TMConstants.TMR_PARAM_READER_STATUS_FREQUENCY);
            temperatureEnable = (boolean) reader.paramGet(TMConstants.TMR_PARAM_READER_STATUS_TEMPERATURE);
        }
        else
        {

        }
        recordHighestRssi = (boolean) reader.paramGet(TMConstants.TMR_PARAM_TAGREADDATA_RECORDHIGHESTRSSI);
        reportRssiInDbm = (boolean) reader.paramGet(TMConstants.TMR_PARAM_TAGREADDATA_REPORTRSSIINDBM);
        uniqueByAntenna = (boolean) reader.paramGet(TMConstants.TMR_PARAM_TAGREADDATA_UNIQUEBYANTENNA);
        uniqueByData = (boolean) reader.paramGet(TMConstants.TMR_PARAM_TAGREADDATA_UNIQUEBYDATA);
        tagopSuccess = (int) reader.paramGet(TMConstants.TMR_PARAM_READER_TAGOP_SUCCESSES);
        tagopFailures = (int) reader.paramGet(TMConstants.TMR_PARAM_READER_TAGOP_FAILURES);
        antenna = (int) reader.paramGet(TMConstants.TMR_PARAM_TAGOP_ANTENNA);
        protocol = (TagProtocol) reader.paramGet(TMConstants.TMR_PARAM_TAGOP_PROTOCOL);
    }

    public int getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    public int getCommandTimeout() {
        return commandTimeout;
    }

    public void setCommandTimeout(int commandTimeout) {
        this.commandTimeout = commandTimeout;
    }

    public byte[] getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(byte[] licenseKey) {
        this.licenseKey = licenseKey;
    }

    public SerialReader.PowerMode getPowerMode() {
        return powerMode;
    }

    public void setPowerMode(SerialReader.PowerMode powerMode) {
        this.powerMode = powerMode;
    }

    public int getTransportTimeout() {
        return transportTimeout;
    }

    public void setTransportTimeout(int transportTimeout) {
        this.transportTimeout = transportTimeout;
    }

    public SerialReader.UserMode getUserMode() {
        return userMode;
    }

    public void setUserMode(SerialReader.UserMode userMode) {
        this.userMode = userMode;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public SerialReader.UserConfigOp getUserConfig() {
        return userConfig;
    }

    public void setUserConfig(SerialReader.UserConfigOp userConfig) {
        this.userConfig = userConfig;
    }

    public boolean isCheckPort() {
        return checkPort;
    }

    public void setCheckPort(boolean checkPort) {
        this.checkPort = checkPort;
    }

    public int[] getConnectedPortList() {
        return connectedPortList;
    }

    public void setConnectedPortList(int[] connectedPortList) {
        this.connectedPortList = connectedPortList;
    }

    public int[] getPortList() {
        return portList;
    }

    public void setPortList(int[] portList) {
        this.portList = portList;
    }

    public Integer[] getPortSwitchGpos() {
        return portSwitchGpos;
    }

    public void setPortSwitchGpos(Integer[] portSwitchGpos) {
        this.portSwitchGpos = portSwitchGpos;
    }

    public int[][] getReturnloss() {
        return returnloss;
    }

    public void setReturnloss(int[][] returnloss) {
        this.returnloss = returnloss;
    }

    public int[][] getSettlingTimeList() {
        return settlingTimeList;
    }

    public void setSettlingTimeList(int[][] settlingTimeList) {
        this.settlingTimeList = settlingTimeList;
    }

    public int[][] getTxRxMap() {
        return txRxMap;
    }

    public void setTxRxMap(int[][] txRxMap) {
        this.txRxMap = txRxMap;
    }

    public Gen2.Password getAccessPassword() {
        return accessPassword;
    }

    public void setAccessPassword(Gen2.Password accessPassword) {
        this.accessPassword = accessPassword;
    }

    public Enum getWriteMode() {
        return writeMode;
    }

    public void setWriteMode(Enum writeMode) {
        this.writeMode = writeMode;
    }

    public Gen2.LinkFrequency getBLF() {
        return BLF;
    }

    public void setBLF(Gen2.LinkFrequency BLF) {
        this.BLF = BLF;
    }

    public Gen2.Q getQ() {
        return q;
    }

    public void setQ(Gen2.Q q) {
        this.q = q;
    }

    public Gen2.TagEncoding getTagEncoding() {
        return tagEncoding;
    }

    public void setTagEncoding(Gen2.TagEncoding tagEncoding) {
        this.tagEncoding = tagEncoding;
    }

    public Gen2.Session getSession() {
        return session;
    }

    public void setSession(Gen2.Session session) {
        this.session = session;
    }

    public Gen2.Target getTarget() {
        return target;
    }

    public void setTarget(Gen2.Target target) {
        this.target = target;
    }

    public Gen2.Tari getTari() {
        return tari;
    }

    public void setTari(Gen2.Tari tari) {
        this.tari = tari;
    }

    public int getWriteReplyTimeout() {
        return writeReplyTimeout;
    }

    public void setWriteReplyTimeout(int writeReplyTimeout) {
        this.writeReplyTimeout = writeReplyTimeout;
    }

    public boolean isWriteEarlyExit() {
        return writeEarlyExit;
    }

    public void setWriteEarlyExit(boolean writeEarlyExit) {
        this.writeEarlyExit = writeEarlyExit;
    }

    public int[] getInputList() {
        return inputList;
    }

    public void setInputList(int[] inputList) {
        this.inputList = inputList;
    }

    public int[] getOutputList() {
        return outputList;
    }

    public void setOutputList(int[] outputList) {
        this.outputList = outputList;
    }

    public boolean isEnablePowerSave() {
        return enablePowerSave;
    }

    public void setEnablePowerSave(boolean enablePowerSave) {
        this.enablePowerSave = enablePowerSave;
    }

    public int getPowerMax() {
        return powerMax;
    }

    public void setPowerMax(int powerMax) {
        this.powerMax = powerMax;
    }

    public int getPowerMin() {
        return powerMin;
    }

    public void setPowerMin(int powerMin) {
        this.powerMin = powerMin;
    }

    public int[][] getPortReadPowerList() {
        return portReadPowerList;
    }

    public void setPortReadPowerList(int[][] portReadPowerList) {
        this.portReadPowerList = portReadPowerList;
    }

    public int[][] getPortWritePowerList() {
        return portWritePowerList;
    }

    public void setPortWritePowerList(int[][] portWritePowerList) {
        this.portWritePowerList = portWritePowerList;
    }

    public int getReadPower() {
        return readPower;
    }

    public void setReadPower(int readPower) {
        this.readPower = readPower;
    }

    public int getWritePower() {
        return writePower;
    }

    public void setWritePower(int writePower) {
        this.writePower = writePower;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getAsyncOffTime() {
        return asyncOffTime;
    }

    public void setAsyncOffTime(int asyncOffTime) {
        this.asyncOffTime = asyncOffTime;
    }

    public int getAsyncOnTime() {
        return asyncOnTime;
    }

    public void setAsyncOnTime(int asyncOnTime) {
        this.asyncOnTime = asyncOnTime;
    }

    public ReadPlan getPlan() {
        return plan;
    }

    public void setPlan(ReadPlan plan) {
        this.plan = plan;
    }

    public int[][] getTriggerGpi() {
        return triggerGpi;
    }

    public void setTriggerGpi(int[][] triggerGpi) {
        this.triggerGpi = triggerGpi;
    }

    public Reader.Region getRegion() {
        return region;
    }

    public void setRegion(Reader.Region region) {
        this.region = region;
    }

    public Reader.Region[] getSupportedRegions() {
        return supportedRegions;
    }

    public void setSupportedRegions(Reader.Region[] supportedRegions) {
        this.supportedRegions = supportedRegions;
    }

    public int[] getHopTable() {
        return hopTable;
    }

    public void setHopTable(int[] hopTable) {
        this.hopTable = hopTable;
    }

    public int getHopTime() {
        return hopTime;
    }

    public void setHopTime(int hopTime) {
        this.hopTime = hopTime;
    }

    public boolean isLbtEnable() {
        return lbtEnable;
    }

    public void setLbtEnable(boolean lbtEnable) {
        this.lbtEnable = lbtEnable;
    }

    public boolean isAntennaEnable() {
        return antennaEnable;
    }

    public void setAntennaEnable(boolean antennaEnable) {
        this.antennaEnable = antennaEnable;
    }

    public boolean isFrequencyEnable() {
        return frequencyEnable;
    }

    public void setFrequencyEnable(boolean frequencyEnable) {
        this.frequencyEnable = frequencyEnable;
    }

    public boolean isTemperatureEnable() {
        return temperatureEnable;
    }

    public void setTemperatureEnable(boolean temperatureEnable) {
        this.temperatureEnable = temperatureEnable;
    }

    public boolean isRecordHighestRssi() {
        return recordHighestRssi;
    }

    public void setRecordHighestRssi(boolean recordHighestRssi) {
        this.recordHighestRssi = recordHighestRssi;
    }

    public boolean isReportRssiInDbm() {
        return reportRssiInDbm;
    }

    public void setReportRssiInDbm(boolean reportRssiInDbm) {
        this.reportRssiInDbm = reportRssiInDbm;
    }

    public boolean isUniqueByAntenna() {
        return uniqueByAntenna;
    }

    public void setUniqueByAntenna(boolean uniqueByAntenna) {
        this.uniqueByAntenna = uniqueByAntenna;
    }

    public boolean isUniqueByData() {
        return uniqueByData;
    }

    public void setUniqueByData(boolean uniqueByData) {
        this.uniqueByData = uniqueByData;
    }

    public int getTagopSuccess() {
        return tagopSuccess;
    }

    public void setTagopSuccess(int tagopSuccess) {
        this.tagopSuccess = tagopSuccess;
    }

    public int getTagopFailures() {
        return tagopFailures;
    }

    public void setTagopFailures(int tagopFailures) {
        this.tagopFailures = tagopFailures;
    }

    public int getAntenna() {
        return antenna;
    }

    public void setAntenna(int antenna) {
        this.antenna = antenna;
    }

    public TagProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(TagProtocol protocol) {
        this.protocol = protocol;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public TagProtocol[] getSupportedProtocols() {
        return supportedProtocols;
    }

    public void setSupportedProtocols(TagProtocol[] supportedProtocols) {
        this.supportedProtocols = supportedProtocols;
    }
}
