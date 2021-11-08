package cn.com.heyue.utils.bin;

import cn.com.heyue.utils.HexStringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

/**
 * @author fuzuyuan
 * @Description
 * @create 2020-11-11 2:13 下午
 */
public class BinParseUtils {
    /**
     * 解析B001
     *
     * @param jsonObject 要返回的json
     * @param content    原始内容
     */
    private static void parseB001(JSONObject jsonObject, String content) {
        String cardIdentify = content.substring(0, 16); // 发卡方标识
        String appIdentify = content.substring(16, 18); // 应用类型标识
        String appVersion = content.substring(18, 20); // 发卡方应用版本
        String appSeq = content.substring(20, 40); // 应用序列号
        String appStartDate = content.substring(40, 48); // 应用启用日期
        String appEndDate = content.substring(48, 56); // 应用有效日期
        String fci = content.substring(56, 60); // FCI
        jsonObject.put("cardIdentify", cardIdentify);
        jsonObject.put("appIdentify", appIdentify);
        jsonObject.put("appVersion", appVersion);
        jsonObject.put("appSeq", appSeq);
        jsonObject.put("appStartDate", appStartDate);
        jsonObject.put("appEndDate", appEndDate);
        jsonObject.put("fci", fci);
    }

    /**
     * 解析B002
     *
     * @param jsonObject 要返回的json
     * @param content    原始内容
     */
    private static void parseB002(JSONObject jsonObject, String content) {
        String cardTypeIdentify = content.substring(0, 2); // 发类型标识
        String staff = content.substring(2, 4); // 本行职工标识
        String name = content.substring(4, 44); // 持卡人姓名
        String cardNo = content.substring(44, 108); // 持卡人证件号码
        String cardType = content.substring(108, 110); // 持卡人证件类型
        jsonObject.put("cardTypeIdentify", cardTypeIdentify);
        jsonObject.put("staff", staff);
        jsonObject.put("name", name);
        jsonObject.put("cardNo", cardNo);
        jsonObject.put("cardType", cardType);
    }

    /**
     * 解析B003
     *
     * @param jsonObject 要返回的json
     * @param content    原始内容
     */
    private static void parseB003(JSONObject jsonObject, String content) {
        String worldCode = content.substring(0, 8); // 国际代码
        String proviceCode = content.substring(8, 12); // 省级代码
        String cityCode = content.substring(12, 16); // 城市代码
        String uniCardType = content.substring(16, 20); // 互通卡种
        String cardType = content.substring(20, 22); // 卡种类型
        String reserver = content.substring(22, 120); // 预留
        jsonObject.put("worldCode", worldCode);
        jsonObject.put("proviceCode", proviceCode);
        jsonObject.put("cityCode", cityCode);
        jsonObject.put("uniCardType", uniCardType);
        jsonObject.put("cardType", cardType);
        jsonObject.put("reserver", reserver);
    }

    /**
     * 解析B0A0
     *
     * @param jsonObjectSrc 要返回的json
     * @param content       原始内容
     */
    private static void parseB0A0(JSONObject jsonObjectSrc, String content) {
        String cardPriKey = content.substring(0, 38); // 用户卡主控密钥
        String cardStickKey = content.substring(38, 76); // 用户卡维护密钥
        String cardAppPriKey = content.substring(76, 114); // 用户卡应用主控密钥
        String cardAppStickKey = content.substring(114, 152); // 用户卡应用维护密钥
        String cousumeKey = content.substring(152, 190); // 消费密钥
        String rechargeKey = content.substring(190, 228); // 充值密钥
        String tacKey = content.substring(228, 266); // TAC 密钥
        String cardAppStickLockKey = content.substring(266, 304); // 用户卡应用维护密钥（应用锁定）
        String pinKey = content.substring(304, 342); // PIN 密钥
        String cashKey = content.substring(342, 380); // 互通记录保护密钥-电子现金
        String walletKey = content.substring(380, 418); // 互通记录保护密钥-电子钱包
        String cashKey2 = content.substring(418, 456); // 互通记录保护密钥（现金备用）
        String appDelLockKey = content.substring(456, 494); // 用户卡应用维护密钥（应用解锁）
        String reserverKey1 = content.substring(494, 532); // 预留密钥
        String reserverKey2 = content.substring(532, 570); // 预留密钥
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cardPriKey", cardPriKey);
        jsonObject.put("cardStickKey", cardStickKey);
        jsonObject.put("cardAppPriKey", cardAppPriKey);
        jsonObject.put("cardAppStickKey", cardAppStickKey);
        jsonObject.put("cousumeKey", cousumeKey);
        jsonObject.put("rechargeKey", rechargeKey);
        jsonObject.put("tacKey", tacKey);
        jsonObject.put("cardAppStickLockKey", cardAppStickLockKey);
        jsonObject.put("pinKey", pinKey);
        jsonObject.put("cashKey", cashKey);
        jsonObject.put("walletKey", walletKey);
        jsonObject.put("cashKey2", cashKey2);
        jsonObject.put("appDelLockKey", appDelLockKey);
        jsonObject.put("reserverKey1", reserverKey1);
        jsonObject.put("reserverKey2", reserverKey2);
        jsonObjectSrc.put("keyData", jsonObject);
    }

