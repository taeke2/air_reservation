import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Cus_ticket extends JPanel implements ActionListener {
	JLabel m_label, l_항공기번호, l_날짜, l_스케쥴, l_출국공항, l_입국공항, l_출발시간, l_도착시간, l_비행기기체번호, l_요금, l_항공사ID, l_keyword,
			l_남은일등석좌석수, l_남은비즈니스좌석수, l_남은이코노미좌석수;
	JTextField txt_항공기번호, txt_날짜, txt_스케쥴, txt_출국공항, txt_입국공항, txt_출발시간, txt_도착시간, txt_비행기기체번호, txt_요금, txt_항공사ID,
			txt_keyword1, txt_keyword2, txt_남은일등석좌석수, txt_남은이코노미좌석수, txt_남은비즈니스좌석수;
	JTable table;
	JScrollPane scroll;
	JButton bt_select, bt_getListAll, bt_exit, bt_예약; // 버튼
	JPanel south, north, center, p1, p2, p3, p4;
	JComboBox strCombo1 = new JComboBox();
	JComboBox strCombo2 = new JComboBox();
	JComboBox strCombo3 = new JComboBox();

	private String colName1[] = { "항공기번호", "출국공항", "입국공항", "날짜", "출발시간", "도착시간", "남은일등석좌석수", "남은비즈니스좌석수", "남은이코노미좌석수" };
	private DefaultTableModel model1 = new DefaultTableModel(colName1, 0) {
		// Jtable 내용 편집 안되게
		public boolean isCellEditable(int i, int c) {
			return false;
		}
	};
	
	String driver = "com.mysql.cj.jdbc.Driver";
	Connection con;
	PreparedStatement pstmt;
	PreparedStatement pstmt1, pstmt2, pstmt3;
	Statement stmt;
	ResultSet rs;
	ResultSet rs1, rs2, rs3;
	String sql, sql1, start, end, date, Cusname, Cuspay, Cuslevel;
	int selRow;
	public static int best, normal, worst;

	public Cus_ticket() {
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
		l_남은일등석좌석수 = new JLabel("남은일등석좌석수");
		l_남은비즈니스좌석수 = new JLabel("남은비즈니스좌석수");
		l_남은이코노미좌석수 = new JLabel("남은이코노미좌석수");
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
		txt_남은일등석좌석수 = new JTextField(10);
		txt_남은비즈니스좌석수 = new JTextField(10);
		txt_남은이코노미좌석수 = new JTextField(10);
		txt_keyword1 = new JTextField(10);
		txt_keyword2 = new JTextField(10);

		bt_select = new JButton("조회");
		bt_getListAll = new JButton("전체조회");
		bt_exit = new JButton("종료");
		bt_예약 = new JButton("예약");
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
		p2.add(l_출국공항);
		p2.add(strCombo1);
		p2.add(l_입국공항);
		p2.add(strCombo2);
		p2.add(l_날짜);
		p2.add(strCombo3);

		p3.add(l_남은일등석좌석수);
		p3.add(txt_남은일등석좌석수);
		p3.add(l_남은비즈니스좌석수);
		p3.add(txt_남은비즈니스좌석수);
		p3.add(l_남은이코노미좌석수);
		p3.add(txt_남은이코노미좌석수);

		p4.add(bt_select);
		p4.add(bt_getListAll);

		center.setLayout(new GridLayout(1, 1));
		center.add(scroll);
		south.setLayout(new GridLayout(1, 4));
		south.add(bt_예약);
		south.add(bt_exit);

		// 버튼에 액션 리스너 달아주기
		bt_select.addActionListener(this);
		bt_getListAll.addActionListener(this);
		bt_예약.addActionListener(this);
		bt_exit.addActionListener(this);

		// 마우스가 클릭되었을때
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { 
				selRow = table.getSelectedRow();
				System.out.println(selRow);
				String 항공기번호 = (String) table.getValueAt(selRow, 0);
				String 출국공항 = (String) table.getValueAt(selRow, 1);
				String 입국공항 = (String) table.getValueAt(selRow, 2);
				String 날짜 = (String) table.getValueAt(selRow, 3);
				String 출발시간 = (String) table.getValueAt(selRow, 4);
				String 도착시간 = (String) table.getValueAt(selRow, 5);
				String 남은일등석좌석수 = (String) table.getValueAt(selRow, 6);
				String 남은비즈니스좌석수 = (String) table.getValueAt(selRow, 7);
				String 남은이코노미좌석수 = (String) table.getValueAt(selRow, 8);

				txt_항공기번호.setText(항공기번호);
				strCombo1.setSelectedItem(출국공항);
				strCombo2.setSelectedItem(입국공항);
				strCombo3.setSelectedItem(날짜);
				txt_남은일등석좌석수.setText(남은일등석좌석수);
				txt_남은비즈니스좌석수.setText(남은비즈니스좌석수);
				txt_남은이코노미좌석수.setText(남은이코노미좌석수);
			}
		});

		//////////////////////////////////////////////////////////////////////////////////////////////원하는 항공편을  찾기위한 콤보박스
		strCombo1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	//출국공항
				// TODO Auto-generated method stub
				JComboBox combo = (JComboBox) e.getSource();
				int index = combo.getSelectedIndex();
				start = (String) combo.getSelectedItem();

			}
		});

		strCombo2.addActionListener(new ActionListener() {	//도착공항
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JComboBox combo = (JComboBox) e.getSource();
				int index = combo.getSelectedIndex();
				end = (String) combo.getSelectedItem();

			}
		});

		strCombo3.addActionListener(new ActionListener() {	//날짜
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JComboBox combo = (JComboBox) e.getSource();
				int index = combo.getSelectedIndex();
				date = (String) combo.getSelectedItem();

			}
		});
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
			System.out.println("DB 연결 완료");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return isConnect;
	}

	public void getListAll() {	
		// 항공편 데이터 전체 조회
		String sql = "select * from aircraft A, flight F where A.비행기기체번호 = F.비행기기체번호 order by 날짜,출국공항,입국공항;";			// 항공기와 항공편 테이블을 조인하여 전체 예약가능한 항공편 데이터 조회
		String sql1 = "select distinct F.출국공항 from aircraft A, flight F where A.비행기기체번호 = F.비행기기체번호 order by 출국공항; ";	// 출국공항의 속성값들을 중복값을 없애고 조회
		String sql2 = "select distinct F.입국공항 from aircraft A, flight F where A.비행기기체번호 = F.비행기기체번호 order by 입국공항; ";	// 입국공항의 속성값들을 중복값을 없애고 조회
		String sql3 = "select distinct F.날짜 from aircraft A, flight F where A.비행기기체번호 = F.비행기기체번호 order by 날짜; ";
		try {
			for (int i = model1.getRowCount() - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			pstmt = con.prepareStatement(sql);
			pstmt1 = con.prepareStatement(sql1);
			pstmt2 = con.prepareStatement(sql2);
			pstmt3 = con.prepareStatement(sql3);
			
			// select
			rs = pstmt.executeQuery(); 
			rs1 = pstmt1.executeQuery();
			rs2 = pstmt2.executeQuery();
			rs3 = pstmt3.executeQuery();
			
			
			while (rs.next()) {
				String[] row = new String[10];
				row[0] = rs.getString("F.항공기번호");		//항공기와 항공편을 조인한 데이터중에서 원하는 속성값을 받아온다.
				row[1] = rs.getString("F.출국공항");
				row[2] = rs.getString("F.입국공항");
				row[3] = rs.getString("F.날짜");
				row[4] = rs.getString("F.출발시간");
				row[5] = rs.getString("F.도착시간");
				row[6] = rs.getString("A.일등석좌석수");
				row[7] = rs.getString("A.비즈니스좌석수");
				row[8] = rs.getString("A.이코노미좌석수");

				model1.addRow(row);
			}
			
			while (rs1.next()) {
				strCombo1.addItem(rs1.getString("출국공항"));	//출국공항의 속성값들을 중복값을 없애고 콤보박스에 넣는다
			}
			while (rs2.next()) {
				strCombo2.addItem(rs2.getString("입국공항"));	//입국공항의 속성값들을 중복값을 없애고 콤보박스에 넣는다
			}
			while (rs3.next()) {
				strCombo3.addItem(rs3.getString("날짜"));		//날짜 속성값들을 중복값을 없애고 콤보박스에 넣는다
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public boolean select(String start, String end) {		//원하는 항공편의 출국공항,입국공항,날짜를 선택하여 조회한다.
		// 데이터 검색
		boolean result = false;
		String sql = "select * from flight where 출국공항 = '" + start + "' and 입국공항= '" + end + "' and 날짜 ='" + date + "'";
		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			for (int i = model1.getRowCount() - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			while (rs.next()) {
				String[] row = new String[10];
				row[0] = rs.getString("항공기번호");
				row[1] = rs.getString("날짜");
				row[2] = rs.getString("스케쥴");
				row[3] = rs.getString("출국공항");
				row[4] = rs.getString("입국공항");
				row[5] = rs.getString("출발시간");
				row[6] = rs.getString("도착시간");
				row[7] = rs.getString("비행기기체번호");
				row[8] = rs.getString("요금");
				row[9] = rs.getString("항공사ID");
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

	public int reservation() { ////////// 예약정보 기입 후 예약테이블에 정보 넣기
		// 데이터 추가
		Temper tp = new Temper();
		int result = 0;
		best = Integer.parseInt(txt_남은일등석좌석수.getText().toString());		//남은 일등석 좌석수
		normal = Integer.parseInt(txt_남은비즈니스좌석수.getText().toString());	//남은 비즈니스석 좌석수
		worst = Integer.parseInt(txt_남은이코노미좌석수.getText().toString());	//남은 이코노미 좌석수
		
		//예약할 때 선택한 좌석 등급에 때라 항공편의 남은 좌석수 차감
		if (Cuslevel.equals("일등석")) {
			best = best - 1;
		} else if (Cuslevel.equals("비즈니스석")) {
			normal = normal - 1;
		} else if (Cuslevel.equals("이코노미석")) {
			worst = worst - 1;
		}

		// 고객이 예약한 정보들을 관리자모드의 예약관리 테이블에 데이터 삽입.
		sql = "insert into reservation value(?,?,?,?,?,?,?)";	
		//예약할 때 선택한 좌석 등급에 때라 항공편의 남은 좌석수 차감
		sql1 = "update aircraft A, flight F set A.일등석좌석수=?, A.비즈니스좌석수=?, A.이코노미좌석수=? where A.비행기기체번호 = F.비행기기체번호 and F.항공기번호=?";
											
		try {
			
			pstmt = con.prepareStatement(sql);
			pstmt1 = con.prepareStatement(sql1);
			pstmt.setString(1, start + "출발 " + end + "도착");
			pstmt.setString(2, Cusname);
			pstmt.setString(3, "Ok");
			pstmt.setString(4, Cuspay);
			pstmt.setString(5, Cuslevel);
			pstmt.setString(6, txt_항공기번호.getText().toString());
			pstmt.setString(7, tp.id);
			pstmt.executeUpdate();

			pstmt1.setInt(1, best);		//남은 일등석
			pstmt1.setInt(2, normal);	//남은 비즈니스좌석수
			pstmt1.setInt(3, worst);	//남은 이코노미좌석수
			pstmt1.setString(4, txt_항공기번호.getText().toString());
			result = pstmt1.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	class reservation extends JFrame {////////////////////////////////////// 고객의 예약정보 기입 (고객이름, 즉시결제, 좌석등급)
		reservation() {
			setTitle("예약하기");
			String[] pay = { "선택없음", "O", "X" };
			String[] level = { "선택없음", "일등석", "비즈니스석", "이코노미석" };
			int result = 1;

			JPanel cp = new JPanel();
			setContentPane(cp);

			JLabel l_name = new JLabel("고객이름");
			JLabel l_pay = new JLabel("즉시결제");
			JLabel l_level = new JLabel("좌석등급");
			JButton btn_ok = new JButton("확인");
			JComboBox CusCombo1 = new JComboBox(pay);
			JComboBox CusCombo2 = new JComboBox(level);
			JTextField txt_name = new JTextField(5);

			CusCombo1.addActionListener(new ActionListener() {	//즉시결제 유무

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JComboBox combo = (JComboBox) e.getSource();
					int index = combo.getSelectedIndex();
					Cuspay = pay[index];

				}
			});
			CusCombo2.addActionListener(new ActionListener() {	// 좌석등급 (일등급, 비즈니스, 이코노미)

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JComboBox combo = (JComboBox) e.getSource();
					int index = combo.getSelectedIndex();
					Cuslevel = level[index];

				}
			});

			btn_ok.addActionListener(new ActionListener() { ////////////////// 예약정보 기입 완료
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Cusname = txt_name.getText().toString();
					int result = reservation();
					
					if (result != 0) {

						JOptionPane.showMessageDialog(getParent(),
								date + " [" + start + "출발 " + end + "도착] 하는 티켓 예약 성공");
						setVisible(false);
						getListAll();

						clear();
						table.updateUI();
					} else {
						JOptionPane.showMessageDialog(getParent(),
								date + " [" + start + "출발 " + end + "도착] 하는 티켓 예약 실패");
					}

				}
			});

			cp.add(l_name);
			cp.add(txt_name);
			cp.add(l_pay);
			cp.add(CusCombo1);
			cp.add(l_level);
			cp.add(CusCombo2);
			cp.add(btn_ok);

			setSize(500, 300);
			setVisible(true);

		}
	}

	public void actionPerformed(ActionEvent e) {

		Object obj = e.getSource();
		// 예약버튼
		if (obj.equals(bt_예약)) {
			new reservation();
		}
		
		// 조회버튼
		if (obj.equals(bt_select)) {
			select(start, end);
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
