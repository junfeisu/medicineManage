package src;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class login extends JFrame {
	private JPanel panel1 = new JPanel();
	private JTextField userFiled = new JTextField();
	private JLabel userLabel = new JLabel("username:");
	private JLabel passwordLabel = new JLabel("password:");
	private JPasswordField passwordFiled = new JPasswordField();
	private JButton loginButton = new JButton("login");

	public login() {
		userLabel.setPreferredSize(new Dimension(90, 30));
		passwordLabel.setPreferredSize(new Dimension(90, 30));

		userFiled.setPreferredSize(new Dimension(120, 30));
		passwordFiled.setPreferredSize(new Dimension(120, 30));

		panel1.add(userLabel);
		panel1.add(userFiled);
		panel1.add(passwordLabel);
		panel1.add(passwordFiled);
		panel1.add(loginButton);
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		this.setLayout(new GridBagLayout());
		this.add(panel1);
		this.setVisible(true);
		this.setTitle("login");
		this.setSize(600, 500);
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				String username = userFiled.getText();
				System.out.println("username is " + username);
				String password = passwordFiled.getText();
				String sqlSearch = "select username from user where username='" + username + "' && password = '"
						+ password + "'";

				try {
					new connectMysql();
					Statement connectStat = connectMysql.getStat();
					ResultSet rs = connectStat.executeQuery(sqlSearch);
					if (!rs.isBeforeFirst()) {
						JOptionPane.showMessageDialog(null, "There is some error with username or password");
					} else {
						System.out.println("login successful");
						medcineManage frame = new medcineManage(username);
					}
					rs.close();
					connectStat.close();
					connectMysql.getConn().close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
}
