package org.acme.opentracing;

/*
This Pi computation from
http://javahowto.blogspot.com/2011/08/example-of-expensive-computation.html

*/

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;
import static java.math.RoundingMode.HALF_UP;

public class Pi implements java.io.Serializable {
    private static final long serialVersionUID = 227L;
    private static final BigDecimal FOUR = BigDecimal.valueOf(4);

    /**
     * Compute the value of pi to the specified number of digits after the
     * decimal point. The value is computed using Machin's formula:
     * pi/4 = 4*arctan(1/5) - arctan(1/239)
     * and a power series expansion of arctan(x) to sufficient precision.
     */
    public static BigDecimal computePi(int digits) {
        int scale = digits + 5;
        final BigDecimal arctan1_5 = arcTan(5, scale);
        final BigDecimal arctan1_239 = arcTan(239, scale);
        BigDecimal pi = arctan1_5.multiply(FOUR).subtract(arctan1_239).multiply(FOUR);
        return pi.setScale(digits, HALF_UP);
    }

    /**
     * Compute the value, in radians, of the arctangent of the inverse of the
     * supplied integer to the specified number of digits after the decimal
     * point. The value is computed using the power series expansion for the arc
     * tangent:
     * arctan(x) = x - (x^3)/3 + (x^5)/5 - (x^7)/7 + (x^9)/9 ...
     */
    public static BigDecimal arcTan(int inverseX, int scale) {
        BigDecimal result, invScale, term;
        BigDecimal invX = BigDecimal.valueOf(inverseX);
        BigDecimal invX2 = BigDecimal.valueOf((long) inverseX * inverseX);
        invScale = BigDecimal.ONE.divide(invX, scale, HALF_EVEN);
        result = invScale;
        int i = 1;
        do {
            invScale = invScale.divide(invX2, scale, HALF_EVEN);
            int denom = 2 * i + 1;
            term = invScale.divide(BigDecimal.valueOf(denom), scale, HALF_EVEN);
            result = (i % 2) != 0 ? result.subtract(term) : result.add(term);
            i++;
        } while (term.compareTo(BigDecimal.ZERO) != 0);
        return result;
    }
}