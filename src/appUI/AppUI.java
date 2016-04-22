package appUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import solution.VerifyFile;

public class AppUI
{
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				SimpleFrame frame = new SimpleFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		}); 
	}
}

class SimpleFrame extends JFrame
{
	private JPanel panel;
	private static final int DEFAULT_WIDTH = 600;
	private static final int DEFAULT_HEIGHT = 500;
	private JTextField configureFilePath;
	private JTextField keywordFilePath;
	private JTextField header1Word;
	private JTextField header2Word;
	private JTextArea resultMessage;
	private JLabel configureFileLabel;
	private JLabel keywordFileLabel;
	private JLabel header1Label;
	private JLabel header2Label;

	public SimpleFrame()
	{
		//设置大小，运行时居中显示
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocation(screenSize.width/2-DEFAULT_WIDTH/2, screenSize.height/2-DEFAULT_HEIGHT/2);
		
		//设置图标
		//Image img = new ImageIcon().getImage();
		//setIconImage(img);
		
		//设置标题 : 显示当前文件路径
		//setTitle("...");
		
		//不允许改变窗口大小
		setResizable(false);
		//创建面板
		panel = new JPanel();
		//创建按钮
		JButton beginButton = new JButton("确定");
		//输入文本框
		configureFilePath = new JTextField(40);
		keywordFilePath = new JTextField(40);
		header1Word = new JTextField(40);
		header2Word = new JTextField(40);
		//输出文本框，上下左右滚动
		resultMessage = new JTextArea(22,51);
		resultMessage.setLineWrap(true);
		resultMessage.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(resultMessage);
		configureFileLabel = new JLabel("配置文件的绝对路径");
		  keywordFileLabel = new JLabel("关键词文件绝对路径");
		      header1Label = new JLabel("文件头固定字符串一");
		      header2Label = new JLabel("文件头固定字符串二");
		
		setLayout(new BorderLayout());
		//添加按钮和文本框
		//配置文件框
		JPanel panel1 = new JPanel();
		panel.add(configureFileLabel);
		panel.add(configureFilePath);
		//panel.add(panel1);
		//关键词框
		JPanel panel2 = new JPanel();
		panel.add(keywordFileLabel);
		panel.add(keywordFilePath);
		//panel.add(panel2);
		JPanel panel1And2 = new JPanel();
		//panel1And2.add(panel1, BorderLayout.NORTH);
		//panel1And2.add(panel2, BorderLayout.SOUTH);
		//panel.add(panel1And2,BorderLayout.NORTH);
		panel.add(header1Label);
		panel.add(header1Word);
		panel.add(header2Label);
		panel.add(header2Word);
		panel.add(beginButton);
		panel.add(scrollPane);
		
		//向框架添加面板
		add(panel);
		
		//创建并添加监听器
		VerifyAction verifyAction = new VerifyAction();
		beginButton.addActionListener(verifyAction);
		
	}
	
	//创建按钮事件
	private class VerifyAction implements ActionListener {
		
		public void actionPerformed (ActionEvent event) {
			//System.out.println("YES");
			//获取必要信息
			String configureFile = configureFilePath.getText().trim();
			String keywordFile = keywordFilePath.getText().trim();
			String header1 = header1Word.getText().trim();
			String header2 = header2Word.getText().trim();
			
			//显示到界面
			String tips = ">>>>>>>>>>>>>>>>>>>>  校验结果  <<<<<<<<<<<<<<<<<<<<\r\n";
			try {
				String result = new VerifyFile(configureFile,keywordFile,header1,header2).sectionVerify();
				resultMessage.setText(tips + result);
			} catch (Exception e) {
				
				resultMessage.setText(tips + "找不到指定文件！");
			}
		}
	}
}