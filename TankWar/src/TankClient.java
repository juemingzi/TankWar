import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;


import java.util.ArrayList;

public class TankClient extends Frame{

	public static final int GAME_WIDE = 800;
	public static final int GAME_HEIGHT = 600;
	Tank myTank = new Tank(50,50,true,Tank.Direction.STOP,this);
	
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();
	
	Image offScreenImage = null;//创建背后虚拟的图像
	
	public void paint(Graphics g) {
		g.drawString("missiles count: "+missiles.size(), 10, 50);
		g.drawString("missiles count: "+explodes.size(), 10, 60);
		g.drawString("missiles count: "+tanks.size(), 10, 70);
		
		for(int i=0;i<missiles.size();i++){
			Missile m = missiles.get(i);
			m.draw(g);
			m.hitTanks(tanks);
			m.hitTank(myTank);
		}
		
		for(int i=0;i<explodes.size();i++){
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		for(int i=0;i<tanks.size();i++){
			Tank t = tanks.get(i);
			t.draw(g);
		}
		myTank.draw(g);
	}
	
	public void update(Graphics g) {//为了实现双缓冲（double buffer）
		if(offScreenImage == null){
			offScreenImage = this.createImage(GAME_WIDE, GAME_HEIGHT);
		}
		Graphics gOffiScreen = offScreenImage.getGraphics();//背后图片的画笔
		
		Color c = gOffiScreen.getColor();
		gOffiScreen.setColor(Color.DARK_GRAY);
		gOffiScreen.fillRect(0, 0, GAME_WIDE, GAME_HEIGHT);
		gOffiScreen.setColor(c);
		
		paint(gOffiScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	public void launchFrame(){
		
		for(int i=0;i<10;i++){
			tanks.add(new Tank(50+40*(i+1),50,false,Tank.Direction.D,this));
		}
		
		this.setLocation(300,100);
		this.setSize(GAME_WIDE, GAME_HEIGHT);
		this.setTitle("TankWar");
		
		this.addWindowListener(new WindowAdapter(){

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		
		this.setResizable(false);
		this.setBackground(Color.DARK_GRAY);	
		
		this.addKeyListener(new KeyMonitor());
		
		setVisible(true);
		new Thread(new PaintThread()).start();
	}
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();
	}
	
	
	private class PaintThread implements Runnable{
		public void run(){
			while(true){
				repaint();//调用的外面包装类的repaint方法
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter{

		public void keyReleased(KeyEvent e) {
			myTank.keyRelesed(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
	}
}
