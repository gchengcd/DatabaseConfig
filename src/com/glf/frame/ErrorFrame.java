package com.glf.frame;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ErrorFrame extends JFrame{
	JPanel determine_jp = new JPanel();
	JButton determine_jb = new JButton("确定");
	JLabel determine_jl = new JLabel("出错了！！！");
	public ErrorFrame() {
        super("失败");
        setLocation(400, 200);
        setSize(227, 120);
        determine_jp.setLayout(null);
        add(determine_jp);
        determine_jb.addActionListener(new ActionListener(){
        	   public void actionPerformed(ActionEvent e) {
        		   dispose();
        	   }
        	  });
        determine_jl.setBounds(new Rectangle(72,10,220,20));
        determine_jb.setBounds(new Rectangle(80,32,60,30));
        determine_jp.add(determine_jl);
        determine_jp.add(determine_jb);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
