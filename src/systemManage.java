package src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

class administator  extends JPanel{
    static int page = 1;
	static int flag = 0;
	static int num[] = { 9, 9, 9, 9, 9, 9, 9, 9 };
	static int cout = 0;
	int ref = 0;
	int totalPage = 0;
	int total = 0;
	public administator(){
                ArrayList<HashMap<String, String>> list = sqlOperate.getAllUsers();
                totalPage = list.size();
		Font font = new Font("隶书", Font.PLAIN, 13);
		UIManager.put("Label.font", font);
		// 全局
		setLayout(new BorderLayout(10, 10));
		JPanel header = new JPanel();
		final JPanel content = new JPanel();
		JPanel footer = new JPanel();
		add(header, BorderLayout.NORTH);
		add(content, BorderLayout.CENTER);
		add(footer, BorderLayout.SOUTH);

		// <--- 操作 --->
		header.setLayout(new BorderLayout(0, 10));
		JPanel function = new JPanel();
		JPanel search = new JPanel();
		header.add(function, BorderLayout.WEST);
		header.add(search, BorderLayout.EAST);
		function.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 15));
		search.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 15));

		// 搜索
		MyTextField tf = new MyTextField(15);
		tf.setOpaque(false);
		search.add(tf);
		RButton rt = new RButton(" 搜索 ");
		search.add(rt);

		// <--- 列表 --->
		content.setLayout(new BorderLayout(10, 10));
		Vector<Vector<String>> rows = new Vector<Vector<String>>();
		Vector<Vector<String>> columns = new Vector<Vector<String>>();
		String[] str3 = { "user_id", "username", "role" };
		String[] str = { "账号ID", "账号名", "角色" };
		for (int i = 0; i < list.size(); i++) {
			Vector<String> currentRow = new Vector<String>();
			for (int k = 0; k < 3; k++)
				currentRow.add(list.get(i).get(str3[k]));
			rows.add(currentRow);
		}

		// 行转列
		for (int s = 0; s < 3; s++) {
			Vector<String> currentColumn = new Vector<String>();
			for (int g = 0; g < list.size(); g++)
				currentColumn.add(rows.get(g).get(s));
			columns.add(currentColumn);
		}

		final MyTable table = new MyTable() {
			public boolean isCellEditable(int row, int column) {
				if (column == 0)
					return true;
				else
					return false;
			}
		};

		// 添加列表内容
		final DefaultTableModel model = (DefaultTableModel) table.getModel();

		Vector<String> view = new Vector<String>();
		Vector<String> updata = new Vector<String>();
		for (int v = 0; v < list.size(); v++) {
			view.add("修改密码");
			updata.add("删除账号");
		}
		for (int f = 0; f < 3; f++)
			model.addColumn(str[f], columns.get(f));
		model.addColumn("操作", view);
		model.addColumn("操作", updata);

		table.setRowHeight(30);
//		for (int c = 10; c < 13; c++) {
//			table.getColumnModel().getColumn(c).setPreferredWidth(30);
//		}

		table.setOpaque(false);
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setOpaque(false);// render单元格设置为透明
		renderer.setHorizontalAlignment(SwingConstants.CENTER);// 设置居中显示
		table.setDefaultRenderer(Object.class, renderer);

		final JScrollPane scroller = new JScrollPane(table);
		scroller.setOpaque(false);// 将scroller根面板设置为透明
		scroller.getViewport().setOpaque(false);// 将scroller的viewport设置为透明
		scroller.setPreferredSize(new Dimension(100, 263));
		setOpaque(false);
		content.add(scroller, BorderLayout.CENTER);
	}
}
class initStatus  extends JPanel{	
	public initStatus(){
	}
}
