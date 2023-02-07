import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Aircraft extends JPanel implements ActionListener {
	JLabel m_label, l_비행기기체번호, l_제작사, l_종류, l_일등석좌석수, l_비즈니스좌석수, l_이코노미좌석수, l_길이, l_날개폭, l_소속항공사, l_keyword;
	JTextField txt_비행기기체번호, txt_제작사, txt_종류, txt_일등석좌석수, txt_비즈니스좌석수, txt_이코노미좌석수, txt_길이, txt_날개폭, txt_소속항공사,
			txt_keyword;
	JTable table;
	JScrollPane scroll;
	JButton bt_insert, bt_update, bt_delete, bt_select, bt_getListAll, bt_exit;
	JPanel south, north, center, p1, p2, p3, p4;

	private String colName1[] = { "비행기 기체번호", "제작사", "종류", "일등석 좌석수", "비즈니스 좌석수", "이코노미 좌석수", "길이", "날개폭", "소속 항공사" };
	private DefaultTableModel model1 = new DefaultTableModel(colName1, 0) {
		// Jtable 내용 편집 안되게
		public boolean isCellEditable(int i, int c) {
			return false;
		}
	};
	String driver = "com.mysql.cj.jdbc.Driver";
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	String sql;
	int selRow;

	class MyPanel extends JFrame {

		MyPanel() {
			this.setBackground(Color.YELLOW);
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					closeDB(); // ㅇ윈도우창이 종료될때 DB도종료
					System.exit(0);
				}
			});
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			pack();
		}
	}

	public Aircraft() {
		m_label = new JLabel("항공기 관리");
		l_비행기기체번호 = new JLabel("비행기 기체번호");
		l_제작사 = new JLabel("제작사");
		l_종류 = new JLabel("종류");
		l_일등석좌석수 = new JLabel("일등석 좌석수");
		l_비즈니스좌석수 = new JLabel("비즈니스 좌석수");
		l_이코노미좌석수 = new JLabel("이코노미 좌석수");
		l_길이 = new JLabel("길이");
		l_날개폭 = new JLabel("날개폭");
		l_소속항공사 = new JLabel("소속 항공사");
		l_keyword = new JLabel("항공기 번호를 입력하여 조회하시오.");
		txt_비행기기체번호 = new JTextField(15);
		txt_제작사 = new JTextField(8);
		txt_종류 = new JTextField(15);
		txt_일등석좌석수 = new JTextField(8);
		txt_비즈니스좌석수 = new JTextField(8);
		txt_이코노미좌석수 = new JTextField(8);
		txt_길이 = new JTextField(8);
		txt_날개폭 = new JTextField(8);
		txt_소속항공사 = new JTextField(8);
		txt_keyword = new JTextField(10);
		bt_insert = new JButton("등록");
		bt_update = new JButton("수정");
		bt_delete = new JButton("삭제");
		bt_select = new JButton("조회");
		bt_getListAll = new JButton("전체조회");
		bt_exit = new JButton("종료");
		table = new JTable(model1);

		table.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
		table.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가

		scroll = new JScrollPane(table);
		south = new JPanel();
		north = new JPanel();
		center = new JPanel();
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		p4 = new JPanel();
//		setTitle("항공기 관리");

		north.setPreferredSize(new Dimension(1200, 150));
		center.setPreferredSize(new Dimension(1200, 300));
		south.setPreferredSize(new Dimension(1200, 50));
		add(north, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(south, BorderLayout.SOUTH);
		north.setLayout(new GridLayout(4, 1));

		north.add(p1);
		north.add(p2);
		north.add(p3);
		north.add(p4);

		p1.add(m_label);

		p2.add(l_비행기기체번호);
		p2.add(txt_비행기기체번호);
		p2.add(l_제작사);
		p2.add(txt_제작사);
		p2.add(l_종류);
		p2.add(txt_종류);
		p3.add(l_일등석좌석수);
		p3.add(txt_일등석좌석수);
		p3.add(l_비즈니스좌석수);
		p3.add(txt_비즈니스좌석수);
		p3.add(l_이코노미좌석수);
		p3.add(txt_이코노미좌석수);
		p3.add(l_길이);
		p3.add(txt_길이);
		p3.add(l_날개폭);
		p3.add(txt_날개폭);
		p3.add(l_소속항공사);
		p3.add(txt_소속항공사);

		p4.add(l_keyword);
		p4.add(txt_keyword);
		p4.add(bt_select);
		p4.add(bt_getListAll);

		center.setLayout(new GridLayout(1, 1));
		center.add(scroll);
		south.setLayout(new GridLayout(1, 4));
		south.add(bt_insert);
		south.add(bt_update);
		south.add(bt_delete);
		south.add(bt_exit);

		// 버튼에 액션 리스너 달아주기
		bt_insert.addActionListener(this);
		bt_update.addActionListener(this);
		bt_delete.addActionListener(this);
		bt_select.addActionListener(this);
		bt_getListAll.addActionListener(this);
		bt_exit.addActionListener(this);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 마우스가 클릭되었을때
				selRow = table.getSelectedRow();
				System.out.println(selRow);
				String 비행기기체번호 = (String) table.getValueAt(selRow, 0);
				String 제작사 = (String) table.getValueAt(selRow, 1);
				String 종류 = (String) table.getValueAt(selRow, 2);
				String 일등석좌석수 = (String) table.getValueAt(selRow, 3);
				String 비즈니스좌석수 = (String) table.getValueAt(selRow, 4);
				String 이코노미좌석수 = (String) table.getValueAt(selRow, 5);
				String 길이 = (String) table.getValueAt(selRow, 6);
				String 날개폭 = (String) table.getValueAt(selRow, 7);
				String 소속항공사 = (String) table.getValueAt(selRow, 8);

				txt_비행기기체번호.setText(비행기기체번호);
				txt_제작사.setText(제작사);
				txt_종류.setText(종류);
				txt_일등석좌석수.setText(일등석좌석수);
				txt_비즈니스좌석수.setText(비즈니스좌석수);
				txt_이코노미좌석수.setText(이코노미좌석수);
				txt_길이.setText(길이);
				txt_날개폭.setText(날개폭);
				txt_소속항공사.setText(소속항공사);
			}
		});

		new MyPanel();
		connect();
		getListAll();

		setVisible(true);
	}

	public boolean connect() {
		boolean isConnect = false;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost/mydb?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC", "root",
					"1234");
			isConnect = true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return isConnect;
	}

	public void getListAll() {
		// 전체 데이터 조회
		String sql = "select * from aircraft order by 비행기기체번호; ";
		try {
			for (int i = model1.getRowCount() - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); // select
			Vector list = new Vector();
			while (rs.next()) {
				String[] row = new String[9];
				row[0] = rs.getString("비행기기체번호");
				row[1] = rs.getString("제작사");
				row[2] = rs.getString("종류");
				row[3] = rs.getString("일등석좌석수");
				row[4] = rs.getString("비즈니스좌석수");
				row[5] = rs.getString("이코노미좌석수");
				row[6] = rs.getString("길이");
				row[7] = rs.getString("날개폭");
				row[8] = rs.getString("소속항공사");

				model1.addRow(row);
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int insert() {
		// 데이터 추가
		int result = 0;
		sql = "Insert into aircraft ";
		sql = sql + "Value(?,?,?,?,?,?,?,?,?)";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_비행기기체번호.getText());
			pstmt.setString(2, txt_제작사.getText());
			pstmt.setString(3, txt_종류.getText());
			pstmt.setString(4, txt_일등석좌석수.getText());
			pstmt.setString(5, txt_비즈니스좌석수.getText());
			pstmt.setString(6, txt_이코노미좌석수.getText());
			pstmt.setString(7, txt_길이.getText());
			pstmt.setString(8, txt_날개폭.getText());
			pstmt.setString(9, txt_소속항공사.getText());
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int delete() {
		// 데이터 삭제
		int result = 0;
		String sql = "Delete from aircraft where 비행기기체번호=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_비행기기체번호.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int update() {
		// 데이터 수정
		int result = 0;
		String sql = "Update aircraft set 제작사=?, 종류=?, 일등석좌석수=?, 비즈니스좌석수=?, 이코노미좌석수=?, 길이=?, 날개폭=?, 소속항공사=? where 비행기기체번호=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_제작사.getText());
			pstmt.setString(2, txt_종류.getText());
			pstmt.setString(3, txt_일등석좌석수.getText());
			pstmt.setString(4, txt_비즈니스좌석수.getText());
			pstmt.setString(5, txt_이코노미좌석수.getText());
			pstmt.setString(6, txt_길이.getText());
			pstmt.setString(7, txt_날개폭.getText());
			pstmt.setString(8, txt_소속항공사.getText());
			pstmt.setString(9, txt_비행기기체번호.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean select(String keyword) {
		// 데이터 검색
		boolean result = false;
		String sql = "select * from aircraft where 비행기기체번호 = '" + keyword + "'"; // 키워드인 항공기번호를 입력하여 검색함
		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			for (int i = model1.getRowCount() - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			while (rs.next()) {
				String[] row = new String[10];
				row[0] = rs.getString("비행기기체번호");
				row[1] = rs.getString("제작사");
				row[2] = rs.getString("종류");
				row[3] = rs.getString("일등석좌석수");
				row[4] = rs.getString("비즈니스좌석수");
				row[5] = rs.getString("이코노미좌석수");
				row[6] = rs.getString("길이");
				row[7] = rs.getString("날개폭");
				row[8] = rs.getString("소속항공사");
				model1.addRow(row);
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public void clear() {
		txt_비행기기체번호.setText("");
		txt_제작사.setText("");
		txt_종류.setText("");
		txt_일등석좌석수.setText("");
		txt_비즈니스좌석수.setText("");
		txt_이코노미좌석수.setText("");
		txt_길이.setText("");
		txt_날개폭.setText("");
		txt_소속항공사.setText("");
		txt_비행기기체번호.requestFocus();
	}

	public void closeDB() {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if (pstmt != null)
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if (con != null)
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj.equals(bt_insert)) {
			int result = insert();
			if (result != 0) {
				JOptionPane.showMessageDialog(getParent(), txt_비행기기체번호.getText() + " 등록성공");
				getListAll();
				clear();
				table.updateUI();
			} else {
				JOptionPane.showMessageDialog(getParent(), txt_비행기기체번호.getText() + " 등록실패");
			}
		}
		// 수정버튼
		if (obj.equals(bt_update)) {
			if (JOptionPane.showConfirmDialog(getParent(), txt_비행기기체번호.getText() + " 의 정보를 수정 하시겠습니까?") == 0) {
				int result = update();
				if (result != 0) {
					getListAll();
					clear();
					table.updateUI();
				}
			}
		}
		if (obj.equals(bt_delete)) {
			if (JOptionPane.showConfirmDialog(getParent(), txt_비행기기체번호.getText() + " 의 정보를 삭제 하시겠습니까?") == 0) {
				int result = delete();
				if (result != 0) {
					getListAll();
					clear();
					table.updateUI();
				}
			}
		}
		// 조회버튼
		if (obj.equals(bt_select)) {
			select(txt_keyword.getText());
		}
		// 전체 조회버튼
		if (obj.equals(bt_getListAll)) {
			getListAll();
		}
		// 종료버튼
		if (obj.equals(bt_exit)) {
			closeDB();
			System.exit(0);
		} // end exit버튼 action구현
	}// end action이벤트 구현
}
