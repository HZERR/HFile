package ru.hzerr;

import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("DuplicatedCode")
public enum SizeType {
    BYTE,
    KB,
    MB,
    GB,
    TB,
    PB;

    public BigInteger toByte(BigInteger size) {
        return switch (this) {
            case BYTE -> size;
            case KB -> size.multiply(BigInteger.valueOf(1024));
            case MB -> size.multiply(BigInteger.valueOf(1024 * 2));
            case GB -> size.multiply(BigInteger.valueOf(1024 * 3));
            case TB -> size.multiply(BigInteger.valueOf(1024 * 4));
            case PB -> size.multiply(BigInteger.valueOf(1024 * 5));
        };
    }

    public BigInteger toKb(BigInteger size) {
        return switch (this) {
            case BYTE -> size.divide(BigInteger.valueOf(1024));
            case KB -> size;
            case MB -> size.multiply(BigInteger.valueOf(1024));
            case GB -> size.multiply(BigInteger.valueOf(1024 * 2));
            case TB -> size.multiply(BigInteger.valueOf(1024 * 3));
            case PB -> size.multiply(BigInteger.valueOf(1024 * 4));
        };
    }

    public BigInteger toMb(BigInteger size) {
        return switch (this) {
            case BYTE -> size.divide(BigInteger.valueOf(1024 * 2));
            case KB -> size.divide(BigInteger.valueOf(1024));
            case MB -> size;
            case GB -> size.multiply(BigInteger.valueOf(1024));
            case TB -> size.multiply(BigInteger.valueOf(1024 * 2));
            case PB -> size.multiply(BigInteger.valueOf(1024 * 3));
        };
    }

    public BigInteger toGb(BigInteger size) {
        return switch (this) {
            case BYTE -> size.divide(BigInteger.valueOf(1024 * 3));
            case KB -> size.divide(BigInteger.valueOf(1024 * 2));
            case MB -> size.divide(BigInteger.valueOf(1024));
            case GB -> size;
            case TB -> size.multiply(BigInteger.valueOf(1024));
            case PB -> size.multiply(BigInteger.valueOf(1024 * 2));
        };
    }

    public BigInteger toTb(BigInteger size) {
        return switch (this) {
            case BYTE -> size.divide(BigInteger.valueOf(1024 * 4));
            case KB -> size.divide(BigInteger.valueOf(1024 * 3));
            case MB -> size.divide(BigInteger.valueOf(1024 * 2));
            case GB -> size.divide(BigInteger.valueOf(1024));
            case TB -> size;
            case PB -> size.multiply(BigInteger.valueOf(1024));
        };
    }

    public BigInteger toPb(BigInteger size) {
        return switch (this) {
            case BYTE -> size.divide(BigInteger.valueOf(1024 * 5));
            case KB -> size.divide(BigInteger.valueOf(1024 * 4));
            case MB -> size.divide(BigInteger.valueOf(1024 * 3));
            case GB -> size.divide(BigInteger.valueOf(1024 * 2));
            case TB -> size.divide(BigInteger.valueOf(1024));
            case PB -> size;
        };
    }

    @SuppressWarnings("BigDecimalMethodWithoutRoundingCalled")
    public BigDecimal to(SizeType type, BigDecimal size) {
        return switch (this) {
            case BYTE -> switch (type) {
                case BYTE -> size;
                case KB -> size.divide(BigDecimal.valueOf(1024));
                case MB -> size.divide(BigDecimal.valueOf(1024 * 2));
                case GB -> size.divide(BigDecimal.valueOf(1024 * 3));
                case TB -> size.divide(BigDecimal.valueOf(1024 * 4));
                case PB -> size.divide(BigDecimal.valueOf(1024 * 5));
            };
            case KB -> switch (type) {
                case BYTE -> size.multiply(BigDecimal.valueOf(1024));
                case KB -> size;
                case MB -> size.divide(BigDecimal.valueOf(1024));
                case GB -> size.divide(BigDecimal.valueOf(1024 * 2));
                case TB -> size.divide(BigDecimal.valueOf(1024 * 3));
                case PB -> size.divide(BigDecimal.valueOf(1024 * 4));
            };
            case MB -> switch (type) {
                case BYTE -> size.multiply(BigDecimal.valueOf(1024 * 2));
                case KB -> size.multiply(BigDecimal.valueOf(1024));
                case MB -> size;
                case GB -> size.divide(BigDecimal.valueOf(1024));
                case TB -> size.divide(BigDecimal.valueOf(1024 * 2));
                case PB -> size.divide(BigDecimal.valueOf(1024 * 3));
            };
            case GB -> switch (type) {
                case BYTE -> size.multiply(BigDecimal.valueOf(1024 * 3));
                case KB -> size.multiply(BigDecimal.valueOf(1024 * 2));
                case MB -> size.multiply(BigDecimal.valueOf(1024));
                case GB -> size;
                case TB -> size.divide(BigDecimal.valueOf(1024));
                case PB -> size.divide(BigDecimal.valueOf(1024 * 2));
            };
            case TB -> switch (type) {
                case BYTE -> size.multiply(BigDecimal.valueOf(1024 * 4));
                case KB -> size.multiply(BigDecimal.valueOf(1024 * 3));
                case MB -> size.multiply(BigDecimal.valueOf(1024 * 2));
                case GB -> size.multiply(BigDecimal.valueOf(1024));
                case TB -> size;
                case PB -> size.divide(BigDecimal.valueOf(1024));
            };
            case PB -> switch (type) {
                case BYTE -> size.multiply(BigDecimal.valueOf(1024 * 5));
                case KB -> size.multiply(BigDecimal.valueOf(1024 * 4));
                case MB -> size.multiply(BigDecimal.valueOf(1024 * 3));
                case GB -> size.multiply(BigDecimal.valueOf(1024 * 2));
                case TB -> size.multiply(BigDecimal.valueOf(1024));
                case PB -> size;
            };
        };
    }
}
