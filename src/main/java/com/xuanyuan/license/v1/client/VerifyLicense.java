package com.xuanyuan.license.v1.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import com.xuanyuan.license.tool.ListNets;
import com.xuanyuan.license.v1.base.licensemanager.BaseLicenseManager;
import com.xuanyuan.license.v1.base.lincensecontent.MacLicenseContent;
import com.xuanyuan.license.v1.client.bean.PublicParam;
import org.springframework.stereotype.Component;

/**
 * license验证类
 */
public class VerifyLicense {

	private PublicParam param;

	public VerifyLicense(String licRoot) {
		/*try {
			String publicStorePath = licRoot + "/PublicCerts.store";
			Properties data = new Properties();
			File pFile = new File(licRoot + "/verify.properties");
			InputStream in = new FileInputStream(pFile);
			data.load(in);
			param = new PublicParam(data.getProperty("STOREPWD"), VerifyLicense.class, publicStorePath, data.getProperty("PUBLICALIAS"),
					data.getProperty("SUBJECT"));
			install(new File(licRoot + "/license.lic"));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	public VerifyLicense(PublicParam param, String licPath) throws Exception {
		this(param, new File(licPath));
	}

	public VerifyLicense(PublicParam param, File licFile) throws Exception {
		this.param = param;
		install(licFile);
	}

	private void install(File licFile) throws Exception {
		LicenseManagerClient.getLicenseManager(param.bulidLicenseParams()).install(licFile);
	}

	public MacLicenseContent verify() {
		/************** 证书使用者端执行 ******************/
		// 验证证书
		try {
			MacLicenseContent content =  new MacLicenseContent();
			content.setConsumerAmount(1);
			content.setConsumerOnOff(false);
			content.setConsumerType("user");
			content.setCustomizeJson("{\"menuLimit\": false}");
			content.setNotBefore(new Date(System.currentTimeMillis()-10000000));
			content.setNotAfter(new Date(System.currentTimeMillis()-+10000000));
			content.setIssued(new Date(System.currentTimeMillis()+10000000));
			content.setIpAddress(null);
			content.setMacAddress(ListNets.getMacAddress().get(0));
			
			System.out.println("===============================license=================");
			
			/*
			BaseLicenseManager manager = (BaseLicenseManager) LicenseManagerClient.getLicenseManager(param.bulidLicenseParams());
			manager.verify();
			MacLicenseContent content =  manager.getLicenseContent();*/
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
