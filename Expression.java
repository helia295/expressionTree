/* Expression.java
 * 
 * This file implements the whole expression tree
 * 
 * Part of HW 5
 */


import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Stack;

/*
 *  Note: the indentation and placment of {'s and }'s is somewhat
 * of a mess in this file.  That is on purpose to give you (the student)
 * a chance to see what it's like working in code that has arbitrary
 * and inconsistent style.  I encourage you to clean it up for your 
 * submission (and sanity!).
 * 
 * /

/**
 * A class representing an abstract arithmetic expression
 */
public abstract class Expression
{	
	static Stack<Expression> stack;
   /**
    * Creates a tree from an expression in postfix notation
    * @param postfix an array of Strings representing a postfix arithmetic expression
    * @return a new Expression that represents postfix
    */
	public static Expression expressionFromPostfix(String[] postfix)
	{
		stack = new Stack<Expression>();
		Expression sum = null;
		Expression difference = null;
		Expression product = null;
		Expression quotient = null;
	
		for (int i = 0; i < postfix.length; i++) {
			try { //check to see if the element is an Integer
				
				int num = Integer.parseInt(postfix[i]);
				Expression intoperand = new IntegerOperand(num);
				stack.push(intoperand);
				
			} catch (NumberFormatException e) {
				
				//check to see if the element is an Operator
				if (postfix[i].equals("+") || postfix[i].equals("-") 
						|| postfix[i].equals("*") || postfix[i].equals("/")) {
							
					if (postfix[i].equals("+")) { //check if it's a Sum
						
						if (stack.peek() != null) {
							Expression right =  stack.pop();
							Expression left = stack.pop();
							sum = new SumExpression(left, right);
							stack.push(sum);
						}
						
					} else if (postfix[i].equals("-")) { //check if it's a Difference
						
						if (stack.peek() != null) {
							Expression right =  stack.pop();
							Expression left = stack.pop();
							difference = new DifferenceExpression(left, right);
							stack.push(difference);
						}
						
					} else if (postfix[i].equals("*")) { //check if it's a Product
						
						if (stack.peek() != null) {
							Expression right =  stack.pop();
							Expression left = stack.pop();
							product = new ProductExpression(left, right);
							stack.push(product);
						}
						
					} else if (postfix[i].equals("/")) { //check if it's a Quotient
						
						if (stack.peek() != null) {
							Expression right =  stack.pop();
							Expression left = stack.pop();
							quotient = new QuotientExpression(left, right);
							stack.push(quotient);
						}
					}
					
				} else { //check to see if the element is a Variable
				
					Expression variableoperand = new VariableOperand(postfix[i]);
					stack.push(variableoperand);
				}
			}	
		}
		return stack.pop();
	}

   /**
    * @return a String that represents this expression in prefix notation.
    */
	public abstract String toPrefix();

   /**
    * @return a String that represents this expression in infix notation.
    */  
	public abstract String toInfix();

   /**
    * @return a String that represents this expression in postfix notation.
    */  
	public abstract String toPostfix();

   /**
    * @return a String that represents the expression in infix notation
    */
	@Override
	public String toString()
	{
		return toInfix();
	}
   
   /**
    * @return a new Expression mathematically equivalent to this one, but simplified.
    */  
	public abstract Expression simplify();

   /**
    * Evaluates the expression given assignments of values to variables.
    * @param assignments a HashMap from Strings (variable names) to Integers (values).
    * @return the result of evaluating the expression with the given variable assignments
    */
	public abstract int evaluate(HashMap<String, Integer> assignments);

   /**
    * @return a Set of the variables contained in this expression
    */
	public abstract Set<String> getVariables();

	@Override
	public abstract boolean equals(Object obj);

