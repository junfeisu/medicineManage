package src;
import java.awt.*;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

public class medcineManage extends JFrame {
	final String[] str = { "首页", "基础信息", "库存管理", "销售管理", "采购管理", "系统管理" };
	final static JLabel jl_location = new JLabel();
	public static int currentNum = 2;
	public static boolean[] expand = {false, false, false, false, false};

	public medcineManage(String s) {
		JPanel[] channel = new JPanel[11];
		channel[0] = new index();
		
		channel[1] = new medicineInfo();
		channel[2] = new medicineType();
		
		channel[3] = new inventoryInfo();
		channel[4] = new inventoryType();
		
		channel[5] = new salesIncome();
		channel[6] = new salesRank();
		
		channel[7] = new purchasingExpenses();
		channel[8] = new purchasingNum();
		
		channel[9] = new administator();
		channel[10] = new initStatus();

		Font font = new Font("隶书", Font.PLAIN, 15);
		UIManager.put("Label.font", font);
		setTitle("医药管理系统");
		setSize(1375, 768);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		// 全局
		setLayout(new BorderLayout(0, 10));
		JPanel header = new JPanel();
		JPanel content = new JPanel();
		final JPanel footer = new JPanel();
		add(header, BorderLayout.NORTH);
		add(content, BorderLayout.CENTER);
		add(footer, BorderLayout.SOUTH);

		// <--- 页头 --->
		header.setLayout(new BorderLayout(5, 0));
		// 标题
		ImageIcon icon1 = new ImageIcon("./src/img/bg1.jpg");// 创建图片对象
		JLabel bg1 = new JLabel(icon1);
		header.add(bg1, BorderLayout.NORTH);

		// 登录
		JPanel login = new JPanel();
		JPanel date = new JPanel();
		JPanel quit = new JPanel();
		login.setLayout(new BorderLayout(5, 0));
		login.add(date, BorderLayout.WEST);
		login.add(quit, BorderLayout.EAST);

		// 当前时间
		date.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		for (int i = 0; i < 40; i++)
			date.add(new JLabel());
		date.add(new JLabel("现在时间："));
		TimeFrame datetime = new TimeFrame();
		date.add(datetime);

		// 退出系统
		quit.setLayout(new FlowLayout(FlowLayout.RIGHT, 50, 0));
		quit.add(new JLabel(s + " , 欢迎您"));
		JLabel jl = new JLabel("退出系统");
		jl.addMouseListener(new MouseAdp());
		quit.add(jl);
		header.add(login, BorderLayout.SOUTH);

		// <--- 主体 --->
		content.setLayout(new BorderLayout(10, 0));
		JPanel menu = new JPanel();
		JPanel object = new JPanel();
		content.add(menu, BorderLayout.WEST);
		content.add(object, BorderLayout.CENTER);

		// 菜单
		final RButton[] rt = new RButton[6];
		for (int i = 0; i < 6; i++) {
			rt[i] = new RButton(str[i]);
			rt[i].setFont(new java.awt.Font("Dialog", 1, 20));
			rt[i].setPreferredSize(new Dimension(180,50));
			rt[i].setContentAreaFilled(false);
			rt[i].setBorderPainted(false);
			rt[i].setBorder(BorderFactory.createRaisedBevelBorder());
		}

		// 主窗口
		object.setBorder(BorderFactory.createTitledBorder("分组框"));
		object.setBorder(BorderFactory.createLineBorder(Color.black));
		object.setLayout(new BorderLayout(20,0));

		JPanel location = new JPanel();
		final JPanel table = new JPanel();
		object.add(location, BorderLayout.NORTH);
		object.add(new JPanel(), BorderLayout.WEST);
		object.add(table, BorderLayout.CENTER);
		object.add(new JPanel(), BorderLayout.EAST);

		// 当前位置
		location.setLayout(new FlowLayout(FlowLayout.LEFT, 50, 0));
		jl_location.setText("  当前位置>>首页");
		location.setBackground(Color.DARK_GRAY);
		jl_location.setForeground(Color.WHITE);
		jl_location.setFont(new java.awt.Font("Dialog", 0, 15));
		location.add(jl_location);

		//菜单
		final JPanel pNorth=new JPanel();
        pNorth.setLayout(new GridLayout(6,1));
        final JPanel pSouth=new JPanel();
        final JPanel subMenuContainer=new JPanel();
        subMenuContainer.setLayout(new GridLayout(2,1));
        
        menu.setLayout(new BorderLayout());
        for(int k=0;k<6;k++){
        	pNorth.add(rt[k]);
    	}

        final JScrollPane pCenter=new JScrollPane(subMenuContainer);
        pCenter.setBorder(null);

        menu.add(pNorth,"North");
        menu.add(pCenter,"Center");
        menu.add(pSouth,"South");
        
        final String[] str2 = {"药品信息管理","药品类别管理","药品库存查看","库存不足预警",
        		"销售收入趋势","销售热门排行","采购支出查看","采购需求量统计","管理员账号管理","登录记录查询"};
        final JButton[] bt = new JButton[10];
        for(int g=0;g<10;g++){
        	bt[g]=new JButton(str2[g]);
			bt[g].setFont(new java.awt.Font("Dialog", 1, 15));
			bt[g].setPreferredSize(new Dimension(100,50));
			bt[g].setContentAreaFilled(false);
			bt[g].setBorderPainted(false);
			bt[g].setBorder(BorderFactory.createRaisedBevelBorder());
			bt[g].setForeground(Color.ORANGE);
        }
        
        for (int i = 1; i < 6; i++) {
    		final int m = i + 1;
    		rt[i].addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				if (currentNum == m && expand[currentNum - 2]) {
						subMenuContainer.removeAll();
						pNorth.removeAll();
						pSouth.removeAll();
						pNorth.setLayout(new GridLayout(6, 1));
						for (int l = 0; l < 6; l++) {
							pNorth.add(rt[l]);
						}
						pNorth.repaint();
						pCenter.repaint();
						pSouth.repaint();
						validate();
						expand[currentNum - 2] = false;
					} else {
						subMenuContainer.removeAll();
						pNorth.removeAll();
						pSouth.removeAll();
						pNorth.setLayout(new GridLayout(m, 1));
						pSouth.setLayout(new GridLayout(6 - m, 1));
						for (int l = 0; l < m; l++) {
							pNorth.add(rt[l]);
						}
						for (int n = m; n < 6; n++) {
							pSouth.add(rt[n]);
						}
						subMenuContainer.add(bt[2 * m - 4]);
						subMenuContainer.add(bt[2 * m - 3]);
						pNorth.repaint();
						pCenter.repaint();
						pSouth.repaint();
						validate();
						expand[currentNum - 2] = false;
						currentNum = m;
						expand[m - 2] = true;
					}
				}
    		});
    	}

		// 列表
		final CardLayout card = new CardLayout();
		table.setLayout(card);
		table.add(channel[0], str[0]);
		for(int i = 1; i < 11; i++){
			table.add(channel[i], str2[i-1]);
		}

		rt[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jl_location.setText("  当前位置>>首页");
				card.show(table, str[0]);
				JPanel test = new medicineInfo();
				channel[2] = test;
			}
		});
		
		for (int i = 0; i < 10; i++) {
			final int m = i;
			bt[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jl_location.setText("  当前位置>>" + str[(m+2)/2] + ">>"+ str2[m]);
					card.show(table, str2[m]);
				}
			});
		}

		// <--- 页脚 --->
		ImageIcon icon3 = new ImageIcon("./src/img/bg3.jpg");
		JLabel bg3 = new JLabel(icon3);
		footer.add(bg3);
	}

	public static void main(String[] args) {
//		new login();
//		String[] msg = {"创可贴", "10008", "江西制药厂", "这是创可贴", "11.9", "6", "2", "2016-09-12", "感冒发热"};
//		System.out.print(sqlOperate.insertMedicine(msg));
		String msg = "1 OR 1=1;drop table test";
		System.out.println(sqlOperate.test(msg));
	}
}