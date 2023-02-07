import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import java.util.*;

class Temper {
	public static String id, password, ��ID, �̸�, �ּ�, ��ȭ��ȣ, �����, ����, �¼�;
}

class ManagerDialog extends JDialog { ////////// ������ ���
	Container contentPane;

	public ManagerDialog(JFrame frame, String title) {
		super(frame, title);
		contentPane = getContentPane();
		JTabbedPane pane = createTabbedPane();
		contentPane.add(pane, BorderLayout.CENTER);
		setSize(1350, 600);

	}

	JTabbedPane createTabbedPane() {
		JTabbedPane pane = new JTabbedPane(JTabbedPane.LEFT);
		pane.addTab("�װ������", new Aircraft());
		pane.addTab("�װ������", new Airline());
		pane.addTab("���װ���", new Airport());
		pane.addTab("������", new Contract());
		pane.addTab("������", new Customer());
		pane.addTab("�װ������", new Flight());
		pane.addTab("�������", new Reservation());
		return pane;
	}
}

class CustomerDialog extends JDialog { ///////////// �����
	Container contentPane;

	public CustomerDialog(JFrame frame, String title) {
		super(frame, title);
		contentPane = getContentPane();
		JTabbedPane pane = createTabbedPane();
		contentPane.add(pane, BorderLayout.CENTER);
		setSize(1350, 600);

	}

	JTabbedPane createTabbedPane() {
		JTabbedPane pane = new JTabbedPane(JTabbedPane.LEFT);
		pane.addTab("Ƽ����ȸ", new Cus_ticket());
		pane.addTab("������ȸ", new Cus_reservation());
		return pane;
	}
}

public class MainUI extends JFrame {
	ManagerDialog dialog_m = new ManagerDialog(this, "Manager mode");
	CustomerDialog dialog_c = new CustomerDialog(this, "Cutomer mode");
	String driver = "com.mysql.cj.jdbc.Driver";
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	String sql;

	Temper tp = new Temper();

	public static String cus_id, cus_name;

