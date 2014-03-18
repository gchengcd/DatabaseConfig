package com.glf.glfconfig;

import java.awt.Frame;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;

import com.glf.frame.DetermineFrame;
import com.glf.frame.FailFrame;
import com.glf.frame.NullErrorFrame;
import com.glf.frame.Ui_Update;
import com.mysql.jdbc.Statement;
import com.sun.java.swing.plaf.windows.resources.windows;
/*
 * 新建新增列选项卡SubscriptionSystem
 * 实现新增列的所有功能操作
 */
public class AddRow {
	private JPanel addrow_panel;
	private JLabel tablename_label, direction_label, type_label, rowname_label,
			ch_rowname_label;
	private JComboBox tablename_jc, direction_jc, type_jc;
	private JTextField rowname_jt, ch_rowname_jt;
	private JButton addrow_jb, clear_jb;
	static Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
//新建新增列选项卡，增加事件监听
	public JPanel build(Connection conn) throws SQLException {
		con = conn;
		addrow_panel = new JPanel();
		tablename_label = new JLabel("表名");
		direction_label = new JLabel("采集/控制");
		type_label = new JLabel("模拟/开关");
		rowname_label = new JLabel("新建列名称");
		ch_rowname_label = new JLabel("中文列名称");
		tablename_jc = new JComboBox();
		direction_jc = new JComboBox();
		type_jc = new JComboBox();
		rowname_jt = new JTextField("", 15);
		ch_rowname_jt = new JTextField("", 15);
		addrow_jb = new JButton("新增列");
		clear_jb = new JButton("清除");
		addrow_panel.setLayout(null);

		Ui_Update.update_tablename(con, tablename_jc);
		tablename_label.setBounds(new Rectangle(250, 20, 150, 25));
		tablename_jc.setBounds(new Rectangle(360, 20, 150, 25));
		addrow_panel.add(tablename_label);
		addrow_panel.add(tablename_jc);

		direction_jc.addItem("采集");
		direction_jc.addItem("控制");
		direction_label.setBounds(new Rectangle(250, 50, 150, 25));
		direction_jc.setBounds(new Rectangle(360, 50, 150, 25));
		addrow_panel.add(direction_label);
		addrow_panel.add(direction_jc);

		type_jc.addItem("模拟");
		type_jc.addItem("开关");
		type_label.setBounds(new Rectangle(250, 80, 150, 25));
		type_jc.setBounds(new Rectangle(360, 80, 150, 25));
		addrow_panel.add(type_label);
		addrow_panel.add(type_jc);

		rowname_label.setBounds(new Rectangle(250, 110, 150, 25));
		rowname_jt.setBounds(new Rectangle(360, 110, 150, 25));
		addrow_panel.add(rowname_label);
		addrow_panel.add(rowname_jt);

		ch_rowname_label.setBounds(new Rectangle(250, 140, 150, 25));
		ch_rowname_jt.setBounds(new Rectangle(360, 140, 150, 25));
		addrow_panel.add(ch_rowname_label);
		addrow_panel.add(ch_rowname_jt);

		addrow_jb.setBounds(new Rectangle(340, 180, 80, 30));
		clear_jb.setBounds(new Rectangle(440, 180, 60, 30));
		addrow_panel.add(addrow_jb);
		addrow_panel.add(clear_jb);

		addrow_jb.addActionListener(new jb1ActionListener());
		clear_jb.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				rowname_jt.setText("");
				ch_rowname_jt.setText("");
			}

		});

		return (addrow_panel);
	}
//新增列按钮监听
	private class jb1ActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (rowname_jt.getText().equals("") || rowname_jt.getText() == null
					|| ch_rowname_jt.getText().equals("")
					|| ch_rowname_jt.getText() == null) {
				new NullErrorFrame();
			} else {
				String direction = null;
				String type = null;
				int fail = 0;
				if (direction_jc.getSelectedItem() == "采集") {
					direction = "MEASURE";
				} else {
					direction = "CONTROL";
				}
				if (type_jc.getSelectedItem() == "模拟") {
					type = "ANALOG";
				} else {
					type = "DIGITAL";
				}
				try {
					addrow((String) tablename_jc.getSelectedItem(),
							rowname_jt.getText(), direction, type,
							ch_rowname_jt.getText());
				} catch (SQLException e2) {
					e2.printStackTrace();
					fail = 1;
					new FailFrame();
				}
				if (fail == 0)
					new DetermineFrame();
				try {
					Ui_Update.update_tablename(con, tablename_jc);
					clear_ui();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
//新增列实现方法
	public void addrow(String kind, String field, String direction, String type,
			String name) throws SQLException {
		String addconfig_sql = null;
		String addtable_sql = null;
		String addstatics_sql = null;
		String getglfid_sql = null;
		int glf_id = 0;

		String table_name = null;

		// try {
		table_name = kind.toLowerCase();
		
		getglfid_sql = "select GLF_ID from glf_info";
		ps = con.prepareStatement(getglfid_sql);
		rs = ps.executeQuery();
		while(rs.next()){
		glf_id = rs.getInt(1);
		}
		

		addconfig_sql = "insert into config (GLF_ID,KIND,FIELD,DIRECTION,TYPE,NAME,RUN) values("+glf_id+",'"
				+ kind
				+ "','"
				+ field
				+ "','"
				+ direction
				+ "','"
				+ type
				+ "','" + name + "',0)";
		ps = con.prepareStatement(addconfig_sql);
		ps.executeUpdate();
		addtable_sql = "alter table " + table_name + " add " + field
				+ " float(20,2)";
		ps = con.prepareStatement(addtable_sql);
		ps.executeUpdate();
		addstatics_sql = "alter table " + table_name + "_statics add "
				+ field + " float(20,2)";
		ps = con.prepareStatement(addstatics_sql);
		ps.executeUpdate();
		/*addstatics_sql = "alter table " + table_name + "_statics add MAX_"
				+ field + " float(20,2)";
		ps = con.prepareStatement(addstatics_sql);
		ps.executeUpdate();
		addstatics_sql = "alter table " + table_name + "_statics add AVG_"
				+ field + " float(20,2)";
		ps = con.prepareStatement(addstatics_sql);
		ps.executeUpdate();
		addstatics_sql = "alter table " + table_name + "_statics add SUM_"
				+ field + " float(20,2)";
		ps = con.prepareStatement(addstatics_sql);
		ps.executeUpdate();*/
		//刷新一遍视图
		new Create8View(kind, con);
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// } finally {
		// JdbcUtils.free(rs, ps, null);
		// }
	}
	//更新界面操作
	public void update_ui() throws SQLException {
		Ui_Update.update_tablename(con, tablename_jc);
		// clear_ui();

	}
//清空操作
	void clear_ui() {
		rowname_jt.setText("");
		ch_rowname_jt.setText("");
	}

}
