package com.epam.rd.autotasks.springstatefulcalc;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Stack;

import static com.epam.rd.autotasks.springstatefulcalc.ExpressionController.*;

@Component
public class CalculateExpresion {
    private final String MSG_ERROR_DIVIDE_ZERO = "Cannot divide by zero";


    public Integer calculate(String expression, Map<String, Integer> allValues) {
        Stack<Character> operations = new Stack<>();
        Stack<Integer> numbers = new Stack<>();
        char[] symbols = expression.toCharArray();
        for (Character symbol : symbols) {
            if (symbol == SPACE) {
                continue;
            }
            if (symbol >= NUMBER_0 && symbol <= NUMBER_9) {
                numbers.push(Integer.parseInt(symbol.toString()));
            } else if ((symbol >= LETTER_A && symbol <= LETTER_Z)) {
                Integer value = allValues.get(String.valueOf(symbol));
                if (value == null) {
                    return null;
                }
                numbers.push(value);
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
