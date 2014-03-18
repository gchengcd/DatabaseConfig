package com.glf.glfconfig;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
//创建修改8张视图
class Create8View {
	PreparedStatement ps = null;
	ResultSet rs = null;
	String biaoge_view_sql = null;
	String statics_view_sql = null;
	String select_view_sql = null;
	String tongji_view_sql = null;
	String direction = null;
	String table_type_sql = null;
	int table_type = 1;
	String ch_table_name = null;
	String perfect1_sql = "";
	String perfect2_sql = "";
	String buselect = null;
	String select_chname_sql = null;
	ArrayList<String> field_name_strs = new ArrayList<String>();
	ArrayList<String> field2_name_strs = new ArrayList<String>();
	String table_name = null;

	public Create8View(String cap_table_name, Connection con)
			throws SQLException {

		table_name = cap_table_name.toLowerCase();
		select_chname_sql = "select CNTABLENAME from table_config where KIND='"
				+ cap_table_name + "'";
		ps = con.prepareStatement(select_chname_sql);
		rs = ps.executeQuery();
		while (rs.next()) {
			ch_table_name = rs.getString(1);//得到中文表名
		}

		rs = con.getMetaData().getTables(null, null,
				(table_name + "_info").toUpperCase(), null);// 判断是否含有INFO表
		if (rs.next()) {
			table_type = 0;
		}

		createBiaogeView(cap_table_name, con);

		createStaticsView(cap_table_name, con);

		createSelectView(cap_table_name, con);

		createTongjiView(cap_table_name, con);

	}

	private void createTongjiView(String cap_table_name, Connection con)
			throws SQLException {
		buselect = "select FIELD, NAME from config where KIND='"
				+ cap_table_name + "'";
		ps = con.prepareStatement(buselect);
		rs = ps.executeQuery();
		while (rs.next()) {
			field_name_strs.add(rs.getString(2));//"最小值_" + 
			/*field_name_strs.add(rs.getString(2));//"最大值_" + 
			field_name_strs.add(rs.getString(2));//"平均值_" + 
			field_name_strs.add(rs.getString(2));//"累加值_" + */			
			field2_name_strs.add(rs.getString(1));//"MIN_" + 
			/*field2_name_strs.add("MAX_" + rs.getString(1));//
			field2_name_strs.add("AVG_" + rs.getString(1));//
			field2_name_strs.add("SUM_" + rs.getString(1));//
*/		}
		
		for (int i = 0; i < field_name_strs.size(); i++) {
			perfect1_sql += ", " + field2_name_strs.get(i) + " as "
					+ field_name_strs.get(i);
		}
		if (table_type == 0) {
			tongji_view_sql = "create or replace view " + table_name
					+ "_tongjitu as select DISTINCT glf_info.GLF_NAME AS 锅炉房名称,"
					+ table_name + "_info." + cap_table_name + "_NAME AS "
					+ ch_table_name + "名称," + table_name
					+ "_statics.DT AS 采集时间, " + table_name
					+ "_statics.STATYPE AS 统计类型 ";
			perfect2_sql = " from ((" + table_name + "_statics join "
					+ table_name + "_info) join glf_info) where (("
					+ table_name + "_statics.ID = " + table_name + "_info."
					+ cap_table_name + "_ID) and (" + table_name
					+ "_info.GLF_ID = glf_info.GLF_ID)) ORDER BY '采集时间' ASC , '统计类型' ASC";
		} else {
			tongji_view_sql = "create or replace view " + table_name
					+ "_tongjitu as select DISTINCT glf_info.GLF_NAME AS 锅炉房名称,"
					+ table_name
					+ "_statics.ID AS 锅炉房ID,glf_info.GLF_ID AS GLF_ID,"
					+ table_name + "_statics.DT AS 采集时间, " + table_name
					+ "_statics.STATYPE AS 统计类型 ";
			perfect2_sql = " from (" + table_name
					+ "_statics join glf_info) where (" + table_name
					+ "_statics.ID = glf_info.GLF_ID) ORDER BY '采集时间' ASC, '统计类型' ASC";
		}
		tongji_view_sql = tongji_view_sql + perfect1_sql + perfect2_sql;
		ps = con.prepareStatement(tongji_view_sql);
		ps.executeUpdate();
		tongji_view_sql = "create or replace view " + table_name
				+ "_tongjitu_check as select * from " + table_name
				+ "_tongjitu";
		ps = con.prepareStatement(tongji_view_sql);
		ps.executeUpdate();
		field_name_strs.clear();
		perfect1_sql = "";
	}