   /**
    * Prints the expression as a tree in DOT format for visualization
    * @param filename the name of the output file
    */
	public void drawExpression(String filename) throws IOException
	{
		BufferedWriter bw = null;
		FileWriter fw = new FileWriter(filename);
		bw = new BufferedWriter(fw);

		bw.write("graph Expression {\n");

		drawExprHelper(bw);

		bw.write("}\n");

		bw.close();
		fw.close();     
	}

   /**
    * Recursively prints the vertices and edges of the expression tree for visualization
    * @param bw the BufferedWriter to write to
    */
	protected abstract void drawExprHelper(BufferedWriter bw) throws IOException;
}

/**
 * A class representing an abstract operand
 */
abstract class Operand extends Expression
{
}

/**
 * A class representing an expression containing only a single integer value
 */
class IntegerOperand extends Operand
{
	protected int operand;

   /**
    * Create the expression
    * @param operand the integer value this expression represents
    */
	public IntegerOperand(int operand)
	{
		this.operand = operand;
	}

   /**
    * @return a String that represents this expression in prefix notation.
    */   
	public String toPrefix()
	{
		return Integer.toString(operand) + " ";
	}

   /**
    * @return a String that represents this expression in postfix notation.
    */  
	public String toPostfix()
	{
		return Integer.toString(operand) + " ";
	}   

   /**
    * @return a String that represents the expression in infix notation
    */
	public String toInfix()
	{
		return Integer.toString(operand);      
	}

   /**
    * @return a new Expression mathematically equivalent to this one, but simplified.
    */  
	public Expression simplify()
	{
      //////REPLACE WITH YOUR CODE
		//return new VariableOperand("not_yet_implemented");
		
		return this;
	}   

   /**
    * Evaluates the expression given assignments of values to variables.
    * @param assignments a HashMap from Strings (variable names) to Integers (values).
    * @return the result of evaluating the expression with the given variable assignments
    */
	public int evaluate(HashMap<String, Integer> assignments)
	{
		return operand;
	}

   /**
    * @return a Set of the variables contained in this expression
    */
	public Set<String> getVariables()
	{
		Set<String> emptySet = new TreeSet<String>();
		
		return emptySet;
	}

   /**
    * @param obj and Object to compare to
    * @return true if obj is a logically equivalent Expression 
    */
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null || ! (obj instanceof IntegerOperand)) {
			
			return false;
		}
		
		return ((IntegerOperand)obj).operand == operand;			
	}   

   /**
    * Recursively prints the vertices and edges of the expression tree for visualization
    * @param bw the BufferedWriter to write to
    */
	protected void drawExprHelper(BufferedWriter bw) throws IOException
	{
		bw.write("\tnode"+hashCode()+"[label="+operand+"];\n");
	}
}

/**
 * A class representing an expression containing only a single variable
 */
class VariableOperand extends Operand
{
	protected String variable;

   /**
    * Create the expression
    * @param variable the variable name contained with this expression
    */
	public VariableOperand(String variable)
	{	
		this.variable = variable;
	}

   /**
    * @return a String that represents this expression in prefix notation.
    */   
	public String toPrefix()
	{
		return variable + " ";
	}

   /**
    * @return a String that represents this expression in postfix notation.
    */  
	public String toPostfix()
	{
		return variable + " ";
	}   

   /**
    * @return a String that represents the expression in infix notation
    */
	public String toInfix()
	{
		return variable;      
	}

   /**
    * @return a new Expression mathematically equivalent to this one, but simplified.
    */  
	public Expression simplify()
	{
		Expression simplified = new VariableOperand(variable);
		
		return simplified;
	}   

   /**
    * Evaluates the expression given assignments of values to variables.
    * @param assignments a HashMap from Strings (variable names) to Integers (values).
    * @return the result of evaluating the expression with the given variable assignments
    */
	public int evaluate(HashMap<String, Integer> assignments)
	{
		return assignments.get(variable);
	}

   /**
    * @return a Set of the variables contained in this expression
    */
	public Set<String> getVariables()
	{
		Set<String> set = new TreeSet<String>();
		
		set.add(variable);
		
		return set;
	}

