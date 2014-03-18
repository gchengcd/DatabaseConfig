package com.glf.glfconfig;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.glf.frame.DetermineFrame;
import com.glf.frame.NullErrorFrame;
import com.glf.frame.Ui_Update;

public class DeleteRow {
	private JPanel delete_panel;
	private JLabel tablename_label, delete_rowname_label;
	private JComboBox tablename_jc, delete_rowname_jc;
	private JButton deleterow_jb, clear_jb;

	static Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public JPanel build(Connection conn) throws SQLException {
		con = conn;
		delete_panel = new JPanel();
		tablename_label = new JLabel("表名");
		delete_rowname_label = new JLabel("待删除列名");

		tablename_jc = new JComboBox();
		delete_rowname_jc = new JComboBox();

		deleterow_jb = new JButton("删除名");
		clear_jb = new JButton("清除");
		delete_panel.setLayout(null);
		Ui_Update.update_tablename(con, tablename_jc);
		tablename_label.setBounds(new Rectangle(250, 20, 150, 25));
		tablename_jc.setBounds(new Rectangle(360, 20, 150, 25));
		delete_panel.add(tablename_label);
		delete_panel.add(tablename_jc);
		updaterow();
		// Ui_Update.update_rowname(con, (String)
		// tablename_jc.getSelectedItem(), delete_rowname_jc);
		tablename_jc.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				try {
					updaterow();
					// Ui_Update.update_rowname(con, (String)
					// tablename_jc.getSelectedItem(), delete_rowname_jc);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		delete_rowname_label.setBounds(new Rectangle(250, 50, 150, 25));
		delete_rowname_jc.setBounds(new Rectangle(360, 50, 150, 25));
		delete_panel.add(delete_rowname_label);
		delete_panel.add(delete_rowname_jc);

		deleterow_jb.setBounds(new Rectangle(340, 90, 80, 30));
		clear_jb.setBounds(new Rectangle(440, 90, 60, 30));
		delete_panel.add(deleterow_jb);
		delete_panel.add(clear_jb);

		deleterow_jb.addActionListener(new jb1ActionLiatener());

		return (delete_panel);
	}

	private class jb1ActionLiatener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			try {
				if (delete_rowname_jc.getSelectedItem().equals("")
						|| delete_rowname_jc.getSelectedItem() == null) {
					new NullErrorFrame();
				} else {

					deleterow((String) tablename_jc.getSelectedItem(),
							(String) delete_rowname_jc.getSelectedItem());
					new DetermineFrame();
					try {
						update_ui();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				new NullErrorFrame();
			}
		}
	}

	void deleterow(String kind, String field) {
		String deleteconfig_sql = null;
		String deletetable_sql = null;
		String deletestatics_sql = null;
		String table_name = null;

		try {
			table_name = kind.toLowerCase();
			deleteconfig_sql = "delete from config where KIND='" + kind
					+ "' and FIELD='" + field + "' ";
			ps = con.prepareStatement(deleteconfig_sql);
			ps.executeUpdate();
			deletetable_sql = "alter table " + table_name + " drop column "
					+ field;
			ps = con.prepareStatement(deletetable_sql);
			ps.executeUpdate();
			deletestatics_sql = "alter table " + table_name
					+ "_statics drop column " + field;
			ps = con.prepareStatement(deletestatics_sql);
			ps.executeUpdate();
			/*deletestatics_sql = "alter table " + table_name
					+ "_statics drop column MAX_" + field;
			ps = con.prepareStatement(deletestatics_sql);
			ps.executeUpdate();
			deletestatics_sql = "alter table " + table_name
					+ "_statics drop column AVG_" + field;
			ps = con.prepareStatement(deletestatics_sql);
			ps.executeUpdate();
			deletestatics_sql = "alter table " + table_name
					+ "_statics drop column SUM_" + field;
			ps = con.prepareStatement(deletestatics_sql);
			ps.executeUpdate();*/
			new Create8View(kind, con);

		} catch (SQLException e1) {
			e1.printStackTrace();
		} 
	}

	public void update_ui() throws SQLException {
		Ui_Update.update_tablename(con, tablename_jc);
		// Ui_Update.update_rowname(con, (String)
		// tablename_jc.getSelectedItem(), delete_rowname_jc);
		updaterow();
	}

	void updaterow() throws SQLException {
		delete_rowname_jc.removeAllItems();
		String sq1 = "select FIELD from config where KIND='"
				+ tablename_jc.getSelectedItem() + "'";
		ps = con.prepareStatement(sq1);
		rs = ps.executeQuery();
		while (rs.next()) {
			delete_rowname_jc.addItem(rs.getString(1));
		}
	}
}
