package com.snail.fitment.protocol.bpackage;



public class BpPackageFactory {
	
	public static IBpPackage parseNormalBpPackage(byte[] bytebuffer) {
		return new BpPackage(bytebuffer);
	}
	
	
	public static IBpPackage createEmptyResponseByRequest(IBpPackage request) {
		BpPackage pkg = createEmptyPackage(IBpHeader.MESSAGE_TYPE_RESPONSE, request.getBpHeader().getSeq(), request.getBpHeader().getEncryptType(),(byte) 0);
		pkg.getBpHeader().setOpCode(((BpPackage)request).getBpHeader().getOpCode());
		return pkg;
	}
	

	public static IBpPackage createPackage(byte messageType, int seq, byte encryptType, byte compressType, int opCode, String bodyStr) {
		BpPackage pkg = createEmptyPackage(messageType, seq, encryptType, compressType);
		pkg.getBpHeader().setOpCode(opCode);
		pkg.getBpBody().setBodyString(bodyStr);
		pkg.getBpBody().parseBpBody();
		
		return pkg;
	}
	
	public static BpPackage createEmptyPackage(byte messageType, int seq,
			byte encryptType, byte compressType) {
		BpPackage pkg = new BpPackage(messageType, seq,(byte) encryptType, (byte) compressType);
		return pkg;
	}
	

	public static BpPackage createPackage(byte messageType, int seq, byte encryptType, byte compressType, int opCode, String bodyStr, byte[] attachMent) {
		BpPackage pkg = createEmptyPackage(messageType, seq, encryptType,
				compressType);
		pkg.getBpHeader().setOpCode(opCode);
		pkg.getBpBody().setBodyString(bodyStr);
		pkg.getBpBody().setAttachment(attachMent);
		
		return pkg;
	}
	
}