   /**
    * @param obj and Object to compare to
    * @return true if obj is a logically equivalent Expression 
    */
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null || !(obj instanceof VariableOperand)) {
			
			return false;
		}
		
		return ((String)((VariableOperand)obj).variable).equals(variable);
	}   

   /**
    * Recursively prints the vertices and edges of the expression tree for visualization
    * @param bw the BufferedWriter to write to
    */
	protected void drawExprHelper(BufferedWriter bw) throws IOException
	{
		bw.write("\tnode"+hashCode()+"[label="+variable+"];\n");
	}   
}

/**
 * A class representing an expression involving an operator
 */
abstract class OperatorExpression extends Expression
{
	protected Expression left;
	protected Expression right;

   /**
    * Create the expression
    * @param left the expression representing the left operand
    * @param right the expression representing the right operand
    */
	public OperatorExpression(Expression left, Expression right)
	{
		this.left = left;
		this.right = right;
	}

   /**
    * @return a string representing the operator
    */
	protected abstract String getOperator();     
   
   /**
    * Recursively prints the vertices and edges of the expression tree for visualization
    * @param bw the BufferedWriter to write to
    */
	protected void drawExprHelper(BufferedWriter bw) throws IOException
	{
		String rootID = "\tnode"+hashCode();
		bw.write(rootID+"[label=\""+getOperator()+"\"];\n");

		bw.write(rootID + " -- node" + left.hashCode() + ";\n");
		bw.write(rootID + " -- node" + right.hashCode() + ";\n");
		left.drawExprHelper(bw);
		right.drawExprHelper(bw);
	}   
}

/**
 * A class representing an expression involving an sum
 */
class SumExpression extends OperatorExpression
{ 
   /**
    * Create the expression
    * @param left the expression representing the left operand
    * @param right the expression representing the right operand
    */
	public SumExpression(Expression left, Expression right)
	{
		super(left, right);
	}

   /**
    * @return a string representing the operand
    */
	protected String getOperator()
	{
		return "+";
	}
   
   /**
    * @return a String that represents this expression in prefix notation.
    */   
	public String toPrefix()
	{
		return getOperator() + " " + left.toPrefix() + right.toPrefix();
	}

   /**
    * @return a String that represents this expression in postfix notation.
    */  
	public String toPostfix()
	{
		return right.toPostfix() + left.toPostfix() + getOperator() + " " ;
	}   

   /**
    * @return a String that represents the expression in infix notation
    */
	public String toInfix()
	{
		return "(" + left.toInfix() + " " + getOperator() + " " + right.toInfix() + ")";      
	}

   /**
    * @return a new Expression mathematically equivalent to this one, but simplified.
    */  
	public Expression simplify()
	{

			Expression templeft = left.simplify(); //assume they can't be null 
			Expression tempright = right.simplify(); //assume they can't be null 

			//A sum x + y is simplified in 2 ways: a) if either one is 0, return the other, b) if both are numbers return their sum:
			if((templeft instanceof IntegerOperand) && ((IntegerOperand)templeft).operand == 0) {
				
				return tempright; //0 + x => x
			}
			
			if((tempright instanceof IntegerOperand) && ((IntegerOperand)tempright).operand == 0) {
				
				return templeft; //x + 0 => x
			} 
			
			if((templeft instanceof IntegerOperand) && (tempright instanceof IntegerOperand)) {
				
				return new IntegerOperand(((IntegerOperand)templeft).operand + ((IntegerOperand)tempright).operand); //2 numbers => number
			}
			
			return new SumExpression(templeft, tempright); //nothing to simplify here, but templeft and tempright may be simplifed so don't return 'this'
	}   

