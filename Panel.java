import java.awt.Graphics;
import javax.swing.JPanel;

public class Panel extends JPanel
{
	String shape;
	int[][] pattern;
	Panel(){
		
			
	}
	
	
	public void paintComponent(Graphics g)
	{
		int x = 0;
		int y = 0;
		
		if (pattern != null)
		{
			for (int i = 0; i < 21; i++)
			{
				for (int j = 0; j < 21; j++)
				{
					if (pattern[i][j] == 1)
					{
						g.fillRect(x, y, 20, 20);
					}
					x += 20;
				}
				x = 0;
				y += 20;
			}
		}
		
		/*if(shape!=null) {
			switch(shape) {
			case "line": 	g.drawLine(0,0, 100,100);
							break;
			case "rect": 	g.fillRect(0,0, 100,100);
							break;
			case "circle": 	g.fillOval(0,0, 100,100);
							break; */

		}

}


