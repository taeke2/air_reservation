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
	public static String id, password, 고객ID, 이름, 주소, 전화번호, 멤버쉽, 결제, 좌석;
}

class ManagerDialog extends JDialog { ////////// 관리자 모드
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
		pane.addTab("항공기관리", new Aircraft());
		pane.addTab("항공사관리", new Airline());
		pane.addTab("공항관리", new Airport());
		pane.addTab("계약관리", new Contract());
		pane.addTab("고객관리", new Customer());
		pane.addTab("항공편관리", new Flight());
		pane.addTab("예약관리", new Reservation());
		return pane;
	}
}

class CustomerDialog extends JDialog { ///////////// 고객모드
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
		pane.addTab("티켓조회", new Cus_ticket());
		pane.addTab("예약조회", new Cus_reservation());
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
			System.out.println("DB 연결 완료");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isConnect;
	}

	public boolean select(String id) {
		// 데이터 검색
		boolean result = false;
		String sql = "select * from customer where 고객ID ='" + tp.id + "'";
		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				cus_id = rs.getString("고객ID");
				cus_name = rs.getString("이름");
			}
			if (tp.id.equals("root") && tp.password.equals("1234")) {
				dialog_m.setVisible(true); // 관리자모드
				JOptionPane.showMessageDialog(getParent(), "관리자님 안녕하세요.");

			} else if (cus_id == null) {
				JOptionPane.showMessageDialog(getParent(), "아이디가 존재하지 않습니다.", "ErrorMessage",
						JOptionPane.ERROR_MESSAGE);
			} else {
				dialog_c.setVisible(true); // 고객모드
				JOptionPane.showMessageDialog(getParent(), cus_name + "님 안녕하세요.");
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int signup() { ////////// 예약정보 기입 후 예약테이블에 정보 넣기
		// 데이터 추가
		Temper tp = new Temper();
		int result = 0;

		try {

			System.out.println(tp.고객ID);
			System.out.println(tp.이름);
			System.out.println(tp.주소);
			System.out.println(tp.전화번호);
			System.out.println(tp.멤버쉽);
			System.out.println(tp.결제);
			System.out.println(tp.좌석);

			sql = "insert into customer value(?,?,?,?,?,?,?)";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, tp.고객ID);
			pstmt.setString(2, tp.이름);
			pstmt.setString(3, tp.주소);
			pstmt.setString(4, tp.전화번호);
			pstmt.setString(5, tp.멤버쉽);
			pstmt.setString(6, tp.결제);
			pstmt.setString(7, tp.좌석);
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	class Signup extends JFrame {////////////////////////////////////// 회원가입

		Signup() {
			setTitle("회원가입");
			String[] co_멤버쉽 = { "선택없음", "YES", "NO" };
			String[] co_결제 = { "선택없음", "cash", "card" };
			String[] co_좌석 = { "선택없음", "일등석", "비즈니스석", "이코노미석" };
			int result = 1;

			Temper tp = new Temper();
			JPanel cp = new JPanel();
			setContentPane(cp);

			JLabel l_고객ID = new JLabel("아이디");
			JLabel l_이름 = new JLabel("이름");
			JLabel l_주소 = new JLabel("주소");
			JLabel l_전화번호 = new JLabel("전화번호");
			JLabel l_항공사멤버쉽가입 = new JLabel("항공사멤버쉽가입");
			JLabel l_선호결제방식 = new JLabel("선호결제방식");
			JLabel l_선호좌석 = new JLabel("선호좌석");
			JTextField txt_아이디 = new JTextField(15);
			JTextField txt_이름 = new JTextField(15);
			JTextField txt_주소 = new JTextField(15);
			JTextField txt_전화번호 = new JTextField(15);
			JButton btn_ok = new JButton("회원가입완료");
			JComboBox co_항공사멤버쉽가입 = new JComboBox(co_멤버쉽);
			JComboBox co_선호결제방식 = new JComboBox(co_결제);
			JComboBox co_선호좌석 = new JComboBox(co_좌석);

			co_항공사멤버쉽가입.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub

					JComboBox combo = (JComboBox) e.getSource();
					int index = combo.getSelectedIndex();
					tp.멤버쉽 = co_멤버쉽[index];

				}
			});
			co_선호결제방식.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JComboBox combo = (JComboBox) e.getSource();
					int index = combo.getSelectedIndex();
					tp.결제 = co_결제[index];

				}
			});
			co_선호좌석.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JComboBox combo = (JComboBox) e.getSource();
					int index = combo.getSelectedIndex();
					tp.좌석 = co_좌석[index];
				}
			});

			btn_ok.addActionListener(new ActionListener() { //////// 회원가입 완료
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					tp.고객ID = txt_아이디.getText().toString();
					tp.이름 = txt_이름.getText().toString();
					tp.주소 = txt_주소.getText().toString();
					tp.전화번호 = txt_전화번호.getText().toString();

					setVisible(false);
					int result = signup();
					if (result != 0) {

						JOptionPane.showMessageDialog(getParent(), tp.이름 + "님 회원가입을 축하드립니다");

					} else {
						JOptionPane.showMessageDialog(getParent(), "회원가입 실패");
					}

				}
			});

			cp.add(l_고객ID);
			cp.add(txt_아이디);
			cp.add(l_이름);
			cp.add(txt_이름);
			cp.add(l_주소);
			cp.add(txt_주소);
			cp.add(l_전화번호);
			cp.add(txt_전화번호);
			cp.add(l_항공사멤버쉽가입);
			cp.add(co_항공사멤버쉽가입);
			cp.add(l_선호결제방식);
			cp.add(co_선호결제방식);
			cp.add(l_선호좌석);
			cp.add(co_선호좌석);
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
		setTitle("Hannam 여행사");
		JLabel l_id = new JLabel("아이디");
		JLabel l_main = new JLabel("Welcom to 한남여행사");
		l_main.setFont(new Font("Serif", Font.BOLD, 35));
		JLabel l_password = new JLabel("비밀번호");
		JTextField txt_id = new JTextField(10);
		JTextField txt_password = new JTextField(10);
		JButton btn_login = new JButton("로그인");
		JButton btn_signup = new JButton("회원가입");

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