   /**
    * Evaluates the expression given assignments of values to variables.
    * @param assignments a HashMap from Strings (variable names) to Integers (values).
    * @return the result of evaluating the expression with the given variable assignments
    */
	public int evaluate(HashMap<String, Integer> assignments)
	{
		int result = 0;
		
		result += this.left.evaluate(assignments);
		result += this.right.evaluate(assignments);
		
		return result;
	}

   /**
    * @return a Set of the variables contained in this expression
    */
	public Set<String> getVariables()
	{
		TreeSet<String> treeSet = new TreeSet<String>();
				
		treeSet.addAll(this.left.getVariables());
		
		treeSet.addAll(this.right.getVariables());
		
		return treeSet;		
	}

   /**
    * @param obj and Object to compare to
    * @return true if obj is a logically equivalent Expression 
    */
	@Override
	public boolean equals(Object obj)
	{
		SumExpression object = (SumExpression) obj;
		if (getOperator().equals(object.getOperator())) {
			if ((this.left.equals(object.left) && this.right.equals(object.right)) 
			|| (this.left.equals(object.right) && this.right.equals(object.left))) {
				
				return true;
			} else {
				return false;
			}
		}	
		return false;
	}   
}

/**
 * A class representing an expression involving an differnce
 */
class DifferenceExpression extends OperatorExpression
{
   /**
    * Create the expression
    * @param left the expression representing the left operand
    * @param right the expression representing the right operand
    */
	public DifferenceExpression(Expression left, Expression right)
	{
		super(left, right);
	}

   /**
    * @return a string representing the operand
    */
	protected String getOperator()
	{
		return "-";
	}

   /**
    * @return a String that represents this expression in prefix notation.
    */   
	public String toPrefix()
	{
		return getOperator() + " " + left.toPrefix() + right.toPrefix();
	}

   /**
    * @return a String that represents this expression in postfix notation.
    */  
	public String toPostfix()
	{
		return right.toPostfix() + left.toPostfix() + getOperator() + " " ;
	}   

   /**
    * @return a String that represents the expression in infix notation
    */
	public String toInfix()
	{
		return "(" + left.toInfix() + " " + getOperator() + " " + right.toInfix() + ")";    
	}

   /**
    * @return a new Expression mathematically equivalent to this one, but simplified.
    */  
	public Expression simplify()
	{
		//follow SumExpression example for 2 cases: a) x - 0 => x  (ignore 0 - x => -x from PDF), b) both are numbers so return number difference.
		
		Expression templeft = left.simplify(); //assume they can't be null 
		Expression tempright = right.simplify(); //assume they can't be null 

		/*if((templeft instanceof IntegerOperand) && ((IntegerOperand)templeft).operand == 0) {
			
			return tempright; //0 - x => -x
		}*/
		
		if((tempright instanceof IntegerOperand) && ((IntegerOperand)tempright).operand == 0) {
			
			return templeft; //x - 0 => x
		} 
		
		if((templeft instanceof IntegerOperand) && (tempright instanceof IntegerOperand)) {
			
			return new IntegerOperand(((IntegerOperand)templeft).operand - ((IntegerOperand)tempright).operand); //2 numbers => number
		}
		
		if((templeft instanceof VariableOperand) && (tempright instanceof VariableOperand) 
		&& ((String)((VariableOperand)templeft).variable).equals(((String)((VariableOperand)tempright).variable))) {
			
			return new IntegerOperand(0);
		}
		
		return new DifferenceExpression(templeft, tempright);
	}   

   /**
    * Evaluates the expression given assignments of values to variables.
    * @param assignments a HashMap from Strings (variable names) to Integers (values).
    * @return the result of evaluating the expression with the given variable assignments
    */
	public int evaluate(HashMap<String, Integer> assignments)
	{
		int result = 0;
		
		result += this.left.evaluate(assignments);
		result -= this.right.evaluate(assignments);
		
		return result;
	}

   /**
    * @return a Set of the variables contained in this expression
    */
	public Set<String> getVariables()
	{
		TreeSet<String> treeSet = new TreeSet<String>();
				
		treeSet.addAll(this.left.getVariables());
		
		treeSet.addAll(this.right.getVariables());
		
		return treeSet;		
	}

