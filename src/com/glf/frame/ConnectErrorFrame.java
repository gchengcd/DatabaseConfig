package com.glf.frame;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConnectErrorFrame extends JFrame{
	JPanel determine_jp = new JPanel();
	JButton determine_jb = new JButton("确定");
	JLabel determine_jl = new JLabel("连接数据库错误，请检查网络！");
	public ConnectErrorFrame() {
        super("失败");
        setLocation(400, 200);
        setSize(227, 120);
        determine_jp.setLayout(null);
        add(determine_jp);
        determine_jb.addActionListener(new ActionListener(){
        	   public void actionPerformed(ActionEvent e) {
        		   System.exit(0);
        	   }
        	  });
        determine_jl.setBounds(new Rectangle(30,10,220,20));
        determine_jb.setBounds(new Rectangle(80,32,60,30));
        determine_jp.add(determine_jl);
        determine_jp.add(determine_jb);
        setResizable(false);
        setVisible(true);
        addWindowListener(new WindowListener() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //System.exit(0);
	}
}
