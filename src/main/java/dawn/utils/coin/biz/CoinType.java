package dawn.utils.coin.biz;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * 币种Bean，用于在业务上区分各个业务系统的差异性
 */
@Data
public class CoinType {

    /**
     * 本币币种
     */
    private String standardCoinType;
    /**
     * 本币名称
     */
    private String standardCoinName;
    /**
     * 代币币种
     * 如果是本币，则该字段返回本币
     * eg：ETH-BNB：tokenType返回BNB
     * eg：ETH:tokenType返回ETH
     */
    private String tokenType;
    /**
     * 原始币种
     */
    private String requestCoinType;
    /**
     * 币种显示名称
     */
    private String coinName;
    /**
     * 是否有token
     **/
    private boolean hasToken;

    public CoinType(String requestCoinType) {
        this.standardCoinType = CoinTypeUtils.getCoin(requestCoinType);
        this.standardCoinName = CoinTypeUtils.getCoinName(standardCoinType);
        this.hasToken = CoinTypeUtils.hasToken(requestCoinType);
        this.tokenType = CoinTypeUtils.getToken(requestCoinType)==null?requestCoinType:CoinTypeUtils.getToken(requestCoinType);
        this.coinName = CoinTypeUtils.getCoinName(requestCoinType);
        this.requestCoinType = requestCoinType;

    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(new CoinType("BNB")));
        System.out.println(JSON.toJSONString(new CoinType("ETH-")));
        System.out.println(JSON.toJSONString(new CoinType("ETH-BNB")));
        System.out.println(JSON.toJSONString(new CoinType("ETH")));
        System.out.println(JSON.toJSONString(new CoinType("")));
        System.out.println(JSON.toJSONString(new CoinType(null)));
        System.out.println(JSON.toJSONString(new CoinType("-")));
    }

}
