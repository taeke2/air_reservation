import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Flight extends JPanel implements ActionListener {
	JLabel m_label, l_항공기번호, l_날짜, l_스케쥴, l_출국공항,l_입국공항,l_출발시간,l_도착시간, l_비행기기체번호,l_요금,l_항공사ID,l_keyword;
	JTextField txt_항공기번호, txt_날짜, txt_스케쥴, txt_출국공항,txt_입국공항, txt_출발시간,txt_도착시간, txt_비행기기체번호,txt_요금,txt_항공사ID,txt_keyword1,txt_keyword2;
	JTable table;
	JScrollPane scroll;
	JButton bt_insert, bt_update, bt_delete, bt_select, bt_getListAll, bt_exit;
	JPanel south, north, center, p1, p2, p3, p4;

	private String colName1[] = {"항공기번호","날짜","스케쥴", "항공사ID","출국공항","입국공항","출발시간","도착시간", "비행기기체번호","요금"};
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
	
	public Flight() {
		m_label = new JLabel("항공편 관리");
		l_항공기번호 = new JLabel("항공기번호");
		l_날짜 = new JLabel("날짜");
		l_스케쥴 = new JLabel("스케쥴");
		l_출국공항 = new JLabel("출국공항");
		l_입국공항 = new JLabel("입국공항");
		l_출발시간 = new JLabel("출발시간");
		l_도착시간 = new JLabel("도착시간");
		l_비행기기체번호 = new JLabel("비행기기체번호");
		l_요금 = new JLabel("요금");
		l_항공사ID = new JLabel("항공사ID");
		l_keyword = new JLabel("항공편 번호와 출발 날짜를 입력하여 조회하시오.");
		
		txt_항공기번호 = new JTextField(10);
		txt_날짜 = new JTextField(10);
		txt_스케쥴 = new JTextField(10);
		txt_출국공항 = new JTextField(10);
		txt_입국공항 = new JTextField(10);
		txt_출발시간 = new JTextField(8);
		txt_도착시간 = new JTextField(8);
		txt_비행기기체번호 = new JTextField(20);
		txt_요금 = new JTextField(8);
		txt_항공사ID = new JTextField(5);
		txt_keyword1 = new JTextField(10); 
		txt_keyword2 = new JTextField(10); 
		
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
//		setTitle("항공편 관리");

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
		
		p2.add(l_항공기번호);
		p2.add(txt_항공기번호);
		p2.add(l_날짜);
		p2.add(txt_날짜);
		p2.add(l_스케쥴);
		p2.add(txt_스케쥴);
		p2.add(l_출국공항);
		p2.add(txt_출국공항);
		p2.add(l_입국공항);
		p2.add(txt_입국공항);
		
		p3.add(l_출발시간);
		p3.add(txt_출발시간);
		p3.add(l_도착시간);
		p3.add(txt_도착시간);
		p3.add(l_비행기기체번호);
		p3.add(txt_비행기기체번호);
		p3.add(l_요금);
		p3.add(txt_요금);
		p3.add(l_항공사ID);
		p3.add(txt_항공사ID);

		p4.add(l_keyword);
		p4.add(txt_keyword1);
		p4.add(txt_keyword2);
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
				String 항공기번호 = (String) table.getValueAt(selRow, 0);
				String 날짜 = (String) table.getValueAt(selRow, 1);
				String 스케쥴 = (String) table.getValueAt(selRow, 2);
				String 항공사ID = (String) table.getValueAt(selRow, 3);
				String 출국공항 = (String) table.getValueAt(selRow, 4);
				String 입국공항 = (String) table.getValueAt(selRow, 5);
				String 출발시간 = (String) table.getValueAt(selRow, 6);
				String 도착시간 = (String) table.getValueAt(selRow, 7);
				String 비행기기체번호 = (String) table.getValueAt(selRow, 8);
				String 요금 = (String) table.getValueAt(selRow, 9);
				
				
				txt_항공기번호.setText(항공기번호);
				txt_날짜.setText(날짜);
				txt_스케쥴.setText(스케쥴);
				txt_항공사ID.setText(항공사ID);
				txt_출국공항.setText(출국공항);
				txt_입국공항.setText(입국공항);
				txt_출발시간.setText(출발시간);
				txt_도착시간.setText(도착시간);
				txt_비행기기체번호.setText(비행기기체번호);
				txt_요금.setText(요금);
				
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
		String sql = "select * from flight order by 날짜, 출국공항, 입국공항; ";
		try {
			for(int i= model1.getRowCount()-1; i>=0; i--) {
			     model1.removeRow(i);
			    }
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); //select
			Vector list = new Vector();
			while (rs.next()) {
				String[] row = new String[15];
			    row[0] = rs.getString("항공기번호");
			    row[1] = rs.getString("날짜");
			    row[2] = rs.getString("스케쥴");
			    row[3] = rs.getString("항공사ID");
			    row[4] = rs.getString("출국공항");
			    row[5] = rs.getString("입국공항");
			    row[6] = rs.getString("출발시간");
			    row[7] = rs.getString("도착시간");
			    row[8] = rs.getString("비행기기체번호");
			    row[9] = rs.getString("요금");
			    
			  
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
		sql = "Insert into flight ";
		sql = sql + "Value(?,?,?,?,?,?,?,?,?,?)";
		
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_항공기번호.getText());
			pstmt.setString(2, txt_날짜.getText());
			pstmt.setString(3, txt_스케쥴.getText());
			pstmt.setString(4, txt_항공사ID.getText());
			pstmt.setString(5, txt_출국공항.getText());
			pstmt.setString(6, txt_입국공항.getText());
			pstmt.setString(7, txt_출발시간.getText());
			pstmt.setString(8, txt_도착시간.getText());
			pstmt.setString(9, txt_비행기기체번호.getText());
			pstmt.setString(10, txt_요금.getText());
			result = pstmt.executeUpdate(); 

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int delete() {
		//데이터 삭제
		int result = 0;
		String sql = "Delete from flight where 항공기번호=? and 날짜=? ";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_항공기번호.getText());
			pstmt.setString(2, txt_날짜.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int update() {
		//데이터 수정
		int result = 0;
		String sql = "Update flight set 스케쥴=?, 항공사ID=?, 출국공항=?, 입국공항=?, 출발시간=?, 도착시간=?, 비행기기체번호=?, 요금=?where 항공기번호=? and 날짜=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_스케쥴.getText());
			pstmt.setString(2, txt_항공사ID.getText());
			pstmt.setString(3, txt_출국공항.getText());
			pstmt.setString(4, txt_입국공항.getText());
			pstmt.setString(5, txt_출발시간.getText());
			pstmt.setString(6, txt_도착시간.getText());
			pstmt.setString(7, txt_비행기기체번호.getText());
			pstmt.setString(8, txt_요금.getText());
			pstmt.setString(9, txt_항공기번호.getText());
			pstmt.setString(10, txt_날짜.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public boolean select(String keyword1,String keyword2) {
		//데이터 검색
		boolean result = false;
		String sql = "select * from flight where 항공기번호 = '" + keyword1 + "' and 날짜= '"+keyword2 + "'";
		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			for(int i= model1.getRowCount()-1; i>=0; i--) {
			     model1.removeRow(i);
			    }
			while (rs.next()) {
				String[] row = new String[10];
				row[0] = rs.getString("항공기번호");
			    row[1] = rs.getString("날짜");
			    row[2] = rs.getString("스케쥴");
			    row[3] = rs.getString("항공사ID");
			    row[4] = rs.getString("출국공항");
			    row[5] = rs.getString("입국공항");
			    row[6] = rs.getString("출발시간");
			    row[7] = rs.getString("도착시간");
			    row[8] = rs.getString("비행기기체번호");
			    row[9] = rs.getString("요금");
			    
			    model1.addRow(row);
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public void clear() {
		
		txt_항공기번호.setText("");
		txt_날짜.setText("");
		txt_스케쥴.setText("");
		txt_출국공항.setText("");
		txt_입국공항.setText("");
		txt_출발시간.setText("");
		txt_도착시간.setText("");
		txt_비행기기체번호.setText("");
		txt_요금.setText("");
		txt_항공사ID.setText("");
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
				JOptionPane.showMessageDialog(getParent(), txt_날짜.getText()+" 에 출발하는 항공편 변호"+ txt_항공기번호.getText() + " 등록성공");
				getListAll();
				clear();
				table.updateUI();
			} else {
				JOptionPane.showMessageDialog(getParent(), txt_날짜.getText()+" 에 출발하는 항공편 변호"+ txt_항공기번호.getText() + " 등록실패");
			}
		}
		// 수정버튼
		if (obj.equals(bt_update)) {
			if (JOptionPane.showConfirmDialog(getParent(),
					txt_날짜.getText()+" 에 출발하는 항공편 변호"+ txt_항공기번호.getText() + " 의 정보를 수정 하시겠습니까?") == 0) {
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
					txt_날짜.getText()+" 에 출발하는 항공편 변호"+ txt_항공기번호.getText() + " 의 정보를 삭제 하시겠습니까?") == 0) { //맨 윗줄이 삭제됨. 맨 윗줄이 아니라 검색한 항공사ID가 삭제되도록 수정해야함.
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
			select(txt_keyword1.getText(),txt_keyword2.getText());
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

