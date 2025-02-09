package service;

import dto.*;
import model.shoppingmall.ShoppingMall;
import model.user.User;
import util.CouponGenerator;
import validator.CouponValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShoppingService {

    private final ShoppingMall shoppingMall;
    private final User user;

    public ShoppingService(ShoppingMall shoppingMall, User user) {
        this.shoppingMall = shoppingMall;
        this.user = user;
    }

    public int issueRandomCoupon() {
        CouponValidator.checkUserHasCoupon(user);

        user.receiveCoupon(CouponGenerator.issueRandomCoupon());
        return user.getCouponDiscountRate();
    }

    public int getUserPoint() {
        return user.getPoint();
    }

    public Map<String, List<ProductSimpleInfo>> getProducts() {
        return shoppingMall.getProductsSimpleInfo();
    }

    public ProductDetailInfo getProductDetailInfoByName(String productName) {
        return shoppingMall.findProductDetailByName(productName);
    }

    public void addProductToCart(CartProductInfo cartProductInfo) {
        shoppingMall.addCart(cartProductInfo);
    }

    public List<ProductSimpleInfo> getCartProducts() {
        return shoppingMall.getCartProducts();
    }

    public void deleteCartProduct(CartProductInfo deleteInfo) {
        shoppingMall.deleteCartProduct(deleteInfo);
    }

    public DiscountInfo getUserDiscountInfo() {
        return user.generateDiscountInfo(LocalDateTime.now());
    }

    public CompletableFuture<ChangeAndPoint> pay(PaymentInfo paymentInfo) {
        AtomicBoolean paymentCompleted = new AtomicBoolean(false);
        GameService gameService = new GameService(paymentCompleted);

        CompletableFuture<Void> battleFuture = CompletableFuture.runAsync(gameService::startBattle);
        CompletableFuture<ChangeAndPoint> futurePayment = shoppingMall.paymentProgress(LocalDateTime.now(), paymentInfo);

        return futurePayment.thenApply(changeAndPoint -> {
            paymentCompleted.set(true); // 결제 완료 후 전투 종료 신호
            user.payProcess(paymentInfo, changeAndPoint.rewardPoint());
            return changeAndPoint;
        }).whenComplete((result, ex) -> {
            try {
                battleFuture.get(); // 전투가 완료될 때까지 대기
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("[⚠] 전투 종료 대기 중 오류 발생!");
            }
        });
    }

    public List<OrderHistory> getUserOrderHistory() {
        return shoppingMall.getOrderHistory();
    }
}
