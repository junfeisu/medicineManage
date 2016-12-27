package src;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class popFrame extends JFrame {
	public popFrame(final HashMap<String, String> s ,String st) {
		if(st=="view"){
			setTitle("查看药品信息");
		}else if(st=="update"){
			setTitle("修改药品信息");
		}else if(st=="new"){
			setTitle("添加药品信息");
		}
		setLayout(new BorderLayout(10, 45));
		setSize(600, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(true);

		// 全局
		JPanel jp = new JPanel();
		JPanel jp2 = new JPanel();
		JPanel jp3 = new JPanel();
		add(jp3, BorderLayout.NORTH);
		add(jp, BorderLayout.CENTER);
		add(jp2, BorderLayout.SOUTH);
		jp.setLayout(new GridLayout(5, 4, 10, 10));

		String[] str3 = { "name", "code", "address", "description", "price", "count", "needNum", "time", "type" };
		String[] str = { "药品名 :", "编码 :", "生产地址 :", "药品描述 :", "价格 :", "数量 :", "需求数 :", "生产日期 :", "类型 :" };
		JLabel[] jl = new JLabel[9];
		final JTextField[] jf = new JTextField[9];
		for (int j = 0; j < 9; j++) {
			jl[j] = new JLabel(str[j]);
			jl[j].setHorizontalAlignment(SwingConstants.RIGHT); // 表头标签居右
			if(st=="view"){
				jf[j] = new JTextField(s.get(str3[j]));	
				jf[j].setEditable(false);
			}else if(st=="update"){
				jf[j] = new JTextField(s.get(str3[j]));	
				if(j == 0){
					jf[j].setEditable(false);
				}else{
					jf[j].setEditable(true);
				}
			}else if(st=="new"){
				jf[j] = new JTextField();
			}
			jf[j].setOpaque(false);
			jp.add(jl[j]);
			jp.add(jf[j]);
		}

		// 按钮
		jp2.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 0));
		RButton rt1 = new RButton("");
		RButton rt2 = new RButton("");		
		if(st=="view"){
			rt1.setText("修改");
			medicineInfo.rt_1.setText("修改");
			rt2.setText("关闭");
		}else if(st=="update"||st=="new"){
			rt1.setText("保存");
			medicineInfo.rt_1.setText("保存");
			rt2.setText("取消");
		}			
//		jp2.add(rt1);
		jp2.add(medicineInfo.rt_1);
		jp2.add(rt2);

		// 按钮事件
		medicineInfo.rt_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if(st=="view"){
					new popFrame(s,"update");
					setVisible(false);
				}else if(st=="update"){
					String[] str = new String[9] ;			
					for(int k = 0; k < 9; k++){
						str[k] = jf[k].getText();
					}
					sqlOperate.updateMedicine(StrToHash(str),Integer.parseInt(s.get("medicine_id")));
					setVisible(false);
				}else if(st=="new"){
					String[] str = new String[9];
					for (int i = 0; i < 9; i++) {
						str[i] = jf[i].getText();
					}
					if (str[0].isEmpty() == true || str[1].isEmpty() == true || str[2].isEmpty() == true
							|| str[5].isEmpty() == true || str[6].isEmpty() == true || str[8].isEmpty() == true) {
						JOptionPane.showMessageDialog(null, "添加失败,请填写完整信息");
						dispose();
						new popFrame(null,"new");
					}
					HashMap<String, String> result = sqlOperate.insertMedicine(str);
					if (result.get("success").isEmpty()==false){
						JOptionPane.showMessageDialog(null, "添加成功");
					}					
					else{
						JOptionPane.showMessageDialog(null, "添加失败");
					}					
					setVisible(false);
				}								
			}
		});

		rt2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
	}

	// String数组转Hashmap方法
	public static HashMap<String, String> StrToHash(String[] str) {
		HashMap<String, String> result = new HashMap<>();
		result.put("name", str[0]);
		result.put("code", str[1]);
		result.put("address", str[2]);
		result.put("description", str[3]);
		result.put("price", str[4]);
		result.put("count", str[5]);
		result.put("needNum", str[6]);		
		result.put("time", str[7]);
		result.put("type", str[8]);
		return result;
	}
}
