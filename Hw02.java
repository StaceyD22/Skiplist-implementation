import java.util.Random;
import java.io.*;
import java.util.Scanner;

class Node{
	Node left, right, up, down;
	int data;
	Node(int value){
		left = null;
		right = null;
		up = null;
		down = null;
		data = value;
	}
}
public class Hw02 {
	Node head, tail;
	int maxlvl;
	
	// Create list containing min and max java values
	Hw02(){
		maxlvl = 0;
		head = new Node(Integer.MIN_VALUE);
		tail = new Node(Integer.MAX_VALUE);
		head.right = tail;
		tail.left = head;
	}
	
	int coinFlip(Random number)
	{
		int coin = number.nextInt();
		coin = coin % 2;
		return coin;
	}
	
	void insert(int x, int coin, Random number)
	{
		int height = 0;
		
		while (coin == 1)
		{
			height++;
			coin = coinFlip(number);
		}
		
		while (maxlvl < height)
		{
			Node tempHead = new Node(Integer.MIN_VALUE);
			Node tempTail = new Node(Integer.MAX_VALUE);
			head.up = tempHead;
			tail.up = tempTail;
			tempTail.down = tail;
			tempHead.down = head;
			head = tempHead;
			tail = tempTail;
			head.right = tail;
			tail.left = head;
			maxlvl++;
		}
		
		// Start insertion at the desired level
		Node t = findLowest(head);
		Node above = null;
		
		if (height == 0)
			t = insertRec(t, above, x, height, 0);
		
		else
		{
			for (int i = 0; i < height; i++)
				t = t.up;
			
			// insert recursively from desired point
			t = insertRec(t, above, x, height, 0);
		}
	}
	
	Node insertRec(Node t, Node above, int x, int height, int level) {
		
		// Keep moving forward
		if (t.right.data < x)
			return insertRec(t.right, above, x, height, level);
		
		// the data to the right is not greater than or equal to x (thus it is less than)
		else
		{
			// is the level the same as the height (this is as far as we need to go)
			if (level == height)
			{
				Node temp = createNode(t, x);
				if (above != null)
				{
					above.down = temp;
					temp.up = above;
				}
				
				return t;
			}
			
			else if (level < height)
			{
				Node temp = createNode(t, x);
				if (above != null)
				{
					above.down = temp;
					temp.up = above;
				}
				
				above = temp;
				
				return insertRec(t.down, above, x, height, level+1);
			}
		}
		return t;
	}
	
	Node createNode(Node t, int x)
	{
		Node temp = new Node(x);
		temp.left = t;
		temp.right = t.right;
		t.right = temp;
		if (temp.right != null)
			temp.right.left = temp;
		return temp;
	}
	
	boolean find(int x) {
		Node t = this.head;
		while (t != null)
		{
			if (t.data < x)
				t = t.right;
			else if (t.data > x)
				t = t.left.down;
			else
				return true;
		}
		
		return false;
	}
	
	Node search(Node t, int x) {
		while (t != null)
		{
			if (t.right.data < x)
				t = t.right;
			else if (t.right.data > x)
				t = t.down;
			else
				return t.right;
		}
		
		return null;
	}
	
	// uses search and deletes all instances of x
	boolean delete(int x) {
		Node t = search(this.head, x);
		
		if (t == null)
		{
			return false;
		}
		
		while (t != null)
		{
			t.left.right = t.right;
			t.right.left = t.left;
			t.up = null;
			t = t.down;
		}
		return true;
	}

	
	void print() {
		printList(this.head);
	}
	
	void printList(Node t) {
		
		if (t.data == Integer.MAX_VALUE)
		{
			printTower(t);
			return;
		}
		
		t = findLowest(t);
		printTower(t);
		
		printList(t.right);
	}
	
	Node findLowest(Node t) {
		while (t.down != null)
			t = t.down;
		return t;
	}
	
	void printNode(Node t) {
		System.out.print(" "+t.data+"; ");
	}
	
	void printTower(Node t) {

		if (t.data == Integer.MIN_VALUE)
		{
			System.out.println("---infinity");
		}
		else if (t.data == Integer.MAX_VALUE)
		{
			System.out.println("+++infinity");
		}
		
		else
		{
			printNode(t);
			while (t.up != null)
			{
				t = t.up;
				printNode(t);
			}
			System.out.println("");
		}
		return;
	}
	
	static void complexityIndicator() {
		System.err.println("St727876;3.0;5");
	}
	
	public static void main(String[] args) {
		complexityIndicator();
		if (args.length == 0)
			return;
		
		File in = null;
		try
		{
			in = new File(args[0]);
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		System.out.println("For the input file named "+args[0]+":");
		
		long seed;
		if (args.length > 1)
		{
			seed = Integer.parseInt(args[1]);
			System.out.println("With the RNG seeded at "+seed+",");
		}
		else
		{
			seed = 42;
			System.out.println("With the RNG unseeded,");
		}
		
		Scanner input = null;
		try
		{
			input = new Scanner(in);
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
		Hw02 skippy = new Hw02();
		boolean print = false;
		try
		{
			Random number = new Random(seed);
			
          	while (input.hasNext())
			{
            	String i = new String(input.next());
           		int x = 0;
           		boolean isPresent = false;
				
        		switch (i)
				{
				case "i":
					x = input.nextInt();
					isPresent = skippy.find(x);
					if (isPresent)
						break;
					int coin = skippy.coinFlip(number);
					skippy.insert(x, coin, number);
  					break;
				case "d":
					x = input.nextInt();
					isPresent = skippy.delete(x);
					if (isPresent)
						System.out.println(x+" deleted");
					else
						System.out.println(x+" integer not found - delete not successful");
					break;
				case "s":
					x = input.nextInt();
					isPresent = skippy.find(x);
					if (isPresent)
						System.out.println(x+" found");
					else
						System.out.println(x+" NOT FOUND");
					break;
				case "p":
					System.out.println("The current Skip List is shown below:");
					skippy.print();
					System.out.println("---End of Skip List---");
					break;
				case "q":
					break;
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
}