   /**
    * @param obj and Object to compare to
    * @return true if obj is a logically equivalent Expression 
    */
	@Override
	public boolean equals(Object obj)
	{
		DifferenceExpression object = (DifferenceExpression) obj;
		
		if (getOperator().equals(object.getOperator())) {
			
			if ((this.left.equals(object.left) && this.right.equals(object.right))) {
				
				return true;
				
			} else {
				
				return false;
			}
		}	
		return false;
	}      
}

/**
 * A class representing an expression involving a product
 */
class ProductExpression extends OperatorExpression
{
   /**
    * Create the expression
    * @param left the expression representing the left operand
    * @param right the expression representing the right operand
    */
	public ProductExpression(Expression left, Expression right)
	{
		super(left, right);
	}

   /**
    * @return a string representing the operand
    */
	protected String getOperator()
	{
		return "*";
	}

   /**
    * @return a String that represents this expression in prefix notation.
    */   
	public String toPrefix()
	{
		return getOperator() + " " + left.toPrefix() + right.toPrefix();
	}

   /**
    * @return a String that represents this expression in postfix notation.
    */  
	public String toPostfix()
	{
		return right.toPostfix() + left.toPostfix() + getOperator() + " " ;
	}   

   /**
    * @return a String that represents the expression in infix notation
    */
	public String toInfix()
	{
		return "(" + left.toInfix() + " " + getOperator() + " " + right.toInfix() + ")";      
	}

   /**
    * @return a new Expression mathematically equivalent to this one, but simplified.
    */  
	public Expression simplify()
	{
		Expression templeft = left.simplify(); //assume they can't be null 
		Expression tempright = right.simplify(); //assume they can't be null 

		if((templeft instanceof IntegerOperand) && ((IntegerOperand)templeft).operand == 0) {
			
			return new IntegerOperand(0); //0 * x => 0
		}
		
		if((tempright instanceof IntegerOperand) && ((IntegerOperand)tempright).operand == 0) {
			
			return new IntegerOperand(0); //x * 0 => x
		} 
		
		if((templeft instanceof IntegerOperand) && ((IntegerOperand)templeft).operand == 1) {
			
			return new VariableOperand((String) ((VariableOperand)tempright).variable); //1 * x => x
		}
		
		if((tempright instanceof IntegerOperand) && ((IntegerOperand)tempright).operand == 1) {
			
			return new VariableOperand((String) ((VariableOperand)templeft).variable); //x * 1 => x
		} 
		
		if((templeft instanceof IntegerOperand) && (tempright instanceof IntegerOperand)) {
			
			return new IntegerOperand(((IntegerOperand)templeft).operand * ((IntegerOperand)tempright).operand); //2 numbers => number
		}
		
		return new ProductExpression(templeft, tempright);
	}   

   /**
    * Evaluates the expression given assignments of values to variables.
    * @param assignments a HashMap from Strings (variable names) to Integers (values).
    * @return the result of evaluating the expression with the given variable assignments
    */
	public int evaluate(HashMap<String, Integer> assignments)
	{
		int result = 0;
		
		result += this.left.evaluate(assignments);
		result *= this.right.evaluate(assignments);
		
		return result;
	}

   /**
    * @return a Set of the variables contained in this expression
    */
	public Set<String> getVariables()
	{
		TreeSet<String> treeSet = new TreeSet<String>();
				
		treeSet.addAll(this.left.getVariables());
		
		treeSet.addAll(this.right.getVariables());
		
		return treeSet;	
	}

