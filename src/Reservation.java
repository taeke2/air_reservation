import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Reservation extends JPanel implements ActionListener {
	JLabel m_label, l_포함된항공편, l_고객이름, l_예약상황,l_지불상태,l_좌석등급, l_항공기번호,l_고객ID,l_keyword;
	JTextField txt_포함된항공편, txt_고객이름, txt_예약상황, txt_지불상태,txt_좌석등급, txt_항공기번호,txt_고객ID,txt_keyword;
	JTable table;
	JScrollPane scroll;
	JButton bt_insert, bt_update, bt_delete, bt_select, bt_getListAll, bt_exit;
	JPanel south, north, center, p1, p2, p3, p4;

	private String colName1[] = {"포함된항공편","고객이름", "예약상황","지불상태","좌석등급", "항공기번호","고객ID"};
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
					closeDB(); //ㅇ윈도우창이 종료될때 DB도종료
					System.exit(0);
				}
			});
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			pack();
		}
	}

	public Reservation() {
		m_label = new JLabel("예약 관리");
		l_포함된항공편 = new JLabel("포함된항공편");
		l_고객이름 = new JLabel("고객이름");
		l_예약상황 = new JLabel("예약상황");
		l_지불상태 = new JLabel("지불상태");
		l_좌석등급 = new JLabel("좌석등급");
		l_항공기번호 = new JLabel("항공기번호");
		l_고객ID = new JLabel("고객ID");
		l_keyword = new JLabel("예약현황을 입력하여 조회하시오.   ex)뉴욕출발 서울도착 ");
		
		txt_포함된항공편 = new JTextField(20);
		txt_고객이름 = new JTextField(5);
		txt_예약상황 = new JTextField(10);
		txt_지불상태 = new JTextField(5);
		txt_좌석등급 = new JTextField(5);
		txt_항공기번호 = new JTextField(10);
		txt_고객ID = new JTextField(10);
		txt_keyword = new JTextField(20); 
		
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
//		setTitle("예약 관리");

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
		
		p2.add(l_포함된항공편);
		p2.add(txt_포함된항공편);
		p2.add(l_고객이름);
		p2.add(txt_고객이름);
		p2.add(l_예약상황);
		p2.add(txt_예약상황);
		
		p3.add(l_지불상태);
		p3.add(txt_지불상태);
		p3.add(l_좌석등급);
		p3.add(txt_좌석등급);
		p3.add(l_항공기번호);
		p3.add(txt_항공기번호);
		p3.add(l_고객ID);
		p3.add(txt_고객ID);

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
			public void mouseClicked(MouseEvent e) { //마우스가 클릭되었을때
				selRow = table.getSelectedRow();
				System.out.println(selRow);
				String 포함된항공편 = (String) table.getValueAt(selRow, 0);
				String 고객이름 = (String) table.getValueAt(selRow, 1);
				String 예약상황 = (String) table.getValueAt(selRow, 2);
				String 지불상태 = (String) table.getValueAt(selRow, 3);
				String 좌석등급 = (String) table.getValueAt(selRow, 4);
				String 항공기번호 = (String) table.getValueAt(selRow, 5);
				String 고객ID = (String) table.getValueAt(selRow, 6);
				
				txt_포함된항공편.setText(포함된항공편);
				txt_고객이름.setText(고객이름);
				txt_예약상황.setText(예약상황);
				txt_지불상태.setText(지불상태);
				txt_좌석등급.setText(좌석등급);
				txt_항공기번호.setText(항공기번호);
				txt_고객ID.setText(고객ID);
			}
		});

//		this.addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent e) {
//				closeDB(); //ㅇ윈도우창이 종료될때 DB도종료
//				System.exit(0);
//			}
//		});
		new MyPanel();
		connect();
		getListAll();
