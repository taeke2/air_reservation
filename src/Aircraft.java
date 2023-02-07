import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Aircraft extends JPanel implements ActionListener {
	JLabel m_label, l_������ü��ȣ, l_���ۻ�, l_����, l_�ϵ�¼���, l_����Ͻ��¼���, l_���ڳ���¼���, l_����, l_������, l_�Ҽ��װ���, l_keyword;
	JTextField txt_������ü��ȣ, txt_���ۻ�, txt_����, txt_�ϵ�¼���, txt_����Ͻ��¼���, txt_���ڳ���¼���, txt_����, txt_������, txt_�Ҽ��װ���,
			txt_keyword;
	JTable table;
	JScrollPane scroll;
	JButton bt_insert, bt_update, bt_delete, bt_select, bt_getListAll, bt_exit;
	JPanel south, north, center, p1, p2, p3, p4;

	private String colName1[] = { "����� ��ü��ȣ", "���ۻ�", "����", "�ϵ �¼���", "����Ͻ� �¼���", "���ڳ�� �¼���", "����", "������", "�Ҽ� �װ���" };
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

	public Aircraft() {
		m_label = new JLabel("�װ��� ����");
		l_������ü��ȣ = new JLabel("����� ��ü��ȣ");
		l_���ۻ� = new JLabel("���ۻ�");
		l_���� = new JLabel("����");
		l_�ϵ�¼��� = new JLabel("�ϵ �¼���");
		l_����Ͻ��¼��� = new JLabel("����Ͻ� �¼���");
		l_���ڳ���¼��� = new JLabel("���ڳ�� �¼���");
		l_���� = new JLabel("����");
		l_������ = new JLabel("������");
		l_�Ҽ��װ��� = new JLabel("�Ҽ� �װ���");
		l_keyword = new JLabel("�װ��� ��ȣ�� �Է��Ͽ� ��ȸ�Ͻÿ�.");
		txt_������ü��ȣ = new JTextField(15);
		txt_���ۻ� = new JTextField(8);
		txt_���� = new JTextField(15);
		txt_�ϵ�¼��� = new JTextField(8);
		txt_����Ͻ��¼��� = new JTextField(8);
		txt_���ڳ���¼��� = new JTextField(8);
		txt_���� = new JTextField(8);
		txt_������ = new JTextField(8);
		txt_�Ҽ��װ��� = new JTextField(8);
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

		p2.add(l_������ü��ȣ);
		p2.add(txt_������ü��ȣ);
		p2.add(l_���ۻ�);
		p2.add(txt_���ۻ�);
		p2.add(l_����);
		p2.add(txt_����);
		p3.add(l_�ϵ�¼���);
		p3.add(txt_�ϵ�¼���);
		p3.add(l_����Ͻ��¼���);
		p3.add(txt_����Ͻ��¼���);
		p3.add(l_���ڳ���¼���);
		p3.add(txt_���ڳ���¼���);
		p3.add(l_����);
		p3.add(txt_����);
		p3.add(l_������);
		p3.add(txt_������);
		p3.add(l_�Ҽ��װ���);
		p3.add(txt_�Ҽ��װ���);

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
			public void mouseClicked(MouseEvent e) { // ���콺�� Ŭ���Ǿ�����
				selRow = table.getSelectedRow();
				System.out.println(selRow);
				String ������ü��ȣ = (String) table.getValueAt(selRow, 0);
				String ���ۻ� = (String) table.getValueAt(selRow, 1);
				String ���� = (String) table.getValueAt(selRow, 2);
				String �ϵ�¼��� = (String) table.getValueAt(selRow, 3);
				String ����Ͻ��¼��� = (String) table.getValueAt(selRow, 4);
				String ���ڳ���¼��� = (String) table.getValueAt(selRow, 5);
				String ���� = (String) table.getValueAt(selRow, 6);
				String ������ = (String) table.getValueAt(selRow, 7);
				String �Ҽ��װ��� = (String) table.getValueAt(selRow, 8);

				txt_������ü��ȣ.setText(������ü��ȣ);
				txt_���ۻ�.setText(���ۻ�);
				txt_����.setText(����);
				txt_�ϵ�¼���.setText(�ϵ�¼���);
				txt_����Ͻ��¼���.setText(����Ͻ��¼���);
				txt_���ڳ���¼���.setText(���ڳ���¼���);
				txt_����.setText(����);
				txt_������.setText(������);
				txt_�Ҽ��װ���.setText(�Ҽ��װ���);
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
		String sql = "select * from aircraft order by ������ü��ȣ; ";
		try {
			for (int i = model1.getRowCount() - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); // select
			Vector list = new Vector();
			while (rs.next()) {
				String[] row = new String[9];
				row[0] = rs.getString("������ü��ȣ");
				row[1] = rs.getString("���ۻ�");
				row[2] = rs.getString("����");
				row[3] = rs.getString("�ϵ�¼���");
				row[4] = rs.getString("����Ͻ��¼���");
				row[5] = rs.getString("���ڳ���¼���");
				row[6] = rs.getString("����");
				row[7] = rs.getString("������");
				row[8] = rs.getString("�Ҽ��װ���");

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
		sql = "Insert into aircraft ";
		sql = sql + "Value(?,?,?,?,?,?,?,?,?)";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_������ü��ȣ.getText());
			pstmt.setString(2, txt_���ۻ�.getText());
			pstmt.setString(3, txt_����.getText());
			pstmt.setString(4, txt_�ϵ�¼���.getText());
			pstmt.setString(5, txt_����Ͻ��¼���.getText());
			pstmt.setString(6, txt_���ڳ���¼���.getText());
			pstmt.setString(7, txt_����.getText());
			pstmt.setString(8, txt_������.getText());
			pstmt.setString(9, txt_�Ҽ��װ���.getText());
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int delete() {
		// ������ ����
		int result = 0;
		String sql = "Delete from aircraft where ������ü��ȣ=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_������ü��ȣ.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int update() {
		// ������ ����
		int result = 0;
		String sql = "Update aircraft set ���ۻ�=?, ����=?, �ϵ�¼���=?, ����Ͻ��¼���=?, ���ڳ���¼���=?, ����=?, ������=?, �Ҽ��װ���=? where ������ü��ȣ=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_���ۻ�.getText());
			pstmt.setString(2, txt_����.getText());
			pstmt.setString(3, txt_�ϵ�¼���.getText());
			pstmt.setString(4, txt_����Ͻ��¼���.getText());
			pstmt.setString(5, txt_���ڳ���¼���.getText());
			pstmt.setString(6, txt_����.getText());
			pstmt.setString(7, txt_������.getText());
			pstmt.setString(8, txt_�Ҽ��װ���.getText());
			pstmt.setString(9, txt_������ü��ȣ.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean select(String keyword) {
		// ������ �˻�
		boolean result = false;
		String sql = "select * from aircraft where ������ü��ȣ = '" + keyword + "'"; // Ű������ �װ����ȣ�� �Է��Ͽ� �˻���
		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			for (int i = model1.getRowCount() - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			while (rs.next()) {
				String[] row = new String[10];
				row[0] = rs.getString("������ü��ȣ");
				row[1] = rs.getString("���ۻ�");
				row[2] = rs.getString("����");
				row[3] = rs.getString("�ϵ�¼���");
				row[4] = rs.getString("����Ͻ��¼���");
				row[5] = rs.getString("���ڳ���¼���");
				row[6] = rs.getString("����");
				row[7] = rs.getString("������");
				row[8] = rs.getString("�Ҽ��װ���");
				model1.addRow(row);
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public void clear() {
		txt_������ü��ȣ.setText("");
		txt_���ۻ�.setText("");
		txt_����.setText("");
		txt_�ϵ�¼���.setText("");
		txt_����Ͻ��¼���.setText("");
		txt_���ڳ���¼���.setText("");
		txt_����.setText("");
		txt_������.setText("");
		txt_�Ҽ��װ���.setText("");
		txt_������ü��ȣ.requestFocus();
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
				JOptionPane.showMessageDialog(getParent(), txt_������ü��ȣ.getText() + " ��ϼ���");
				getListAll();
				clear();
				table.updateUI();
			} else {
				JOptionPane.showMessageDialog(getParent(), txt_������ü��ȣ.getText() + " ��Ͻ���");
			}
		}
		// ������ư
		if (obj.equals(bt_update)) {
			if (JOptionPane.showConfirmDialog(getParent(), txt_������ü��ȣ.getText() + " �� ������ ���� �Ͻðڽ��ϱ�?") == 0) {
				int result = update();
				if (result != 0) {
					getListAll();
					clear();
					table.updateUI();
				}
			}
		}
		if (obj.equals(bt_delete)) {
			if (JOptionPane.showConfirmDialog(getParent(), txt_������ü��ȣ.getText() + " �� ������ ���� �Ͻðڽ��ϱ�?") == 0) {
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
