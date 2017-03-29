import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shine Kaye on 2017/3/20.
 * A simple class created by me used to calculate an expression
 * implemented by Reverse Polish Notation algorithm
 */
public class ExpressionCalculation {

    private static final String LEFT_BRACKET = "(";

    private static final String RIGHT_BRACKET = ")";

    private static final String ADD_SIGN = "+";

    private static final String SUBTRACT_SIGN = "-";

    private static final String MULTIPLY_SIGN = "*";

    private static final String DIVIDE_SIGN = "/";

    private boolean isNumber(String targetString)
    {
        Pattern pattern = Pattern.compile("([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9]|0)");
        Matcher isNum = pattern.matcher(targetString);
        return isNum.matches();
    }

    private int icp(String operator)
    {
        switch (operator)
        {
            case LEFT_BRACKET:
                return 7;

            case MULTIPLY_SIGN:case DIVIDE_SIGN:
                return 4;

            case SUBTRACT_SIGN:case ADD_SIGN:
                return 2;

            case RIGHT_BRACKET:
                return 1;

            default:
                //do nothing
                break;
        }
        return 0;
    }

    private int isp(String operator)
    {
        switch (operator)
        {
            case LEFT_BRACKET:
                return 1;

            case MULTIPLY_SIGN:case DIVIDE_SIGN:
                return 5;

            case SUBTRACT_SIGN:case ADD_SIGN:
                return 3;

            case RIGHT_BRACKET:
                return 7;

            default:
                //do nothing
                break;
        }
        return 0;
    }

    private ArrayList<String> infixToPostfix(ArrayList<String> sourceExpression)
    {
        ArrayList<String> resultExpressions = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        stack.push("#");
        for (int i = 0; i < sourceExpression.size(); i++)
        {
            String item = sourceExpression.get(i);
            String topItem;
            if (isNumber(item))
                resultExpressions.add(item);
            else if (item.equals(")"))
            {
                for (topItem = stack.pop(); !topItem.equals("("); topItem = stack.pop())
                    resultExpressions.add(topItem);
            }
            else
            {
                for (topItem = stack.pop(); icp(item) <= isp(topItem); topItem = stack.pop())
                    resultExpressions.add(topItem);
                stack.push(topItem);
                stack.push(item);
            }
        }
        while (!stack.isEmpty())
        {
            String topItem = stack.pop();
            resultExpressions.add(topItem);
        }
        return resultExpressions;
    }

    public double calculate(ArrayList<String> sourceExpression)
    {
        Stack<Double> stack = new Stack<>();
        ArrayList<String> resultExpression = infixToPostfix(sourceExpression);
        for (int i = 0; i < resultExpression.size() - 1; i++)
        {
            String item = resultExpression.get(i);
            if (isNumber(item))
                stack.push(Double.parseDouble(item));
            else
            {
                double value_1 = stack.pop();
                double value_2 = stack.pop();
                double result = 0.0;
                switch (item)
                {
                    case ADD_SIGN:
                        result = value_2 + value_1;
                        break;

                    case SUBTRACT_SIGN:
                        result = value_2 - value_1;
                        break;

                    case MULTIPLY_SIGN:
                        result = value_2 * value_1;
                        break;

                    case DIVIDE_SIGN:
                        if (value_1 < 0.0000001)
                        {
                            ErrorMsg.getErrMsg(ErrorMsg.MATH_ERROR);
                        }
                        result = value_2 / value_1;
                        break;
                }
                stack.push(result);
            }
        }
        return stack.pop();
    }
}