//		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		pack();
		//setSize(600, 650);
		setVisible(true);
	}

	public boolean connect() {
		boolean isConnect = false;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection("jdbc:mysql://localhost/mydb?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC", "root", "1234");
			isConnect = true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return isConnect;
	}

	public void getListAll() {
		//전체 데이터 조회 
		String sql = "select * from reservation order by 고객이름 ; ";
		try {
			for(int i= model1.getRowCount()-1; i>=0; i--) {
			     model1.removeRow(i);
			    }
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); //select
			Vector list = new Vector();
			while (rs.next()) {
				String[] row = new String[10];
			    row[0] = rs.getString("포함된항공편");
			    row[1] = rs.getString("고객이름");
			    row[2] = rs.getString("예약상황");
			    row[3] = rs.getString("지불상태");
			    row[4] = rs.getString("좌석등급");
			    row[5] = rs.getString("항공기번호");
			    row[6] = rs.getString("고객ID");
			    model1.addRow(row);
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public int insert() {
		//데이터 추가
		int result = 0;
		sql = "Insert into reservation ";
		sql = sql + "Value(?,?,?,?,?,?,?)";
		
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_포함된항공편.getText());
			pstmt.setString(2, txt_고객이름.getText());
			pstmt.setString(3, txt_예약상황.getText());
			pstmt.setString(4, txt_지불상태.getText());
			pstmt.setString(5, txt_좌석등급.getText());
			pstmt.setString(6, txt_항공기번호.getText());
			pstmt.setString(7, txt_고객ID.getText());
			result = pstmt.executeUpdate(); 

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int delete() {
		//데이터 삭제
		int result = 0;
		String sql = "Delete from reservation where 항공기번호=? and 고객ID=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_항공기번호.getText());
			pstmt.setString(2, txt_고객ID.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int update() {
		//데이터 수정
		int result = 0;
		String sql = "Update reservation set 포함된항공편=?,고객이름=?, 예약상황=?, 지불상태=?, 좌석등급=? where 항공기번호=? and 고객ID=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_포함된항공편.getText());
			pstmt.setString(2, txt_고객이름.getText());
			pstmt.setString(3, txt_예약상황.getText());
			pstmt.setString(4, txt_지불상태.getText());
			pstmt.setString(5, txt_좌석등급.getText());
			pstmt.setString(6, txt_항공기번호.getText());
			pstmt.setString(7, txt_고객ID.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public boolean select(String keyword) {
		//데이터 검색
		boolean result = false;
		String sql = "select * from reservation where 포함된항공편 = '" + keyword + "'";	//키워드인 포함된 항공편을 입력하여 검색함
		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			for(int i= model1.getRowCount()-1; i>=0; i--) {
			     model1.removeRow(i);
			    }
			while (rs.next()) {
				String[] row = new String[10];
				row[0] = rs.getString("포함된항공편");
			    row[1] = rs.getString("고객이름");
			    row[2] = rs.getString("예약상황");
			    row[3] = rs.getString("지불상태");
			    row[4] = rs.getString("좌석등급");
			    row[5] = rs.getString("항공기번호");
			    row[6] = rs.getString("고객ID");
			    model1.addRow(row);
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public void clear() {
		
		txt_포함된항공편.setText("");
		txt_고객이름.setText("");
		txt_예약상황.setText("");
		txt_지불상태.setText("");
		txt_좌석등급.setText("");
		txt_항공기번호.setText("");
		txt_고객ID.setText("");
		txt_항공기번호.requestFocus();
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
				JOptionPane.showMessageDialog(getParent(), txt_포함된항공편.getText()+"하는 예약 등록성공");
				getListAll();
				clear();
				table.updateUI();
			} else {
				JOptionPane.showMessageDialog(getParent(), txt_포함된항공편.getText()+"하는 예약 등록실패");
			}
		}
		// 수정버튼
		if (obj.equals(bt_update)) {
			if (JOptionPane.showConfirmDialog(getParent(),
					txt_포함된항공편.getText()+"하는 예약의 정보를 수정 하시겠습니까?") == 0) {
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
					txt_포함된항공편.getText()+"하는 예약의 정보를 삭제 하시겠습니까?") == 0) { //맨 윗줄이 삭제됨. 맨 윗줄이 아니라 검색한 항공사ID가 삭제되도록 수정해야함.
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