   /**
    * @param obj and Object to compare to
    * @return true if obj is a logically equivalent Expression 
    */
	@Override
	public boolean equals(Object obj)
	{
		ProductExpression object = (ProductExpression) obj;
		
		if (getOperator().equals(object.getOperator())) {
			
			if ((this.left.equals(object.left) && this.right.equals(object.right))
			|| (this.left.equals(object.right) && this.right.equals(object.left))) {
				
				return true;
				
			} else {
				
				return false;
			}
		}	
		return false;
	}      
}

/**
 * A class representing an expression involving a division
 */
class QuotientExpression extends OperatorExpression
{
   /**
    * Create the expression
    * @param left the expression representing the left operand
    * @param right the expression representing the right operand
    */
	public QuotientExpression(Expression left, Expression right)
	{
		super(left, right);
	}

   /**
    * @return a string representing the operand
    */
	protected String getOperator()
	{
		return "/";
	}

   /**
    * @return a String that represents this expression in prefix notation.
    */   
	public String toPrefix()
	{ 
		return getOperator() + " " + left.toPrefix() + right.toPrefix();
	}

   /**
    * @return a String that represents this expression in postfix notation.
    */  
	public String toPostfix()
	{
		return right.toPostfix() + left.toPostfix() + getOperator() + " " ;
	}   

   /**
    * @return a String that represents the expression in infix notation
    */
	public String toInfix()
	{
		return "(" + left.toInfix() + " " + getOperator() + " " + right.toInfix() + ")"; 
	}

   /**
    * @return a new Expression mathematically equivalent to this one, but simplified.
    */  
	public Expression simplify()
	{
		Expression templeft = left.simplify(); //assume they can't be null 
		Expression tempright = right.simplify(); //assume they can't be null 

		if((templeft instanceof IntegerOperand) && ((IntegerOperand)templeft).operand == 0) {
			
			return new IntegerOperand(0); //0 / x => 0
		}
		
		if((tempright instanceof IntegerOperand) && ((IntegerOperand)tempright).operand == 0) {
			
			return new IntegerOperand(0); //x / 0 => x
		} 
		
		if((tempright instanceof IntegerOperand) && ((IntegerOperand)tempright).operand == 1) {
			
			return new VariableOperand((String) ((VariableOperand)templeft).variable); //x / 1 => x
		} 
		
		if((templeft instanceof VariableOperand) && (tempright instanceof VariableOperand)
		&& ((String)((VariableOperand)templeft).variable).equals(((String)((VariableOperand)tempright).variable))) {
			
			return new IntegerOperand(1); //x / x => 1
		}
		
		if((templeft instanceof IntegerOperand) && (tempright instanceof IntegerOperand)) {
			
			return new IntegerOperand(((IntegerOperand)templeft).operand / ((IntegerOperand)tempright).operand); //2 numbers => number
		}
		
		return new QuotientExpression(templeft, tempright);
	}   

   /**
    * Evaluates the expression given assignments of values to variables.
    * @param assignments a HashMap from Strings (variable names) to Integers (values).
    * @return the numerica result of evaluating the expression with the given variable assignments
    */
	public int evaluate(HashMap<String, Integer> assignments)
	{
		int result = 0;
		
		result += this.left.evaluate(assignments);
		if (result != 0) {
			
			result /= this.right.evaluate(assignments);
			return result;
			
		} else {
		
			return result;
		}
	}

   /**
    * @return a Set of the variables contained in this expression
    */
	public Set<String> getVariables()
	{
		TreeSet<String> treeSet = new TreeSet<String>();
				
		treeSet.addAll(this.left.getVariables());
		
		treeSet.addAll(this.right.getVariables());
		
		return treeSet;	
	}

   /**
    * @param obj and Object to compare to
    * @return true if obj is a logically equivalent Expression 
    */
	@Override
	public boolean equals(Object obj)
	{
		QuotientExpression object = (QuotientExpression) obj;
		if (getOperator().equals(object.getOperator())) {
			if ((this.left.equals(object.left) && this.right.equals(object.right))) {
				
				return true;
			} else {
				return false;
			}
		}	
		return false;
	}      
}
