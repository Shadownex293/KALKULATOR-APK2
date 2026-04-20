package com.shadownex.calculator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvExpression, tvResult;
    private String currentInput = "";
    private String operator = "";
    private double firstNum = 0;
    private boolean newInput = false;
    private boolean justCalculated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvExpression = findViewById(R.id.tvExpression);
        tvResult = findViewById(R.id.tvResult);

        int[] numIds = {
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };
        String[] numVals = {"0","1","2","3","4","5","6","7","8","9"};

        for (int i = 0; i < numIds.length; i++) {
            final String val = numVals[i];
            Button btn = findViewById(numIds[i]);
            btn.setOnClickListener(v -> { animateButton(v); onNumberClick(val); });
        }

        findViewById(R.id.btnDot).setOnClickListener(v -> { animateButton(v); onDotClick(); });
        findViewById(R.id.btnAdd).setOnClickListener(v -> { animateButton(v); onOperatorClick("+"); });
        findViewById(R.id.btnSub).setOnClickListener(v -> { animateButton(v); onOperatorClick("-"); });
        findViewById(R.id.btnMul).setOnClickListener(v -> { animateButton(v); onOperatorClick("\u00d7"); });
        findViewById(R.id.btnDiv).setOnClickListener(v -> { animateButton(v); onOperatorClick("\u00f7"); });
        findViewById(R.id.btnPercent).setOnClickListener(v -> { animateButton(v); onPercentClick(); });
        findViewById(R.id.btnEquals).setOnClickListener(v -> { animateButton(v); onEqualsClick(); });
        findViewById(R.id.btnClear).setOnClickListener(v -> { animateButton(v); onClearClick(); });
        findViewById(R.id.btnPlusMinus).setOnClickListener(v -> { animateButton(v); onPlusMinusClick(); });
        findViewById(R.id.btnBackspace).setOnClickListener(v -> { animateButton(v); onBackspaceClick(); });
    }

    private void onNumberClick(String num) {
        if (justCalculated) { currentInput = ""; justCalculated = false; }
        if (newInput) { currentInput = ""; newInput = false; }
        if (currentInput.equals("0") && !num.equals(".")) {
            currentInput = num;
        } else {
            currentInput += num;
        }
        updateDisplay();
    }

    private void onDotClick() {
        if (justCalculated) { currentInput = "0"; justCalculated = false; }
        if (newInput) { currentInput = "0"; newInput = false; }
        if (!currentInput.contains(".")) {
            if (currentInput.isEmpty()) currentInput = "0";
            currentInput += ".";
        }
        updateDisplay();
    }

    private void onOperatorClick(String op) {
        if (!currentInput.isEmpty()) {
            if (!operator.isEmpty() && !newInput) {
                calculate();
            } else {
                firstNum = Double.parseDouble(currentInput);
            }
        }
        operator = op;
        newInput = true;
        justCalculated = false;
        tvExpression.setText(formatNumber(firstNum) + " " + operator);
    }

    private void onEqualsClick() {
        if (operator.isEmpty() || currentInput.isEmpty()) return;
        calculate();
        operator = "";
        newInput = false;
        justCalculated = true;
    }

    private void calculate() {
        double secondNum = currentInput.isEmpty() ? firstNum : Double.parseDouble(currentInput);
        double result;
        switch (operator) {
            case "+": result = firstNum + secondNum; break;
            case "-": result = firstNum - secondNum; break;
            case "\u00d7": result = firstNum * secondNum; break;
            case "\u00f7":
                if (secondNum == 0) {
                    tvResult.setText("Error");
                    tvExpression.setText("Tidak bisa dibagi 0");
                    currentInput = ""; firstNum = 0; operator = "";
                    return;
                }
                result = firstNum / secondNum; break;
            default: return;
        }
        tvExpression.setText(formatNumber(firstNum) + " " + operator + " " + formatNumber(secondNum) + " =");
        firstNum = result;
        currentInput = formatNumber(result);
        tvResult.setText(currentInput);
    }

    private void onClearClick() {
        currentInput = ""; operator = ""; firstNum = 0;
        newInput = false; justCalculated = false;
        tvExpression.setText(""); tvResult.setText("0");
    }

    private void onPlusMinusClick() {
        if (!currentInput.isEmpty() && !currentInput.equals("0")) {
            currentInput = currentInput.startsWith("-") ? currentInput.substring(1) : "-" + currentInput;
            updateDisplay();
        }
    }

    private void onBackspaceClick() {
        if (!currentInput.isEmpty() && !justCalculated) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            if (currentInput.isEmpty() || currentInput.equals("-")) {
                currentInput = ""; tvResult.setText("0");
            } else {
                tvResult.setText(currentInput);
            }
        }
    }

    private void onPercentClick() {
        if (!currentInput.isEmpty()) {
            double val = Double.parseDouble(currentInput) / 100;
            currentInput = formatNumber(val);
            updateDisplay();
        }
    }

    private void updateDisplay() {
        tvResult.setText(currentInput.isEmpty() ? "0" : currentInput);
    }

    private String formatNumber(double num) {
        if (num == (long) num) return String.valueOf((long) num);
        return String.format("%.8f", num).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    private void animateButton(View v) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 0.88f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 0.88f, 1f);
        set.playTogether(scaleX, scaleY);
        set.setDuration(120);
        set.start();
    }
}
