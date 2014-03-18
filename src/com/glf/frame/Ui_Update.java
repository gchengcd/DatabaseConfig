package com.glf.frame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;

public final class Ui_Update {
	static PreparedStatement ps = null;
	static ResultSet rs = null;

	private Ui_Update() {

	}

	public static void update_tablename(Connection con, JComboBox tablename_jc)
			throws SQLException {
		tablename_jc.removeAllItems();
		String tablename_sql = "select KIND from table_config";
		ps = con.prepareStatement(tablename_sql);
		rs = ps.executeQuery();
		while (rs.next()) {
			tablename_jc.addItem(rs.getString("KIND"));
		}
	}

	public static void update_rowname(Connection con, String tablename,
			JComboBox rowname_jc) throws SQLException {
		rowname_jc.removeAllItems();
		String sq1 = "select FIELD from config where KIND='" + tablename + "'";
		ps = con.prepareStatement(sq1);
		rs = ps.executeQuery();
		while (rs.next()) {
			rowname_jc.addItem(rs.getString(1));
		}
	}
}
