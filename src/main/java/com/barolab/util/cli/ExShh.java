package com.kosh.ecli.ex_ecli;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

//import com.ericssonlg.common.logger.LoggerUtil;
//import com.ericssonlg.common.logger.model.LogFileEnum;
//import com.ericssonlg.common.util.epcUtil;
//import com.ericssonlg.hmi.model.CommandSshType;
//import com.ericssonlg.hmi.model.NetworkElement;
//import com.ericssonlg.hmi.server.main.HmiServerProperties;
//import com.ericssonlg.hmi.server.neadaptor.adaptor.NeAdaptorI;
//import com.ericssonlg.hmi.server.neadaptor.common.NeAdaptorConstants;
//import com.ericssonlg.hmi.server.neadaptor.model.CliRequest;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshTerminal4NeAdaptor extends SshTerminalInfo implements NeAdaptorI {

           private final Logger logger = LoggerUtil.getInstance().getLogger(LogFileEnum.HMI_SSH.name());
           
//         private final String ENTER_DELIMETER = new String(new byte[] {13, 10}); // 13(Carriage Return) 10(New Line)
//         private final String TAB_DELIMETER = new String(new byte[] {27, 91, 74}); // 27(Escape) 91([) 74(j)
           
           private final Semaphore semaphore = new Semaphore(1, true);
           
           private Session session = null;
           private Channel channel = null;
           private OutputStream output = null;
           private InputStream input = null;
           private CommandSshType cmdSshType;
           private long lastRequestTime;
           private CliRequest cliRequest;
           private ExecutorService es;
           private final String[] errorResults = {
                                "Connection to COM failed",
                                "Maximum number of administrators has been reached",
                                "Initialization is not complete",
                                "start shell failed"
           };

           public SshTerminal4NeAdaptor(CommandSshType cmdSshType, String sessionKey, NetworkElement ne) {
                     this.cmdSshType = cmdSshType;
                     this.hostname = ne.getIpAddress();
                     this.port = ne.getPort();
                     this.username = ne.getUsername();
                     this.password = ne.getPassword();
                     this.nodeId = ne.getNodeId();
                     this.nodeName = ne.getNodeName();
                     this.neType = ne.getNeType();
                     this.sessionKey = sessionKey;
                     lastRequestTime = System.currentTimeMillis();
                     es = Executors.newSingleThreadExecutor();
           }

           public CommandSshType getCmdSshType() {
                     return cmdSshType;
           }

           public long getLastRequestTime() {
                     return lastRequestTime;
           }

           public boolean isReady() {
                     if(channel != null && channel.isConnected()) {
                                return true;
                     } else {
                                return false;
                     }
           }

           public boolean checkReady() throws JSchException, SocketTimeoutException, IOException, InterruptedException, ExecutionException, TimeoutException {
                     long startTime = System.currentTimeMillis();
                     boolean isReady = true;
                     JSch jsch = new JSch();

                     // HostKey 체크 제외
                     java.util.Properties config = new java.util.Properties();
                     config.put("UserKnownHostsFile", "/dev/null");
                     config.put("StrictHostKeyChecking", "no");

//                   session = jsch.getSession(username, hostname, port);
//                   session.setTimeout(5000);
//                   session.setPassword(password);
//                   session.setConfig(config);
                     if(Boolean.parseBoolean(System.getProperty("IS_TEST_MODE"))) {
                                session = jsch.getSession("ngepc", HmiServerProperties.RMI_SERVER_IP, 22);
                                session.setPassword("ngepc./");
                     } else {
                                session = jsch.getSession(username, hostname, port);
                                session.setPassword(password);
                     }
                     session.setTimeout(5000);
                     session.setConfig(config);
                     session.connect();

                     channel = session.openChannel("shell");
                     ((ChannelShell) channel).setPtyType("vt102");

                     channel.connect(5000);
                     
                     if(channel != null && channel.isConnected()) {
                                getInOutStream();
                                
                                if(Boolean.parseBoolean(System.getProperty("IS_TEST_MODE"))) {
                                           doCommand("pwd");
                                           doCommand("ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no vepc@10.180.86.10");
                                           doCommand("vepc");
                                }
                                
                                String result = doCommand("terminal length 0");
                                if(result.length() == 0 || isErrorResultMet(result)) {
                                           isReady = false;
                                }
                                logger.fine("'terminal length 0' ready=" + isReady);
                                
                                switch (cmdSshType) {
                                // OAM-CLI
                                case HMI_OAM_CLI_DIS:
                                case EM_GW_OAM_CLI_DIS:
                                case PM_OAM_CLI_DIS:
                                case VNF_GW_OAM_CLI_DIS:
                                case CARD_PORT_OAM_CLI_DIS:
                                case NBI_OAM_CLI_DIS:
                                case HMI_OAM_CLI_CONFIG:
                                case VNF_GW_OAM_CLI_CONFIG:
                                           if(isReady) {
                                                     result = doCommand("start oam-cli");
                                                     if(result.length() == 0 || isErrorResultMet(result)){
                                                                isReady = false;
                                                     }
                                           }
                                           logger.fine("'start oam-cli' ready=" + isReady);
                                           break;
                                default:
                                           break;
                                }
                     } else {
                                isReady = false;
                     }
                     
                     long now = System.currentTimeMillis();
                     logger.severe("ELSP connectTime=" + (now - startTime) + ",isReady=" + isReady + "\t " + cmdSshType + "_" + nodeId + "," + nodeName);
                     return isReady;
           }
           
           private void getInOutStream() throws IOException {
                     output = channel.getOutputStream();
                     input = channel.getInputStream();
           }
           
           public void close() {
//                   logger.fine("close called.\t" + cmdSshType + "_" + nodeId + "," + nodeName + "," + lastRequestTime);
                     if (session != null) {
                                try {
                                           session.disconnect();
                                } catch (Exception e) {
                                           logger.severe("Exception : " + epcUtil.printExceptionString(e) + "\t" + cmdSshType + "_" + nodeId + "," + nodeName + "," + lastRequestTime);
                                }
                     }
                     logger.severe("close end.\t" + cmdSshType + "_" + nodeId + "," + nodeName + "," + lastRequestTime);
           }
           
           private String doCommand(String commandValue) throws IOException, InterruptedException, ExecutionException, TimeoutException {
                     logger.severe("commandValue=[" + commandValue + "]\t" + cmdSshType + "_" + nodeId + "," + nodeName + "," + lastRequestTime);
//                   commandValue = commandValue + "\n";
                     final String commandValueFinal = commandValue + "\n";
                     
                     Future<String> f = es.submit(new Callable<String>() {
                                @Override
                                public String call() throws Exception {
                                           getInOutStream();
                                           output.write(commandValueFinal.getBytes());
                                           output.flush();
                                           StringBuilder stringBuilder = new StringBuilder();
                                           StringBuilder tempSb = new StringBuilder();
                                           byte buffer[] = new byte[8096];
//                                         logger.fine("input.available()=[" + input.available() + "]");
                                           boolean promptFound = false;
                                           while (true) {
                                                     while (input.available() > 0) {
                                                                int count = input.read(buffer, 0, 8096);
                                                                if (count >= 0) {
                                //                                         logger.fine("input.read()count=[" + count + "]");
                                                                           for (int i = 0; i < count; i++) {
                                                                                     tempSb.append(buffer[i]).append(' ');
                                                                           }
                                //                                         logger.fine("tempSb=[" + tempSb.toString() + "]");
                                                                           String tmp = new String(buffer, 0, count);
                                //                                         logger.fine("input.read()data=[" + tmp + "]");
                                                                           stringBuilder.append(tmp);
                                                                           
                                                                           if(isPromptMet(stringBuilder.toString().trim())) {
                                                                                     promptFound = true;
                                                                                     break;
                                                                           }
                                                                           if(isReady() == false) {
                                                                                     break;
                                                                           }
                                                                           
                                                                } else {
                                                                           break;
                                                                }
                                                     }
                                                     if(promptFound) {
                                                                break;
                                                     }
                                                     if(isReady() == false) {
                                                                break;
                                                     }
                                                     Thread.sleep(10L);
                                           }
                                           
                                           String result = stringBuilder.toString();
                                           
                                           logger.severe("result=[" + result + "]\t" + cmdSshType + "_" + nodeId + "," + nodeName + "," + lastRequestTime);
                                           
                                           return result;
                                }
                     });
                     
                     if(cliRequest != null && cliRequest.commandTimeOut > 0) {
                                return f.get(cliRequest.commandTimeOut, TimeUnit.MILLISECONDS);
                     } else {
                                return f.get(HmiServerProperties.NEADAPTOR_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
                     }
           }
           
           /**
           * 명령어 결과의 끝인지를 프롬프트로 판단하여 처리
           * @param result
           * @return
           */
           private boolean isPromptMet(String result){
                     for(String end_prompt : HmiServerProperties.NEADAPTOR_AUTO_PROMPT){
//                              if (result.toString().trim().indexOf(end_prompt.trim()) > -1) {
                                if(result == null) {
                                           logger.info("result is null --> break!");
                                           return true;
                                }
                                if (result.toString().trim().endsWith(end_prompt.trim())) {
                                           return true;
                                }
                     }
                     
                     if(isErrorResultMet(result)) {
                                return true;
                     }
                     return false;
           }
           
           private boolean isErrorResultMet(String result) {
                     for(String errorResult : errorResults){
                                if (result.contains(errorResult)) {
                                           return true;
                                }
                     }
                     return false;   
           }
           
           public void loadCliRequest(CliRequest cliRequest) throws InterruptedException, IOException, ExecutionException, TimeoutException {
                     semaphore.acquire();
                     lastRequestTime = System.currentTimeMillis();
                     this.cmdSshType = cliRequest.cmdSshType;
                     this.cliRequest = cliRequest;
                     logger.severe("Start semaphore.\t" + cmdSshType + "_" + nodeId + "," + nodeName + "," + lastRequestTime);
                     switch (cmdSshType) {
                     // CONFIG 'configure'
                     case HMI_PLATFORM_CONFIG:
                     case HMI_OAM_CLI_CONFIG:
                     case VNF_GW_PLATFORM_CONFIG:
                     case VNF_GW_OAM_CLI_CONFIG:
                                doCommand("configure");
                                break;
                     default:
                                break;
                     }
           }

           @Override
           public int connect() {
                     if(isReady()) {
                                return NeAdaptorConstants.OK;
                     }
                     return NeAdaptorConstants.CONNECT_FAIL;
           }

           @Override
           public String[] preCommand() throws Exception {
                     if(cliRequest.preCommand == null) return null;
                     
                     String resultString[] = new String[cliRequest.preCommand.length];
                     
                     for(int i=0; i<cliRequest.preCommand.length; i++){
                                resultString[i] = doCommand(cliRequest.preCommand[i]);
                                logger.info("[end preCommand]"+cliRequest.preCommand[i]);
                     }
                     
                     return resultString;
           }

           @Override
           public String executeCommand() throws Exception {
                     String result = doCommand(cliRequest.neCommand);
                     return result;
           }

           @Override
           public String[] postCommand() throws Exception {
                     if(cliRequest.postCommand == null) return null;
                     
                     String resultString[] = new String[cliRequest.postCommand.length];
                     
                     for(int i=0; i<cliRequest.postCommand.length; i++){
                                resultString[i] = doCommand(cliRequest.postCommand[i]);
                                logger.info("[end postCommand]"+cliRequest.postCommand[i]);
                     }
                     
                     return resultString;
           }

           @Override
           public void disconnect() {
                     if(isReady()) {
                                try {
                                           switch (cmdSshType) {
                                           // OAM-CLI
                                           case HMI_OAM_CLI_DIS:
                                           case EM_GW_OAM_CLI_DIS:
                                           case PM_OAM_CLI_DIS:
                                           case VNF_GW_OAM_CLI_DIS:
                                           case CARD_PORT_OAM_CLI_DIS:
                                           case NBI_OAM_CLI_DIS:
                                                     try {
                                                                doCommand("top");
                                                     } catch (Exception e) {
                                                                close();
                                                     }
                                                     break;
                                           // CONFIG 'commit'
                                           case HMI_PLATFORM_CONFIG:
                                           case VNF_GW_PLATFORM_CONFIG:
                                                     try {
                                                                doCommand("commit");
                                                     } catch (Exception e) {
                                                                close();
                                                     }
                                                     break;
                                           case HMI_OAM_CLI_CONFIG:
                                           case VNF_GW_OAM_CLI_CONFIG:
                                                     try {
                                                                doCommand("commit");
                                                                doCommand("top");
                                                     } catch (Exception e) {
                                                                close();
                                                     }
                                                     break;
                                           case DISPOSABLE:
                                                     close();
                                                     break;
                                           default:
                                                     break;
                                           }
                                } catch (Exception e) {
                                           logger.severe("Exception : " + epcUtil.printExceptionString(e) + "\t" + cmdSshType + "_" + nodeId + "," + nodeName + "," + lastRequestTime);
                                }
                     }
                     logger.severe("End semaphore.\t" + cmdSshType + "_" + nodeId + "," + nodeName + "," + lastRequestTime);
                     semaphore.release();
           }

           @Override
           public void shutdown() {
                     close();
           }
}



