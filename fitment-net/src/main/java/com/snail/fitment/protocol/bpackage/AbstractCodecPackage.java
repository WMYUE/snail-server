package com.snail.fitment.protocol.bpackage;

import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.snail.fitment.common.cipher.CipherAlgorithm;
import com.snail.fitment.common.cipher.CipherManager;
import com.snail.fitment.common.cipher.ICipher;
import com.snail.fitment.common.compress.CompressAlgorithm;
import com.snail.fitment.common.compress.CompressFactory;
import com.snail.fitment.common.compress.ICompress;
import com.snail.fitment.common.config.FitmentConfig;
import com.snail.fitment.common.exception.StateException;
import com.snail.fitment.common.lang.StringUtils;

/**
 * 加解密和解压缩
 * 
 * @author Administrator
 *
 */
public abstract class AbstractCodecPackage implements IBpPackage, Serializable {

	private static final long serialVersionUID = -6474147573612923673L;

	private static final Logger log = Logger.getLogger(AbstractCodecPackage.class);

	protected boolean isBodyBinary = false;

	protected void initBodyBinaryFlag() {
		if (isEncrypted(this.getBpHeader().getEncryptType()) || isCompressed(this.getBpHeader().getCompressType())) {
			isBodyBinary = true;
		}
	}

	protected String getBodyLogString() {
		String bodyString = this.getBpBody().getBodyString();
		return bodyString;
	}

	public void decode(String sessionKey) {

		if (isEncrypted(this.getBpHeader().getEncryptType()) || isCompressed(this.getBpHeader().getCompressType())) {
			isBodyBinary = true;

			// 仅当原来为加密或压缩数据时才打印日志，避免重复输出
			// if (log.isDebugEnabled()) {
			// log.debug("decode - begin, oldPkg: "
			// + ", _encryptType: " + this.getBpHeader().getEncryptType() + ",
			// _compressType: " + this.getBpHeader().getCompressType()
			// + ", sessionKey: " + sessionKey
			// );
			// }

			/**
			 * 处理压缩数据
			 */
			byte[] bodyPlainText = this.getBpBody().getBodyBuffer();
			byte[] attachmentPlainText = this.getBpBody().getAttachment();

			/**
			 * 处理加解密数据
			 */
			if (isEncrypted(this.getBpHeader().getEncryptType())) {
				// 解密包体和附件
				try {
					ICipher cipher = CipherManager.getInstance().getCipher(this.getBpHeader().getEncryptType());

					String base64Key = sessionKey;
					if (StringUtils.isEmpty(base64Key)) {
						log.error("会话密钥为空，不能进行解密!");
						throw new StateException(2, "会话密钥为空，不能进行解密！");
					}
					byte[] key = Base64.decodeBase64(base64Key);
					bodyPlainText = cipher.decrypt(bodyPlainText, key);

					// 解密附件
					if (attachmentPlainText != null) {
						attachmentPlainText = cipher.decrypt(attachmentPlainText, key);
					}

				} catch (IllegalArgumentException ex) {
					// 不支持的加密算法
					log.error("IllegalArgumentException:", ex);
					throw new StateException(2, "参数或数据错误！");
				} catch (Exception ex) {
					StringBuffer errmsg = new StringBuffer("数据解密异常,");
					errmsg.append("opCode=").append(this.getBpHeader().getOpCode()).append(",");
					errmsg.append("encryptType=").append(this.getBpHeader().getEncryptType()).append(",");
					errmsg.append("sessionKey=").append(sessionKey).append(",");
					log.error(errmsg, ex);

					throw new StateException(2, "数据解密错误！");
				}
			}

			if (isCompressed(this.getBpHeader().getCompressType())) {
				// 解压包体和附件
				try {
					ICompress compress = CompressFactory.getCompressInstance(this.getBpHeader().getCompressType());
					bodyPlainText = compress.unzip(bodyPlainText);

					// 解压附件
					if (this.getBpBody().getAttachment() != null) {
						attachmentPlainText = compress.unzip(attachmentPlainText);
					}
				} catch (IllegalArgumentException ex) {
					// 不支持的压缩算法
					log.error("IllegalArgumentException:", ex);
					throw new StateException(2, "参数或数据错误！");
				} catch (Exception ex) {
					log.error("数据解压异常:", ex);
					throw new StateException(2, "数据解压错误！");
				}
			}

			this.getBpBody().setBodyBuffer(bodyPlainText);
			this.getBpBody().setAttachment(attachmentPlainText);

			isBodyBinary = false;

			// 仅当原来为加密或压缩数据时才打印日志，避免重复输出
			// if (log.isDebugEnabled()) {
			// log.debug("decode - end"
			// + ", newPkg: " + this.getBpBody().getBodyString()
			// + ", sessionKey: " + sessionKey
			// );
			// }
		}

		this.getBpBody().parseBpBody();
	}