	private void createSelectView(String cap_table_name, Connection con)
			throws SQLException {
		buselect = "select FIELD, NAME from config where KIND='"
				+ cap_table_name + "'";
		ps = con.prepareStatement(buselect);
		rs = ps.executeQuery();
		while (rs.next()) {
			field_name_strs.add(rs.getString(2));
			field2_name_strs.add(rs.getString(1));
		}
		for (int i = 0; i < field_name_strs.size(); i++) {
			perfect1_sql += ", " + field2_name_strs.get(i) + " as "
					+ field_name_strs.get(i);
		}
		if (table_type == 0) {
			select_view_sql = "create or replace view " + table_name
					+ "_select as select DISTINCT " + table_name + "_info."
					+ cap_table_name + "_NAME AS " + ch_table_name
					+ "名称,glf_info.GLF_NAME AS 锅炉房名称," + table_name
					+ ".DT AS 采集时间," + table_name + ".F1 AS " + ch_table_name
					+ "描述 ";
			perfect2_sql = " from ((" + table_name
					+ "_info join glf_info) join " + table_name + ") where (("
					+ table_name + "_info." + cap_table_name + "_ID = "
					+ table_name + ".ID) and (" + table_name
					+ "_info.GLF_ID = glf_info.GLF_ID) and (" + table_name
					+ ".GLF_ID = glf_info.GLF_ID)) ORDER BY '采集时间' ASC";
		} else {
			select_view_sql = "create or replace view " + table_name
					+ "_select as select DISTINCT glf_info.GLF_NAME AS 锅炉房名称,"
					+ table_name + ".DT AS 采集时间," + table_name
					+ ".F1 AS 锅炉房描述 ";
			perfect2_sql = " from (glf_info join " + table_name
					+ ") where (glf_info.GLF_ID = " + table_name + ".ID) ORDER BY '采集时间' ASC";
		}
		select_view_sql = select_view_sql + perfect1_sql + perfect2_sql;
		/*try {
			select_view_sql =  new String(select_view_sql.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} ;*/
System.out.println("select_view++"+select_view_sql);
		ps = con.prepareStatement(select_view_sql);
		ps.executeUpdate();
		select_view_sql = "create or replace view " + table_name
				+ "_select_check as select * from " + table_name + "_select";
		ps = con.prepareStatement(select_view_sql);
		ps.executeUpdate();
		field_name_strs.clear();
		field2_name_strs.clear();
		perfect1_sql = "";
	}