    /**
     * 解析B005
     *
     * @param jsonObject 要返回的json
     * @param content    原始内容
     */
    private static void parseB005(JSONObject jsonObject, String content) {
        String rechargeKey2 = content.substring(0, 38); // 国际代码
        jsonObject.put("rechargeKey2", rechargeKey2);
    }

    /**
     * 要解析的内容
     *
     * @param content
     * @return
     */
    private static JSONObject parseBin(String content) {
        JSONObject jsonObject = new JSONObject();
        String INFO = content.substring(0, 24);  // 此信息未知，文档未注明，长度固定24
        String MIC = content.substring(24, 54);  // MIC 信息
        int LCCA = Integer.parseInt(content.substring(54, 70), 16);  // LCCA 长度
        int DGINum = Integer.parseInt(content.substring(70, 78), 16);  // DGINum 长度
        jsonObject.put("INFO", INFO);
        jsonObject.put("MIC", HexStringUtils.hexStringToString(MIC));
        jsonObject.put("LCCA", LCCA);
        jsonObject.put("DGINum", DGINum);
        // DGI数组，个数=DGINum
        JSONArray jsonArray = new JSONArray();
        int index = 78; // 目前索引
        // 取part2
        for (int i = 0; i < DGINum; i++) {
            int DGINameLen = Integer.parseInt(content.substring(index, index + 4), 16);  // DGINameLen 长度
            index = index + 4; // 下移
            String DGIName = HexStringUtils.hexStringToString(content.substring(index, index + DGINameLen * 2)); // 向后取 DGINameLen*2 长度
            index = index + DGINameLen * 2; // 下移

            JSONObject dgiJsonObject = new JSONObject();
            dgiJsonObject.put("DGIName", DGIName);

            jsonArray.add(dgiJsonObject);
        }

        String SeqNo = content.substring(index, index + 8);  // SeqNo 卡片序列号
        jsonObject.put("SeqNo", SeqNo);
        index = index + 8; // 下移
        String LDATA = content.substring(index, index + 4);  // LDATA 该张卡片后续所有数据的长度
        jsonObject.put("LDATA", LDATA);
        index = index + 4; // 下移
        for (int i = 0; i < DGINum; i++) {
            JSONObject dgiJsonObject = jsonArray.getJSONObject(i);

            String DGIStart = content.substring(index, index + 2);  // DGIStart 起始 86
            dgiJsonObject.put("DGIStart", DGIStart);
            index = index + 2; // 下移

            int DGI_len = Integer.parseInt(content.substring(index, index + 2), 16);  // DGI_len 检查长度是否大于0x80=128
            dgiJsonObject.put("DGI_len", DGI_len);
            index = index + 2; // 下移

            if (DGI_len < 128) {
                // 小于128，一个字节
                String DGI = content.substring(index, index + 4);  // DGI
                dgiJsonObject.put("DGI", DGI_len);
                index = index + 4; // 下移

                int DGILen = Integer.parseInt(content.substring(index, index + 2), 16);  // DGILen DGI数据长度
                dgiJsonObject.put("DGILen", DGILen);
                index = index + 2; // 下移

                String DGIContent = content.substring(index, index + DGILen * 2);  // DGILen DGI数据长度
                if (StringUtils.endsWithIgnoreCase("B001", DGI)) {
                    parseB001(dgiJsonObject, DGIContent);
                } else if (StringUtils.endsWithIgnoreCase("B002", DGI)) {
                    parseB002(dgiJsonObject, DGIContent);
                } else if (StringUtils.endsWithIgnoreCase("B003", DGI)) {
                    parseB003(dgiJsonObject, DGIContent);
                } else if (StringUtils.endsWithIgnoreCase("B0A0", DGI)) {
                    parseB0A0(dgiJsonObject, DGIContent);
                } else if (StringUtils.endsWithIgnoreCase("B005", DGI)) {
                    parseB005(dgiJsonObject, DGIContent);
                } else if (StringUtils.endsWithIgnoreCase("B1A0", DGI)) {
                    parseB0A0(dgiJsonObject, DGIContent);
                } else if (StringUtils.endsWithIgnoreCase("B1A5", DGI)) {
                    parseB005(dgiJsonObject, DGIContent);
                }
                index = index + DGILen * 2; // 下移
            } else {
                // 大于128，二个字节
                index = index + 4; // 需要下移四个
                String DGI = content.substring(index, index + 4);  // DGI
                dgiJsonObject.put("DGI", DGI_len);
                index = index + 4; // 下移

                int DGILen = Integer.parseInt(content.substring(index, index + 4), 16);  // DGILen DGI数据长度
                dgiJsonObject.put("DGILen", DGILen);
                index = index + 4; // 下移

                String DGIContent = content.substring(index, index + DGILen * 2);  // DGILen DGI数据长度
                if (StringUtils.endsWithIgnoreCase("B001", DGI)) {
                    parseB001(dgiJsonObject, DGIContent);
                } else if (StringUtils.endsWithIgnoreCase("B002", DGI)) {
                    parseB002(dgiJsonObject, DGIContent);
                } else if (StringUtils.endsWithIgnoreCase("B003", DGI)) {
                    parseB003(dgiJsonObject, DGIContent);
                } else if (StringUtils.endsWithIgnoreCase("B0A0", DGI)) {
                    parseB0A0(dgiJsonObject, DGIContent);
                } else if (StringUtils.endsWithIgnoreCase("B005", DGI)) {
                    parseB005(dgiJsonObject, DGIContent);
                } else if (StringUtils.endsWithIgnoreCase("B1A0", DGI)) {
                    parseB0A0(dgiJsonObject, DGIContent);
                } else if (StringUtils.endsWithIgnoreCase("B1A5", DGI)) {
                    parseB005(dgiJsonObject, DGIContent);
                }
                index = index + DGILen * 2; // 下移
            }
        }

        jsonObject.put("DGIArray", jsonArray);

        System.out.println(jsonObject.toJSONString());
        return jsonObject;
    }

