package com.glf.glfconfig;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.glf.frame.DetermineFrame;
import com.glf.frame.Ui_Update;
import com.glf.glfconfig.CreateTable.jb1ActionListener;

public class DeleteTable {
	private JPanel deletetable_panel;
	private JLabel deletetable_name_label;
	private JComboBox deletetable_jc;
	private JButton deletetable_jb, clear_jb;

	static Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public JPanel build(Connection conn) throws SQLException {
		con = conn;
		deletetable_panel = new JPanel();
		deletetable_name_label = new JLabel("待删除表名");
		deletetable_jc = new JComboBox();

		deletetable_jb = new JButton("删除表");
		clear_jb = new JButton("清除");
		deletetable_panel.setLayout(null);
		Ui_Update.update_tablename(con, deletetable_jc);
		deletetable_name_label.setBounds(new Rectangle(250, 20, 150, 25));
		deletetable_jc.setBounds(new Rectangle(360, 20, 150, 25));
		deletetable_panel.add(deletetable_name_label);
		deletetable_panel.add(deletetable_jc);

		deletetable_jb.setBounds(new Rectangle(340, 60, 80, 30));
		clear_jb.setBounds(new Rectangle(440, 60, 60, 30));
		deletetable_panel.add(deletetable_jb);
		deletetable_panel.add(clear_jb);

		deletetable_jb.addActionListener(new jb1ActionListener());
		return (deletetable_panel);
	}

	class jb1ActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String deletetable_config_sql;
			String deleteconfig_sql;
			String deletetable_sql;
			String deleteview_sql;

			try {
				deletetable_config_sql = "delete from table_config where KIND='"
						+ deletetable_jc.getSelectedItem() + "'";
				ps = con.prepareStatement(deletetable_config_sql);
				ps.execute();
				deleteconfig_sql = "delete from config where KIND='"
						+ deletetable_jc.getSelectedItem() + "'";
				ps = con.prepareStatement(deleteconfig_sql);
				ps.execute();
				rs = con.getMetaData().getTables(
						null,
						null,
						(deletetable_jc.getSelectedItem() + "_info")
								.toUpperCase(), null);
				if (rs.next()) {
					deletetable_sql = "drop table "
							+ deletetable_jc.getSelectedItem() + ", "
							+ deletetable_jc.getSelectedItem() + "_info" + ", "
							+ deletetable_jc.getSelectedItem() + "_statics";
				} else {
					deletetable_sql = "drop table "
							+ deletetable_jc.getSelectedItem() + ", "
							+ deletetable_jc.getSelectedItem() + "_statics";
				}
				ps = con.prepareStatement(deletetable_sql);
				ps.execute();
				deleteview_sql = "drop view "
						+ deletetable_jc.getSelectedItem() + "_p, "
						+ deletetable_jc.getSelectedItem() + "_biaoge, "
						+ deletetable_jc.getSelectedItem() + "_biaoge_check, "
						+ deletetable_jc.getSelectedItem() + "_daystatics, "
						+ deletetable_jc.getSelectedItem()
						+ "_daystatics_check, "
						+ deletetable_jc.getSelectedItem() + "_select, "
						+ deletetable_jc.getSelectedItem() + "_select_check, "
						+ deletetable_jc.getSelectedItem() + "_tongjitu, "
						+ deletetable_jc.getSelectedItem() + "_tongjitu_check";
				ps = con.prepareStatement(deleteview_sql);
				ps.execute();

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			new DetermineFrame();
			try {
				update_ui();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}

	public void update_ui() throws SQLException {
		Ui_Update.update_tablename(con, deletetable_jc);

	}
}
