import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shine Kaye on 2017/3/28.
 * A simple lexical analyzer created by me
 */
public class Analyzer {

    private static String[] definedSignTable = {"+", "-", "*", "/", "(", ")", ";", "="};

    private static String[] definedOperator = {"+", "-", "*", "/"};

    private static String[] definedKeyWordTable = {"print"};

    private static Map<String, Double> definedIdentifierTable = new HashMap<>();

    private static ArrayList<StringBuilder> variableTable = new ArrayList<>();

    private static int position = 0;

    private static int curPosition = 0;

    private static int line = 1;

    private static int expNum = 0;

    private static int leftBracket = 0;

    private static int rightBracket = 0;

    private static boolean isKeyWord = false;

    private static String getNextChar(String sourceStr)
    {
        String nextChar = String.valueOf(sourceStr.charAt(curPosition));
        position++;
        curPosition++;
        if (nextChar.equals("\n"))
        {
            line++;
            position = 0;
        }
        return nextChar;
    }

    private static void getBackChar()
    {
        curPosition--;
    }

    private static String getCurrentLineStr(String sourceStr)
    {
        String[] lines = sourceStr.split("\n");
        String currentLine = lines[line - 1];
        return currentLine;
    }

    private static boolean isExist(String[] strs, String targetStr)
    {
        for (String str: strs)
            if (str.equals(targetStr)) return true;
        return false;
    }

    private static boolean isLetter(String targetStr)
    {
        String reg = "[a-zA-Z]";
        return targetStr.matches(reg);
    }

    private static boolean isLetterOrDigit(String targetStr)
    {
        String reg = "[a-zA-Z0-9]";
        return targetStr.matches(reg);
    }

    private static boolean isDigit(String targetStr)
    {
        String reg = "[0-9]";
        return targetStr.matches(reg);
    }

