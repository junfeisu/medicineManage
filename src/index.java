package src;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class index extends JPanel{
	public index(){
		ImageIcon icon1 = new ImageIcon("./src/img/bg2.jpg");// 创建图片对象
		JLabel bg1 = new JLabel(icon1);
		add(bg1);
	}
}