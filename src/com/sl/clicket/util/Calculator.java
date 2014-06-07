package com.sl.clicket.util;

public class Calculator {
	
	public static String createExpression(int answer){
		String expression = ""+answer;
		
		expression = createSubtraction(answer);
		return expression; 
	}
	
	public static String evaluateExpression(String expression){
		System.out.println("expression === "+expression);
		if(expression.contains("--")){
			String [] numbers = expression.split("--");
			return ""+(Integer.parseInt(numbers[0])-Integer.parseInt(numbers[1]));
		}else if(expression.contains("-")){
			String [] numbers = expression.split("-");
			return ""+(Integer.parseInt(numbers[0])-Integer.parseInt(numbers[1]));
		}else{
			return expression;
		}
	}

	private static String createSubtraction(int answer) {
		int number1 = (int)(Math.random()*100);
		int number2 = number1-answer;
		
		return number1 + "" + "-" + number2;
	}
}
