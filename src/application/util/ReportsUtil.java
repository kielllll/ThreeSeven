package application.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

public class ReportsUtil {
	
	private static ReportsUtil instance = new ReportsUtil();
	
	public void showVehicleReport(File file, String choice, String type, String encumberedTo) {
		try {
			JasperDesign jd = JRXmlLoader.load(file);
			String query = "";
			
			switch(choice) {
				case "All": if(type.equalsIgnoreCase("All")&&encumberedTo.equalsIgnoreCase("All"))
								query = "SELECT v.vehicle_ID, v.plate_number, v.mv_number, v.engine_number, v.chassis_number, m.description as model_name, c.description as category, v.encumbered_to, v.amount, v.maturity_date, v.status, v.image FROM models m INNER JOIN vehicles v ON m.model_ID=v.model_ID INNER JOIN vehicle_categories c ON c.category_ID=v.category_ID ORDER BY v.plate_number ASC";
							else {
								if(type.equalsIgnoreCase("All"))
									query = "SELECT v.vehicle_ID, v.plate_number, v.mv_number, v.engine_number, v.chassis_number, m.description as model_name, c.description as category, v.encumbered_to, v.amount, v.maturity_date, v.status, v.image FROM models m INNER JOIN vehicles v ON m.model_ID=v.model_ID INNER JOIN vehicle_categories c ON c.category_ID=v.category_ID WHERE v.encumbered_to = '"+encumberedTo+"' ORDER BY v.plate_number ASC";
								else if(encumberedTo.equalsIgnoreCase("All"))
									query = "SELECT v.vehicle_ID, v.plate_number, v.mv_number, v.engine_number, v.chassis_number, m.description as model_name, c.description as category, v.encumbered_to, v.amount, v.maturity_date, v.status, v.image FROM models m INNER JOIN vehicles v ON m.model_ID=v.model_ID INNER JOIN vehicle_categories c ON c.category_ID=v.category_ID WHERE c.description = '"+type+"' ORDER BY v.plate_number ASC";
								else query = "SELECT v.vehicle_ID, v.plate_number, v.mv_number, v.engine_number, v.chassis_number, m.description as model_name, c.description as category, v.encumbered_to, v.amount, v.maturity_date, v.status, v.image FROM models m INNER JOIN vehicles v ON m.model_ID=v.model_ID INNER JOIN vehicle_categories c ON c.category_ID=v.category_ID WHERE c.description = '"+type+"' AND v.encumbered_to = '"+encumberedTo+"' ORDER BY v.plate_number ASC";
							}
							break;
				case "Active":  if(type.equalsIgnoreCase("All")&&encumberedTo.equalsIgnoreCase("All"))
									query = "SELECT v.vehicle_ID, v.plate_number, v.mv_number, v.engine_number, v.chassis_number, m.description as model_name, c.description as category, v.encumbered_to, v.amount, v.maturity_date, v.status, v.image FROM models m INNER JOIN vehicles v ON m.model_ID=v.model_ID INNER JOIN vehicle_categories c ON c.category_ID=v.category_ID WHERE v.status = 'active' ORDER BY v.plate_number ASC";
								else {
									if(type.equalsIgnoreCase("All"))
										query = "SELECT v.vehicle_ID, v.plate_number, v.mv_number, v.engine_number, v.chassis_number, m.description as model_name, c.description as category, v.encumbered_to, v.amount, v.maturity_date, v.status, v.image FROM models m INNER JOIN vehicles v ON m.model_ID=v.model_ID INNER JOIN vehicle_categories c ON c.category_ID=v.category_ID WHERE v.status = 'active' AND v.encumbered_to = '"+encumberedTo+"' ORDER BY v.plate_number ASC";
									else if(encumberedTo.equalsIgnoreCase("All"))
										query = "SELECT v.vehicle_ID, v.plate_number, v.mv_number, v.engine_number, v.chassis_number, m.description as model_name, c.description as category, v.encumbered_to, v.amount, v.maturity_date, v.status, v.image FROM models m INNER JOIN vehicles v ON m.model_ID=v.model_ID INNER JOIN vehicle_categories c ON c.category_ID=v.category_ID WHERE v.status = 'active' AND c.description = '"+type+"' ORDER BY v.plate_number ASC";
									else query = "SELECT v.vehicle_ID, v.plate_number, v.mv_number, v.engine_number, v.chassis_number, m.description as model_name, c.description as category, v.encumbered_to, v.amount, v.maturity_date, v.status, v.image FROM models m INNER JOIN vehicles v ON m.model_ID=v.model_ID INNER JOIN vehicle_categories c ON c.category_ID=v.category_ID WHERE v.status = 'active' AND c.description = '"+type+"' AND v.encumbered_to = '"+encumberedTo+"' ORDER BY v.plate_number ASC";
								}
								break;
				case "Inactive": if(type.equalsIgnoreCase("All")&&encumberedTo.equalsIgnoreCase("All"))
									query = "SELECT v.vehicle_ID, v.plate_number, v.mv_number, v.engine_number, v.chassis_number, m.description as model_name, c.description as category, v.encumbered_to, v.amount, v.maturity_date, v.status, v.image FROM models m INNER JOIN vehicles v ON m.model_ID=v.model_ID INNER JOIN vehicle_categories c ON c.category_ID=v.category_ID WHERE v.status = 'inactive' ORDER BY v.plate_number ASC";
								else {
									if(type.equalsIgnoreCase("All"))
										query = "SELECT v.vehicle_ID, v.plate_number, v.mv_number, v.engine_number, v.chassis_number, m.description as model_name, c.description as category, v.encumbered_to, v.amount, v.maturity_date, v.status, v.image FROM models m INNER JOIN vehicles v ON m.model_ID=v.model_ID INNER JOIN vehicle_categories c ON c.category_ID=v.category_ID WHERE v.status = 'inactive' AND v.encumbered_to = '"+encumberedTo+"' ORDER BY v.plate_number ASC";
									else if(encumberedTo.equalsIgnoreCase("All"))
										query = "SELECT v.vehicle_ID, v.plate_number, v.mv_number, v.engine_number, v.chassis_number, m.description as model_name, c.description as category, v.encumbered_to, v.amount, v.maturity_date, v.status, v.image FROM models m INNER JOIN vehicles v ON m.model_ID=v.model_ID INNER JOIN vehicle_categories c ON c.category_ID=v.category_ID WHERE v.status = 'inactive' AND c.description = '"+type+"' ORDER BY v.plate_number ASC";
									else query = "SELECT v.vehicle_ID, v.plate_number, v.mv_number, v.engine_number, v.chassis_number, m.description as model_name, c.description as category, v.encumbered_to, v.amount, v.maturity_date, v.status, v.image FROM models m INNER JOIN vehicles v ON m.model_ID=v.model_ID INNER JOIN vehicle_categories c ON c.category_ID=v.category_ID WHERE v.status = 'inactive' AND c.description = '"+type+"' AND v.encumbered_to = '"+encumberedTo+"' ORDER BY v.plate_number ASC";
								}
								break;
			}
			
			JRDesignQuery jrQuery = new JRDesignQuery();
			jrQuery.setText(query);
			jd.setQuery(jrQuery);
			JasperReport jr = JasperCompileManager.compileReport(jd);
			Map<String, Object> parameters = new HashMap<String, Object>();
			JasperPrint jp = JasperFillManager.fillReport(jr, parameters, Database.getInstance().getDBConn());
			JRViewer jrv = new JRViewer(jp);
			jrv.setVisible(true);
			
			JFrame frm = new JFrame("Report");
			frm.setSize(650, 700);
			frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frm.setLocationRelativeTo(null);
			frm.getContentPane().add(jrv);
			frm.setVisible(true);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void showItemReport(File file, String choice, String supplier) {
		try {
			JasperDesign jd = JRXmlLoader.load(file);
			String query = "";
			
			switch(choice) {
				case "All": query = (supplier.equalsIgnoreCase("All"))?"SELECT i.item_ID, i.name, s.name AS supplier, i.description, i.price, u.description AS unit, st.quantity, i.status FROM suppliers s INNER JOIN items i ON s.supplier_ID = i.supplier_ID INNER JOIN unit_of_measurements u ON i.unit_ID = u.unit_ID INNER JOIN stocks st ON st.item_ID = i.item_id ORDER BY i.item_id ASC"
												:"SELECT i.item_ID, i.name, s.name AS supplier, i.description, i.price, u.description AS unit, st.quantity, i.status FROM suppliers s INNER JOIN items i ON s.supplier_ID = i.supplier_ID INNER JOIN unit_of_measurements u ON i.unit_ID = u.unit_ID INNER JOIN stocks st ON st.item_ID = i.item_id WHERE s.name = '"+supplier+"' ORDER BY i.item_id ASC";
					break;
				case "In Stock": query = (supplier.equalsIgnoreCase("All"))?"SELECT i.item_ID, i.name, s.name AS supplier, i.description, i.price, u.description AS unit, st.quantity, i.status FROM suppliers s INNER JOIN items i ON s.supplier_ID = i.supplier_ID INNER JOIN unit_of_measurements u ON i.unit_ID = u.unit_ID INNER JOIN stocks st ON st.item_ID = i.item_id WHERE i.status = 'in stock' ORDER BY i.item_id ASC"
						:"SELECT i.item_ID, i.name, s.name AS supplier, i.description, i.price, u.description AS unit, st.quantity, i.status FROM suppliers s INNER JOIN items i ON s.supplier_ID = i.supplier_ID INNER JOIN unit_of_measurements u ON i.unit_ID = u.unit_ID INNER JOIN stocks st ON st.item_ID = i.item_id WHERE i.status = 'in stock' AND s.name = '"+supplier+"' ORDER BY i.item_id ASC"; 
					break;
				case "Out of Stock": query = (supplier.equalsIgnoreCase("All"))?"SELECT i.item_ID, i.name, s.name AS supplier, i.description, i.price, u.description AS unit, st.quantity, i.status FROM suppliers s INNER JOIN items i ON s.supplier_ID = i.supplier_ID INNER JOIN unit_of_measurements u ON i.unit_ID = u.unit_ID INNER JOIN stocks st ON st.item_ID = i.item_id WHERE i.status = 'out of stock' ORDER BY i.item_id ASC"
						:"SELECT i.item_ID, i.name, s.name AS supplier, i.description, i.price, u.description AS unit, st.quantity, i.status FROM suppliers s INNER JOIN items i ON s.supplier_ID = i.supplier_ID INNER JOIN unit_of_measurements u ON i.unit_ID = u.unit_ID INNER JOIN stocks st ON st.item_ID = i.item_id WHERE i.status = 'out of stock' AND s.name = '"+supplier+"' ORDER BY i.item_id ASC";
					break;
			}
			
			JRDesignQuery jrQuery = new JRDesignQuery();
			jrQuery.setText(query);
			jd.setQuery(jrQuery);
			JasperReport jr = JasperCompileManager.compileReport(jd);
			Map<String, Object> parameters = new HashMap<String, Object>();
			JasperPrint jp = JasperFillManager.fillReport(jr, parameters, Database.getInstance().getDBConn());
			JRViewer jrv = new JRViewer(jp);
			jrv.setVisible(true);
			
			JFrame frm = new JFrame("Report");
			frm.setSize(650, 700);
			frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frm.setLocationRelativeTo(null);
			frm.getContentPane().add(jrv);
			frm.setVisible(true);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	
	public static ReportsUtil getInstance() {
		return instance;
	}
}
