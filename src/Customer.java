import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Customer extends JPanel implements ActionListener {
	JLabel m_label, l_��ID, l_�̸�, l_�ּ�,l_��ȭ��ȣ,l_�װ�����������,l_��ȣ�������,l_��ȣ�¼�, l_keyword;
	JTextField txt_��ID,txt_�̸�,txt_�ּ�,txt_��ȭ��ȣ,txt_�װ�����������,txt_��ȣ�������,txt_��ȣ�¼�,txt_keyword;
	JTable table;
	JScrollPane scroll;
	JButton bt_insert, bt_update, bt_delete, bt_select, bt_getListAll, bt_exit;
	JPanel south, north, center, p1, p2, p3, p4;

	private String colName1[] = {"��ID","�̸�","�ּ�", "��ȭ��ȣ","�װ�����������","��ȣ�������", "��ȣ�¼�"};
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
	
	public Customer() {
		m_label = new JLabel("�� ����");
		l_��ID = new JLabel("�� ID");
		l_�̸� = new JLabel("�̸�");
		l_�ּ� = new JLabel("�ּ�");
		l_��ȭ��ȣ = new JLabel("��ȭ��ȣ");
		l_�װ����������� = new JLabel("�װ�����������");
		l_��ȣ������� = new JLabel("��ȣ�������");
		l_��ȣ�¼� = new JLabel("��ȣ�¼�");
		l_keyword = new JLabel("�� ���̵� �Է��Ͽ� ��ȸ�Ͻÿ�.");
		txt_��ID = new JTextField(8);
		txt_�̸� = new JTextField(5);
		txt_�ּ� = new JTextField(15);
		txt_��ȭ��ȣ = new JTextField(15);
		txt_�װ����������� = new JTextField(5);
		txt_��ȣ������� = new JTextField(5);
		txt_��ȣ�¼� = new JTextField(8);
		txt_keyword = new JTextField(8); 
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
		
		p2.add(l_��ID);
		p2.add(txt_��ID);
		p2.add(l_�̸�);
		p2.add(txt_�̸�);
		p2.add(l_�ּ�);
		p2.add(txt_�ּ�);
		p2.add(l_��ȭ��ȣ);
		p2.add(txt_��ȭ��ȣ);
		p2.add(l_�װ�����������);
		p2.add(txt_�װ�����������);
		p2.add(l_��ȣ�������);
		p2.add(txt_��ȣ�������);
		p2.add(l_��ȣ�¼�);
		p2.add(txt_��ȣ�¼�);

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
				String ��ID = (String) table.getValueAt(selRow, 0);
				String �̸� = (String) table.getValueAt(selRow, 1);
				String �ּ� = (String) table.getValueAt(selRow, 2);
				String ��ȭ��ȣ = (String) table.getValueAt(selRow, 3);
				String �װ����������� = (String) table.getValueAt(selRow, 4);
				String ��ȣ������� = (String) table.getValueAt(selRow, 5);
				String ��ȣ�¼� = (String) table.getValueAt(selRow, 6);
				
				txt_��ID.setText(��ID);
				txt_�̸�.setText(�̸�);
				txt_�ּ�.setText(�ּ�);
				txt_��ȭ��ȣ.setText(��ȭ��ȣ);
				txt_�װ�����������.setText(�װ�����������);
				txt_��ȣ�������.setText(��ȣ�������);
				txt_��ȣ�¼�.setText(��ȣ�¼�);
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
			con = DriverManager.getConnection("jdbc:mysql://localhost/mydb?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC", "root", "1234");
			isConnect = true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return isConnect;
	}

	public void getListAll() {
		//��ü ������ ��ȸ 
		String sql = "select * from customer order by �̸�; ";
		try {
			for(int i= model1.getRowCount()-1; i>=0; i--) {
			     model1.removeRow(i);
			    }
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); //select
			Vector list = new Vector();
			while (rs.next()) {
				String[] row = new String[10];
			    row[0] = rs.getString("��ID");
			    row[1] = rs.getString("�̸�");
			    row[2] = rs.getString("�ּ�");
			    row[3] = rs.getString("��ȭ��ȣ");
			    row[4] = rs.getString("�װ�����������");
			    row[5] = rs.getString("��ȣ�������");
			    row[6] = rs.getString("��ȣ�¼�");
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
		sql = "Insert into customer ";
		sql = sql + "Value(?,?,?,?,?,?,?)";
		
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_��ID.getText());
			pstmt.setString(2, txt_�̸�.getText());
			pstmt.setString(3, txt_�ּ�.getText());
			pstmt.setString(4, txt_��ȭ��ȣ.getText());
			pstmt.setString(5, txt_�װ�����������.getText());
			pstmt.setString(6, txt_��ȣ�������.getText());
			pstmt.setString(7, txt_��ȣ�¼�.getText());
			result = pstmt.executeUpdate(); 

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int delete() {
		//������ ����
		int result = 0;
		String sql = "Delete from customer where ��ID=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_��ID.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int update() {
		//������ ����
		int result = 0;
		String sql = "Update customer set �̸�=?, �ּ�=?, ��ȭ��ȣ=?, �װ�����������=?, ��ȣ�������=?, ��ȣ�¼�=? where ��ID=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_�̸�.getText());
			pstmt.setString(2, txt_�ּ�.getText());
			pstmt.setString(3, txt_��ȭ��ȣ.getText());
			pstmt.setString(4, txt_�װ�����������.getText());
			pstmt.setString(5, txt_��ȣ�������.getText());
			pstmt.setString(6, txt_��ȣ�¼�.getText());
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
		String sql = "select * from customer where ��ID = '" + keyword + "'";
		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			for(int i= model1.getRowCount()-1; i>=0; i--) {
			     model1.removeRow(i);
			    }
			while (rs.next()) {
				String[] row = new String[10];
				row[0] = rs.getString("��ID");
			    row[1] = rs.getString("�̸�");
			    row[2] = rs.getString("�ּ�");
			    row[3] = rs.getString("��ȭ��ȣ");
			    row[4] = rs.getString("�װ�����������");
			    row[5] = rs.getString("��ȣ�������");
			    row[6] = rs.getString("��ȣ�¼�");
			    model1.addRow(row);
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public void clear() {
		txt_��ID.setText("");
		txt_�̸�.setText("");
		txt_�ּ�.setText("");
		txt_��ȭ��ȣ.setText("");
		txt_�װ�����������.setText("");
		txt_��ȣ�������.setText("");
		txt_��ȣ�¼�.setText("");
		txt_��ID.requestFocus();
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
				JOptionPane.showMessageDialog(getParent(), txt_��ID.getText() + " ��ϼ���");
				getListAll();
				clear();
				table.updateUI();
			} else {
				JOptionPane.showMessageDialog(getParent(), txt_��ID.getText() + " ��Ͻ���");
			}
		}
		// ������ư
		if (obj.equals(bt_update)) {
			if (JOptionPane.showConfirmDialog(getParent(),
					txt_��ID.getText() + " �� ������ ���� �Ͻðڽ��ϱ�?") == 0) {
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
					txt_��ID.getText() + " �� ������ ���� �Ͻðڽ��ϱ�?") == 0) { //�� ������ ������. �� ������ �ƴ϶� �˻��� �װ���ID�� �����ǵ��� �����ؾ���.
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

