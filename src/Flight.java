import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Flight extends JPanel implements ActionListener {
	JLabel m_label, l_�װ����ȣ, l_��¥, l_������, l_�ⱹ����,l_�Ա�����,l_��߽ð�,l_�����ð�, l_������ü��ȣ,l_���,l_�װ���ID,l_keyword;
	JTextField txt_�װ����ȣ, txt_��¥, txt_������, txt_�ⱹ����,txt_�Ա�����, txt_��߽ð�,txt_�����ð�, txt_������ü��ȣ,txt_���,txt_�װ���ID,txt_keyword1,txt_keyword2;
	JTable table;
	JScrollPane scroll;
	JButton bt_insert, bt_update, bt_delete, bt_select, bt_getListAll, bt_exit;
	JPanel south, north, center, p1, p2, p3, p4;

	private String colName1[] = {"�װ����ȣ","��¥","������", "�װ���ID","�ⱹ����","�Ա�����","��߽ð�","�����ð�", "������ü��ȣ","���"};
	private DefaultTableModel model1 = new DefaultTableModel(colName1, 0) {
		// Jtable ���� ���� �ȵǰ�
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
					closeDB(); //��������â�� ����ɶ� DB������
					System.exit(0);
				}
			});
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			pack();
		}
	}
	
	public Flight() {
		m_label = new JLabel("�װ��� ����");
		l_�װ����ȣ = new JLabel("�װ����ȣ");
		l_��¥ = new JLabel("��¥");
		l_������ = new JLabel("������");
		l_�ⱹ���� = new JLabel("�ⱹ����");
		l_�Ա����� = new JLabel("�Ա�����");
		l_��߽ð� = new JLabel("��߽ð�");
		l_�����ð� = new JLabel("�����ð�");
		l_������ü��ȣ = new JLabel("������ü��ȣ");
		l_��� = new JLabel("���");
		l_�װ���ID = new JLabel("�װ���ID");
		l_keyword = new JLabel("�װ��� ��ȣ�� ��� ��¥�� �Է��Ͽ� ��ȸ�Ͻÿ�.");
		
		txt_�װ����ȣ = new JTextField(10);
		txt_��¥ = new JTextField(10);
		txt_������ = new JTextField(10);
		txt_�ⱹ���� = new JTextField(10);
		txt_�Ա����� = new JTextField(10);
		txt_��߽ð� = new JTextField(8);
		txt_�����ð� = new JTextField(8);
		txt_������ü��ȣ = new JTextField(20);
		txt_��� = new JTextField(8);
		txt_�װ���ID = new JTextField(5);
		txt_keyword1 = new JTextField(10); 
		txt_keyword2 = new JTextField(10); 
		
		bt_insert = new JButton("���");
		bt_update = new JButton("����");
		bt_delete = new JButton("����");
		bt_select = new JButton("��ȸ");
		bt_getListAll = new JButton("��ü��ȸ");
		bt_exit = new JButton("����");
		table = new JTable(model1);
		
		table.getTableHeader().setReorderingAllowed(false); // �÷��� �̵� �Ұ�
        table.getTableHeader().setResizingAllowed(false); // �÷� ũ�� ���� �Ұ�
        
		scroll = new JScrollPane(table);
		south = new JPanel();
		north = new JPanel();
		center = new JPanel();
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		p4 = new JPanel();
//		setTitle("�װ��� ����");

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
		
		p2.add(l_�װ����ȣ);
		p2.add(txt_�װ����ȣ);
		p2.add(l_��¥);
		p2.add(txt_��¥);
		p2.add(l_������);
		p2.add(txt_������);
		p2.add(l_�ⱹ����);
		p2.add(txt_�ⱹ����);
		p2.add(l_�Ա�����);
		p2.add(txt_�Ա�����);
		
		p3.add(l_��߽ð�);
		p3.add(txt_��߽ð�);
		p3.add(l_�����ð�);
		p3.add(txt_�����ð�);
		p3.add(l_������ü��ȣ);
		p3.add(txt_������ü��ȣ);
		p3.add(l_���);
		p3.add(txt_���);
		p3.add(l_�װ���ID);
		p3.add(txt_�װ���ID);

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
	

		// ��ư�� �׼� ������ �޾��ֱ�
		bt_insert.addActionListener(this);
		bt_update.addActionListener(this);
		bt_delete.addActionListener(this);
		bt_select.addActionListener(this);
		bt_getListAll.addActionListener(this);
		bt_exit.addActionListener(this);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { //���콺�� Ŭ���Ǿ�����
				selRow = table.getSelectedRow();
				System.out.println(selRow);
				String �װ����ȣ = (String) table.getValueAt(selRow, 0);
				String ��¥ = (String) table.getValueAt(selRow, 1);
				String ������ = (String) table.getValueAt(selRow, 2);
				String �װ���ID = (String) table.getValueAt(selRow, 3);
				String �ⱹ���� = (String) table.getValueAt(selRow, 4);
				String �Ա����� = (String) table.getValueAt(selRow, 5);
				String ��߽ð� = (String) table.getValueAt(selRow, 6);
				String �����ð� = (String) table.getValueAt(selRow, 7);
				String ������ü��ȣ = (String) table.getValueAt(selRow, 8);
				String ��� = (String) table.getValueAt(selRow, 9);
				
				
				txt_�װ����ȣ.setText(�װ����ȣ);
				txt_��¥.setText(��¥);
				txt_������.setText(������);
				txt_�װ���ID.setText(�װ���ID);
				txt_�ⱹ����.setText(�ⱹ����);
				txt_�Ա�����.setText(�Ա�����);
				txt_��߽ð�.setText(��߽ð�);
				txt_�����ð�.setText(�����ð�);
				txt_������ü��ȣ.setText(������ü��ȣ);
				txt_���.setText(���);
				
			}
		});

