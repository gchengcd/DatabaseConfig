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
import com.glf.frame.Ui_Update;

public class DeleteCollect {
	private JPanel deletecollect_panel;
    private JLabel tablename_label,rowname_label;
    private JComboBox tablename_jc,rowname_jc;
    private JButton deletecollect_jb,clear_jb;
    private String select_sql = null;
	private String update_sql = null;
	static Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
    

    
    public JPanel build(Connection conn) throws SQLException{
    	con = conn;
    	deletecollect_panel=new JPanel();  
    	tablename_label=new JLabel("待配置表名");
        rowname_label=new JLabel("待配置列名");

        tablename_jc = new JComboBox();
        rowname_jc = new JComboBox();
        deletecollect_jb = new JButton("删除配置项");
        clear_jb = new JButton("清除");
        deletecollect_panel.setLayout(null);
        Ui_Update.update_tablename(con, tablename_jc);
        tablename_label.setBounds(new Rectangle(250, 20, 150, 25));
        tablename_jc.setBounds(new Rectangle(360, 20, 150, 25)); 
        deletecollect_panel.add(tablename_label);
        deletecollect_panel.add(tablename_jc);

        rowname_label.setBounds(new Rectangle(250, 50, 150, 25));
        rowname_jc.setBounds(new Rectangle(360, 50, 150, 25));
        deletecollect_panel.add(rowname_label);
        deletecollect_panel.add(rowname_jc);
       
        
        deletecollect_jb.setBounds(new Rectangle(320, 90, 100, 30));
        clear_jb.setBounds(new Rectangle(440, 90, 60, 30));
        deletecollect_panel.add(deletecollect_jb);
        deletecollect_panel.add(clear_jb);
        updaterow();
		tablename_jc.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				try {
					updaterow();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		deletecollect_jb.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {

				update_sql = "update config set RUN=0 where KIND='"
				+ tablename_jc.getSelectedItem() +"' and FIELD='"+rowname_jc.getSelectedItem()
				+"'";
				try {
					ps = con.prepareStatement(update_sql);
					ps.executeUpdate();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				new DetermineFrame(); 
				try {
					update_ui();
					updaterow();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
        
        return(deletecollect_panel);
    }
    public void update_ui() throws SQLException {
		Ui_Update.update_tablename(con, tablename_jc);
		updaterow();
	}
    void updaterow() throws SQLException{
    	rowname_jc.removeAllItems();
		String rowname_sql = "select FIELD from config where KIND='" 
	+ tablename_jc.getSelectedItem() + "' and RUN=1";
		ps = con.prepareStatement(rowname_sql);
		rs = ps.executeQuery();
		while (rs.next()) {
			rowname_jc.addItem(rs.getString(1));
		}
	}
}
