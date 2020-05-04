import java.math.BigInteger;

/**
 * 生成地址对应IBAN账户
 * 具体可以了解 https://www.jianshu.com/p/ab15564f711a?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation
 */
public class IBAN {

    /**
     * 根据地址生成qr
     * @param address
     */
    public static void addressToIBAN(String address) {
        address = address.toLowerCase();
        if (address.startsWith("0x")) {
            address = address.substring(2);
        }

        BigInteger addressValue = new BigInteger(address,16);
        StringBuilder bban = new StringBuilder(addressValue.toString(36).toUpperCase());

        while (bban.length() <15*2) {
            bban.insert(0,"0");
        }

        String iban = "XE00"+ bban;

    }

    private static String IBAN2Address(String iban) {
        String base36 = iban.substring(4);
        StringBuilder base16 = new StringBuilder(new BigInteger(base36, 36).toString(16));
        while (base16.length() < 40) {
            base16.insert(0, "0");
        }
        return "0x" + base16.toString().toLowerCase();
    }
}
