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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.glf.frame.DetermineFrame;
import com.glf.frame.NullErrorFrame;
import com.glf.frame.Ui_Update;

public class UpdateRow {
	private JPanel updaterow_panel;
	private JLabel tablename_label, updaterow_name_label, newrow_name_label;
	private JComboBox tablename_jc, rowname_jc;
	private JTextField newrow_name_jt;
	private JButton updaterow_jb, clear_jb;

	static Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	String table_name = null;
	String sq1;

	public JPanel build(Connection conn) throws SQLException {
		con = conn;
		updaterow_panel = new JPanel();
		tablename_label = new JLabel("表名");
		updaterow_name_label = new JLabel("待更新列名");
		newrow_name_label = new JLabel("新列名");

		tablename_jc = new JComboBox();
		rowname_jc = new JComboBox();

		newrow_name_jt = new JTextField("", 15);

		updaterow_jb = new JButton("更新列");
		clear_jb = new JButton("清除");
		updaterow_panel.setLayout(null);
		Ui_Update.update_tablename(con, tablename_jc);
		tablename_label.setBounds(new Rectangle(250, 20, 150, 25));
		tablename_jc.setBounds(new Rectangle(360, 20, 150, 25));
		updaterow_panel.add(tablename_label);
		updaterow_panel.add(tablename_jc);

		updaterow_name_label.setBounds(new Rectangle(250, 50, 150, 25));
		rowname_jc.setBounds(new Rectangle(360, 50, 150, 25));
		updaterow_panel.add(updaterow_name_label);
		updaterow_panel.add(rowname_jc);

		newrow_name_label.setBounds(new Rectangle(250, 80, 150, 25));
		newrow_name_jt.setBounds(new Rectangle(360, 80, 150, 25));
		updaterow_panel.add(newrow_name_label);
		updaterow_panel.add(newrow_name_jt);

		updaterow_jb.setBounds(new Rectangle(340, 120, 80, 30));
		clear_jb.setBounds(new Rectangle(440, 120, 60, 30));
		updaterow_panel.add(updaterow_jb);
		updaterow_panel.add(clear_jb);
		updaterow();
		// Ui_Update.update_rowname(con, (String)
		// tablename_jc.getSelectedItem(), rowname_jc);
		tablename_jc.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				try {
					updaterow();
					// Ui_Update.update_rowname(con, (String)
					// tablename_jc.getSelectedItem(), rowname_jc);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		updaterow_jb.addActionListener(new jb1ActionListener());

		return (updaterow_panel);
	}

	private class jb1ActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (newrow_name_jt.getText().equals("")
					|| newrow_name_jt.getText() == null) {
				new NullErrorFrame();
			} else {
				String new_row_name, direction = null, type = null, name = null;
				String row_name;
				String select_row_sql;
				row_name = (String) rowname_jc.getSelectedItem();
				new_row_name = newrow_name_jt.getText();
				select_row_sql = "select * from config where KIND='"
						+ tablename_jc.getSelectedItem() + "' and FIELD='"
						+ row_name + "'";
				try {
					ps = con.prepareStatement(select_row_sql);
					rs = ps.executeQuery();
					while (rs.next()) {
						direction = rs.getString("DIRECTION");//记录待更新列的信息
						type = rs.getString("TYPE");
						name = rs.getString("NAME");
					}
					new DeleteRow().deleterow(
							(String) tablename_jc.getSelectedItem(), row_name);//删除列
					new AddRow().addrow(
							(String) tablename_jc.getSelectedItem(),
							new_row_name, direction, type, name);//增加列
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				new DetermineFrame();
				try {
					update_ui();
					newrow_name_jt.setText("");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

	public void update_ui() throws SQLException {
		Ui_Update.update_tablename(con, tablename_jc);
		// Ui_Update.update_rowname(con, (String)
		// tablename_jc.getSelectedItem(), rowname_jc);
		updaterow();
	}

	void updaterow() throws SQLException {
		rowname_jc.removeAllItems();
		String sq1 = "select FIELD from config where KIND='"
				+ tablename_jc.getSelectedItem() + "'";
		ps = con.prepareStatement(sq1);
		rs = ps.executeQuery();
		while (rs.next()) {
			rowname_jc.addItem(rs.getString(1));
		}
	}
}