	public void fillBodyBuffer() {
		if (this.getBpBody() != null && this.getBpBody().hasContent()) {
			this.getBpBody().setBodyString(this.getBpBody().toJsonString());
		}
	}

	public void encode(String sessionKey) {
		this.fillBodyBuffer();

		isBodyBinary = false;

		byte[] bodyCodeText = this.getBpBody().getBodyBuffer();

		if (isEncrypted(this.getBpHeader().getEncryptType()) || isCompressed(this.getBpHeader().getCompressType())) {
			// 仅当原来为加密或压缩数据时才打印日志，避免重复输出
			// if (log.isDebugEnabled()) {
			// log.debug("encode - begin"
			// + ", oldPkg: " + this.getBpBody().getBodyString()
			// + ", _encryptType: " + this.getBpHeader().getEncryptType() + ",
			// _compressType: " + this.getBpHeader().getCompressType()
			// + ", sessionKey: " + sessionKey
			// );
			// }

			byte[] attachmentCodeText = this.getBpBody().getAttachment();

			/**
			 * 处理压缩数据
			 */
			// 如果消息体大小不大于阈值，则不必压缩
			if (bodyCodeText.length > FitmentConfig.compressThreshold) {
				// 压缩包体和附件
				this.getBpHeader().setCompressType((byte) CompressAlgorithm.ALGORITHM_GZIP_ID);
				try {
					ICompress compress = CompressFactory.getCompressInstance(this.getBpHeader().getCompressType());
					bodyCodeText = compress.zip(bodyCodeText);

					// 压缩附件
					if (attachmentCodeText != null) {
						attachmentCodeText = compress.zip(attachmentCodeText);
					}
				} catch (IllegalArgumentException ex) {
					// 不支持的压缩算法
					log.error("IllegalArgumentException:", ex);
					throw new StateException(2, "参数或数据错误！");
				} catch (Exception ex) {
					log.error("数据压缩异常:", ex);
					throw new StateException(2, "数据压缩错误！");
				}
			} else {
				this.getBpHeader().setCompressType((byte) CompressAlgorithm.ALGORITHM_NOZIP_ID);
			}

			/**
			 * 处理加解密数据
			 */
			if (isEncrypted(this.getBpHeader().getEncryptType())) {
				// 加密包体和附件
				try {
					ICipher cipher = CipherManager.getInstance().getCipher(this.getBpHeader().getEncryptType());
					String base64Key = sessionKey;
					if (StringUtils.isEmpty(base64Key)) {
						log.error("会话密钥为空，不能进行加密!");
						throw new StateException(2, "会话密钥为空，不能进行加密！");
					}
					byte[] key = Base64.decodeBase64(base64Key);
					bodyCodeText = cipher.encrypt(bodyCodeText, key);

					// 加密附件
					if (attachmentCodeText != null) {
						attachmentCodeText = cipher.encrypt(attachmentCodeText, key);
					}
				} catch (IllegalArgumentException ex) {
					// 不支持的加密算法
					log.error("IllegalArgumentException:", ex);
					throw new StateException(2, "不支持的加密类型！");
				} catch (Exception ex) {
					log.error("数据解密异常:", ex);
					throw new StateException(2, "数据加密错误！");
				}
			} else {
				this.getBpHeader().setEncryptType((byte) 0);
			}

			this.getBpBody().setAttachment(attachmentCodeText);
			this.getBpBody().setBodyBuffer(bodyCodeText);

			isBodyBinary = true;

			// 仅当原来为加密或压缩数据时才打印日志，避免重复输出
			// if (log.isDebugEnabled()) {
			// log.debug("encode - end"
			// + ", newPkg: " + this.getBpBody().getBodyString()
			// + ", sessionKey: " + sessionKey
			// );
			// }
		}

		this.fillPackageBuffer();
	}

	/**
	 * 验证是否压缩
	 * 
	 * @param compressAlgorithm
	 * @return
	 */
	public static boolean isCompressed(int compressAlgorithm) {
		if (compressAlgorithm < CompressAlgorithm.ALGORITHM_GZIP_ID) {
			return false;
		}
		return true;
	}

	/**
	 * 验证是否加解密
	 * 
	 * @param cipherAlgorithm
	 * @return
	 */
	public static boolean isEncrypted(int cipherAlgorithm) {
		if (cipherAlgorithm < CipherAlgorithm.ALGORITHM_AES_ID) {
			return false;
		}
		return true;
	}

}
