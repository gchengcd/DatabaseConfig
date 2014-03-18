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
import com.glf.frame.FailFrame;
import com.glf.frame.NullErrorFrame;

public class CreateTable {
	private JPanel createtable_panel;
	private JLabel tablename_label, ch_tablename_label, tabletype_label;
	private JComboBox tabletype_jc;
	private JTextField tablename_jt, ch_tablename_jt;
	private JButton createtable_jb, clear_jb;

	static Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public JPanel build(Connection conn) {
		con = conn;
		createtable_panel = new JPanel();
		tablename_label = new JLabel("新建表名");
		ch_tablename_label = new JLabel("中文表名");
		tabletype_label = new JLabel("表类型");
		tabletype_jc = new JComboBox();
		tablename_jt = new JTextField("", 15);
		ch_tablename_jt = new JTextField("", 15);
		createtable_jb = new JButton("新建表");
		clear_jb = new JButton("清除");
		createtable_panel.setLayout(null);
		tabletype_jc.addItem("GL_TYPE");
		tabletype_jc.addItem("GLF_TYPE");
		tabletype_label.setBounds(new Rectangle(250, 80, 150, 25));
		tabletype_jc.setBounds(new Rectangle(360, 80, 150, 25));
		createtable_panel.add(tabletype_label);
		createtable_panel.add(tabletype_jc);

		tablename_label.setBounds(new Rectangle(250, 20, 150, 25));
		tablename_jt.setBounds(new Rectangle(360, 20, 150, 25));
		createtable_panel.add(tablename_label);
		createtable_panel.add(tablename_jt);

		ch_tablename_label.setBounds(new Rectangle(250, 50, 150, 25));
		ch_tablename_jt.setBounds(new Rectangle(360, 50, 150, 25));
		createtable_panel.add(ch_tablename_label);
		createtable_panel.add(ch_tablename_jt);

		createtable_jb.setBounds(new Rectangle(340, 120, 80, 30));
		clear_jb.setBounds(new Rectangle(440, 120, 60, 30));
		createtable_panel.add(createtable_jb);
		createtable_panel.add(clear_jb);

		createtable_jb.addActionListener(new jb1ActionListener());
		clear_jb.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				tablename_jt.setText("");
				ch_tablename_jt.setText("");
			}

		});

		return (createtable_panel);
	}

	class jb1ActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (tablename_jt.getText().equals("")
					|| tablename_jt.getText() == null
					|| ch_tablename_jt.getText().equals("")
					|| ch_tablename_jt.getText() == null) {
				new NullErrorFrame();
			} else {
				
				String table_config_sql = null;
				String createtable_sql = null;
				String createtable_statics_sql = null;
				String createtable_info_sql = null;
				String createview_p_sql = "";
				String FENQU_TEMPLATE = " PARTITION BY RANGE (DT div 100)"
						+ " (PARTITION p0 VALUES LESS THAN (201206) ENGINE = InnoDB,"
						+ " PARTITION p1 VALUES LESS THAN (201207) ENGINE = InnoDB,"
						+ " PARTITION p2 VALUES LESS THAN (201208) ENGINE = InnoDB,"
						+ " PARTITION p3 VALUES LESS THAN (201209) ENGINE = InnoDB,"
						+ " PARTITION p4 VALUES LESS THAN (201210) ENGINE = InnoDB,"
						+ " PARTITION p5 VALUES LESS THAN (201211) ENGINE = InnoDB,"
						+ " PARTITION p6 VALUES LESS THAN (201212) ENGINE = InnoDB,"
						+ " PARTITION p7 VALUES LESS THAN (201301) ENGINE = InnoDB,"
						+ " PARTITION p8 VALUES LESS THAN (201302) ENGINE = InnoDB,"
						+ " PARTITION p9 VALUES LESS THAN (201303) ENGINE = InnoDB,"
						+ " PARTITION p10 VALUES LESS THAN (201304) ENGINE = InnoDB,"
						+ " PARTITION p11 VALUES LESS THAN (201305) ENGINE = InnoDB,"
						+ " PARTITION p12 VALUES LESS THAN (201306) ENGINE = InnoDB,"
						+ " PARTITION p13 VALUES LESS THAN (201307) ENGINE = InnoDB,"
						+ " PARTITION p14 VALUES LESS THAN (201308) ENGINE = InnoDB,"
						+ " PARTITION p15 VALUES LESS THAN (201309) ENGINE = InnoDB,"
						+ " PARTITION p16 VALUES LESS THAN (201310) ENGINE = InnoDB,"
						+ " PARTITION p17 VALUES LESS THAN (201311) ENGINE = InnoDB,"
						+ " PARTITION p18 VALUES LESS THAN (201312) ENGINE = InnoDB,"
						+ " PARTITION p19 VALUES LESS THAN (201401) ENGINE = InnoDB,"
						+ " PARTITION p20 VALUES LESS THAN (201402) ENGINE = InnoDB,"
						+ " PARTITION p21 VALUES LESS THAN (201403) ENGINE = InnoDB,"
						+ " PARTITION p22 VALUES LESS THAN (201404) ENGINE = InnoDB,"
						+ " PARTITION p23 VALUES LESS THAN (201405) ENGINE = InnoDB,"
						+ " PARTITION p24 VALUES LESS THAN (201406) ENGINE = InnoDB,"
						+ " PARTITION p25 VALUES LESS THAN (201407) ENGINE = InnoDB,"
						+ " PARTITION p26 VALUES LESS THAN (201408) ENGINE = InnoDB,"
						+ " PARTITION p27 VALUES LESS THAN (201409) ENGINE = InnoDB,"
						+ " PARTITION p28 VALUES LESS THAN (201411) ENGINE = InnoDB,"
						+ " PARTITION p29 VALUES LESS THAN (201412) ENGINE = InnoDB,"
						+ " PARTITION p30 VALUES LESS THAN (201501) ENGINE = InnoDB,"
						+ " PARTITION p31 VALUES LESS THAN (201502) ENGINE = InnoDB,"
						+ " PARTITION p32 VALUES LESS THAN (201503) ENGINE = InnoDB,"
						+ " PARTITION p33 VALUES LESS THAN (201504) ENGINE = InnoDB,"
						+ " PARTITION p34 VALUES LESS THAN (201505) ENGINE = InnoDB,"
						+ " PARTITION p35 VALUES LESS THAN (201506) ENGINE = InnoDB,"
						+ " PARTITION p36 VALUES LESS THAN (201507) ENGINE = InnoDB,"
						+ " PARTITION p37 VALUES LESS THAN (201508) ENGINE = InnoDB,"
						+ " PARTITION p38 VALUES LESS THAN (201509) ENGINE = InnoDB,"
						+ " PARTITION p39 VALUES LESS THAN (201510) ENGINE = InnoDB,"
						+ " PARTITION p40 VALUES LESS THAN (201511) ENGINE = InnoDB,"
						+ " PARTITION p41 VALUES LESS THAN (201512) ENGINE = InnoDB,"
						+ " PARTITION p_catch_all VALUES LESS THAN MAXVALUE ENGINE = InnoDB);";
				int table_uniq = 0;
				int fail = 0;
				String getglfid_sql = null;
				int glf_id = 0;
				try {
					if (tabletype_jc.getSelectedItem() == "GL_TYPE")
						table_uniq = 1;
					getglfid_sql = "select GLF_ID from glf_info";
					ps = con.prepareStatement(getglfid_sql);
					rs = ps.executeQuery();
					while(rs.next()){
					glf_id = rs.getInt(1);
					}
					table_config_sql = "insert into table_config values ("+glf_id+",'"
							+ tablename_jt.getText() + "', " + table_uniq
							+ ", '" + ch_tablename_jt.getText() + "')";
					ps = con.prepareStatement(table_config_sql);
					ps.executeUpdate();

					if (tabletype_jc.getSelectedItem() == "GL_TYPE") {
						createtable_sql = "CREATE TABLE `"
								+ tablename_jt.getText()
								+ "` ("
								+ "`ID`  bigint(20) NOT NULL ,"
								+ "`DT`  datetime NOT NULL ,"
								+ "`F1`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,"
								+ "PRIMARY KEY (`ID`, `DT`)"
								+ ")"
								+ " ENGINE=InnoDB"
								+ " DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci"
								+ " ROW_FORMAT=COMPACT " + FENQU_TEMPLATE;
						ps = con.prepareStatement(createtable_sql);
						ps.execute();

						createtable_statics_sql = "CREATE TABLE `"
								+ tablename_jt.getText()
								+ "_statics` ("
								+ "`ID`  bigint(20) NOT NULL ,"
								+ "`DT`  datetime NOT NULL ,"
								+ "`F1`  varchar(255), `STATYPE`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,"
								+ "PRIMARY KEY (`ID`, `DT`, `STATYPE`)"
								+ ")"
								+ " ENGINE=InnoDB"
								+ " DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci"
								+ " ROW_FORMAT=COMPACT ;";
						ps = con.prepareStatement(createtable_statics_sql);
						ps.execute();

						createview_p_sql = "CREATE OR REPLACE VIEW `"
								+ tablename_jt.getText()
								+ "_p`"
								+ " AS SELECT glf_info.GLF_NAME AS 锅炉房名称,glf_info.GLF_NAME AS GLF_NAME from glf_info";
						ps = con.prepareStatement(createview_p_sql);
						ps.execute();
					} else {
						createtable_sql = "CREATE TABLE `"
								+ tablename_jt.getText()
								+ "` ("
								+ "`GLF_ID`  bigint(20) NOT NULL ,`ID`  bigint(20) NOT NULL ,"
								+ "`DT`  datetime NOT NULL ,"
								+ "`F1`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,"
								+ "PRIMARY KEY (`ID`, `DT`, `GLF_ID`)"
								+ ")"
								+ " ENGINE=InnoDB"
								+ " DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci"
								+ " ROW_FORMAT=COMPACT " + FENQU_TEMPLATE;
						ps = con.prepareStatement(createtable_sql);
						ps.execute();
						createtable_statics_sql = "CREATE TABLE `"
								+ tablename_jt.getText()
								+ "_statics` ("
								+ "`GLF_ID`  bigint(20) NOT NULL ,`ID`  bigint(20) NOT NULL ,"
								+ "`DT`  datetime NOT NULL ,"
								+ "`F1`  varchar(255), `STATYPE`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,"
								+ "PRIMARY KEY (`ID`, `DT`, `GLF_ID`, `STATYPE`)"
								+ ")"
								+ " ENGINE=InnoDB"
								+ " DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci"
								+ " ROW_FORMAT=COMPACT ;";
						ps = con.prepareStatement(createtable_statics_sql);
						ps.execute();
						createtable_info_sql = "CREATE TABLE "
								+ tablename_jt.getText()
								+ "_info (`"
								+ tablename_jt.getText()
								+ "_ID`  bigint(20) NOT NULL ,"
								+ "`GLF_ID`  bigint(20) NOT NULL ,`"
								+ tablename_jt.getText()
								+ "_NAME`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,"
								+ "`"
								+ tablename_jt.getText()
								+ "_LOCATION`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,"
								+ "PRIMARY KEY (`"
								+ tablename_jt.getText()
								+ "_ID`),"
								+ "FOREIGN KEY (`GLF_ID`) REFERENCES `glf_info` (`GLF_ID`) ON DELETE CASCADE ON UPDATE CASCADE,"
								+ "INDEX `RMGL_GLF_ID` (`GLF_ID`) USING BTREE "
								+ ")"
								+ " ENGINE=InnoDB"
								+ " DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci"
								+ " ROW_FORMAT=COMPACT;";
						ps = con.prepareStatement(createtable_info_sql);
						ps.execute();
						createview_p_sql = "CREATE OR REPLACE VIEW `"
								+ tablename_jt.getText() + "_p`"
								+ " AS SELECT "
								+ "`glf_info`.`GLF_NAME` AS `锅炉房名称`," + "`"
								+ tablename_jt.getText() + "_info`.`"
								+ tablename_jt.getText() + "_NAME` AS `"
								+ ch_tablename_jt.getText() + "名称`,"
								+ "`glf_info`.`GLF_NAME` AS `GLF_NAME`," + "`"
								+ tablename_jt.getText() + "_info`.`"
								+ tablename_jt.getText() + "_NAME` AS `"
								+ tablename_jt.getText() + "_NAME`"
								+ " from (`glf_info` join `"
								+ tablename_jt.getText()
								+ "_info`) where (`glf_info`.`GLF_ID` = `"
								+ tablename_jt.getText() + "_info`.`GLF_ID`)";
						ps = con.prepareStatement(createview_p_sql);
						ps.execute();
					}
					new Create8View(tablename_jt.getText(), con);
					tablename_jt.setText("");
					ch_tablename_jt.setText("");

				} catch (SQLException e1) {
					fail = 1;
					new FailFrame();
				}
				if (fail == 0)
					new DetermineFrame();
			}
		}
	}
}
