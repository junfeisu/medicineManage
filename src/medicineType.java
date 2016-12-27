package src;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class medicineType extends JPanel {
	static int page = 1;
	static int flag = 0;
	static int num[] = { 9, 9, 9, 9, 9, 9, 9, 9 };
	static int cout = 0;
	int ref = 0;
	int totalPage = 0;
	int total = 0;

	public medicineType() {
		f(1, sqlOperate.getTypes(1, 8));
	}

	// <--- 函数 --->
	// 刷新
	void refresh() {
		removeAll();
		f(page, sqlOperate.getTypes(page, 8));
	}

	// 动态刷新
	public void timer() {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				refresh();
			}
		}, 5000, 5000);
	}

	public void f(int p, ArrayList<HashMap<String, String>> list) {
		totalPage = Integer.parseInt(list.get(1).get("total"));
		medcineManage.jl_location.setText("  当前位置>>基础信息>>药品类别管理");
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
		String[] str3 = { "description", "typeName", "createTime", "shortDesc" };
		String[] str = { "描述", "类名", "生产日期", "有效期" };
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
		Vector<Boolean> select = new Vector<Boolean>();
		Vector<String> view = new Vector<String>();
		Vector<String> updata = new Vector<String>();
		Vector<String> delete = new Vector<String>();
		for (int v = 0; v < list.size(); v++) {
			if (flag == 0) {
				select.add(false);
			} else {
				select.add(true);
			}
			view.add("查看");
			updata.add("修改");
			delete.add("删除");
		}
		model.addColumn("全选/反选", select);
		for (int f = 0; f < 4; f++)
			model.addColumn(str[f], columns.get(f));
		model.addColumn("操作", view);
		model.addColumn("操作", updata);
		model.addColumn("操作", delete);

		table.setRowHeight(30);
		for (int c = 5; c < 8; c++) {
			table.getColumnModel().getColumn(c).setPreferredWidth(30);
		}

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

		// <--- 分页 --->
		footer.setLayout(new BorderLayout(0, 10));
		JPanel box = new JPanel();
		JPanel key = new JPanel();
		JPanel jump = new JPanel();
		footer.add(box, BorderLayout.WEST);
		footer.add(key, BorderLayout.CENTER);
		footer.add(jump, BorderLayout.EAST);

		box.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
		// 添加、批量删除
		RButton add = new RButton(" 添加 ");
		RButton del = new RButton("批量删除 ");
		box.add(add);
		box.add(del);

		// 翻页
		key.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 15));
		RButton up = new RButton("上一页");
		RButton down = new RButton("下一页");
		key.add(up);
		key.add(new JLabel("当前页:" + p));
		key.add(down);

		// 跳转
		jump.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 15));
		final JTextField tf3 = new JTextField(3);
		RButton bt = new RButton("跳转");
		jump.add(bt);
		jump.add(new JLabel("到第"));
		jump.add(tf3);
		if (totalPage % 8 == 0) {
			total = totalPage / 8;
		} else {
			total = totalPage / 8 + 1;
		}
		jump.add(new JLabel("页/共" + total + "页   " + totalPage + "条记录"));
	}
}