	private void createStaticsView(String cap_table_name, Connection con)
			throws SQLException {
		buselect = "select FIELD from config where KIND='" + cap_table_name
				+ "'";
		ps = con.prepareStatement(buselect);
		rs = ps.executeQuery();
		while (rs.next()) {
			field_name_strs.add(rs.getString(1));	//"MIN_" + 
			/*field_name_strs.add("MAX_" + rs.getString(1));
			field_name_strs.add("AVG_" + rs.getString(1));
			field_name_strs.add("SUM_" + rs.getString(1));*/
		}
		for (int i = 0; i < field_name_strs.size(); i++) {
			perfect1_sql += ", " + field_name_strs.get(i);
		}
		if (table_type == 0) {
			statics_view_sql = "create or replace view " + table_name
					+ "_daystatics as select DISTINCT glf_info.GLF_NAME AS GLF_NAME,"
					+ table_name + "_info." + cap_table_name + "_NAME AS "
					+ cap_table_name + "_NAME," + table_name
					+ "_statics.DT AS DT," + table_name + "_statics.F1 AS F1," + 
					table_name + "_statics.STATYPE AS STATYPE ";
			perfect2_sql = " from ((glf_info join " + table_name
					+ "_info) join " + table_name
					+ "_statics) where ((glf_info.GLF_ID = " + table_name
					+ "_statics.GLF_ID) and (" + table_name + "_info."
					+ cap_table_name + "_ID = " + table_name
					+ "_statics.ID) and (glf_info.GLF_ID = " + table_name
					+ "_info.GLF_ID)) ORDER BY 'DT' ASC";
		} else {
			statics_view_sql = "create or replace view " + table_name
					+ "_daystatics as select DISTINCT glf_info.GLF_NAME AS GLF_NAME,"
					+ table_name + "_statics.DT AS DT," + table_name
					+ "_statics.F1 AS F1," + 
					table_name + "_statics.STATYPE AS STATYPE ";
			perfect2_sql = " from (glf_info join " + table_name
					+ "_statics) where (glf_info.GLF_ID = " + table_name
					+ "_statics.ID) ORDER BY 'DT' ASC";
		}
		statics_view_sql = statics_view_sql + perfect1_sql + perfect2_sql;
System.out.println("------"+statics_view_sql);
		ps = con.prepareStatement(statics_view_sql);
		ps.executeUpdate();
		statics_view_sql = "create or replace view " + table_name
				+ "_daystatics_check as select * from " + table_name
				+ "_daystatics";
		ps = con.prepareStatement(statics_view_sql);
		ps.executeUpdate();
		field_name_strs.clear();
		perfect1_sql = "";
	}

	private void createBiaogeView(String cap_table_name, Connection con)
			throws SQLException {
		buselect = "select FIELD from config where KIND='" + cap_table_name
				+ "'";
		ps = con.prepareStatement(buselect);
		rs = ps.executeQuery();

		while (rs.next()) {
			field_name_strs.add(rs.getString(1));// 字段名添加到字段链表中
		}
		for (int i = 0; i < field_name_strs.size(); i++) {
			perfect1_sql += ", " + field_name_strs.get(i);
		}
		if (table_type == 0) {
			biaoge_view_sql = "create or replace view " + table_name
					+ "_biaoge as select DISTINCT " + table_name + "_info."
					+ cap_table_name + "_NAME AS " + cap_table_name
					+ "_NAME,glf_info.GLF_NAME AS GLF_NAME," + table_name
					+ ".DT AS DT," + table_name + ".F1 AS F1";
			perfect2_sql = " from ((" + table_name
					+ "_info join glf_info) join " + table_name + ") where (("
					+ table_name + "_info." + cap_table_name + "_ID = "
					+ table_name + ".ID) and (" + table_name
					+ "_info.GLF_ID = glf_info.GLF_ID) and (" + table_name
					+ ".GLF_ID = glf_info.GLF_ID)) ORDER BY 'DT' ASC";
		} else {
			biaoge_view_sql = "create or replace view " + table_name
					+ "_biaoge as select DISTINCT glf_info.GLF_NAME AS GLF_NAME,"
					+ table_name + ".DT AS DT," + table_name + ".F1 AS F1";
			perfect2_sql = " from (glf_info join " + table_name
					+ ") where (glf_info.GLF_ID = " + table_name + ".ID) ORDER BY 'DT' ASC";
		}
		biaoge_view_sql = biaoge_view_sql + perfect1_sql + perfect2_sql;// 完整创建视图的SQL语句
		ps = con.prepareStatement(biaoge_view_sql);
		ps.executeUpdate();

		biaoge_view_sql = "create or replace view " + table_name
				+ "_biaoge_check as select * from " + table_name + "_biaoge";
		ps = con.prepareStatement(biaoge_view_sql);
		ps.executeUpdate();
		field_name_strs.clear();

		perfect1_sql = "";
	}
}
