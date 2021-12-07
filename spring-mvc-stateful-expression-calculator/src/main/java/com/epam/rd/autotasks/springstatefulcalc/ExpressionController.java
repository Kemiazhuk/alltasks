package com.epam.rd.autotasks.springstatefulcalc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/calc")
public class ExpressionController {
    public static final Character LETTER_A = 'a';
    public static final Character LETTER_Z = 'z';
    public static final Character SPACE = ' ';
    public static final Character OPEN_BRACKET = '(';
    public static final Character CLOSE_BRACKET = ')';
    public static final Character PLUS = '+';
    public static final Character MINUS = '-';
    public static final Character MULTIPLE = '*';
    public static final Character DIVIDE = '/';
    public static final String EXPRESSION = "expression";
    public static final Character NUMBER_0 = '0';
    public static final Character NUMBER_9 = '9';
    public static final Integer LEFT_RANGE = -10000;
    public static final Integer RIGHT_RANGE = 10000;

    @Autowired
    private ExpressionData expressionData;

    @Autowired
    private CalculateExpresion calculateExpresion;

    @GetMapping("/result")
    public ResponseEntity<Integer> getValues() {
        Integer result = calculateExpresion.calculate(expressionData.getExpression(), expressionData.getAllValues());
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/expression")
    public ResponseEntity getExpression(@RequestBody String body) {
        if (filterExpression(body)) {
            if (expressionData.getExpression().equals("")) {
                expressionData.setExpression(body);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                expressionData.setExpression(body);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{parameter}")
    public ResponseEntity getValues(@PathVariable String parameter, @RequestBody String body) {
        ResponseEntity responseEntity = filterValues(parameter, body);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            if (expressionData.setValue(parameter, body)) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } else {
            return responseEntity;
        }
    }

    @DeleteMapping("/{parameter}")
    public ResponseEntity deleteParameter(@PathVariable String parameter) {
        if (parameter.equals(EXPRESSION)) {
            expressionData.setExpression("");
        } else {
            expressionData.deleteValue(parameter);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    private boolean filterExpression(String expression) {
        char[] arrayOfSymbols = expression.toCharArray();
        char previousSymbol = arrayOfSymbols[0];
        for (int i = 1; i < arrayOfSymbols.length; i++) {
            if (arrayOfSymbols[i] == SPACE) {
                continue;
            }
            if ((previousSymbol >= LETTER_A && previousSymbol <= LETTER_Z)
                    && ((arrayOfSymbols[i] == OPEN_BRACKET)
                    || (arrayOfSymbols[i] >= LETTER_A && arrayOfSymbols[i] <= LETTER_Z))) {
                return false;
            } else if (((previousSymbol == PLUS || previousSymbol == MINUS ||
                    previousSymbol == MULTIPLE || previousSymbol == DIVIDE)
                    && (arrayOfSymbols[i] == CLOSE_BRACKET))) {
                return false;
            } else if (previousSymbol == OPEN_BRACKET && (arrayOfSymbols[i] <= LETTER_A && arrayOfSymbols[i] >= LETTER_Z)) {
                return false;
            } else if (((previousSymbol == CLOSE_BRACKET) || (previousSymbol >= NUMBER_0 && previousSymbol <= NUMBER_9)) &&
                    (!(arrayOfSymbols[i] == PLUS) && !(arrayOfSymbols[i] == MINUS) &&
                            !(arrayOfSymbols[i] == MULTIPLE) && !(arrayOfSymbols[i] == DIVIDE))) {
                return false;
            }
            previousSymbol = arrayOfSymbols[i];
        }
        return true;
    }

    private ResponseEntity filterValues(String parameter, String body) {
        while (expressionData.getExpression().contains(parameter) && expressionData.getAllValues().get(body) != null) {
            parameter = body;
            body = expressionData.getAllValues().get(body).toString();
        }
        try {
            Integer value = Integer.valueOf(body);
            if (value > RIGHT_RANGE || value < LEFT_RANGE) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
