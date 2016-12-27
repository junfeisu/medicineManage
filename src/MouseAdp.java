package src;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MouseAdp implements MouseListener {
	JLabel jl = new JLabel();
	public MouseAdp() {
	}

	public void mouseClicked(MouseEvent e) {
		/** 鼠标点击事件(包括按下和弹起两个动作)处理方法. **/
		int i = JOptionPane.showConfirmDialog(null, "是否真的需要退出系统", "退出确认对话框", JOptionPane.YES_NO_OPTION);
		if (i == 0)
			System.exit(1);
	}

	public void mouseEntered(MouseEvent e) {
		jl = (JLabel) e.getSource();
		jl.setForeground(Color.RED);
		/** 鼠标移到组件上方法时事件处理方法. **/
	}

	public void mouseExited(MouseEvent e) {
		/** 鼠标移开组件时事件处理方法. **/
		jl.setForeground(Color.black);
	}

	public void mousePressed(MouseEvent e) {
		/** 鼠标在组件上按下(但没弹起)时事件处理方法. **/
	}

	public void mouseReleased(MouseEvent e) {
		/** 鼠标在组件上弹起事件处理方法. **/
	}
	
	public static void remove(JPanel j1, JPanel j2, JPanel j3, JPanel j4, JPanel j5, JPanel j6, JPanel j7) {
		j1.remove(j2);
		j1.remove(j3);
		j1.remove(j4);
		j1.remove(j5);
		j1.remove(j6);
		j1.remove(j7);
	}
}