import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Reservation extends JPanel implements ActionListener {
	JLabel m_label, l_���Ե��װ���, l_���̸�, l_�����Ȳ,l_���һ���,l_�¼����, l_�װ����ȣ,l_��ID,l_keyword;
	JTextField txt_���Ե��װ���, txt_���̸�, txt_�����Ȳ, txt_���һ���,txt_�¼����, txt_�װ����ȣ,txt_��ID,txt_keyword;
	JTable table;
	JScrollPane scroll;
	JButton bt_insert, bt_update, bt_delete, bt_select, bt_getListAll, bt_exit;
	JPanel south, north, center, p1, p2, p3, p4;

	private String colName1[] = {"���Ե��װ���","���̸�", "�����Ȳ","���һ���","�¼����", "�װ����ȣ","��ID"};
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

	public Reservation() {
		m_label = new JLabel("���� ����");
		l_���Ե��װ��� = new JLabel("���Ե��װ���");
		l_���̸� = new JLabel("���̸�");
		l_�����Ȳ = new JLabel("�����Ȳ");
		l_���һ��� = new JLabel("���һ���");
		l_�¼���� = new JLabel("�¼����");
		l_�װ����ȣ = new JLabel("�װ����ȣ");
		l_��ID = new JLabel("��ID");
		l_keyword = new JLabel("������Ȳ�� �Է��Ͽ� ��ȸ�Ͻÿ�.   ex)������� ���ﵵ�� ");
		
		txt_���Ե��װ��� = new JTextField(20);
		txt_���̸� = new JTextField(5);
		txt_�����Ȳ = new JTextField(10);
		txt_���һ��� = new JTextField(5);
		txt_�¼���� = new JTextField(5);
		txt_�װ����ȣ = new JTextField(10);
		txt_��ID = new JTextField(10);
		txt_keyword = new JTextField(20); 
		
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
//		setTitle("���� ����");

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
		
		p2.add(l_���Ե��װ���);
		p2.add(txt_���Ե��װ���);
		p2.add(l_���̸�);
		p2.add(txt_���̸�);
		p2.add(l_�����Ȳ);
		p2.add(txt_�����Ȳ);
		
		p3.add(l_���һ���);
		p3.add(txt_���һ���);
		p3.add(l_�¼����);
		p3.add(txt_�¼����);
		p3.add(l_�װ����ȣ);
		p3.add(txt_�װ����ȣ);
		p3.add(l_��ID);
		p3.add(txt_��ID);

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
				String ���Ե��װ��� = (String) table.getValueAt(selRow, 0);
				String ���̸� = (String) table.getValueAt(selRow, 1);
				String �����Ȳ = (String) table.getValueAt(selRow, 2);
				String ���һ��� = (String) table.getValueAt(selRow, 3);
				String �¼���� = (String) table.getValueAt(selRow, 4);
				String �װ����ȣ = (String) table.getValueAt(selRow, 5);
				String ��ID = (String) table.getValueAt(selRow, 6);
				
				txt_���Ե��װ���.setText(���Ե��װ���);
				txt_���̸�.setText(���̸�);
				txt_�����Ȳ.setText(�����Ȳ);
				txt_���һ���.setText(���һ���);
				txt_�¼����.setText(�¼����);
				txt_�װ����ȣ.setText(�װ����ȣ);
				txt_��ID.setText(��ID);
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
		String sql = "select * from reservation order by ���̸� ; ";
		try {
			for(int i= model1.getRowCount()-1; i>=0; i--) {
			     model1.removeRow(i);
			    }
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); //select
			Vector list = new Vector();
			while (rs.next()) {
				String[] row = new String[10];
			    row[0] = rs.getString("���Ե��װ���");
			    row[1] = rs.getString("���̸�");
			    row[2] = rs.getString("�����Ȳ");
			    row[3] = rs.getString("���һ���");
			    row[4] = rs.getString("�¼����");
			    row[5] = rs.getString("�װ����ȣ");
			    row[6] = rs.getString("��ID");
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
		sql = "Insert into reservation ";
		sql = sql + "Value(?,?,?,?,?,?,?)";
		
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_���Ե��װ���.getText());
			pstmt.setString(2, txt_���̸�.getText());
			pstmt.setString(3, txt_�����Ȳ.getText());
			pstmt.setString(4, txt_���һ���.getText());
			pstmt.setString(5, txt_�¼����.getText());
			pstmt.setString(6, txt_�װ����ȣ.getText());
			pstmt.setString(7, txt_��ID.getText());
			result = pstmt.executeUpdate(); 

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int delete() {
		//������ ����
		int result = 0;
		String sql = "Delete from reservation where �װ����ȣ=? and ��ID=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_�װ����ȣ.getText());
			pstmt.setString(2, txt_��ID.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int update() {
		//������ ����
		int result = 0;
		String sql = "Update reservation set ���Ե��װ���=?,���̸�=?, �����Ȳ=?, ���һ���=?, �¼����=? where �װ����ȣ=? and ��ID=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_���Ե��װ���.getText());
			pstmt.setString(2, txt_���̸�.getText());
			pstmt.setString(3, txt_�����Ȳ.getText());
			pstmt.setString(4, txt_���һ���.getText());
			pstmt.setString(5, txt_�¼����.getText());
			pstmt.setString(6, txt_�װ����ȣ.getText());
			pstmt.setString(7, txt_��ID.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public boolean select(String keyword) {
		//������ �˻�
		boolean result = false;
		String sql = "select * from reservation where ���Ե��װ��� = '" + keyword + "'";	//Ű������ ���Ե� �װ����� �Է��Ͽ� �˻���
		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			for(int i= model1.getRowCount()-1; i>=0; i--) {
			     model1.removeRow(i);
			    }
			while (rs.next()) {
				String[] row = new String[10];
				row[0] = rs.getString("���Ե��װ���");
			    row[1] = rs.getString("���̸�");
			    row[2] = rs.getString("�����Ȳ");
			    row[3] = rs.getString("���һ���");
			    row[4] = rs.getString("�¼����");
			    row[5] = rs.getString("�װ����ȣ");
			    row[6] = rs.getString("��ID");
			    model1.addRow(row);
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public void clear() {
		
		txt_���Ե��װ���.setText("");
		txt_���̸�.setText("");
		txt_�����Ȳ.setText("");
		txt_���һ���.setText("");
		txt_�¼����.setText("");
		txt_�װ����ȣ.setText("");
		txt_��ID.setText("");
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
				JOptionPane.showMessageDialog(getParent(), txt_���Ե��װ���.getText()+"�ϴ� ���� ��ϼ���");
				getListAll();
				clear();
				table.updateUI();
			} else {
				JOptionPane.showMessageDialog(getParent(), txt_���Ե��װ���.getText()+"�ϴ� ���� ��Ͻ���");
			}
		}
		// ������ư
		if (obj.equals(bt_update)) {
			if (JOptionPane.showConfirmDialog(getParent(),
					txt_���Ե��װ���.getText()+"�ϴ� ������ ������ ���� �Ͻðڽ��ϱ�?") == 0) {
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
					txt_���Ե��װ���.getText()+"�ϴ� ������ ������ ���� �Ͻðڽ��ϱ�?") == 0) { //�� ������ ������. �� ������ �ƴ϶� �˻��� �װ���ID�� �����ǵ��� �����ؾ���.
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
			select(txt_keyword.getText());
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