    public static void analyze(String sourceStr) throws Exception {
        int length = sourceStr.length();
        while (curPosition < length)
        {
            String currentChar = getNextChar(sourceStr);
            //Space not allowed
            if (currentChar.equals(" "))
            {
                do {
                    currentChar = getNextChar(sourceStr);
                }while (currentChar.equals(" "));
            }
            if (currentChar.equals("\n"))
            {
                if (curPosition == sourceStr.length())
                    break;
                do {
                    currentChar = getNextChar(sourceStr);
                }while (currentChar.equals("\n"));
            }
            //Must begin with a letter - Invalid Variable Defined Error
            if (!isLetter(currentChar))
                ErrorMsg.getErrMsg(line, position, ErrorMsg.SYNTAX_ERROR, ErrorMsg.INVALID_VAR);
            //Input Variable
            else
            {
                StringBuilder variable = new StringBuilder("");
                for ( ; isLetterOrDigit(currentChar); currentChar = getNextChar(sourceStr))
                {
                    variable.append(currentChar);
                }
                getBackChar();
                //Is it a keyword or a variable
                if (isExist(definedKeyWordTable, variable.toString())) isKeyWord = true;
                else variableTable.add(variable);
                currentChar = getNextChar(sourceStr);
                //Space not allowed
                if (currentChar.equals(" "))
                {
                    do {
                        currentChar = getNextChar(sourceStr);
                    }while (currentChar.equals(" "));
                }
                //No Space Allowed between any two chars of a variable
                //Invalid Variable Defined Error
                if (isLetterOrDigit(currentChar))
                    ErrorMsg.getErrMsg(line, position, ErrorMsg.SYNTAX_ERROR, ErrorMsg.INVALID_VAR);
                //print branch
                else if (currentChar.equals("("))
                {
                    leftBracket++;
                    if (!isKeyWord)
                        ErrorMsg.getErrMsg(line, position, ErrorMsg.SYNTAX_ERROR, ErrorMsg.KEY_WORD_NOT_FOUND);
                    else
                    {
                        ArrayList<String> sourceExpression = new ArrayList<>();
                        sourceExpression.add("(");
                        String currentLine = getCurrentLineStr(sourceStr);
                        if (!currentLine.substring(currentLine.length()-1).equals(";"))
                            ErrorMsg.getErrMsg(line, currentLine.length()-1, ErrorMsg.SYNTAX_ERROR, ErrorMsg.SEMICOLON);
                        if (!currentLine.substring(currentLine.length()-2, currentLine.length()-1).equals(")"))
                            ErrorMsg.getErrMsg(line, currentLine.length()-2, ErrorMsg.SYNTAX_ERROR, ErrorMsg.RIGHT_BRACKET);
                        while (true)
                        {
                            currentChar = getNextChar(sourceStr);
                            if (currentChar.equals(";"))
                            {
                                if (leftBracket > rightBracket)
                                {
                                    leftBracket = 0;
                                    rightBracket = 0;
                                    ErrorMsg.getErrMsg(line, position, ErrorMsg.SYNTAX_ERROR, ErrorMsg.RIGHT_BRACKET);
                                }
                                else if (leftBracket < rightBracket)
                                {
                                    leftBracket = 0;
                                    rightBracket = 0;
                                    ErrorMsg.getErrMsg(line, position, ErrorMsg.SYNTAX_ERROR, ErrorMsg.LEFT_BRACKET);
                                }

                                double expResult = 0;
                                try {
                                    ExpressionCalculation ec = new ExpressionCalculation();
                                    expResult = ec.calculate(sourceExpression);
                                }
                                catch (ArithmeticException e)
                                {
                                    ErrorMsg.getErrMsg(line, position, ErrorMsg.MATH_ERROR);
                                }
                                catch (Exception e)
                                {
                                    ErrorMsg.getErrMsg(line, position, ErrorMsg.SYNTAX_ERROR, ErrorMsg.INVALID_EXP);
                                }
                                System.out.println(expResult);
                                leftBracket = 0;
                                rightBracket = 0;
                                break;
                            }
                            if (currentChar.equals("("))
                            {
                                sourceExpression.add(currentChar);
                                leftBracket++;
                            }
                            else if (currentChar.equals(")"))
                            {
                                sourceExpression.add(currentChar);
                                rightBracket++;
                            }
                            else if (isExist(definedOperator, currentChar))
                                sourceExpression.add(currentChar);
                            else if (isLetter(currentChar))
                            {
                                StringBuilder definedVariable = new StringBuilder("");
                                for ( ; isLetterOrDigit(currentChar); currentChar = getNextChar(sourceStr))
                                {
                                    definedVariable.append(currentChar);
                                }
                                getBackChar();
                                if (definedIdentifierTable.containsKey(definedVariable.toString()))
                                    sourceExpression.add(Double.toString(definedIdentifierTable.get(definedVariable.toString())));
                                else
                                    ErrorMsg.getErrMsg(line, position, ErrorMsg.VAR_NOT_EXIST);
                            }
                            else if (isDigit(currentChar))
                            {
                                StringBuilder operand = new StringBuilder("");
                                for ( ; isDigit(currentChar) || currentChar.equals("."); currentChar = getNextChar(sourceStr))
                                {
                                    operand.append(currentChar);
                                }
                                getBackChar();
                                sourceExpression.add(operand.toString());
                            }
                            else if (currentChar.equals(" "))
                            {
                                do {
                                    currentChar = getNextChar(sourceStr);
                                }while (currentChar.equals(" "));
                                getBackChar();
                            }
                        }
                    }
                }
                //expression branch
                else if (currentChar.equals("="))
                {
                    expNum++;
                    //Input Expression
                    ArrayList<String> sourceExpression = new ArrayList<>();
                    String currentLine = getCurrentLineStr(sourceStr);
                    if (!currentLine.substring(currentLine.length()-1).equals(";"))
                        ErrorMsg.getErrMsg(line, currentLine.length()-1, ErrorMsg.SYNTAX_ERROR, ErrorMsg.SEMICOLON);
                    while (true)
                    {
                        currentChar = getNextChar(sourceStr);
                        if (currentChar.equals(";"))
                        {
                            if (leftBracket > rightBracket)
                            {
                                leftBracket = 0;
                                rightBracket = 0;
                                ErrorMsg.getErrMsg(line, position, ErrorMsg.SYNTAX_ERROR, ErrorMsg.RIGHT_BRACKET);
                            }
                            else if (leftBracket < rightBracket)
                            {
                                leftBracket = 0;
                                rightBracket = 0;
                                ErrorMsg.getErrMsg(line, position, ErrorMsg.SYNTAX_ERROR, ErrorMsg.LEFT_BRACKET);
                            }

                            double expResult = 0;
                            try {
                                ExpressionCalculation ec = new ExpressionCalculation();
                                expResult = ec.calculate(sourceExpression);
                            }
                            catch (ArithmeticException e)
                            {
                                ErrorMsg.getErrMsg(line, position, ErrorMsg.MATH_ERROR);
                            }
                            catch (Exception e)
                            {
                                ErrorMsg.getErrMsg(line, position, ErrorMsg.SYNTAX_ERROR, ErrorMsg.INVALID_EXP);
                            }
                            definedIdentifierTable.put((variableTable.get(expNum-1)).toString(), expResult);
                            leftBracket = 0;
                            rightBracket = 0;
                            break;
                        }
                        if (currentChar.equals("("))
                        {
                            sourceExpression.add(currentChar);
                            leftBracket++;
                        }
                        else if (currentChar.equals(")"))
                        {
                            sourceExpression.add(currentChar);
                            rightBracket++;
                        }
                        else if (isExist(definedOperator, currentChar))
                            sourceExpression.add(currentChar);
                        else if (isLetter(currentChar))
                        {
                            StringBuilder definedVariable = new StringBuilder("");
                            for ( ; isLetterOrDigit(currentChar); currentChar = getNextChar(sourceStr))
                            {
                                definedVariable.append(currentChar);
                            }
                            getBackChar();
                            if (definedIdentifierTable.containsKey(definedVariable.toString()))
                                sourceExpression.add(Double.toString(definedIdentifierTable.get(definedVariable.toString())));
                            else
                                ErrorMsg.getErrMsg(line, position, ErrorMsg.VAR_NOT_EXIST);
                        }
                        else if (isDigit(currentChar))
                        {
                            StringBuilder operand = new StringBuilder("");
                            for ( ; isDigit(currentChar) || currentChar.equals("."); currentChar = getNextChar(sourceStr))
                            {
                                operand.append(currentChar);
                            }
                            getBackChar();
                            sourceExpression.add(operand.toString());
                        }
                        else if (currentChar.equals(" "))
                        {
                            do {
                                currentChar = getNextChar(sourceStr);
                            }while (currentChar.equals(" "));
                            getBackChar();
                        }
                        else
                        {
                            ErrorMsg.getErrMsg(line, position, ErrorMsg.INVALID_OPERATOR);
                        }
                    }
                }
                else
                {
                    if (currentChar.equals(";") || currentChar.equals(")"))
                        ErrorMsg.getErrMsg(line, position, ErrorMsg.SYNTAX_ERROR, ErrorMsg.LEFT_BRACKET);
                    else if (isExist(definedSignTable, currentChar))
                        ErrorMsg.getErrMsg(line, position, ErrorMsg.SYNTAX_ERROR, ErrorMsg.INVALID_EXP);
                    else
                        ErrorMsg.getErrMsg(line, position, ErrorMsg.INVALID_OPERATOR);
                }
            }
        }
    }
}
