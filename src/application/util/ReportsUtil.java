package application.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class ReportsUtil {
	
	private static ReportsUtil instance = new ReportsUtil();
	
	public void showReport(File file) {
		try {
			InputStream is = new FileInputStream(file);
			JasperReport jr = JasperCompileManager.compileReport(is);
			Map<String, Object> parameters = new HashMap<String, Object>();
			JasperPrint jp = JasperFillManager.fillReport(jr, parameters, Database.getInstance().getDBConn());
//			JasperViewer.viewReport(jp, false);
			JasperViewer jw = new JasperViewer(jp,false);
			jw.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			jw.setVisible(true);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	
	public static ReportsUtil getInstance() {
		return instance;
	}
}
