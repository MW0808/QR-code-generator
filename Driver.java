import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;

public class Driver 
{
public static void main(String[] args)
{
	JFrame frame = new JFrame();
	Panel panel = new Panel();
	frame.setSize(800,800);
	frame.add(panel);
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	int size = 21;
	Random rand = new Random();
		
		int[][] pattern = new int[size][size];
		
		for(int i = 0; i < pattern.length;i ++) {
			for(int j = 0; j < pattern[i].length;j++) {
				pattern[i][j] = rand.nextInt(2);
			}
		}
		
		pattern = patternGen();
		printArr2d(pattern);
		panel.pattern = pattern;
		panel.repaint();
}

static int[][] patternGen() {
	
	Scanner kb = new Scanner(System.in);
	String str = kb.nextLine();
	int[][] pattern = new int[21][21];
	
	String encodedWord = "0100";
	encodedWord = encodedWord + toBinary(str.length(),8);
	
	for (int i = 0; i < str.length(); i++)
	{
		encodedWord = encodedWord + toBinary((int)str.charAt(i), 8);
	}
	
	for (int i = 0; i < 17 - str.length(); i++)
	{
		encodedWord = encodedWord + "00000000";
	}
	encodedWord = encodedWord + "0000";
	
	int[] message = new int[19];
	for (int i = 0; i < 19; i++)
	{
		message[18 - i] = Integer.parseInt(encodedWord.substring(i*8, i*8+8),2);
	}
	
	int[] generatorExp = {21, 102, 238, 149, 146, 229, 87, 0};
	Polynomial polyMessage = new Polynomial(message);
	Polynomial polyGen = new Polynomial(generatorExp,true);
	
	polyMessage = polyMessage.mul(singleTermPoly(1,7));
	Polynomial polyEcc = polyMessage.div(polyGen)[1];
	
	System.out.print(polyEcc);
	for (int i = 0; i < polyEcc.coefficients.length; i++)
	{
		System.out.print(polyEcc.coefficients[i] + " ");
	}
	
	
	String ecc = "";
	for (int i = 0; i < 7; i++)
	{
		ecc = toBinary(polyEcc.coefficients[i],8) + ecc;
	}
	
	encodedWord = encodedWord + ecc;
	drawBrick(pattern, 0,0);
	drawBrick(pattern, 14,0);
	drawBrick(pattern, 0,14);
	printArr2d(pattern);
	
	drawCodeWordNoConvert(pattern, 17, 19, 0, encodedWord.substring(0,8));
	drawCodeWordNoConvert(pattern, 13, 19, 0, encodedWord.substring(8, 16));
	drawCodeWordNoConvert(pattern, 9, 19, 0, encodedWord.substring(16,24));
	
	drawCodeWordNoConvert(pattern, 9, 17, 1, encodedWord.substring(24,32));
	drawCodeWordNoConvert(pattern, 13, 17, 1, encodedWord.substring(32, 40));
	drawCodeWordNoConvert(pattern, 17, 17, 1, encodedWord.substring(40, 48));
	
	drawCodeWordNoConvert(pattern, 17, 15, 0, encodedWord.substring(48, 56));
	drawCodeWordNoConvert(pattern, 13, 15, 0, encodedWord.substring(56, 64));
	drawCodeWordNoConvert(pattern, 9, 15, 0, encodedWord.substring(64, 72));
	
	drawCodeWordNoConvert(pattern, 9, 13, 1, encodedWord.substring(72,80));
	drawCodeWordNoConvert(pattern, 13, 13, 1, encodedWord.substring(80,88));
	drawCodeWordNoConvert(pattern, 17, 13, 1, encodedWord.substring(88,96));
	
	drawCodeWordNoConvert(pattern, 17, 11, 0, encodedWord.substring(96,104));
	drawCodeWordNoConvert(pattern, 13, 11, 0, encodedWord.substring(104,112));
	drawCodeWordNoConvert(pattern, 9, 11, 0, encodedWord.substring(112,120));	
	drawCodeWordNoConvertSpecial(pattern, 4, 11, 0, encodedWord.substring(120,128));
	drawCodeWordNoConvert(pattern, 0, 11, 0, encodedWord.substring(128,136));
	
	
	drawCodeWordNoConvert(pattern, 0, 9, 1, encodedWord.substring(136,144));
	drawCodeWordNoConvertSpecial(pattern, 4, 9, 1, encodedWord.substring(144,152));
	drawCodeWordNoConvert(pattern, 9, 9, 1, encodedWord.substring(152,160));
	drawCodeWordNoConvert(pattern, 13, 9, 1, encodedWord.substring(160,168));
	drawCodeWordNoConvert(pattern, 17, 9, 1, encodedWord.substring(168,176));
	
	drawCodeWordNoConvert(pattern, 9, 7, 0, encodedWord.substring(176,184));
	drawCodeWordNoConvert(pattern, 9,4, 1, encodedWord.substring(184,192));
	drawCodeWordNoConvert(pattern, 9,2, 0, encodedWord.substring(192,200));
	drawCodeWordNoConvert(pattern, 9,0, 1, encodedWord.substring(200,208));
	
	int[][] mask = maskGen();
	printArr2d(mask);
	XOR(pattern,mask);
	drawFormatBits(pattern);
	drawTimingBits(pattern);
	System.out.println();
	return pattern;
	
}

static void printArr2d(int[][] arr) {
	for(int i = 0; i < arr.length;i ++) {
		for(int j = 0; j < arr[i].length;j++) {
			System.out.print(arr[i][j]+" ");
		}
		System.out.println();
	}
}
	 
static int[][] maskGen(){
	int[][] mask = new int[21][21];
	for(int i = 0;i < mask.length;i ++) {
		for(int j = 0;j < mask.length;j ++) {
			if(i%2==0 && j%2==0)
				mask[i][j] = 1;
			else if(i%2==1 && j%2==1) {
				mask[i][j] = 1;
			}
		}
	}
	for(int i = 0;i < 9;i ++) {
		for(int j =0 ; j < 9;j ++) {
			mask[i+0][j+0] = 0;
		}
	}
	for(int i = 0;i < 8;i ++) {
		for(int j =0 ; j < 8;j ++) {
			mask[i+13][j+0] = 0;
			mask[i+0][j+13] = 0;
		}
	}
	return mask;
}

static void XOR(int[][] arr,int[][] mask) {
	for(int i = 0; i < arr.length;i ++) {
		for(int j = 0; j < arr[i].length;j++) {
			if((arr[i][j]==1 && mask[i][j]==0)||(arr[i][j]==0 && mask[i][j]==1))
				arr[i][j] = 1;
			else
				arr[i][j] = 0;
				
		}
	}
}

static void drawFormatBits(int[][] arr) {
	int[] horizontal0 = {1,1,1,0,1,1};
	int[] horizontal1 = {1,1};
	int[] horizontal2 = {1,1,0,0,0,1,0,0};
	int[] vertical0 = {0,0,1,0,0,0,0};
	int[] vertical1 = {1,1};
	int[] vertical2 = {1,1,1,1,0,1,1,1};
	
	for(int i = 0;i < horizontal0.length;i ++) {
		arr[8][i] = horizontal0[i];
	}
	for(int i = 0;i < horizontal1.length;i ++) {
		arr[8][i+7] = horizontal1[i];
	}
	for(int i = 0;i < horizontal2.length;i ++) {
		arr[8][i+13] = horizontal2[i];
	}
	for(int i = 0;i < vertical0.length;i ++) {
		arr[i][8] = vertical0[i];
	}
	for(int i = 0;i < vertical1.length;i ++) {
		arr[i+7][8] = vertical1[i];
	}
	for(int i = 0;i < vertical2.length;i ++) {
		arr[i+13][8] = vertical2[i];
	}
}

static void drawTimingBits(int[][] arr) {
	int[] bits = {1,0,1,0,1};
	for(int i = 0;i < bits.length;i ++) {
		arr[6][i+8] = bits[i];
	}
	for(int i = 0;i < bits.length;i ++) {
		arr[i+8][6] = bits[i];
	}
}



static void drawBrick(int arr[][], int x, int y)
{
	for (int i = 0; i < 7; i++)
	{
		for(int j = 0; j < 7; j++)
		{
			if ((i == 1 && j > 0 && j < 6) || ( i == 5 && j != 0 && j != 6) || (i > 0 && i < 6 && ( j == 1 || j ==5)))
			{
				arr[i+x][j+y] = 0;
			}
			else
			{
				arr[i+x][j+y] = 1;
			}
		}
	}
}

static void drawCodeWordNoConvert(int arr[][],int x,int y,int type,String word) {

	
	if(type == 1){
		int c = 7;
		for(int i = 3; i >= 0;i --) {
			for(int j = 0; j < 2;j++) {
				if(word.charAt(c) == '1')
					arr[i+x][j+y] = 1;
				else
					arr[i+x][j+y] = 0;
				c --;
			}
			
		}
	}else {
		for(int i = 0; i < 4;i ++) {
			for(int j = 0; j < 2;j++) {
				if(word.charAt((word.length()-1)-(i*2+j)) == '1')
					arr[i+x][j+y] = 1;
				else
					arr[i+x][j+y] = 0;
			}
		}
	}
}
	
static void drawCodeWordNoConvertSpecial(int arr[][],int x,int y,int type,String word) {
	if(type == 1){
		int c = 7;
		for(int i = 3; i >= 0;i --) {
			for(int j = 0; j < 2;j++) {
				//System.out.println(i*2+j);
				if(i > 1) {
					if(word.charAt(c) == '1')
						arr[i+x+1][j+y] = 1;
					else
						arr[i+x+1][j+y] = 0;
				} else {
					if(word.charAt(c) == '1')
						arr[i+x][j+y] = 1;
					else
						arr[i+x][j+y] = 0;
				}

				c --;
			}
			
		}
	}else {
		for(int i = 0; i < 4;i ++) {
			for(int j = 0; j < 2;j++) {
				if(i < 2) {
					if(word.charAt((word.length()-1)-(i*2+j)) == '1')
						arr[i+x][j+y] = 1;
					else
						arr[i+x][j+y] = 0;
				}else {
					if(word.charAt((word.length()-1)-(i*2+j)) == '1')
						arr[i+x+1][j+y] = 1;
					else
						arr[i+x+1][j+y] = 0;
				}

			}
		}
	}
}
	

static Polynomial singleTermPoly(int coefficient, int order)
{
	int[] coe = new int[order+1];
	coe[order] = coefficient;
	return new Polynomial(coe);
}

static String toBinary(int num, int len)
{
	String str = Integer.toBinaryString(num);
	
	if (str.length() > len)
	{
		return str.substring(str.length() - len);
	}
	else {
		for(int i = str.length(); i < len; i++)
		{
			str = "0" + str;
		}
	}
	
	return str;
}

}
