import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Airport extends JPanel implements ActionListener {
	JLabel m_label, l_�����ڵ�, l_�̸�, l_��ġ, l_����, l_��ȭ��ȣ, l_keyword;
	JTextField txt_�����ڵ�, txt_�̸�, txt_��ġ, txt_����, txt_��ȭ��ȣ, txt_keyword;
	JTable table;
	JScrollPane scroll;
	JButton bt_insert, bt_update, bt_delete, bt_select, bt_getListAll, bt_exit;
	JPanel south, north, center, p1, p2, p3, p4;

	private String colName1[] = { "�����ڵ�", "�̸�", "��ġ", "����", "��ȭ��ȣ" };
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
					closeDB(); // ��������â�� ����ɶ� DB������
					System.exit(0);
				}
			});
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			pack();
		}
	}

	public Airport() {
		m_label = new JLabel("���� ����");
		l_�����ڵ� = new JLabel("����� ��ü��ȣ");
		l_�̸� = new JLabel("�̸�");
		l_��ġ = new JLabel("��ġ");
		l_���� = new JLabel("����");
		l_��ȭ��ȣ = new JLabel("��ȭ��ȣ");
		l_keyword = new JLabel("�����ڵ带 �Է��Ͽ� ��ȸ�Ͻÿ�.");

		txt_�����ڵ� = new JTextField(8);
		txt_�̸� = new JTextField(15);
		txt_��ġ = new JTextField(15);
		txt_���� = new JTextField(8);
		txt_��ȭ��ȣ = new JTextField(20);

		txt_keyword = new JTextField(10);
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

		p2.add(l_�����ڵ�);
		p2.add(txt_�����ڵ�);
		p2.add(l_�̸�);
		p2.add(txt_�̸�);
		p2.add(l_��ġ);
		p2.add(txt_��ġ);
		p2.add(l_����);
		p2.add(txt_����);
		p2.add(l_��ȭ��ȣ);
		p2.add(txt_��ȭ��ȣ);

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
			public void mouseClicked(MouseEvent e) { // ���콺�� Ŭ���Ǿ�����
				selRow = table.getSelectedRow();
				System.out.println(selRow);
				String �����ڵ� = (String) table.getValueAt(selRow, 0);
				String �̸� = (String) table.getValueAt(selRow, 1);
				String ��ġ = (String) table.getValueAt(selRow, 2);
				String ���� = (String) table.getValueAt(selRow, 3);
				String ��ȭ��ȣ = (String) table.getValueAt(selRow, 4);

				txt_�����ڵ�.setText(�����ڵ�);
				txt_�̸�.setText(�̸�);
				txt_��ġ.setText(��ġ);
				txt_����.setText(����);
				txt_��ȭ��ȣ.setText(��ȭ��ȣ);
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
		// ��ü ������ ��ȸ
		String sql = "select * from airport order by �̸�; ";
		try {
			for (int i = model1.getRowCount() - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); // select
			Vector list = new Vector();
			while (rs.next()) {
				String[] row = new String[10];
				row[0] = rs.getString("�����ڵ�");
				row[1] = rs.getString("�̸�");
				row[2] = rs.getString("��ġ");
				row[3] = rs.getString("����");
				row[4] = rs.getString("��ȭ��ȣ");

				model1.addRow(row);
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int insert() {
		// ������ �߰�
		int result = 0;
		sql = "Insert into airport ";
		sql = sql + "Value(?,?,?,?,?)";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_�����ڵ�.getText());
			pstmt.setString(2, txt_�̸�.getText());
			pstmt.setString(3, txt_��ġ.getText());
			pstmt.setString(4, txt_����.getText());
			pstmt.setString(5, txt_��ȭ��ȣ.getText());
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int delete() {
		// ������ ����
		int result = 0;
		String sql = "Delete from airport where �����ڵ�=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_�����ڵ�.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int update() {
		// ������ ����
		int result = 0;
		String sql = "Update airport set �̸�=?, ��ġ=?, ����=?, ��ȭ��ȣ=? where �����ڵ�=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_�̸�.getText());
			pstmt.setString(2, txt_��ġ.getText());
			pstmt.setString(3, txt_����.getText());
			pstmt.setString(4, txt_��ȭ��ȣ.getText());
			pstmt.setString(5, txt_�����ڵ�.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean select(String keyword) {
		// ������ �˻�
		boolean result = false;
		String sql = "select * from airport where �����ڵ� = '" + keyword + "'";
		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			for (int i = model1.getRowCount() - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			while (rs.next()) {
				String[] row = new String[10];
				row[0] = rs.getString("�����ڵ�");
				row[1] = rs.getString("�̸�");
				row[2] = rs.getString("��ġ");
				row[3] = rs.getString("����");
				row[4] = rs.getString("��ȭ��ȣ");
				model1.addRow(row);
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void clear() {
		txt_�����ڵ�.setText("");
		txt_�̸�.setText("");
		txt_��ġ.setText("");
		txt_����.setText("");
		txt_��ȭ��ȣ.setText("");
		txt_�����ڵ�.requestFocus();
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
				JOptionPane.showMessageDialog(getParent(), txt_�̸�.getText() + " ��ϼ���");
				getListAll();
				clear();
				table.updateUI();
			} else {
				JOptionPane.showMessageDialog(getParent(), txt_�̸�.getText() + " ��Ͻ���");
			}
		}
		// ������ư
		if (obj.equals(bt_update)) {
			if (JOptionPane.showConfirmDialog(getParent(), txt_�̸�.getText() + " �� ������ ���� �Ͻðڽ��ϱ�?") == 0) {
				int result = update();
				if (result != 0) {
					getListAll();
					clear();
					table.updateUI();
				}
			}
		}
		if (obj.equals(bt_delete)) {
			if (JOptionPane.showConfirmDialog(getParent(), txt_�̸�.getText() + " �� ������ ���� �Ͻðڽ��ϱ�?") == 0) { 
																											
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
