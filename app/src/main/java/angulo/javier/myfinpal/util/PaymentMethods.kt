package angulo.javier.myfinpal.util

enum class PaymentMethods(val stringValue: String) {
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    PAYPAL("PayPal"),
    BANK_TRANSFER("Bank Transfer"),
    CASH("Cash")
}