	public boolean connect() throws SQLException {
		boolean isConnect = false;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost/mydb?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC", "root",
					"1234");
			isConnect = true;
			System.out.println("DB ���� �Ϸ�");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isConnect;
	}

	public boolean select(String id) {
		// ������ �˻�
		boolean result = false;
		String sql = "select * from customer where ��ID ='" + tp.id + "'";
		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				cus_id = rs.getString("��ID");
				cus_name = rs.getString("�̸�");
			}
			if (tp.id.equals("root") && tp.password.equals("1234")) {
				dialog_m.setVisible(true); // �����ڸ��
				JOptionPane.showMessageDialog(getParent(), "�����ڴ� �ȳ��ϼ���.");

			} else if (cus_id == null) {
				JOptionPane.showMessageDialog(getParent(), "���̵� �������� �ʽ��ϴ�.", "ErrorMessage",
						JOptionPane.ERROR_MESSAGE);
			} else {
				dialog_c.setVisible(true); // �����
				JOptionPane.showMessageDialog(getParent(), cus_name + "�� �ȳ��ϼ���.");
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int signup() { ////////// �������� ���� �� �������̺� ���� �ֱ�
		// ������ �߰�
		Temper tp = new Temper();
		int result = 0;

		try {

			System.out.println(tp.��ID);
			System.out.println(tp.�̸�);
			System.out.println(tp.�ּ�);
			System.out.println(tp.��ȭ��ȣ);
			System.out.println(tp.�����);
			System.out.println(tp.����);
			System.out.println(tp.�¼�);

			sql = "insert into customer value(?,?,?,?,?,?,?)";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, tp.��ID);
			pstmt.setString(2, tp.�̸�);
			pstmt.setString(3, tp.�ּ�);
			pstmt.setString(4, tp.��ȭ��ȣ);
			pstmt.setString(5, tp.�����);
			pstmt.setString(6, tp.����);
			pstmt.setString(7, tp.�¼�);
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	class Signup extends JFrame {////////////////////////////////////// ȸ������

		Signup() {
			setTitle("ȸ������");
			String[] co_����� = { "���þ���", "YES", "NO" };
			String[] co_���� = { "���þ���", "cash", "card" };
			String[] co_�¼� = { "���þ���", "�ϵ", "����Ͻ���", "���ڳ�̼�" };
			int result = 1;

			Temper tp = new Temper();
			JPanel cp = new JPanel();
			setContentPane(cp);

			JLabel l_��ID = new JLabel("���̵�");
			JLabel l_�̸� = new JLabel("�̸�");
			JLabel l_�ּ� = new JLabel("�ּ�");
			JLabel l_��ȭ��ȣ = new JLabel("��ȭ��ȣ");
			JLabel l_�װ����������� = new JLabel("�װ�����������");
			JLabel l_��ȣ������� = new JLabel("��ȣ�������");
			JLabel l_��ȣ�¼� = new JLabel("��ȣ�¼�");
			JTextField txt_���̵� = new JTextField(15);
			JTextField txt_�̸� = new JTextField(15);
			JTextField txt_�ּ� = new JTextField(15);
			JTextField txt_��ȭ��ȣ = new JTextField(15);
			JButton btn_ok = new JButton("ȸ�����ԿϷ�");
			JComboBox co_�װ����������� = new JComboBox(co_�����);
			JComboBox co_��ȣ������� = new JComboBox(co_����);
			JComboBox co_��ȣ�¼� = new JComboBox(co_�¼�);

			co_�װ�����������.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub

					JComboBox combo = (JComboBox) e.getSource();
					int index = combo.getSelectedIndex();
					tp.����� = co_�����[index];

				}
			});
			co_��ȣ�������.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JComboBox combo = (JComboBox) e.getSource();
					int index = combo.getSelectedIndex();
					tp.���� = co_����[index];

				}
			});
			co_��ȣ�¼�.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JComboBox combo = (JComboBox) e.getSource();
					int index = combo.getSelectedIndex();
					tp.�¼� = co_�¼�[index];
				}
			});

			btn_ok.addActionListener(new ActionListener() { //////// ȸ������ �Ϸ�
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					tp.��ID = txt_���̵�.getText().toString();
					tp.�̸� = txt_�̸�.getText().toString();
					tp.�ּ� = txt_�ּ�.getText().toString();
					tp.��ȭ��ȣ = txt_��ȭ��ȣ.getText().toString();

					setVisible(false);
					int result = signup();
					if (result != 0) {

						JOptionPane.showMessageDialog(getParent(), tp.�̸� + "�� ȸ�������� ���ϵ帳�ϴ�");

					} else {
						JOptionPane.showMessageDialog(getParent(), "ȸ������ ����");
					}

				}
			});

			cp.add(l_��ID);
			cp.add(txt_���̵�);
			cp.add(l_�̸�);
			cp.add(txt_�̸�);
			cp.add(l_�ּ�);
			cp.add(txt_�ּ�);
			cp.add(l_��ȭ��ȣ);
			cp.add(txt_��ȭ��ȣ);
			cp.add(l_�װ�����������);
			cp.add(co_�װ�����������);
			cp.add(l_��ȣ�������);
			cp.add(co_��ȣ�������);
			cp.add(l_��ȣ�¼�);
			cp.add(co_��ȣ�¼�);
			cp.add(btn_ok);

			setSize(220, 400);
			setLocation(600, 300);
			btn_ok.setPreferredSize(new Dimension(150, 40));
			setVisible(true);

		}

	}

	public MainUI() {

		JPanel cp = new JPanel();
		setContentPane(cp);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Hannam �����");
		JLabel l_id = new JLabel("���̵�");
		JLabel l_main = new JLabel("Welcom to �ѳ������");
		l_main.setFont(new Font("Serif", Font.BOLD, 35));
		JLabel l_password = new JLabel("��й�ȣ");
		JTextField txt_id = new JTextField(10);
		JTextField txt_password = new JTextField(10);
		JButton btn_login = new JButton("�α���");
		JButton btn_signup = new JButton("ȸ������");

		try {
			connect();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		btn_login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				tp.id = txt_id.getText().toString();
				tp.password = txt_password.getText().toString();

//				setVisible(false);
				select(tp.id);
			}
		});

		btn_signup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

//				setVisible(false);
				new Signup();
			}
		});

		btn_login.setPreferredSize(new Dimension(160, 50));
		btn_signup.setPreferredSize(new Dimension(160, 50));

		cp.add(l_main);
		cp.add(l_id);
		cp.add(txt_id);
		cp.add(l_password);
		cp.add(txt_password);
		cp.add(btn_login);
		cp.add(btn_signup);

//		cp.add(btn_c);
		setSize(400, 200);
		setLocation(200, 300);
		setVisible(true);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MainUI();
	}
}