    public static void main(String[] args) {
        String content = "91d15361901a75285630312d54556e696f6e44504743545956303100000000000014d80000003f000744474930313031000744474930313032000744474930323031000744474930323032000744474930323033000744474930323034000744474930323035000744474930333031000744474930333032000744474930343031000744474930353031000744474930373031000744474930373032000744474930373033000744474930373034000744474930373035000744474930383031000744474930383032000744474930383033000744474930393031000744474930393032000744474930443031000744474930453031000744474939313032000744474939313033000744474939313034000744474939313035000744474939323030000744474939323031000744474939323033000744474939323034000744474939323037000744474939323038000744474930303938000744474930303939000744474930313030000744474941303031000744474938303030000744474938343030000744474939303030000744474939303033000744474937373737000744474937373731000744474938303130000744474938303131000744474939303130000744474938323031000744474938323032000744474938323033000744474938323034000744474938323035000744474938343032000744474938303230000744474938303231000744474939303230000744474939303231000744474942303031000744474942303032000744474942303033000744474942304130000744474942314130000744474942304135000744474942314135000000011297861a010117701557133104895199900000003d40122200000079900f864901024670449f612020202020202020202020202020202020202020202020202020202020202020209f6201005f201a2020202020202020202020202020202020202020202020202020868201010201fe7081fb9081f808a8c3b6590d09c32e0ca3c10f51232750a4302002cad486311f454eedf9f13a86be2756602dc48602c5c780baf3bfefa1ed6ccad8a50c7e51ddfc2453103d9477baad421cd5b5d370d67cfd94136bb8355303212e8d4d8f64afcd2d4cfbd25fd121cc5036ec0433881db81b903ee5c36e325966a234a3aa2f42c9dbaf3b01e41fbf596afaa40745d785aff9b42af34632ebfccba49d06c61107786a37d2ed336e2031ebfb1a1957116b2b794fa38390303c271041139ef84312e134f8e0ed566d9d3a3870df35d5861beb573ed058e122ed30cdbdaf4b26a7339982fa78ec47e5142986c961ad41e4902af1502a482fa227930b6ae26356860e02020b70098f01089f3201039200868199020396708193938190186b79d537b3e4839b2e20521d2f289bb7e11e7a93756995c88f444f02268e0ba321c11341839d020f75fe8ecb5ecd5dbef3396ca2cb4724233be9c407b7660f7dec4184022af80303dada7f9f9f05293eeb3136076ed7177e2f6fa42c5e63e30f32d1897b908f16f6db263d89cbf4a4499d4101ba9ddc288404156025d7ba865dcda0bce88df9a32669b393b14a74a786819a0204977081949f46819063f4ba42ab7fc6a7189b8f12bd9343d2ef8dc9cb44c525d22dd73d437265d6b0a0644680941c9be5d6840c4620631186a5bccaa547b001a74b4403e5c4cc3bf068459eba21ae7ced25b71dd398862cf4fb22ec01148d6088007d3791cc39f390d57ad234d845f6724cabd766b4b3cc7d970d9a640964449e5b66e1f1a30d1d94eaf77d97266b839116effaea7caa9b2a862c02052970279f4701039f481a271b8ed85934c442e4152db038e0d32b7b3c3c4bc4e2b394c86d9f49039f370486818f03018c7081895a0a3104895199900000003f5f24034012315f25032104165f3401009f0702ff008e0c000000000000000002031f005f280201569f0d05d86004a8009f0e0500109800009f0f05d86804f8009f4a01828c1b9f02069f03069f1a0295055f2a029a039c019f37049f21039f4e148d1a8a029f02069f03069f1a0295055f2a029a039f21039c019f3704862e03022b70299f420201565f300202209f080200309f4401029f0b1320202020202020202020202020202020202020860d04010a70089f1401009f230100860e05010b70099f7406454343303031868183070180707e5a0a3104895199900000003f5f24034012315f25032104169f0702ff008e0a00000000000000001f009f0d05d8603ca8009f0e0500108000009f0f05d8683cf8009f4a01828c1b9f02069f03069f1a0295055f2a029a039c019f37049f21039f4e148d1a8a029f02069f03069f1a0295055f2a029a039f21039c019f3704861907021670149f420201565f300202209f080200305f2802015686819a0703977081949f468190176c980d21f33dced94780dbc50c73d224c56234f286959d1e58bea849456b829b285c7cca328802aabe2fb9572f2dea73faa537e048c2e49d42a5768ef24cf37b88c4900d9ee88916cd565ffd6ddb1788e16152f1b6d56fce0019fc4f7476a5ed0a70ab0ea504aba3c6e60fe0a50e099d2415a221ad24fa425f22744c4a03e77021189fdf68cf181f2d385ceae6e74f862c07042970279f4701039f481a271b8ed85934c442e4152db038e0d32b7b3c3c4bc4e2b394c86d9f49039f3704868199070596708193938190a97cf0317ad8767d3990e5941d1790415b751fada6369b592e73bd95b8468e2359c045d7647d3993ce9fc18943d58fe2cc7bb802ba78d5f764e182ddb1a2610d1f33c6bf37888865f8daf888677e3efd7caba75e72b892f26c1016786c06c905142178d80fe084a0466474b40d6beea052539b966d87645a55e5cb6a592f8c1f4a8c47eb6449fd592516878a4867ff0a86819a08019770819490818e1231048951124065432104001140b373d53f680bd2963d568f758899c71a8a94ae11b424726bd6a2ff9d1baddfd1db4a60086d30ad4f2a950da602035a325a28aa294476c34e0e99ab6ed2cabe196e54be0958cec44c2ee2803a1704632483f2db507a8e21492cd9ae673fc5a9f97dbaba2486b1dbaa6bdab0403bc15c1bd2b2dcfde4c2b1d350020ff1811314888f0101864a080247704593431331054d99fb6d0afbe701d19d246add50fdc7e88885f788606aaf0460ce408309894fb83a05301814e61715f7ca2629a1b475c450aca92c4f50c103eadee032667c5f8681a40803a170819e9f468194143104895199900000003f124000003104001140307e7978b05f376e1155351e115771040e1f463133ae426e34b3303cad50c7c7724e44a8fa5c11933aa7b1f57a1c49f389e6a785c9f08b07594a35af4144826fde70e59ad7d2f9215d1685affe4864f5c631188cf9c9bc2055f537efb37d5efb7aecbc3f0826ca2579008464d0863dbddb3f9219dbbf04a0b4bc67da4ee363589f49039f37048681a40901a170819e9f468194143104895199900000003f124000003104001140307e7978b05f376e1155351e115771040e1f463133ae426e34b3303cad50c7c7724e44a8fa5c11933aa7b1f57a1c49f389e6a785c9f08b07594a35af4144826f3b2ac5a4eb022d19681012bd93d1d3eddb9a7ff75832d869c0ad8f56a575463e96ccf5ca21f11a5391b29489364fc03f1713fef57b694b40f0a3d16ab6a729869f49039f3704864a09024770459343133105d9bb25091f26ee4e3e61ea48ebd4d2afa41ca481fc39ecc2055b7c182dcceed846531013a0d5000c531bc0936aba808594b10ff7eee33f0873f7df09b7eafeed864c0d01499f5501009f5601009f570201569f5801009f5901009f7201009f5301009f54060000000000009f5c060000000000009f7304000000009f75060000000000009f760201569f360200008681a40e01a19f4f199a039f21039f02069f03069f1a025f2a029f4e149c019f36029f510201569f5202c0009f5d060000000000019f6804815000009f690801000000000000009f6b060000001000009f6c0200009f6d060000000000009f77060000001000009f78060000001000009f7906000000000000df6206000000000000df6306000000000000df4f199a039f21039f02069f03069f1a025f2a029f4e149c019f3602864c910249a547bf0c0a9f4d020b0adf4d020c0a500a4d4f545f545f434153488701019f3812df69019f1a029f7a019f02065f2a029f4e145f2d027a689f1101019f120a50424f43204445424954866c910369a567500a4d4f545f545f434153488701019f3821df6001df69019f66049f02069f03069f1a0295055f2a029a039c019f21039f3704bf0c31df112005212482ffffffff003104895199900000003f010100000156240024820001ff9f4d020b0adf610182df4d020c0a861991041682027c00941008010200100105001801020120010100861991051682027c00941008010200400103001801020120010100861092000d9f100a07010103000000010a01861092010d9f100a07010103000000040a01861d92031a82027c0094140801020010010200200101002801010038010501862192041e82027c009418080102004001010020010100280101003801020148010200862292071f82027c00940c1001020038010501280101009f100a07010103000000010a01862692082382027c009410400101002801010038010201480102009f100a07010103000000040a01862d00982a702861264f08a000000632010106500a4d4f545f545f434153489f120a50424f43204445424954870101861100990ea50c8801015f2d027a689f110101863c010039a537bf0c3461194f08a000000632010106500a4d4f545f545f4341534887010161174f08a00000063201010550084d4f545f545f45508701028642a0013f15010000ff028016010000ff018017010000ff040018010000ff020019010000ff04001a010000ff00001b010000ff01801c010000ff01801e020000301e308633800030bbe8b7ff56d6fa7bb2145c9cf6042c9ba85ed9774edc09edb6fe3f00dfd15ddd04e568fccf412a81bead945f0f0a6d828633840030d6a58a7e0bd7d4c5b0d99540e3fa84d3656e0737d7b172052a44a3b3d089828dbafb6243103809c501f672c8d8ad7ee6860c900009d75832faae4d7c6c91860c900309695def81fc59cc8e2a8616777713d8908dd78c456c140080e5494fb223e94aebe98616777113de26666e20988b1456c223328ffb68f6335a108613801010e3c1901ad4f0c3b05f03e1efa34adc518613801110612c1c0b3e3f13abac2d6bf2666f1a5786059010020303864382014074ef05b3b2612a9704ceabef6a156178314209c57b9dd949f4e4df202ea794b5f147e91f8cf435597cfe7036d53708e1b237e2da5c63d764fa8f5bbc68d7baf786438202407e5125c8ffb7471db04e68b476f9cffd9df287605ccb987902b33245c2019e335c5d02556e7b4a7954af12ae89461f9e3549d330143c83f39a6f0f889a2f382a8643820340b5f7e20e3b457c4ffcd2faabe11623b46258f0abee01692e88025d0d36cea69dd1bc5c9e079cb498c0ad2a7183b987c1b2f7ac0e299bfb099fc927e395edcf378643820440607313a46bd3d07eacf5def58501365c774fff6a96d123fcf95d06d99e46942d1c75e3dcdd4f107eb86d304ac6387e2c5101a848798552745f4aace24662eee2864382054048a779679c16f6a8eff1cdf5fecba37e3c4ee3d182a7227d91f9e3c79e617bad476582e99f0baa91de90d270d8f865829d0dd560299bfdc058325fddb510f88c86238402205bf3d7130cbb7802973bd5b16d5355cd63716ce51947ec313e51e9d91b74d064868193802090a6faa06b22a4c05dca7f6e1ba11f7656a6faa06b22a4c05dca7f6e1ba11f7656a6faa06b22a4c05dca7f6e1ba11f7656a6faa06b22a4c05dca7f6e1ba11f7656a6faa06b22a4c05dca7f6e1ba11f7656a6faa06b22a4c05dca7f6e1ba11f7656a6faa06b22a4c05dca7f6e1ba11f7656a6faa06b22a4c05dca7f6e1ba11f7656a6faa06b22a4c05dca7f6e1ba11f7656868193802190a9d21dc0b01b90085cb35ed0d5a3b862a9d21dc0b01b90085cb35ed0d5a3b862a9d21dc0b01b90085cb35ed0d5a3b862a9d21dc0b01b90085cb35ed0d5a3b862a9d21dc0b01b90085cb35ed0d5a3b862a9d21dc0b01b90085cb35ed0d5a3b862a9d21dc0b01b90085cb35ed0d5a3b862a9d21dc0b01b90085cb35ed0d5a3b862a9d21dc0b01b90085cb35ed0d5a3b862861e90201b93699b93699b93699b93699b93699b93699b93699b93699b93699b861e90211bc11c7ac11c7ac11c7ac11c7ac11c7ac11c7ac11c7ac11c7ac11c7a8621b0011e05212482ffffffff02010310489519990000000320210416204012310000863ab0023700002020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202000863fb0033c00000156240024820001012020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202086820121b0a0011d7f75c41db5eecb6a91547a0b63b274427b044847c443f05c7d3bf230f1caddd8f159ef2320d8e8978653df9b88fc8dcc24f5accaa83373032882aef9802e51c72724b8654e93379bc6de40325140c9fd3469cfcd48d31ee57b71e6fe00f839508aa5b843de45fe480e713809ae7350537f636d98dc5954ba944192e8be8debc8b5657583d591b927e134cfcdff50043634a78fe62f5060ef6d0a30e6f2869b2c5f03e1efa34adc5161b7f0bba487c370360db5b71ce5db622cae748f40f4c512a2186407677ed4d0f8e408b17908ca3b4a3c41af0142d57584aa992957f33ee3ad28be4291b927e134cfcdff50043634a78fe62f5060ef82aef9802e51c72724b8654e93379bc6de403282aef9802e51c72724b8654e93379bc6de403286820121b1a0011d29da31d8f958b8ff0ccf4b762f00fa40cc4881707fdb8b10cd26f159c64acea72ff78822f4e3522cb415c8e5ba22b765fc7d037926d96a81f83c8b5475cc1188021c1b8d98c11521116102dd8e0767dfaecfcad83032e3dd2d2f0e3df20b2c02446cb5874b7f02048c5d13dcbe1ce878b0e04c0ca9de029ea33c140a8374942b14229cbed9a02d43d0ca0e30b7c0fd548e2e79ce75b3d447d9996063079dae5d7314f01ba396bb1b2900456764b4b3eec7b76d719b48b2919627cd44d1f5a3ba38d7f24637ba40f1973c6b5583d6e38d48d8739d6e18f3b8eeaf6c89a6ef3b2253cf0b4ba02d43d0ca0e30b7c0fd548e2e79ce75b3d4473c8b5475cc1188021c1b8d98c11521116102dd3c8b5475cc1188021c1b8d98c11521116102dd8616b0a513508aa5b843de45fe480e713809ae7350537f638616b1a51302446cb5874b7f02048c5d13dcbe1ce878b0e0";
        BinParseUtils.parseBin(content);
    }
}
