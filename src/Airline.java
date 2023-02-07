import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Airline extends JPanel implements ActionListener {
	JLabel m_label, l_id, l_name, l_addr, l_tel, l_ctr, l_keyword;
	JTextField txt_id, txt_name, txt_addr, txt_tel, txt_keyword;
	JTable table;
	JScrollPane scroll;
	JButton bt_insert, bt_update, bt_delete, bt_select, bt_getListAll, bt_exit;
	JPanel south, north, center, p1, p2, p3, p4;

	private String colName1[] = { "항공사ID", "항공사이름", "주소", "전화번호" };
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

	public Airline() {
		m_label = new JLabel("항공사 관리");
		l_id = new JLabel("항공사 아이디");
		l_name = new JLabel("항공사 이름");
		l_addr = new JLabel("주소");
		l_tel = new JLabel("전화번호");
		l_keyword = new JLabel("항공사 이릅을 입력하여 조회하시오.");
//		l_ctr = new JLabel("국적");
		txt_id = new JTextField(8);
		txt_name = new JTextField(8);
		txt_addr = new JTextField(30);
		txt_tel = new JTextField(8);
//		txt_ctr = new JTextField(8);
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
//		p4 = new JPanel();
//		setTitle("항공사 관리");

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
//		north.add(p4);

		p1.add(m_label);
		p2.add(l_id);
		p2.add(txt_id);
		p2.add(l_name);
		p2.add(txt_name);
		p2.add(l_tel);
		p2.add(txt_tel);
		p2.add(l_addr);
		p2.add(txt_addr);
//		p3.add(l_ctr);
//		p3.add(txt_ctr);
		p3.add(l_keyword);
		p3.add(txt_keyword);
		p3.add(bt_select);
		p3.add(bt_getListAll);

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
				String id = (String) table.getValueAt(selRow, 0);
				String name = (String) table.getValueAt(selRow, 1);
				String addr = (String) table.getValueAt(selRow, 2);
				String tel = (String) table.getValueAt(selRow, 3);
//				String country = (String) table.getValueAt(selRow, 4);
				txt_id.setText(id);
				txt_name.setText(name);
				txt_addr.setText(addr);
				txt_tel.setText(tel);
//				txt_ctr.setText(country);
			}
		});
		new MyPanel();
		connect();
		getListAll();

		setVisible(true);
	}

	public boolean connect() {
		boolean isConnect = true;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost/mydb?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC", "root",
					"1234");
//			isConnect = true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return isConnect;
	}

	public void getListAll() {
		// 전체 데이터 조회
		String sql = "select * from airline order by 이름; ";
		try {
			for (int i = model1.getRowCount() - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); // select
			Vector list = new Vector();
			while (rs.next()) {
				String[] row = new String[5];
				row[0] = rs.getString("항공사ID");
				row[1] = rs.getString("이름");
				row[2] = rs.getString("주소");
				row[3] = rs.getString("전화번호");
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
		sql = "Insert into airline ";
		sql = sql + "Value(?,?,?,?)";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_id.getText());
			pstmt.setString(2, txt_name.getText());
			pstmt.setString(3, txt_addr.getText());
			pstmt.setString(4, txt_tel.getText());
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int delete() {
		// 데이터 삭제
		int result = 0;
		String sql = "Delete from airline where 항공사ID=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_id.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int update() {
		// 데이터 수정
		int result = 0;
		String sql = "Update airline set 이름=?, 전화번호=? where 항공사ID=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_name.getText());
			pstmt.setString(2, txt_tel.getText());
			pstmt.setString(3, txt_id.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public boolean select(String keyword) {
		// 데이터 검색
		boolean result = false;
		String sql = "select * from airline where 이름 = '" + keyword + "'";	//키워드인 항공사 이름을 입력하여 검색함
		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			for (int i = model1.getRowCount() - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			while (rs.next()) {
				String[] row = new String[5];
				row[0] = rs.getString("항공사ID");
				row[1] = rs.getString("이름");
				row[2] = rs.getString("주소");
				row[3] = rs.getString("전화번호");
//			    row[4] = rs.getString("country");
				model1.addRow(row);
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public void clear() {
		txt_id.setText("");
		txt_name.setText("");
		txt_addr.setText("");
		txt_tel.setText("");
//		txt_ctr.setText("");
		txt_id.requestFocus();
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
				JOptionPane.showMessageDialog(getParent(), txt_name.getText() + " 등록성공");
				getListAll();
				clear();
				table.updateUI();
			} else {
				JOptionPane.showMessageDialog(getParent(), txt_name.getText() + " 등록실패");
			}
		}
		// 수정버튼
		if (obj.equals(bt_update)) {
			if (JOptionPane.showConfirmDialog(getParent(), txt_name.getText() + " 의 정보를 수정 하시겠습니까?") == 0) {
				int result = update();
				if (result != 0) {
					getListAll();
					clear();
					table.updateUI();
				}
			}
		}
		if (obj.equals(bt_delete)) {
			if (JOptionPane.showConfirmDialog(getParent(), txt_name.getText() + " 의 정보를 삭제 하시겠습니까?") == 0) {

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
