package constant.exception;

public enum ErrorMessage {
    COMMON_EXCEPTION("[ERROR] "),
    INTERRUPTED("\"[⚠] 전투 중단 감지!\""),
    VALIDATOR_CLASS("Validator Class"),
    UTILITY_CLASS("Utility Class"),
    UNKNOWN_TYPE("존재하지 않는 타입입니다."),
    EMPTY_INPUT("비정상적인 입력입니다."),
    INVALID_MAIN_INPUT("1 ~ 6 사이의 숫자를 입력해주세요.\n"),
    INVALID_MENU_INPUT("1 ~ 3 사이의 숫자를 입력해주세요."),
    INVALID_AFTER_PAY_MENU_INPUT("y 또는 n 을 입력해주세요."),
    INVALID_CART_FORMAT("장바구니 형식에 맞게 입력해주세요. ex)반팔,1"),
    INVALID_PAY_FORMAT("1원 이상의 숫자 금액을 입력해주세요. ex)10000"),

    PRODUCT_DOES_NOT_EXIST("[%s]는 존재하지 않는 상품입니다."),
    PRODUCT_DOES_NOT_EXIST_IN_CART("장바구니에 존재하지 않는 상품입니다."),
    COUPON_DOES_NOT_EXIST("적용할 쿠폰이 없습니다."),
    POINT_DOES_NOT_EXIST("적용할 포인트가 없습니다."),
    ALREADY_EXIST_COUPON("이미 쿠폰이 존재합니다.\n"),

    OVER_PURCHASE_QUANTITY("수량이 부족합니다."),
    OVER_DELETE_QUANTITY("장바구니 수량이하의 삭제수량을 입력해주세요."),
    ZERO_DELETE_QUANTITY("1개 이상의 재고를 삭제해주세요."),
    LACK_PAYMENT_AMOUNT("결제 금액이 부족합니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return COMMON_EXCEPTION.message + message;
    }

    public String formatMessage(Object ...args) {
        return COMMON_EXCEPTION.message + String.format(message, args);
    }
}
