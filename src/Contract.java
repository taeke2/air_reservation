import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Contract extends JPanel implements ActionListener {
	JLabel m_label, l_국제코드, l_항공사ID, l_계약기간, l_keyword;
	JTextField txt_국제코드, txt_항공사ID, txt_계약기간, txt_keyword1, txt_keyword2;
	JTable table;
	JScrollPane scroll;
	JButton bt_insert, bt_update, bt_delete, bt_select, bt_getListAll, bt_exit;
	JPanel south, north, center, p1, p2, p3, p4;

	private String colName1[] = { "국제코드", "항공사ID", "계약기간" };
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

	public Contract() {
		m_label = new JLabel("계약 관리");
		l_국제코드 = new JLabel("국제코드 ");
		l_항공사ID = new JLabel("항공사ID");
		l_계약기간 = new JLabel("계약기간");
		l_keyword = new JLabel("국제코드와 항공사ID를 입력하여 조회하시오.");

		txt_국제코드 = new JTextField(8);
		txt_항공사ID = new JTextField(8);
		txt_계약기간 = new JTextField(8);

		txt_keyword1 = new JTextField(5);
		txt_keyword2 = new JTextField(5);

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

//		setTitle("계약 관리");

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

		p1.add(m_label);

		p2.add(l_국제코드);
		p2.add(txt_국제코드);
		p2.add(l_항공사ID);
		p2.add(txt_항공사ID);
		p2.add(l_계약기간);
		p2.add(txt_계약기간);

		p3.add(l_keyword);
		p3.add(txt_keyword1);
		p3.add(txt_keyword2);
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
				String 국제코드 = (String) table.getValueAt(selRow, 0);
				String 항공사ID = (String) table.getValueAt(selRow, 1);
				String 계약기간 = (String) table.getValueAt(selRow, 2);

				txt_국제코드.setText(국제코드);
				txt_항공사ID.setText(항공사ID);
				txt_계약기간.setText(계약기간);
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
		String sql = "select * from contract order by 계약기간; ";
		try {
			for (int i = model1.getRowCount() - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); // select
			Vector list = new Vector();
			while (rs.next()) {
				String[] row = new String[5];
				row[0] = rs.getString("국제코드");
				row[1] = rs.getString("항공사ID");
				row[2] = rs.getString("계약기간");

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
		sql = "Insert into contract ";
		sql = sql + "Value(?,?,?)";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_국제코드.getText());
			pstmt.setString(2, txt_항공사ID.getText());
			pstmt.setString(3, txt_계약기간.getText());
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int delete() {
		// 데이터 삭제
		int result = 0;
		String sql = "Delete from contract where 국제코드=? and 항공사ID=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_국제코드.getText());
			pstmt.setString(2, txt_항공사ID.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int update() {
		// 데이터 수정
		int result = 0;
		String sql = "Update contract set 계약기간=? where 국제코드=? and 항공사ID=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_계약기간.getText());
			pstmt.setString(2, txt_국제코드.getText());
			pstmt.setString(3, txt_항공사ID.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public boolean select(String keyword1, String keyword2) {
		// 데이터 검색
		boolean result = false;
		String sql = "select * from contract where 국제코드 = '" + keyword1 + "' and 항공사ID= '" + keyword2 + "'";
		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			for (int i = model1.getRowCount() - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			while (rs.next()) {
				String[] row = new String[5];
				row[0] = rs.getString("국제코드");
				row[1] = rs.getString("항공사ID");
				row[2] = rs.getString("계약기간");
				model1.addRow(row);
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public void clear() {
		txt_국제코드.setText("");
		txt_항공사ID.setText("");
		txt_계약기간.setText("");
		txt_국제코드.requestFocus();
		txt_항공사ID.requestFocus();
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
				JOptionPane.showMessageDialog(getParent(),
						txt_국제코드.getText() + "와" + txt_항공사ID.getText() + "사이의 계약 등록성공");
				getListAll();
				clear();
				table.updateUI();
			} else {
				JOptionPane.showMessageDialog(getParent(),
						txt_국제코드.getText() + "와" + txt_항공사ID.getText() + "사이의 계약 등록실패");
			}
		}
		// 수정버튼
		if (obj.equals(bt_update)) {
			if (JOptionPane.showConfirmDialog(getParent(),
					txt_국제코드.getText() + "와" + txt_항공사ID.getText() + "사이의 계약 정보를 수정 하시겠습니까?") == 0) {
				int result = update();
				if (result != 0) {
					getListAll();
					clear();
					table.updateUI();
				}
			}
		}
		if (obj.equals(bt_delete)) {
			if (JOptionPane.showConfirmDialog(getParent(),
					txt_국제코드.getText() + "와" + txt_항공사ID.getText() + "사이의 계약 정보를 삭제 하시겠습니까?") == 0) {
																										
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
			select(txt_keyword1.getText(), txt_keyword2.getText());
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
