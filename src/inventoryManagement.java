package src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

class inventoryInfo  extends JPanel{	
        static int page = 1;
	static int flag = 0;
	static int num[] = { 9, 9, 9, 9, 9, 9, 9, 9 };
	static int cout = 0;
	int ref = 0;
	int totalPage = 0;
	int total = 0;
	public inventoryInfo(){
            ArrayList<HashMap<String, String>> list = sqlOperate.getPointedMedicines(1, 8);
		medcineManage.jl_location.setText("  当前位置>>库存管理>>库存信息查看");
                totalPage = Integer.parseInt(list.get(0).get("total"));
		medcineManage.jl_location.setText("  当前位置>>基础信息>>药品信息管理");
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
		String[] str3 = { "name", "count", "needNum", "type" };
		String[] str = { "药名", "库存", "需求量","类型" };
		for (int i = 0; i < list.size(); i++) {
			Vector<String> currentRow = new Vector<String>();
			for (int k = 0; k < 4; k++)
				currentRow.add(list.get(i).get(str3[k]));
			rows.add(currentRow);
		}

		// 行转列
		for (int s = 0; s < 4; s++) {
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
			view.add("查看");
			updata.add("采购");
		}
		for (int f = 0; f < 4; f++)
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
        
        void refresh() {
		removeAll();
		add(new inventoryInfo());
	}

	// 动态刷新
	public void timer() {
		final java.util.Timer timer = new java.util.Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				refresh();
			}
		}, 5000, 5000);
	}
}

class inventoryType  extends JPanel{	
	public inventoryType(){
		medcineManage.jl_location.setText("  当前位置>>库存管理>>库存类别排行");
                                System.out.println();
                ArrayList<HashMap<String, String>> list = sqlOperate.warningMedicine();
                JPanel content=new JPanel();
                content.setPreferredSize(new Dimension(300,100));
                add(content);
                content.setLayout(new GridLayout(list.size()+1,3));
                content.add(new JLabel("药名"));
                    content.add(new JLabel("库存"));
                    content.add(new JLabel("类别"));
                for(int i = 0;i < list.size(); i++){
                    content.add(new JLabel(list.get(i).get("name")));
                    content.add(new JLabel(list.get(i).get("count")));
                    content.add(new JLabel(list.get(i).get("type")));
                }
	}
}
