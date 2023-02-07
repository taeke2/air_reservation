import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Cus_ticket extends JPanel implements ActionListener {
	JLabel m_label, l_�װ����ȣ, l_��¥, l_������, l_�ⱹ����, l_�Ա�����, l_��߽ð�, l_�����ð�, l_������ü��ȣ, l_���, l_�װ���ID, l_keyword,
			l_�����ϵ�¼���, l_��������Ͻ��¼���, l_�������ڳ���¼���;
	JTextField txt_�װ����ȣ, txt_��¥, txt_������, txt_�ⱹ����, txt_�Ա�����, txt_��߽ð�, txt_�����ð�, txt_������ü��ȣ, txt_���, txt_�װ���ID,
			txt_keyword1, txt_keyword2, txt_�����ϵ�¼���, txt_�������ڳ���¼���, txt_��������Ͻ��¼���;
	JTable table;
	JScrollPane scroll;
	JButton bt_select, bt_getListAll, bt_exit, bt_����; // ��ư
	JPanel south, north, center, p1, p2, p3, p4;
	JComboBox strCombo1 = new JComboBox();
	JComboBox strCombo2 = new JComboBox();
	JComboBox strCombo3 = new JComboBox();

	private String colName1[] = { "�װ����ȣ", "�ⱹ����", "�Ա�����", "��¥", "��߽ð�", "�����ð�", "�����ϵ�¼���", "��������Ͻ��¼���", "�������ڳ���¼���" };
	private DefaultTableModel model1 = new DefaultTableModel(colName1, 0) {
		// Jtable ���� ���� �ȵǰ�
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
		l_�����ϵ�¼��� = new JLabel("�����ϵ�¼���");
		l_��������Ͻ��¼��� = new JLabel("��������Ͻ��¼���");
		l_�������ڳ���¼��� = new JLabel("�������ڳ���¼���");
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
		txt_�����ϵ�¼��� = new JTextField(10);
		txt_��������Ͻ��¼��� = new JTextField(10);
		txt_�������ڳ���¼��� = new JTextField(10);
		txt_keyword1 = new JTextField(10);
		txt_keyword2 = new JTextField(10);

		bt_select = new JButton("��ȸ");
		bt_getListAll = new JButton("��ü��ȸ");
		bt_exit = new JButton("����");
		bt_���� = new JButton("����");
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
		p2.add(l_�ⱹ����);
		p2.add(strCombo1);
		p2.add(l_�Ա�����);
		p2.add(strCombo2);
		p2.add(l_��¥);
		p2.add(strCombo3);

		p3.add(l_�����ϵ�¼���);
		p3.add(txt_�����ϵ�¼���);
		p3.add(l_��������Ͻ��¼���);
		p3.add(txt_��������Ͻ��¼���);
		p3.add(l_�������ڳ���¼���);
		p3.add(txt_�������ڳ���¼���);

		p4.add(bt_select);
		p4.add(bt_getListAll);

		center.setLayout(new GridLayout(1, 1));
		center.add(scroll);
		south.setLayout(new GridLayout(1, 4));
		south.add(bt_����);
		south.add(bt_exit);

		// ��ư�� �׼� ������ �޾��ֱ�
		bt_select.addActionListener(this);
		bt_getListAll.addActionListener(this);
		bt_����.addActionListener(this);
		bt_exit.addActionListener(this);

		// ���콺�� Ŭ���Ǿ�����
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { 
				selRow = table.getSelectedRow();
				System.out.println(selRow);
				String �װ����ȣ = (String) table.getValueAt(selRow, 0);
				String �ⱹ���� = (String) table.getValueAt(selRow, 1);
				String �Ա����� = (String) table.getValueAt(selRow, 2);
				String ��¥ = (String) table.getValueAt(selRow, 3);
				String ��߽ð� = (String) table.getValueAt(selRow, 4);
				String �����ð� = (String) table.getValueAt(selRow, 5);
				String �����ϵ�¼��� = (String) table.getValueAt(selRow, 6);
				String ��������Ͻ��¼��� = (String) table.getValueAt(selRow, 7);
				String �������ڳ���¼��� = (String) table.getValueAt(selRow, 8);

				txt_�װ����ȣ.setText(�װ����ȣ);
				strCombo1.setSelectedItem(�ⱹ����);
				strCombo2.setSelectedItem(�Ա�����);
				strCombo3.setSelectedItem(��¥);
				txt_�����ϵ�¼���.setText(�����ϵ�¼���);
				txt_��������Ͻ��¼���.setText(��������Ͻ��¼���);
				txt_�������ڳ���¼���.setText(�������ڳ���¼���);
			}
		});

		//////////////////////////////////////////////////////////////////////////////////////////////���ϴ� �װ�����  ã������ �޺��ڽ�
		strCombo1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	//�ⱹ����
				// TODO Auto-generated method stub
				JComboBox combo = (JComboBox) e.getSource();
				int index = combo.getSelectedIndex();
				start = (String) combo.getSelectedItem();

			}
		});

		strCombo2.addActionListener(new ActionListener() {	//��������
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JComboBox combo = (JComboBox) e.getSource();
				int index = combo.getSelectedIndex();
				end = (String) combo.getSelectedItem();

			}
		});

		strCombo3.addActionListener(new ActionListener() {	//��¥
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
			System.out.println("DB ���� �Ϸ�");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return isConnect;
	}

	public void getListAll() {	
		// �װ��� ������ ��ü ��ȸ
		String sql = "select * from aircraft A, flight F where A.������ü��ȣ = F.������ü��ȣ order by ��¥,�ⱹ����,�Ա�����;";			// �װ���� �װ��� ���̺��� �����Ͽ� ��ü ���డ���� �װ��� ������ ��ȸ
		String sql1 = "select distinct F.�ⱹ���� from aircraft A, flight F where A.������ü��ȣ = F.������ü��ȣ order by �ⱹ����; ";	// �ⱹ������ �Ӽ������� �ߺ����� ���ְ� ��ȸ
		String sql2 = "select distinct F.�Ա����� from aircraft A, flight F where A.������ü��ȣ = F.������ü��ȣ order by �Ա�����; ";	// �Ա������� �Ӽ������� �ߺ����� ���ְ� ��ȸ
		String sql3 = "select distinct F.��¥ from aircraft A, flight F where A.������ü��ȣ = F.������ü��ȣ order by ��¥; ";
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
				row[0] = rs.getString("F.�װ����ȣ");		//�װ���� �װ����� ������ �������߿��� ���ϴ� �Ӽ����� �޾ƿ´�.
				row[1] = rs.getString("F.�ⱹ����");
				row[2] = rs.getString("F.�Ա�����");
				row[3] = rs.getString("F.��¥");
				row[4] = rs.getString("F.��߽ð�");
				row[5] = rs.getString("F.�����ð�");
				row[6] = rs.getString("A.�ϵ�¼���");
				row[7] = rs.getString("A.����Ͻ��¼���");
				row[8] = rs.getString("A.���ڳ���¼���");

				model1.addRow(row);
			}
			
			while (rs1.next()) {
				strCombo1.addItem(rs1.getString("�ⱹ����"));	//�ⱹ������ �Ӽ������� �ߺ����� ���ְ� �޺��ڽ��� �ִ´�
			}
			while (rs2.next()) {
				strCombo2.addItem(rs2.getString("�Ա�����"));	//�Ա������� �Ӽ������� �ߺ����� ���ְ� �޺��ڽ��� �ִ´�
			}
			while (rs3.next()) {
				strCombo3.addItem(rs3.getString("��¥"));		//��¥ �Ӽ������� �ߺ����� ���ְ� �޺��ڽ��� �ִ´�
			}
			this.repaint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public boolean select(String start, String end) {		//���ϴ� �װ����� �ⱹ����,�Ա�����,��¥�� �����Ͽ� ��ȸ�Ѵ�.
		// ������ �˻�
		boolean result = false;
		String sql = "select * from flight where �ⱹ���� = '" + start + "' and �Ա�����= '" + end + "' and ��¥ ='" + date + "'";
		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			for (int i = model1.getRowCount() - 1; i >= 0; i--) {
				model1.removeRow(i);
			}
			while (rs.next()) {
				String[] row = new String[10];
				row[0] = rs.getString("�װ����ȣ");
				row[1] = rs.getString("��¥");
				row[2] = rs.getString("������");
				row[3] = rs.getString("�ⱹ����");
				row[4] = rs.getString("�Ա�����");
				row[5] = rs.getString("��߽ð�");
				row[6] = rs.getString("�����ð�");
				row[7] = rs.getString("������ü��ȣ");
				row[8] = rs.getString("���");
				row[9] = rs.getString("�װ���ID");
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

	public int reservation() { ////////// �������� ���� �� �������̺� ���� �ֱ�
		// ������ �߰�
		Temper tp = new Temper();
		int result = 0;
		best = Integer.parseInt(txt_�����ϵ�¼���.getText().toString());		//���� �ϵ �¼���
		normal = Integer.parseInt(txt_��������Ͻ��¼���.getText().toString());	//���� ����Ͻ��� �¼���
		worst = Integer.parseInt(txt_�������ڳ���¼���.getText().toString());	//���� ���ڳ�� �¼���
		
		//������ �� ������ �¼� ��޿� ���� �װ����� ���� �¼��� ����
		if (Cuslevel.equals("�ϵ")) {
			best = best - 1;
		} else if (Cuslevel.equals("����Ͻ���")) {
			normal = normal - 1;
		} else if (Cuslevel.equals("���ڳ�̼�")) {
			worst = worst - 1;
		}

		// ���� ������ �������� �����ڸ���� ������� ���̺� ������ ����.
		sql = "insert into reservation value(?,?,?,?,?,?,?)";	
		//������ �� ������ �¼� ��޿� ���� �װ����� ���� �¼��� ����
		sql1 = "update aircraft A, flight F set A.�ϵ�¼���=?, A.����Ͻ��¼���=?, A.���ڳ���¼���=? where A.������ü��ȣ = F.������ü��ȣ and F.�װ����ȣ=?";
											
		try {
			
			pstmt = con.prepareStatement(sql);
			pstmt1 = con.prepareStatement(sql1);
			pstmt.setString(1, start + "��� " + end + "����");
			pstmt.setString(2, Cusname);
			pstmt.setString(3, "Ok");
			pstmt.setString(4, Cuspay);
			pstmt.setString(5, Cuslevel);
			pstmt.setString(6, txt_�װ����ȣ.getText().toString());
			pstmt.setString(7, tp.id);
			pstmt.executeUpdate();

			pstmt1.setInt(1, best);		//���� �ϵ
			pstmt1.setInt(2, normal);	//���� ����Ͻ��¼���
			pstmt1.setInt(3, worst);	//���� ���ڳ���¼���
			pstmt1.setString(4, txt_�װ����ȣ.getText().toString());
			result = pstmt1.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	class reservation extends JFrame {////////////////////////////////////// ���� �������� ���� (���̸�, ��ð���, �¼����)
		reservation() {
			setTitle("�����ϱ�");
			String[] pay = { "���þ���", "O", "X" };
			String[] level = { "���þ���", "�ϵ", "����Ͻ���", "���ڳ�̼�" };
			int result = 1;

			JPanel cp = new JPanel();
			setContentPane(cp);

			JLabel l_name = new JLabel("���̸�");
			JLabel l_pay = new JLabel("��ð���");
			JLabel l_level = new JLabel("�¼����");
			JButton btn_ok = new JButton("Ȯ��");
			JComboBox CusCombo1 = new JComboBox(pay);
			JComboBox CusCombo2 = new JComboBox(level);
			JTextField txt_name = new JTextField(5);

			CusCombo1.addActionListener(new ActionListener() {	//��ð��� ����

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JComboBox combo = (JComboBox) e.getSource();
					int index = combo.getSelectedIndex();
					Cuspay = pay[index];

				}
			});
			CusCombo2.addActionListener(new ActionListener() {	// �¼���� (�ϵ��, ����Ͻ�, ���ڳ��)

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JComboBox combo = (JComboBox) e.getSource();
					int index = combo.getSelectedIndex();
					Cuslevel = level[index];

				}
			});

			btn_ok.addActionListener(new ActionListener() { ////////////////// �������� ���� �Ϸ�
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Cusname = txt_name.getText().toString();
					int result = reservation();
					
					if (result != 0) {

						JOptionPane.showMessageDialog(getParent(),
								date + " [" + start + "��� " + end + "����] �ϴ� Ƽ�� ���� ����");
						setVisible(false);
						getListAll();

						clear();
						table.updateUI();
					} else {
						JOptionPane.showMessageDialog(getParent(),
								date + " [" + start + "��� " + end + "����] �ϴ� Ƽ�� ���� ����");
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
		// �����ư
		if (obj.equals(bt_����)) {
			new reservation();
		}
		
		// ��ȸ��ư
		if (obj.equals(bt_select)) {
			select(start, end);
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