//		this.addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent e) {
//				closeDB(); //��������â�� ����ɶ� DB������
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
		//��ü ������ ��ȸ 
		String sql = "select * from flight order by ��¥, �ⱹ����, �Ա�����; ";
		try {
			for(int i= model1.getRowCount()-1; i>=0; i--) {
			     model1.removeRow(i);
			    }
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); //select
			Vector list = new Vector();
			while (rs.next()) {
				String[] row = new String[15];
			    row[0] = rs.getString("�װ����ȣ");
			    row[1] = rs.getString("��¥");
			    row[2] = rs.getString("������");
			    row[3] = rs.getString("�װ���ID");
			    row[4] = rs.getString("�ⱹ����");
			    row[5] = rs.getString("�Ա�����");
			    row[6] = rs.getString("��߽ð�");
			    row[7] = rs.getString("�����ð�");
			    row[8] = rs.getString("������ü��ȣ");
			    row[9] = rs.getString("���");
			    
			  
			    model1.addRow(row);
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public int insert() {
		//������ �߰�
		int result = 0;
		sql = "Insert into flight ";
		sql = sql + "Value(?,?,?,?,?,?,?,?,?,?)";
		
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_�װ����ȣ.getText());
			pstmt.setString(2, txt_��¥.getText());
			pstmt.setString(3, txt_������.getText());
			pstmt.setString(4, txt_�װ���ID.getText());
			pstmt.setString(5, txt_�ⱹ����.getText());
			pstmt.setString(6, txt_�Ա�����.getText());
			pstmt.setString(7, txt_��߽ð�.getText());
			pstmt.setString(8, txt_�����ð�.getText());
			pstmt.setString(9, txt_������ü��ȣ.getText());
			pstmt.setString(10, txt_���.getText());
			result = pstmt.executeUpdate(); 

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int delete() {
		//������ ����
		int result = 0;
		String sql = "Delete from flight where �װ����ȣ=? and ��¥=? ";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_�װ����ȣ.getText());
			pstmt.setString(2, txt_��¥.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int update() {
		//������ ����
		int result = 0;
		String sql = "Update flight set ������=?, �װ���ID=?, �ⱹ����=?, �Ա�����=?, ��߽ð�=?, �����ð�=?, ������ü��ȣ=?, ���=?where �װ����ȣ=? and ��¥=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_������.getText());
			pstmt.setString(2, txt_�װ���ID.getText());
			pstmt.setString(3, txt_�ⱹ����.getText());
			pstmt.setString(4, txt_�Ա�����.getText());
			pstmt.setString(5, txt_��߽ð�.getText());
			pstmt.setString(6, txt_�����ð�.getText());
			pstmt.setString(7, txt_������ü��ȣ.getText());
			pstmt.setString(8, txt_���.getText());
			pstmt.setString(9, txt_�װ����ȣ.getText());
			pstmt.setString(10, txt_��¥.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public boolean select(String keyword1,String keyword2) {
		//������ �˻�
		boolean result = false;
		String sql = "select * from flight where �װ����ȣ = '" + keyword1 + "' and ��¥= '"+keyword2 + "'";
		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			for(int i= model1.getRowCount()-1; i>=0; i--) {
			     model1.removeRow(i);
			    }
			while (rs.next()) {
				String[] row = new String[10];
				row[0] = rs.getString("�װ����ȣ");
			    row[1] = rs.getString("��¥");
			    row[2] = rs.getString("������");
			    row[3] = rs.getString("�װ���ID");
			    row[4] = rs.getString("�ⱹ����");
			    row[5] = rs.getString("�Ա�����");
			    row[6] = rs.getString("��߽ð�");
			    row[7] = rs.getString("�����ð�");
			    row[8] = rs.getString("������ü��ȣ");
			    row[9] = rs.getString("���");
			    
			    model1.addRow(row);
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public void clear() {
		
		txt_�װ����ȣ.setText("");
		txt_��¥.setText("");
		txt_������.setText("");
		txt_�ⱹ����.setText("");
		txt_�Ա�����.setText("");
		txt_��߽ð�.setText("");
		txt_�����ð�.setText("");
		txt_������ü��ȣ.setText("");
		txt_���.setText("");
		txt_�װ���ID.setText("");
		txt_�װ����ȣ.requestFocus();
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
				JOptionPane.showMessageDialog(getParent(), txt_��¥.getText()+" �� ����ϴ� �װ��� ��ȣ"+ txt_�װ����ȣ.getText() + " ��ϼ���");
				getListAll();
				clear();
				table.updateUI();
			} else {
				JOptionPane.showMessageDialog(getParent(), txt_��¥.getText()+" �� ����ϴ� �װ��� ��ȣ"+ txt_�װ����ȣ.getText() + " ��Ͻ���");
			}
		}
		// ������ư
		if (obj.equals(bt_update)) {
			if (JOptionPane.showConfirmDialog(getParent(),
					txt_��¥.getText()+" �� ����ϴ� �װ��� ��ȣ"+ txt_�װ����ȣ.getText() + " �� ������ ���� �Ͻðڽ��ϱ�?") == 0) {
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
					txt_��¥.getText()+" �� ����ϴ� �װ��� ��ȣ"+ txt_�װ����ȣ.getText() + " �� ������ ���� �Ͻðڽ��ϱ�?") == 0) { //�� ������ ������. �� ������ �ƴ϶� �˻��� �װ���ID�� �����ǵ��� �����ؾ���.
				int result = delete();
				if (result != 0) {
					getListAll();
					clear();
					table.updateUI();
				}
			}
		}
		// ��ȸ��ư
		if (obj.equals(bt_select)) {
			select(txt_keyword1.getText(),txt_keyword2.getText());
		}
		// ��ü ��ȸ��ư
		if (obj.equals(bt_getListAll)) {
			getListAll();
		}
		// �����ư
		if (obj.equals(bt_exit)) {
			closeDB();
			System.exit(0);
		} // end exit��ư action����
	}// end action�̺�Ʈ ����
}

