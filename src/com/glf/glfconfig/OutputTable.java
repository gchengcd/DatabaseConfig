package com.glf.glfconfig;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.UnsupportedEncodingException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.eltima.components.ui.DatePicker;
import com.glf.frame.DetermineFrame;
import com.glf.frame.FailFrame;
import com.glf.frame.NullErrorFrame;
import com.glf.frame.Ui_Update;

public class OutputTable {
	private JPanel outputtable_panel;
	private JLabel outputlocation_label, tablename_label, startdate_label,
			finish_label, note1_label, note2_label;
	private JComboBox tablename_jc;
	private JTextField outputlocation_jt;
	private JButton output_jb, clear_jb, select_jb;
	static Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	DatePicker start_datepick = null;
	DatePicker finish_datepick = null;

	public JPanel build(Connection conn) throws SQLException {
		con = conn;
		outputtable_panel = new JPanel();
		outputlocation_label = new JLabel("将导出文件到");//
		tablename_label = new JLabel("导出的表名");
		startdate_label = new JLabel("开始日期");
		finish_label = new JLabel("结束日期");

		note1_label = new JLabel("注意：请不要存入C盘目录下");
		note2_label = new JLabel("请不要存入中文路径下");

		tablename_jc = new JComboBox();
		outputlocation_jt = new JTextField("", 15);
		output_jb = new JButton("导出数据");
		clear_jb = new JButton("清除");
		select_jb = new JButton("浏览");
		outputtable_panel.setLayout(null);

		String DefaultFormat = "yyyy-MM-dd HH:mm:ss";
		Date date = new Date();
		Font font = new Font("Times New Roman", Font.BOLD, 14);
		Dimension dimension = new Dimension(177, 24);
		start_datepick = new DatePicker(date, DefaultFormat, font, dimension);// 鑷畾涔夊弬鏁板�
		finish_datepick = new DatePicker(date, DefaultFormat, font, dimension);// 鑷畾涔夊弬鏁板�

		outputlocation_label.setBounds(new Rectangle(250, 20, 150, 25));
		outputlocation_jt.setBounds(new Rectangle(360, 20, 200, 25));
		outputtable_panel.add(outputlocation_label);
		outputtable_panel.add(outputlocation_jt);

		Ui_Update.update_tablename(con, tablename_jc);
		tablename_label.setBounds(new Rectangle(250, 90, 150, 25));
		tablename_jc.setBounds(new Rectangle(360, 90, 150, 25));
		outputtable_panel.add(tablename_label);
		outputtable_panel.add(tablename_jc);

		startdate_label.setBounds(new Rectangle(250, 120, 150, 25));
		outputtable_panel.add(startdate_label);

		finish_label.setBounds(new Rectangle(250, 150, 150, 25));
		outputtable_panel.add(finish_label);

		note1_label.setBounds(new Rectangle(360, 40, 300, 20));
		outputtable_panel.add(note1_label);
		note2_label.setBounds(new Rectangle(360, 60, 300, 20));
		outputtable_panel.add(note2_label);

		output_jb.setBounds(new Rectangle(320, 190, 100, 30));
		clear_jb.setBounds(new Rectangle(440, 190, 60, 30));
		select_jb.setBounds(new Rectangle(560, 20, 60, 25));
		outputtable_panel.add(output_jb);
		outputtable_panel.add(clear_jb);
		outputtable_panel.add(select_jb);

		start_datepick.setBounds(360, 120, 166, 25);
		start_datepick.setLocale(Locale.CHINA);
		outputtable_panel.add(start_datepick);

		finish_datepick.setBounds(360, 150, 166, 25);
		finish_datepick.setLocale(Locale.CHINA);
		outputtable_panel.add(finish_datepick);

		/* outputlocation_jt.setEditable(false); */

		output_jb.addActionListener(new jb1ActionListene());
		select_jb.addActionListener(new selectjbActionListene());

		return (outputtable_panel);
	}

	class jb1ActionListene implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (outputlocation_jt.getText().equals("")
					|| outputlocation_jt.getText() == null) {
				new NullErrorFrame();
			} else {
				int fail = 0;
				try {
					String startdate, enddate, table_name, select_sql, delete_sql;
					String startdatestring, enddatestring, directory, directoryname;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					sdf.setLenient(false);
					Date ddate;
					startdate = start_datepick.getText();
					enddate = finish_datepick.getText();
					ddate = sdf.parse(startdate);
					startdatestring = (new SimpleDateFormat("yyyyMMdd"))
							.format(ddate);
					ddate = sdf.parse(enddate);
					enddatestring = (new SimpleDateFormat("yyyyMMdd"))
							.format(ddate);

					directory = outputlocation_jt.getText() + startdatestring
							+ "-" + enddatestring + "\\";
					if (!(new File(directory).isDirectory())) {
						new File(directory).mkdir();
					}
					directoryname = directory + tablename_jc.getSelectedItem()
							+ ".txt";
					directoryname = directoryname.replace("\\", "\\\\");
					select_sql = "select * into outfile '"
							+ directoryname
							+ "' from "
							+ ((String) tablename_jc.getSelectedItem())
									.toLowerCase() + " where DT>='" + startdate
							+ "' and DT<'" + enddate + "'";

					ps = con.prepareStatement(select_sql);

					ps.execute();
					delete_sql = "delete from "
							+ tablename_jc.getSelectedItem() + " where DT>='"
							+ startdate + "' and DT<'" + enddate + "'";
					ps = con.prepareStatement(delete_sql);
					ps.execute();

				} catch (Exception e1) {
					fail = 1;
					new FailFrame();
				}
				if (fail == 0)
					new DetermineFrame();
				/*
				 * try { update_ui(); } catch (SQLException e1) {
				 * e1.printStackTrace(); }
				 */
			}
		}

	}

	class selectjbActionListene implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			JFileChooser chooser = new JFileChooser();
			chooser.setMultiSelectionEnabled(false);
			/*
			 * FileNameExtensionFilter filter = new
			 * FileNameExtensionFilter("MP3&WAV","mp3","wav");
			 */
			/* chooser.setFileFilter(filter); */
			chooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
			chooser.setDialogTitle("选择文件夹");

			/* int result = chooser.showOpenDialog(null); */
			int saveresult = chooser.showSaveDialog(null);

			/*
			 * if(result == JFileChooser.APPROVE_OPTION){ //File file =
			 * chooser.getSelectedFile(); File file1 =
			 * chooser.getCurrentDirectory(); }
			 */
			if (saveresult == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				String filename = file.getName();
				File file1 = chooser.getCurrentDirectory();
				String filepath = file1.getAbsolutePath();
				// System.out.println(filename);
				// System.out.println("E:\\");
				if (filename.equals("C:\\") || filename.equals("D:\\")
						|| filename.equals("E:\\") || filename.equals("F:\\")
						|| filename.equals("G:\\") || filename.equals("H:\\")) {
					// System.out.println("ooooooooooo");
					outputlocation_jt.setText(filename + "\\");
				} else {
					// System.out.println("mmmmmmmmmmm");
					//
					outputlocation_jt
							.setText(filepath + "\\" + filename + "\\");// 将文件路径设到JTextField
				}
			}
		}
	}

	public void update_ui() throws SQLException {
		Ui_Update.update_tablename(con, tablename_jc);
	}
}
