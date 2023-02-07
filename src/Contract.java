import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Contract extends JPanel implements ActionListener {
	JLabel m_label, l_�����ڵ�, l_�װ���ID, l_���Ⱓ, l_keyword;
	JTextField txt_�����ڵ�, txt_�װ���ID, txt_���Ⱓ, txt_keyword1, txt_keyword2;
	JTable table;
	JScrollPane scroll;
	JButton bt_insert, bt_update, bt_delete, bt_select, bt_getListAll, bt_exit;
	JPanel south, north, center, p1, p2, p3, p4;

	private String colName1[] = { "�����ڵ�", "�װ���ID", "���Ⱓ" };
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

	public Contract() {
		m_label = new JLabel("��� ����");
		l_�����ڵ� = new JLabel("�����ڵ� ");
		l_�װ���ID = new JLabel("�װ���ID");
		l_���Ⱓ = new JLabel("���Ⱓ");
		l_keyword = new JLabel("�����ڵ�� �װ���ID�� �Է��Ͽ� ��ȸ�Ͻÿ�.");

		txt_�����ڵ� = new JTextField(8);
		txt_�װ���ID = new JTextField(8);
		txt_���Ⱓ = new JTextField(8);

		txt_keyword1 = new JTextField(5);
		txt_keyword2 = new JTextField(5);

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

//		setTitle("��� ����");

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
		p2.add(l_�װ���ID);
		p2.add(txt_�װ���ID);
		p2.add(l_���Ⱓ);
		p2.add(txt_���Ⱓ);

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
				String �װ���ID = (String) table.getValueAt(selRow, 1);
				String ���Ⱓ = (String) table.getValueAt(selRow, 2);

				txt_�����ڵ�.setText(�����ڵ�);
				txt_�װ���ID.setText(�װ���ID);
				txt_���Ⱓ.setText(���Ⱓ);
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
		String sql = "select * from contract order by ���Ⱓ; ";
		try {
			for (int i = model1.getRowCount() - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); // select
			Vector list = new Vector();
			while (rs.next()) {
				String[] row = new String[5];
				row[0] = rs.getString("�����ڵ�");
				row[1] = rs.getString("�װ���ID");
				row[2] = rs.getString("���Ⱓ");

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
		sql = "Insert into contract ";
		sql = sql + "Value(?,?,?)";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_�����ڵ�.getText());
			pstmt.setString(2, txt_�װ���ID.getText());
			pstmt.setString(3, txt_���Ⱓ.getText());
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int delete() {
		// ������ ����
		int result = 0;
		String sql = "Delete from contract where �����ڵ�=? and �װ���ID=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_�����ڵ�.getText());
			pstmt.setString(2, txt_�װ���ID.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public int update() {
		// ������ ����
		int result = 0;
		String sql = "Update contract set ���Ⱓ=? where �����ڵ�=? and �װ���ID=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt_���Ⱓ.getText());
			pstmt.setString(2, txt_�����ڵ�.getText());
			pstmt.setString(3, txt_�װ���ID.getText());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public boolean select(String keyword1, String keyword2) {
		// ������ �˻�
		boolean result = false;
		String sql = "select * from contract where �����ڵ� = '" + keyword1 + "' and �װ���ID= '" + keyword2 + "'";
		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			for (int i = model1.getRowCount() - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			while (rs.next()) {
				String[] row = new String[5];
				row[0] = rs.getString("�����ڵ�");
				row[1] = rs.getString("�װ���ID");
				row[2] = rs.getString("���Ⱓ");
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
		txt_�װ���ID.setText("");
		txt_���Ⱓ.setText("");
		txt_�����ڵ�.requestFocus();
		txt_�װ���ID.requestFocus();
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
						txt_�����ڵ�.getText() + "��" + txt_�װ���ID.getText() + "������ ��� ��ϼ���");
				getListAll();
				clear();
				table.updateUI();
			} else {
				JOptionPane.showMessageDialog(getParent(),
						txt_�����ڵ�.getText() + "��" + txt_�װ���ID.getText() + "������ ��� ��Ͻ���");
			}
		}
		// ������ư
		if (obj.equals(bt_update)) {
			if (JOptionPane.showConfirmDialog(getParent(),
					txt_�����ڵ�.getText() + "��" + txt_�װ���ID.getText() + "������ ��� ������ ���� �Ͻðڽ��ϱ�?") == 0) {
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
					txt_�����ڵ�.getText() + "��" + txt_�װ���ID.getText() + "������ ��� ������ ���� �Ͻðڽ��ϱ�?") == 0) {
																										
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
			select(txt_keyword1.getText(), txt_keyword2.getText());
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
