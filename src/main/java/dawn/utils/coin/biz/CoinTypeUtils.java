package dawn.utils.coin.biz;


import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 币种工具类
 */
public class CoinTypeUtils {

    /**
     * 币种名称分割符
     */
    private static final String COIN_NAME_SEPARATOR = "-";

    /**
     * 根据币种获取币种名称
     * 场景：ERC-20 Token中 内部系统定义CoinType为：ETH-ABN，前端展示应为ABN
     *
     * @param coinType 币种
     * @return 币种名称
     */
    public static String getCoinName(String coinType) {
        if (isBlank(coinType) || !coinType.contains(COIN_NAME_SEPARATOR)) {
            return coinType;
        }
        coinType.indexOf("-");
        return coinType.substring(coinType.indexOf("-")+1);
    }




    /**
     * 根据币种获取该币种的token
     *
     * @param coinType 币种
     * @return 如果没有token则返回null，有则返回token
     * 场景：用于校验该币是否有token，并且获取token
     */
    public static String getToken(String coinType) {
        return hasToken(coinType) ? coinType.split(COIN_NAME_SEPARATOR)[1] : null;
    }

    /**
     * 是否有token
     *
     * @param coinType 币种
     * @return ETH：false
     * ETH-BNB：true
     * ETH-：false
     * -：false
     * ""：false
     * null:false
     */
    public static boolean hasToken(String coinType) {
        return !isBlank(coinType) && (coinType.contains(COIN_NAME_SEPARATOR) &&
                (coinType.split(COIN_NAME_SEPARATOR)).length > 1);
    }

    public static void main(String[] args) {
        System.out.println(hasToken("ETH"));
        System.out.println(hasToken("ETH-BNB"));
        System.out.println(hasToken("ETH-"));
        System.out.println(hasToken("-"));
        System.out.println(hasToken(""));
        System.out.println(hasToken(null));

        System.out.println("=============================");

        System.out.println(getToken("ETH"));
        System.out.println(getToken("ETH-BNB"));
        System.out.println(getToken("ETH-"));
        System.out.println(getToken("-"));
        System.out.println(getToken(""));
        System.out.println(getToken(null));

        System.out.println("=============================");

        System.out.println(getCoinName("ETH"));
        System.out.println(getCoinName("ETH-BNB"));
        System.out.println(getCoinName("ETH-"));
        System.out.println(getCoinName("-"));
        System.out.println(getCoinName(""));
        System.out.println(getCoinName(null));

        System.out.println("=============================");

        System.out.println(getCoin("ETH"));
        System.out.println(getCoin("ETH-BNB"));
        System.out.println(getCoin("ETH-"));
        System.out.println(getCoin("-"));
        System.out.println(getCoin(""));
        System.out.println(getCoin(null));


    }

    /**
     * 获取本币
     *
     * @param coinType 币种
     * @return 如ETH-BNB返回ETH，ETH返回ETH，BTC返回BTC
     */
    public static String getCoin(String coinType) {
        return hasToken(coinType) ? coinType.split(COIN_NAME_SEPARATOR)[0] : coinType;
    }


}
