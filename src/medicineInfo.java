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

public class medicineInfo extends JPanel {
	static int page = 1;
	static int flag = 0;
	static int num[] = { 9, 9, 9, 9, 9, 9, 9, 9 };
	static int cout = 0;
	int ref = 0;
	int totalPage = 0;
	int total = 0;
	static RButton rt_1 = new RButton("");

	public medicineInfo() {
		f(1, sqlOperate.getPointedMedicines(1, 8));
	}

	// <--- 函数 --->
	// 刷新
	void refresh() {
		removeAll();
		f(page, sqlOperate.getPointedMedicines(page, 8));
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
		String[] str3 = { "name", "code", "address", "description", "price", "count", "needNum", "time", "type" };
		String[] str = { "药名", "编码", "生产地址", "描述", "价格", "数量", "需求量", "生产日期", "类型" };
		for (int i = 0; i < list.size(); i++) {
			Vector<String> currentRow = new Vector<String>();
			for (int k = 0; k < 9; k++)
				currentRow.add(list.get(i).get(str3[k]));
			rows.add(currentRow);
		}

		// 行转列
		for (int s = 0; s < 9; s++) {
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
		for (int f = 0; f < 9; f++)
			model.addColumn(str[f], columns.get(f));
		model.addColumn("操作", view);
		model.addColumn("操作", updata);
		model.addColumn("操作", delete);

		table.setRowHeight(30);
		for (int c = 10; c < 13; c++) {
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

		// <--- 事件 --->
		// 全选
		table.getTableHeader().addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (table.getTableHeader().columnAtPoint(e.getPoint()) == 0) {
					switch (flag) {
					case 0:
						flag = 1;
						refresh();
						for (int i = 0; i < 8; i++) {
							num[i] = i;
						}
						break;
					case 1:
						flag = 0;
						refresh();
						for (int i = 0; i < 8; i++) {
							num[i] = 9;
						}
						break;
					}
				}
			}
		});

		// 上一页
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (page > 1) {
					page -= 1;
					refresh();
				} else {
					JOptionPane.showMessageDialog(null, "当前已经在首页");
				}
			}
		});

		// 下一页
		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (page < total) {
					page += 1;
					refresh();
				} else {
					JOptionPane.showMessageDialog(null, "当前已经在尾页");
				}
			}
		});

		// 跳转
		bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Integer.parseInt(tf3.getText()) > 0 && Integer.parseInt(tf3.getText()) <= total) {
					page = Integer.parseInt(tf3.getText());
					refresh();
				} else {
					JOptionPane.showMessageDialog(null, "输入有误，请输入正确页数(1 - " + total + ")");
				}
			}
		});

		// 添加
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new popFrame(null, "new");
				if (ref == 0) {
					ref = 1;
					timer();
				}
			}
		});

		// 批量删除
		del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = JOptionPane.showConfirmDialog(null, "是否真的要删除所选记录", "删除确认对话框", JOptionPane.YES_NO_OPTION);
				if (i == 0) {
					Vector<String> del = new Vector<String>();
					for (int g = 0; g < list.size(); g++) {
						if (num[g] != 9) {
							del.add(list.get(num[g]).get("medicine_id"));
						}
					}
					int[] str = new int[del.size()];
					for (int h = 0; h < del.size(); h++) {
						str[h] = Integer.parseInt(del.get(h));
					}
					sqlOperate.deleteMedicine(str);
					refresh();
				}
			}
		});

		// 搜索
		final JTextField tf2 = tf;
		rt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = tf2.getText().toString();
				ArrayList<HashMap<String, String>> result = sqlOperate.searchMedicine(text);
				if (result.size() != 0) {
					removeAll();
					f(1, result);
				} else {
					JOptionPane.showMessageDialog(null, "there is noe medicine like " + text);
				}
			}
		});

		// 列表
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				HashMap<String, String> hm = list.get(table.getSelectedRow());
				if (table.getSelectedColumn() == 0) {
					num[cout] = table.getSelectedRow();
					cout++;
				}
				// 查看
				if (table.getSelectedColumn() == 10) {
					new popFrame(hm, "view");
				}

				// 修改
				if (table.getSelectedColumn() == 11) {
					new popFrame(hm, "update");
					if (ref == 0) {
						ref = 1;
						timer();
					}
				}

				// 删除
				if (table.getSelectedColumn() == 12) {
					int i = JOptionPane.showConfirmDialog(null, "是否真的要删除所选记录", "删除确认对话框", JOptionPane.YES_NO_OPTION);
					if (i == 0) {
						int[] str = { Integer.parseInt(hm.get("medicine_id")) };
						sqlOperate.deleteMedicine(str);
						refresh();
					}
				}
			}
		});
	}
}

class MyTable extends JTable {
	public Class getColumnClass(int myCol) {
		return getValueAt(0, myCol).getClass();
	}
}
