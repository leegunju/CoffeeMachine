package machine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VendingMachine extends JFrame{
	
	private TitleLabel tl = new TitleLabel();                                               //자판기 메인 타이틀(커피 그림 디자인의 패널)
	private CoffeeButtons cm = new CoffeeButtons();                                         //메뉴 버튼 패널
	private MoneyHole mh = new MoneyHole();                                                 //돈 투입 패널
	private Chages ch = new Chages();                                                       //잔돈 반환 패널
	   
	private MyDialog dialog;                                                                //비밀번호를 입력하는 다이얼로그 설정
	   
	static int UserMoneySum = 0;                                                            //이용자 돈 초기화
	private int [] foodPrice = {1500,1500,1500,1500,1500,1500,1500,1500,1500};              //각 커피의 가격
	private int [] foodPrice1= {1500,1500,2000};                                            //부가메뉴 가격
	private int [] foodNum = {100,100,100,100,100,100,100,100,100};
	private int MoneySum = 0;                                                               //총 매출 초기화
	private int [] cell = {0,0,0,0,0,0,0,0,0};                                              //커피 판매 갯수 초기화
	private int num = 1;
	   
	static JLabel sumLabel = new JLabel(UserMoneySum + "  won");                            //다양한 패널들에게 전체적으로 유저의 현재돈이 표시되도록 static 설정
	   
	static ImageIcon [] images = { new ImageIcon("images//아메리카노.jpg"), new ImageIcon("images//카페라떼.jpg"), new ImageIcon("images//카페모카.jpg"),
			                       new ImageIcon("images//카푸치노.jpg"), new ImageIcon("images//카라멜마끼아또.jpg"), new ImageIcon("images//아이스초코.jpg"), 
                                   new ImageIcon("images//에스프레소.jpg"), new ImageIcon("images//에스프레소마끼아또.jpg"), new ImageIcon("images//에스프레소꼰빠냐.jpg")};
	                                                                                        //자기 메뉴에 나타나는 커피 이미지

	private ImageIcon imagesC = new ImageIcon("images//coffee.jpg");                        //커피 이미지
	private ImageIcon imagesI = new ImageIcon("images//ice.jpg");                           //아이스 이미지
	private ImageIcon imagesH = new ImageIcon("images//hot.jpg");                           //핫 이미지
	private ImageIcon imagesS = new ImageIcon("images//shot.jpg");                          //샷 이미지
	   
	private String passWord = "1234";                                                       //매출 및 판매현황 메뉴에 들어가기 위한 비밀번호 설정
	   
	public VendingMachine() {
		
		setTitle("커피 자판기");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(null);
	      
		createMenu();
	      
		setResizable(false);
	      
		setSize(950,1000);
	      
		tl.setBounds(0, 0, getWidth(), 100);                                                //각  패널들을 프레임의 크기에 맞게 설정함
		cm.setBounds(0, 100, getWidth(), 700);
		mh.setBounds(0, 800, getWidth(), 100);
		ch.setBounds(0, 900, getWidth(), 50);
	      
		dialog = new MyDialog(this, "비밀번호 입력");                                           //다이얼로그 설정
	      
		c.add(tl);
		c.add(cm);
		c.add(mh);
		c.add(ch);
	      
		setVisible(true);
	}
	
	class MyDialog extends JDialog {                                                        //비밀번호 입력을 위한 다이얼 로그
		
		private JTextField tf = new JTextField(10);                                         //다이얼로그 입력 길이 (10)
		private JButton okButton = new JButton("접속");                                      //접속버튼 부착

		public MyDialog(JFrame frame, String title) {
			
			super(frame,title);
			setLayout(new FlowLayout());
			add(tf);
			add(okButton);
			setLocationRelativeTo(null);
			setSize(300, 100);

			okButton.addActionListener(new ActionListener() {                               //OK버튼을 누르면 반응함
				public void actionPerformed(ActionEvent e) {
					
					setVisible(false);
					
					if(passWord.equals( dialog.getInput())) {                               //설정한 패스워스와 다이얼로그 텍스트 필드에 입력한 값이 같으면 매출 및 판매현황이 보임
		                  new Sales();
		                  tf.setText("");                                                   //다이얼 로그에 입력후 입력창(레이블)을 빈칸으로 리셋
					} else if(tf.getText().length() == 0) {
						JOptionPane.showMessageDialog(null, "비밀번호를 입력하지 않았습니다. 입력해 주세요", "비밀번호 오류!", JOptionPane.ERROR_MESSAGE);   //비밀번호를 입력하지 않으면 경고메세지 출력
						setVisible(true);
						tf.setText("");                                                     //다이얼 로그에 입력후 입력창(레이블)을 빈칸으로 리셋
					} else {
						JOptionPane.showMessageDialog(null, "비밀번호가 틀립니다. 다시 입력해 주세요", "비밀번호 오류!", JOptionPane.ERROR_MESSAGE);       //틀리면 경고메세지 출력
						setVisible(true);
						tf.setText("");                                                     //다이얼 로그에 입력후 입력창(레이블)을 빈칸으로 리셋
					}
				}
			});
		}
		      
		public String getInput() {
			if(tf.getText().length() == 0) return null;                                     //다이얼로그에 입력받는 값을 호출 할 수 있도록 메소드 설정
			else return tf.getText();
		}

	}
	   
	private void createMenu() {                                                             //"매출 및 판매현황" , "나가기" 두가지 메뉴를 만드는 메소드
	   
		JMenuBar mb = new JMenuBar();
		JMenuItem [] menuItem = new JMenuItem[2];
		String [] itemTitle = {"매출 및 판매현황" , "나가기"};         
		JMenu screenMenu = new JMenu("메뉴");
	      
		MenuActionListener listener = new MenuActionListener();
		
		for(int i=0; i<menuItem.length; i++) {                                              //각 메뉴아이템을 메뉴와 리스너에 달기 
			menuItem[i] = new JMenuItem(itemTitle[i]); 
			menuItem[i].addActionListener(listener);                                        //리스너달기
			screenMenu.add(menuItem[i]);                                                    //메뉴에 부착
		}
		mb.add(screenMenu);
		setJMenuBar(mb);
	}
	   
	class MenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			String cmd = e.getActionCommand();
			
			switch(cmd) {
			case "매출 및 판매현황" :                                                            //해당 메뉴를 선택하면 바로 매출 및 판매현황이 나타나지 않고 다이얼로그가 실행되어 <비밀번호>를 입력 할 수 있음.
				dialog.setVisible(true);
				break;
	                
			case "나가기" :
				System.exit(0);                                                             //프로그램 종료
				break;
			}
		}
	}
	
	class Sales extends JFrame {
		private JPanel jp1 = new JPanel();                                                  //자판기 메뉴의 각 사진과 판매량 (패널)
		private JPanel jp2 = new JPanel();                                                  //총 매출 현황 (패널)
		private JLabel [] num =  new JLabel[9]; 
		private JLabel money;
		      
		public Sales() {
			setTitle("매출 및 판매현황");
			setLayout(new BorderLayout(0,20));
			jp1.setLayout(new GridLayout(3, 5, 5,5));
			for(int i = 0 ; i<9; i++ ) {
				num[i] = new JLabel("<html>판매 개수: " +cell[i]+ "  개" , images[i] ,SwingConstants.CENTER);   
				jp1.add(num[i]);                                                            //판매개수를 일목요연하게 확인 할 수 있음  <html>/<br>이 소스코드는 라벨 내의 텍스트를 한줄 띄워줌
			}
		         
			money = new JLabel("총 판매 매출은  " + MoneySum + "  입니다.");                       //총 판매매출을 보여주는 라벨
			money.setOpaque(true);
			money.setBackground(Color.LIGHT_GRAY);
		              
			jp2.add(money);                                                                 //아래 패널에 리셋버튼과 매출현황 라벨을 추가 시킨다.
		         
			add(jp1,BorderLayout.CENTER);
			add(jp2,BorderLayout.SOUTH);
		      
			setSize(1600,1000);
			setVisible(true);
		}
	}

	class TitleLabel extends JPanel {
	      
		private ImageIcon icon = new ImageIcon("images\\타이틀.jpg");                         //커피 자판기 디자인이 되어있는 이미지 (자판기 가장 상단 패널에 부착)
		private Image img = icon.getImage();
	      
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img, 0, 0, getWidth(), getHeight(), this);  
		}
	}
	   
	class MoneyHole extends JPanel {
	   
		private JButton [] btn = { new JButton("10,000 won"), new JButton("5,000 won"), new JButton("1,000 won"),new JButton("500 won")};   //돈을 투입할 수 있도록 각 금액별 버튼을 설정
	      
		public MoneyHole() {
			MyMoneyListener listener = new MyMoneyListener();
			for(int i=0; i<4; i++) {                                                        //돈 투입 버튼 (10,000원 , 5,000원, 1,000원 , 500원) 
				btn[i].setBackground(Color.WHITE);
	            btn[i].setFont(new Font("Arial", Font.BOLD, 35));
	            add(btn[i]);
	            btn[i].addActionListener(listener);
			}
			sumLabel.setFont(new Font("Arial", Font.BOLD, 35));
			add(sumLabel);                                                                  //이용자의 돈이 표시되는 라벨 부착
	         
			setBackground(Color.WHITE);
		}
	      
		class MyMoneyListener implements ActionListener {                                   //금액 버튼을 누르면 해당 금액 만큼 이용자의 돈이 올라간다.
	          
			public void actionPerformed(ActionEvent e) {
				JButton b = (JButton)e.getSource();
				if(b == btn[0]) {
					UserMoneySum += 10000;                                                  //10000원 투입
				} else if(b == btn[1]) {
					UserMoneySum += 5000;                                                   //5000원 투입
				} else if(b == btn[2]) {
					UserMoneySum += 1000;                                                   //1000원 투입
				} else {
					UserMoneySum += 500;                                                    //500원 투입
				}
				sumLabel.setText(UserMoneySum + "  won");                                   //이용자의 현재 투입금액을 볼 수 있도록 설정
			}
		}
	}
	   
	class CoffeeButtons extends JPanel {
		private JButton [] btn = new JButton[9];
		private JLabel [] la = new JLabel[9];
		      
		public CoffeeButtons() {
			setLayout(new GridLayout(3, 3, 3, 0));
		    	 
			int price = 1500;
			MyMenuListener listener = new MyMenuListener();
		         
			for(int i = 0 ; i<9; i++ ) {                                                    // 자판기에 판매되는 커피(이미지)을 정렬 부착 
				if(i>5) price = 1500;
		            
				btn[i] = new JButton(price+"원", images[i] );   
				add(btn[i]);
				btn[i].addActionListener(listener);                                         //자판기 메뉴 버튼에 리스너 달기
			}
		         
			setBackground(Color.blue);
		}
		
		class MyMenuListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
		             
				for(int i = 0 ; i<9; i++ ) {
					if(UserMoneySum >= foodPrice[i]) {
						
						JButton b = (JButton)e.getSource();
						if(b == btn[i]) {
							if(foodNum[i] > 0) {
		                    	  
								SubMenu sm = new SubMenu(); 
								cell[i]++;                                                  //판매 갯수 반영
		                    	break;
							}
		                      
						}      
					} else {
						JOptionPane.showMessageDialog(null, "금액이 부족합니다! 돈을 더 투입하세요", "잔액 부족!", JOptionPane.ERROR_MESSAGE);
						break;                                                              //금액이 부족하면 돈을 투입하라는 경고 메세지 다이얼로그
					}
		            	
				}   
		             
			}
		}
	}
	
	class SubMenu extends JDialog {
		private JButton [] btn = { new JButton("<html>ice<br>(+0won)", imagesI), new JButton("<html>hot<br>(+0won)", imagesH), new JButton("<html>one shot<br>(+500won)", imagesS) };
		public SubMenu() {
			
			Container c = getContentPane();
			setLayout(new GridLayout(0, 3, 3, 0));
			MyMenuListener listener = new MyMenuListener();
			int price = 1500;
			for(int i = 0 ; i<3; i++ ) {
		          
				if(i>1) price = 2000;
				add(btn[i]);
				btn[i].addActionListener(listener);
			}
		         
			setBackground(Color.blue);
			setTitle("부가메뉴");
			setSize(500,300);
			setLocationRelativeTo(null);
			setVisible(true);
		}
		
		class MyMenuListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				for(int i = 0 ; i<9; i++) {
			            	
					if(UserMoneySum >= foodPrice1[i]) {
						JButton b = (JButton)e.getSource();
						
						if(b == btn[i]) {
							if(foodNum[i] > 0) {
								
								Coffees fd = new Coffees();                                  //메뉴 버튼을 누르면 해당 커피의 완성품이 새로운 프레임과 함께 나오도록 설정
								if (i == 0){ 
									fd.add(new JLabel("<html>아이스<br>주문번호" +num+ "  번<br>잠시만기다려주세요^^" , imagesC, SwingConstants.CENTER));  //선택한 커피의 완성품 이미지
								} else if (i == 1){
									fd.add(new JLabel("<html>핫<br>주문번호" +num+ "  번<br>잠시만기다려주세요^^" , imagesC, SwingConstants.CENTER));
								} else {
									fd.add(new JLabel("<html>샷추가<br>주문번호" +num+ "  번<br>잠시만기다려주세요^^" , imagesC, SwingConstants.CENTER));
								}
								UserMoneySum -= foodPrice1[i];                               //이용자의 돈이 선택한 메뉴의 액수만큼 차감
			                         
								num++;
								MoneySum += foodPrice1[i];                                   //매출 반영
								sumLabel.setText( UserMoneySum + "  won");
								dispose();	                                                 //이용자의 돈이 새롭게 set(설정)
								break;             
							}
			                      
						}      
					}
			               		            	
				}   
			}
		}
	}
	   
	class Coffees extends JFrame {                                                           //선택한 커피의 완성품이 이 프레임에서 나옴
		public Coffees() {
			
			setTitle("커피  맛있게 드세요~!");
			Container c = getContentPane();
			c.setLayout(new FlowLayout());
	         
			setSize(400,300);
			setLocationRelativeTo(null);
			setVisible(true);
		}
	}
			
	class Chages extends JPanel {
		private JButton btn = new JButton("잔돈 반환");                                         //자판기 가장 하단부 패널에 잔돈 반환 버튼
		private JLabel la = new JLabel("");
		public Chages() {
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					la.setFont(new Font("Arial", Font.ITALIC, 20));
		                 
					if(UserMoneySum > 0) {                                                   //이용자의 금액이 0원보다 많으면 해당 금액이 반환되었다는 문구 출력
						la.setText(UserMoneySum + " won returned!  Thank you for using");      
						UserMoneySum=0;                                                      //문구 출력과 함께 자판기에 보이는 이용자 금액은 0원으로 리셋
						sumLabel.setText(UserMoneySum + "  won");
					} else 
						la.setText("NO MONEY!   Please use after confirmation");             //자판기에 이용자의 돈이 없으면 돈이 없으니 확인후 이용해 달라는 문구 출력
				}
			}); 
		    	  
			btn.setBackground(Color.LIGHT_GRAY);
			setBackground(Color.WHITE);
			add(btn); add(la);
		}
	      
	}
	   
	public static void main(String[] args) {
		new VendingMachine();

	}
}