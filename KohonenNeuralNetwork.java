package kohonen;
/* 
 * Zachary Boster
 * 9th December 2022
 * Implementation of kohonen neural network
 */ 
import java.awt.*;
import javax.swing.JFrame;
import java.util.Random;

public class KohonenNeuralNetwork extends Canvas
{
	public static void main(String[] args) 
	{
		KohonenNeuralNetwork kohonen = new KohonenNeuralNetwork();
		JFrame f = new JFrame();
		f.add(kohonen);
		f.setSize(700,700);
		f.setVisible(true);
	}
	
	public void paint(Graphics g) 
	{
		this.kohonenMethod(g);
	}
	
	public void kohonenMethod(Graphics g) 
	{
		double[][] kohonen = initWeights(500, 2);
		int iterations = 15000;
		double lambda = 0.1;
		trainKohonen(kohonen, g, iterations, lambda);
	}
	
	public static double [][] initWeights(int neurons, int numOfValues)
	{
		double [][] weights = new double[neurons][numOfValues];
		Random rand = new Random();
		for (int i = 0; i < neurons; i++) 
		{
			for (int j = 0; j < numOfValues; j++) 
			{
				weights[i][j] = rand.nextDouble() * 600;
			}
		}
		return weights;
	}
	
	public void trainKohonen(double[][] kohonen, Graphics g, int iterations, double lambda)  
	{
		int count = 0;
		double deltaLambda = lambda / iterations;
		Random rand = new Random();
		double [] point = new double[2];
		int[] winningNeurons = new int[kohonen.length];
		for (int i = 0; i < kohonen.length; i++) 
		{
			winningNeurons[i] = 0;
		}
		
		while (lambda > 0 && count < iterations)
		{
			Boolean legalPoint = false;
			while (legalPoint == false)
				{
					point[0] = rand.nextDouble() * 600;
					point[1] = rand.nextDouble() * 600;
					if ((point[0] < 200 && point[1] > 400) || 
						(point[0] < 200 && point[1] < 200) || 
						(point[0] > 200 && point[1] < 400 && point[1] > 200)) 
					{
						legalPoint = true;
					}
				}
			int winner = 0;
			double dist = 0;
			for (int i = 0; i < kohonen[0].length; i++) 
			{
				dist += Math.pow(kohonen[0][i] - point[i],2);
			}
			for (int i = 0; i < kohonen.length; i++)
			{
				double dist2 = 0;
				for (int j = 0; j < kohonen[0].length; j++) 
				{
					dist2 += Math.pow(kohonen[i][j] - point[j], 2);
				}
				if (dist2 < dist) 
				{
					winner = i;
					dist = dist2;
				}
			}
			winningNeurons[winner] = 1;
			for (int i = 0; i < kohonen[0].length; i++)
			{
				kohonen[winner][i] += lambda * (point[i] - kohonen[winner][i]);
			}
			lambda -= deltaLambda;
			
			if (count % 10 == 0 || count < 250)
			{
				drawKohonen(kohonen, g, winningNeurons);
			}
			count++;
		}
	}
	
	public void drawKohonen(double[][] kohonen, Graphics g, int[] winningNeurons) 
	{
		g.setColor(Color.white);
		g.fillRect(0,0,605,605);
		g.setColor(Color.black);
		for (int i = 0; i < kohonen.length; i++) 
		{
			if (winningNeurons[i] == 1) 
			{
				g.setColor(Color.blue);
				g.drawString("K", (int) kohonen[i][0], (int) kohonen[i][1]);
			}
			else 
			{
				g.setColor(Color.red);
				g.drawString("X", (int) kohonen[i][0], (int) kohonen[i][1]);
			}
		}
	}
}
