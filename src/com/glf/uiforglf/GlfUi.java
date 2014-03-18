package com.glf.uiforglf;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.glf.frame.ConnectErrorFrame;
import com.glf.frame.ErrorFrame;
import com.glf.glfconfig.AddCollect;
import com.glf.glfconfig.AddRow;
import com.glf.glfconfig.CreateTable;
import com.glf.glfconfig.DeleteCollect;
import com.glf.glfconfig.DeleteRow;
import com.glf.glfconfig.DeleteTable;
import com.glf.glfconfig.InputTable;
import com.glf.glfconfig.OutputTable;
import com.glf.glfconfig.UpdateRow;
//主程序
public class GlfUi extends JFrame {
	private JTabbedPane tabbedPane;
	Connection conn = null;

	public static void main(String[] args) {
		try {
			GlfUi d = new GlfUi();
		} catch (Exception e) {
			//e.printStackTrace();
			new ErrorFrame();
		}
	}
/*定义主窗体；
 *连接数据库
 *添加9张选项卡
 *监听选项卡切换操作
 */
	private GlfUi() throws Exception {
		super("数据库配置系统");
		setLocation(100, 60);
		setSize(770, 480);
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		Container c = getContentPane();
		tabbedPane = new JTabbedPane(); // 创建选项卡面板对象
		try {
			conn = JdbcUtils.getConnection();
		} catch (Exception e) {
			//e.printStackTrace();
			new ConnectErrorFrame();
		}
		final AddRow AddRow_jp = new AddRow();
		final DeleteRow DeleteRow_jp = new DeleteRow();
		final UpdateRow UpdateRow_jp = new UpdateRow();
		final AddCollect AddCollect_jp = new AddCollect();
		final DeleteCollect DeleteCollect_jp = new DeleteCollect();
		final CreateTable CreateTable_jp = new CreateTable();
		final DeleteTable DeleteTable_jp = new DeleteTable();
		final OutputTable OutputTable_jp = new OutputTable();
		final InputTable InputTable_jp = new InputTable();
		tabbedPane.addTab(" 新建列 ", null, AddRow_jp.build(conn), "新建列");
		tabbedPane.addTab(" 删除列 ", null, DeleteRow_jp.build(conn), "删除列");
		tabbedPane.addTab(" 更新列 ", null, UpdateRow_jp.build(conn), "更新列");
		tabbedPane.addTab("新增采集量", null, AddCollect_jp.build(conn), "新增采集量");
		tabbedPane.addTab("删除采集量", null, DeleteCollect_jp.build(conn), "删除采集量");
		tabbedPane.addTab(" 新增表 ", null, CreateTable_jp.build(conn), "新增表");
		tabbedPane.addTab(" 删除表 ", null, DeleteTable_jp.build(conn), "删除表");
		tabbedPane.addTab(" 导 出 ", null, OutputTable_jp.build(conn), "导 出");
		tabbedPane.addTab(" 导 入 ", null, InputTable_jp.build(conn), "导 入");

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int index = tabbedPane.getSelectedIndex();
				try {
					if (index == 0) {
						AddRow_jp.update_ui();
					} else if (index == 1) {
						DeleteRow_jp.update_ui();
					} else if (index == 2) {
						UpdateRow_jp.update_ui();
					} else if (index == 3) {
						AddCollect_jp.update_ui();
					} else if (index == 4) {
						DeleteCollect_jp.update_ui();
					} else if (index == 6) {
						DeleteTable_jp.update_ui();
					} else if (index == 7) {
						OutputTable_jp.update_ui();
					} else {

					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		c.add(tabbedPane);
		c.setBackground(Color.white);
		setResizable(false);

		setVisible(true);
		addWindowListener(new WindowListener() {

			public void windowClosing(WindowEvent e) {
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}

			public void windowOpened(WindowEvent e) {
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowActivated(WindowEvent e) {
			}

			public void windowDeactivated(WindowEvent e) {
			}
		});
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
