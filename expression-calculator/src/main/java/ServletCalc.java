import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Stack;

@WebServlet("/calc")
public class ServletCalc extends HttpServlet {
    private static final Character LETTER_A = 'a';
    private static final Character LETTER_Z = 'z';
    private static final Character SPACE = ' ';
    private static final Character OPEN_BRACKET = '(';
    private static final Character CLOSE_BRACKET = ')';
    private static final Character PLUS = '+';
    private static final Character MINUS = '-';
    private static final Character MULTIPLE = '*';
    private static final Character DIVIDE = '/';
    private static final String EXPRESSION = "expression";
    private static final String MSG_ERROR_DIVIDE_ZERO = "Cannot divide by zero";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer result = calculateExpression(req.getParameter(EXPRESSION), req);
        resp.getWriter().println(result);
    }

    private Integer calculateExpression(String expression, HttpServletRequest req) {
        Stack<Character> operations = new Stack<>();
        Stack<Integer> numbers = new Stack<>();
        char[] symbols = expression.toCharArray();
        for (Character symbol : symbols) {
            if (symbol == SPACE) {
                continue;
            }
            if (symbol >= LETTER_A && symbol <= LETTER_Z) {
                String value = req.getParameter(String.valueOf(symbol));
                char charValue = value.charAt(0);
                while (charValue >= LETTER_A && charValue <= LETTER_Z) {
                    value = req.getParameter(String.valueOf(charValue));
                    charValue = value.charAt(0);
                }
                numbers.push(Integer.valueOf(value));
            } else if (symbol == OPEN_BRACKET) {
                operations.push(symbol);
            } else if (symbol == CLOSE_BRACKET) {
                while (operations.peek() != OPEN_BRACKET) {
                    numbers.push(doOperation(operations.pop(), numbers.pop(), numbers.pop()));
                }
                operations.pop();
            } else if (symbol == PLUS || symbol == MINUS || symbol == MULTIPLE || symbol == DIVIDE) {
                while (!operations.isEmpty() && hasPriority(symbol, operations.peek())) {
                    numbers.push(doOperation(operations.pop(), numbers.pop(), numbers.pop()));
                }
                operations.push(symbol);
            }
        }
        while (!operations.isEmpty()) {
            numbers.push(doOperation(operations.pop(), numbers.pop(), numbers.pop()));
        }
        return numbers.pop();
    }

    private Integer doOperation(char operation, Integer second, Integer first) {
        if (operation == DIVIDE && second == 0) {
            throw new UnsupportedOperationException(MSG_ERROR_DIVIDE_ZERO);
        }
        return ((operation == PLUS) ? first + second :
                (operation == MINUS) ? first - second :
                        (operation == MULTIPLE) ? first * second :
                                (operation == DIVIDE) ? first / second : 0);
    }

    private boolean hasPriority(char firstOperation, char secondOperation) {
        if (secondOperation == OPEN_BRACKET || secondOperation == CLOSE_BRACKET)
            return false;
        return (firstOperation != MULTIPLE && firstOperation != DIVIDE) ||
                (secondOperation != PLUS && secondOperation != MINUS);
    }
}
