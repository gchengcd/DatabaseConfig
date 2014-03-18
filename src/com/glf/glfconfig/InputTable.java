package com.glf.glfconfig;

import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.glf.frame.DetermineFrame;
import com.glf.frame.FailFrame;

public class InputTable {
	private JPanel inputtable_panel;
	private JLabel label1, label2,inputlocation_label;
	private JButton input_jb, clear_jb, select_jb;
	private JTextField inputlocation_jt;
	static Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	String direction = null;
	String filename = "";
	String[] tablename = new String[2];
	String import_sql;

	public JPanel build(Connection conn) {
		con = conn;
		inputtable_panel = new JPanel();
		label1 = new JLabel("选择待导入的文件");
		label2 = new JLabel("未选择文件");
		inputlocation_label = new JLabel("文件路径：");

		input_jb = new JButton("导入数据");
		clear_jb = new JButton("清除");
		select_jb = new JButton("选择文件");
		inputlocation_jt = new JTextField("", 15);
		inputtable_panel.setLayout(null);

		label1.setBounds(new Rectangle(250, 20, 150, 25));
		inputtable_panel.add(label1);

		select_jb.setBounds(new Rectangle(360, 20, 100, 25));
		inputtable_panel.add(select_jb);

		label2.setBounds(new Rectangle(500, 20, 150, 25));
		inputtable_panel.add(label2);
		
		inputlocation_label.setBounds(new Rectangle(220, 60, 150, 25));
		inputtable_panel.add(inputlocation_label);
		inputlocation_jt.setBounds(new Rectangle(300, 60, 250, 25));
		inputlocation_jt.setEditable(false);
		inputtable_panel.add(inputlocation_jt);


		input_jb.setBounds(new Rectangle(320, 100, 100, 30));
		clear_jb.setBounds(new Rectangle(440, 100, 60, 30));
		inputtable_panel.add(input_jb);
		inputtable_panel.add(clear_jb);

		select_jb.addActionListener(new selectActionListener());
		input_jb.addActionListener(new inputActionListener());

		return (inputtable_panel);
	}

	class selectActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"数据库文件TXT)", "txt");
			fileChooser.setFileFilter(filter);
			int i = fileChooser.showOpenDialog(null);
			if (i == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				filename = file.getName();
				File file1 = fileChooser.getCurrentDirectory();
				String filepath = file1.getAbsolutePath();
				direction = filepath + "\\" + filename;// 将文件路径设到JTextField
				inputlocation_jt.setText(direction);
			}
			label2.setText("已选择文件");
		}
	}

	class inputActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			int fail = 0;
			tablename = filename.split("\\.");
			import_sql = "load data infile '" + direction + "' into table "
					+ tablename[0] + " CHARACTER SET utf8";
			import_sql = import_sql.replace("\\", "\\\\");
			try {
				ps = con.prepareStatement(import_sql);
				ps.execute();
			} catch (SQLException e1) {
				//e1.printStackTrace();
				fail = 1;
				new FailFrame();
			}
			if (fail == 0)
				new DetermineFrame();
		}
	}
}
