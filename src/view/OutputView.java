package view;

import dto.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OutputView {
    private static final String TOP = "TOP";
    private static final String BOTTOM = "BOTTOM";
    private static final String LINE_SEPARATOR = "-------------------------------------------------------";

    public void handleExceptionMessage(ExceptionDto exceptionDto) {
        printExceptionMessage(exceptionDto.message());
    }

    private void printExceptionMessage(String exceptionMessage) {
        System.out.println(exceptionMessage);
    }

    public void printIssuedCoupon(int couponDiscountRate) {
        System.out.println("축하드립니다. " + couponDiscountRate + "% 쿠폰을 발급해드렸습니다.");
        System.out.println("유효기간은 5분 입니다.\n");
    }

    public void printUserPoint(int userPoint) {
        System.out.println("고객님의 포인트는 " + userPoint + "원 존재합니다.\n");
    }

    public void printProductSimpleInfo(Map<String, List<ProductSimpleInfo>> productSimpleInfos) {
        System.out.println("상품을 구경하고 맘에드는 상품을 장바구니에 넣어보세요.\n");
        System.out.println("[Top]");
        System.out.println(LINE_SEPARATOR);
        List<ProductSimpleInfo> tops = productSimpleInfos.get(TOP);
        for (ProductSimpleInfo top : tops) {
            System.out.println(formatProductSimpleInfo(top));
        }

        System.out.println("\n[Bottom]");
        System.out.println(LINE_SEPARATOR);
        List<ProductSimpleInfo> bottoms = productSimpleInfos.get(BOTTOM);
        for (ProductSimpleInfo bottom : bottoms) {
            System.out.println(formatProductSimpleInfo(bottom));
        }
    }

    public void printProductDetailInfo(ProductDetailInfo detailInfo) {
        System.out.println(LINE_SEPARATOR);
        System.out.println("[%s]의 상세정보 입니다.".formatted(detailInfo.name()));
        System.out.println(LINE_SEPARATOR);
        System.out.print(detailInfo.getDetail());
        System.out.println("가격 - " + formatPrice(detailInfo.price()));
    }

    public void printSuccessAddCartMessage(CartProductInfo cartProductInfo) {
        System.out.println("[" + cartProductInfo.name() + "] 상품 " + cartProductInfo.purchaseOrDeleteQuantity() + "개가 장바구니에 추가되었습니다.");
    }

    public void printCartProducts(List<ProductSimpleInfo> cartProducts) {
        if (cartProducts.isEmpty()) {
            System.out.println("장바구니가 비어있습니다. 쇼핑을 진행해보세요~!\n");
            return;
        }

        System.out.println("장바구니속 상품입니다.");
        int totalPrice = 0;
        for (ProductSimpleInfo cartProduct : cartProducts) {
            System.out.println("- [%s] %s사이즈 %d개 %s".formatted(cartProduct.name(), cartProduct.size(), cartProduct.quantity(), formatPrice(cartProduct.price())));
            totalPrice += cartProduct.price();
        }
        System.out.println("\n총 가격 : " + formatPrice(totalPrice));
        System.out.println(LINE_SEPARATOR);
    }

    private String formatProductSimpleInfo(ProductSimpleInfo product) {
        if (product.quantity() == 0) {
            return "- [%s] %s사이즈, %s, 품절 ".formatted(product.name(), product.size(), formatPrice(product.price()));
        }
        return "- [%s] %s사이즈, %s, %d개 남음 ".formatted(product.name(), product.size(), formatPrice(product.price()), product.quantity());
    }

    private String formatPrice(int price) {
        return String.format("%,d원", price);
    }

    public void printExitMessage() {
        System.out.println("다음에 또 방문해주세요~!");
    }

    public void printSuccessDeleteCartProduct(CartProductInfo deleteInfo) {
        System.out.println(LINE_SEPARATOR);
        System.out.println("[%s] %d개가 정상적으로 삭제되었습니다.".formatted(deleteInfo.name(), deleteInfo.purchaseOrDeleteQuantity()));
        System.out.println(LINE_SEPARATOR);
    }

    public void printPaymentInfos(List<ProductSimpleInfo> cartProducts, DiscountInfo userDiscountInfo) {
        System.out.println(LINE_SEPARATOR);
        System.out.println("결제하실 상품 리스트 입니다.");
        System.out.println(LINE_SEPARATOR);

        int totalPrice = 0;
        for (ProductSimpleInfo cartProduct : cartProducts) {
            System.out.println("- [%s] %s사이즈 %d개 %s".formatted(cartProduct.name(), cartProduct.size(), cartProduct.quantity(), formatPrice(cartProduct.price())));
            totalPrice += cartProduct.price();
        }

        System.out.println("%n쿠폰 할인 금액 : %s".formatted(formatPrice(userDiscountInfo.calculateCouponAppliedPrice(totalPrice))));
        System.out.println("포인트 적용 : %s".formatted(formatPrice(userDiscountInfo.getAppliedPoint())));

        totalPrice -= userDiscountInfo.calculateCouponAppliedPrice(totalPrice);
        totalPrice -= userDiscountInfo.getAppliedPoint();

        System.out.println("총 가격 : %s".formatted(formatPrice(totalPrice)));
        System.out.println(LINE_SEPARATOR);

        if (userDiscountInfo.getCouponRate() == 0 || userDiscountInfo.isCouponUsed()) {
            System.out.println("사용가능한 쿠폰 : 쿠폰 없음");
        } else {
            System.out.println("사용가능한 쿠폰 : %d%% 쿠폰".formatted(userDiscountInfo.getCouponRate()));
        }
        System.out.println("사용가능한 포인트 : %s".formatted(formatPrice(userDiscountInfo.getTotalPoint())));
    }

    public void printFinalPrice(int finalPrice) {
        System.out.println("총 가격은 %s입니다.".formatted(formatPrice(finalPrice)));
    }

    public void printPaymentResult(int payAmount, ChangeAndPoint changeAndPoint) {
        System.out.println("\n[✅] 결제가 완료되었습니다! 전투 종료!");
        System.out.println("지불한 금액 : %s".formatted(formatPrice(payAmount)));
        System.out.println("잔돈 : %s".formatted(formatPrice(changeAndPoint.change())));
        System.out.println("적립 포인트 : %s".formatted(formatPrice(changeAndPoint.rewardPoint())));
    }

    public void printUserOrderHistory(List<OrderHistory> userOrderHistory) {
        if (userOrderHistory.isEmpty()) {
            System.out.println("과거 주문내역이 존재하지 않습니다.\n");
            return;
        }
        System.out.println("======== 주문내역 ========");

        String result = userOrderHistory.stream()
                .map(orderHistory -> """
            주문일시 : %s
            주문 상품 : %s
            적용된 쿠폰 : %s%% 쿠폰
            사용 포인트 : %s 포인트
            총 금액 : %s
            지불 금액 : %s
            잔돈 : %s
            적립 포인트 : %s 포인트
            """.formatted(
                                orderHistory.orderDate(),
                                orderHistory.orderProductInfo(),
                                orderHistory.couponRate() == 0 ? "적용된 쿠폰 없음" : orderHistory.couponRate(),
                                orderHistory.usedPoint(),
                                formatPrice(orderHistory.totalPrice()),
                                formatPrice(orderHistory.payAmount()),
                                formatPrice(orderHistory.change()),
                                orderHistory.rewardPoint()
                        )
                )
                .collect(Collectors.joining("%s\n".formatted(LINE_SEPARATOR)));
        System.out.println(result);
        System.out.println("========================\n");
    }
}

