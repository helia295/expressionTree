/* Calculate.java
 * 
 * This file is to test Expression tree implementation
 * 
 * Part of HW 5
 */


import java.util.Set;
import java.util.HashMap;
import java.util.Scanner;
import java.io.IOException;


public class Calculate
{
	public static void main(String[] args) throws IOException
	{
		Scanner userIn = new Scanner(System.in);
		boolean anotherExpression = true;
		while(anotherExpression) {

			System.out.print("Please type your expression (with a single space between each token): ");
			String strExpr = userIn.nextLine();
			
			System.out.print("Please type 2nd expression to compare: ");
			String strExpr2 = userIn.nextLine();

			Expression expr = Expression.expressionFromPostfix(strExpr.split(" "));
			Expression expr2 = Expression.expressionFromPostfix(strExpr2.split(" "));
	 
			try {
				expr.drawExpression("expr.dot");
			} catch (IOException e) {
			
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
			
			System.out.println("Expression 1: ");
			System.out.println("Postfix: " + expr.toPostfix());
			System.out.println("Prefix: " + expr.toPrefix());
			System.out.println("Infix: " + expr.toInfix());
			
			System.out.println("");
			System.out.println("Expression 2: ");
			System.out.println("Postfix: " + expr2.toPostfix());
			System.out.println("Prefix: " + expr2.toPrefix());
			System.out.println("Infix: " + expr2.toInfix());
			System.out.println("True/False? Expression 1 equals to Expression 2: " + expr.equals(expr2));

			Expression simple = expr.simplify();
			Expression simple2 = expr2.simplify();
			System.out.println("\nSimplified version of expression 1 : " + simple);
			System.out.println("\nSimplified version of expression 2 : " + simple2);

			
			Set<String> variables = expr.getVariables();
			HashMap<String, Integer> assignment = new HashMap<String, Integer>();
			boolean anotherAssignment = true;
			 
			while(variables.size() > 0 && anotherAssignment)
			{
				System.out.println("\nPlease assign integer values to the variables of expression 1:");
				
				for(String v : variables) {
					System.out.print(v + " = ");
					int i = userIn.nextInt();
					assignment.put(v, i);
				}
					  
				assignment.put("not_yet_implemented", 0);
				

				System.out.println("\nThe expression 1 evaluates to: " + expr.evaluate(assignment));
				
				System.out.println("The simplified expression 1 evaluates to: " + simple.evaluate(assignment));
				
				System.out.print("Would you like to reassign the variables for expression 1 (y/n)? ");
				String answer = userIn.next();
				if(!answer.equalsIgnoreCase("y"))
				{
					anotherAssignment = false;
				}
			}

			System.out.print("Would you like to type another expression (y/n)? ");
			String answer = userIn.nextLine();
			if(!answer.equalsIgnoreCase("y"))
			{
				anotherExpression = false;
			} else {
				continue;
			} 
		}
	}
}
