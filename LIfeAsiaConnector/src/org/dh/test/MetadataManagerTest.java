package org.dh.test;

import org.dh.metadata.MetaDataManager;

public class MetadataManagerTest {

	
	public static void main(String[] p_straArgs) throws Exception {
		
		 

		//MetaDataManager manager = new MetaDataManager("/Users/darkhorse/Documents/yogesh/projects/connector/lifeasia/services");
		//System.out.println(manager.getCopyBookMetaData("LDRHDR.txt"));
		String copybook ="<?xml version='1.0' encoding='UTF-8'?><copybook filename='LDRHDR.txt' dialect='Mainframe'><item level='01' name='LEADER-HEADER' position='1' storage-length='100' display-length='100'><item level='03' name='MSGREFNO' picture='9(08)' position='1' storage-length='8' display-length='8' numeric='true'/><item level='03' name='USRPRF' picture='X(10)' position='9' storage-length='10' display-length='10'/><item level='03' name='WKSID' picture='X(10)' position='19' storage-length='10' display-length='10'/><item level='03' name='OBJID' picture='X(10)' position='29' storage-length='10' display-length='10'/><item level='03' name='VRBID' picture='X(10)' position='39' storage-length='10' display-length='10'/><item level='03' name='TOTMSGLNG' picture='9(05)' position='49' storage-length='5' display-length='5' numeric='true'/><item level='03' name='OPMODE' picture='X(01)' position='54' storage-length='1' display-length='1'><condition name='CHK'><condition value='&quot;0&quot;'/></condition><condition name='CHK1'><condition value='&quot;0&quot;'/></condition><condition name='CHK-AND-UPD'><condition value='&quot;1&quot;'/></condition><condition name='UPD'><condition value='&quot;2&quot;'/></condition></item><item level='03' name='CMTCONTROL' picture='X(01)' position='55' storage-length='1' display-length='1'><condition name='CMT'><condition value='&quot;Y&quot;'/></condition><condition name='NO-COMIT'><condition value='&quot;N&quot;'/></condition><condition name='EXTERNAL-CONTROL'><condition value='&quot;E&quot;'/></condition></item><item level='03' name='RSPMODE' picture='X(01)' position='56' storage-length='1' display-length='1'/><item level='03' name='MSGINTENT' picture='X(01)' position='57' storage-length='1' display-length='1'/><item level='03' name='MORE-IND' picture='X(01)' position='58' storage-length='1' display-length='1'><condition name='MORE-YES'><condition value='&quot;Y&quot;'/></condition><condition name='MORE-NO'><condition value='&quot;N&quot;'/></condition></item><item level='03' name='ERRLVL' picture='X(01)' position='59' storage-length='1' display-length='1'><condition name='NO-ERROR'><condition value='&quot;0&quot;'/></condition><condition name='RESP-WARNING'><condition value='&quot;1&quot;'/></condition><condition name='RESP-FATAL'><condition value='&quot;2&quot;'/></condition></item><item level='03' name='IGNORE-DRIVER-HELD' picture='X(01)' position='60' storage-length='1' display-length='1'/><item level='03' name='SUPPRESS-RCLRSC' picture='X(01)' position='61' storage-length='1' display-length='1'/><item level='03' name='FILLER' picture='X(39)' position='62' storage-length='39' display-length='39'/></item></copybook>";
		MetaDataManager manager =MetaDataManager.getInstance();
		String sts ="SESSION.txt";
		System.out.println("indes:"+sts.substring(0, sts.indexOf('.')));
		System.out.println("Removed:"+manager.removeConditionTag(copybook));
	}
}
