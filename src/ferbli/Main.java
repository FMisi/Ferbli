/* Ferbli
 * 2022.10.26. CC0LRB
 * Szerintem ez: 40 pontos
 * A következő szempontok miatt:
 *   fileon dolgozik [OK]
 *   van benne regex match, find vagy search [OK]
 *   kell legyen benne valami advanced regex rész is (capturing group, backreference, possessive vagy lazy iterálás, atomi csoport, vagy lookaround), ami kell is a megoldott taskhoz, mert anélkül vagy nem lehet megoldani, vagy sokkal átláthatatlanabb / lassabban illeszthető lesz a regex [OK]
 *   fileba írja ki az outputot [OK] (5/40 pont so far)
 *   az input, output file neveket paraméterként kapja meg, működjön relatív és abszolút pathra is, zárja le a megnyitott erőforrásokat [OK] (10/40 pont so far)
 *   10 pont jár, ha van benne capturing group illesztés, majd a groupban lévő infó feldolgozása [OK] (20/40 pont so far)
 *   10 pont, ha van benne backreference (ahhoz nyilván kell capturing group is, tehát azzal együtt 20 lesz) [OK] (30/40 pont so far)
 *   10 pont, ha van benne lookaround (és kell is) [OK] (40/40 pont so far)
 *
 *   lehetséges -10 pont, az advanced regex rész lehetne még advancedebb (30/40 pont ha nem elég advanced, egyébként 40/40)
 *
 * Néhány példa input a lapok.txt fájlba a program teszteléséhez, egyéb tetszőleges inputok is megadhatóak ezeken kívül:
 * pTt9z8m7
 * pAtKzFma
 * pAtAmAmA
 * t7tTt8t9
 * tAmApAz7
 * z7m8mTm9
 * pApAt7m8
 * t7t9z8mT
 * t9p7m8zT
 * jksdvbuesdcbhqehbc
 */
package ferbli;

import java.util.*;
import java.util.regex.Pattern;

/*
- ferbli handeket elemzünk (részlegesen)

Girigáré
Kicsi (ez az erősebb): piros X, tök IX, zöld VIII, makk VII., pl.: ("pTt9z8m7")
Nagy: piros ász, tök király, zöld felső, makk alsó, (ász: "A", alsó: "a"), pl.: ("pAtKzFma")

Vannak
négy ász; négy király; négy felső; négy alsó; négy X-es; négy IX-es; négy VIII-es; négy VII-es

Banda
Négy lap egy színből

Aranyhármas
három ász; három király; három felső; három alsó; három X-es; három IX-es; három VIII-es; három VII-es

Háromszoros
Három lap egy színből

Két ász
Két ász (más pár nem értékes kombináció)

Ferbli
Két lap egy színből

Szingli
Négy különböző színű lap egy kézben (nem girigáré)
 */

public class Main {
    private static final boolean HAMIS = false;
    private static final boolean IGAZ = true;

    //<editor-fold desc="Checkerek">
    private static boolean kicsiGirigareE(List<String> lines){
        boolean bl = false;

        Pattern regex = Pattern.compile(".*\\bpT(t9z8m7|t9m7z8|z8t9m7|z8m7t9|m7z8t9|m7t9z8)|" +
                "t9(pTz8m7|pTm7z8|m7pTz8|m7z8pT|z8pTm7|z8m7pT)|" +
                "z8(pTt9m7|pTm7t9|m7pTt9|m7t9pT|t9pTm7|t9m7pT)|" +
                "m7(pTt9z8|pTz8t9|z8pTt9|z8t9pT|t9pTz8|t9z8pT)\\b.*");

        for(String line: lines){
            if (line.matches(regex.pattern())) {
                bl = true;
                break;
            }
        }
        return bl;
    }

    private static boolean nagyGirigareE(List<String> lines){
        boolean bl = false;
        Pattern regex = Pattern.compile(".*\\bpA((tKzFma|tKmazF|zFtKma|zFmatK|mazFtK|matKzF)(?!.))|"+
                "tK((pAzFma|pAmazF|zFpAma|zFmapA|mazFpA|mapAzF)(?!.))|"+
                "zF((pAtKma|pAmatK|tKpAma|tKmapA|matKpA|mapAtK)(?!.))|"+
                "ma((pAtKzF|pAzFtK|tKpAzF|tKzFpA|zFtKpA|zFpAtK)(?!.))\\b.*");

        for(String line: lines){
            if (line.matches(regex.pattern())) {
                bl = true;
                break;
            }
        }
        return bl;
    }



    private static boolean vannakE(List<String> lines){
        boolean bl = false;

        Pattern regex = Pattern.compile(".*\\b(pApA)\\1{1}|(tAtA){2}|pA(pAtAtA)|(pAtA){2}|(tApA){2}|tA(tApApA)|"+
                "(zAzA){2}|pA(pAzAzA)|(pAzA){2}|(zApA){2}|zA(zApApA)|"+
                "(mAmA){2}|pA+(pAmAmA)|(pAmA){2}|(mApA){2}|mA(mApApA)|"+
                "tA(tAzAzA)|(tAzA){2}|(mAzA){2}|zA(zAtAtA)|"+
                "tA(tAmAmA)|(tAmA){2}|(mAtA){2}|m+(mAtAtA)|"+
                "mA(mAzAzA)|"+
                "zA(zAmAmA)|"+
                "(zAtA){2}|"+
                "(zApAtAmA)|"+
                "(pAtAzAmA)|"+
                "(pAtAmAmA)|"+
                "(pKpK){2}|(tKtK){2}|pK(pKtKtK)|(pKtK){2}|(tKpK){2}|tK(tKpKpK)|" +
                "(zKzK){2}|pK(pKzKzK)|(pKzK){2}|(zKpK){2}|zK(zKpKpK)|" +
                "(mKmK){2}|pK(pKmKmK)|(pKmK){2}|(mKpK){2}|mK(mKpKpK)|" +
                "tK(tKzKzK)|(tKzK){2}|(mKzK){2}|zK(zKtKtK)|" +
                "tK(tKmKmK)|(tKmK){2}|(mKtK){2}|mK(mKtKtK)|" +
                "mK(mKzKzK)|" +
                "zK(zKmKmK)|" +
                "(zKtK){2}|" +
                "(zKpKtKmK)|"+
                "(pKtKzKmK)|"+
                "(pKtKmKmK)|"+
                "(pFpF){2}|(tFtF){2}|pF(pFtFtF)|(pFtF){2}|(tFpF){2}|tF(tFpFpF)|" +
                "(zFzF){2}|pF(pFzFzF)|(pFzF){2}|(zFpF){2}|zF(zFpFpF)|" +
                "(mFmF){2}|pF(pFmFmF)|(pFmF){2}|(mFpF){2}|mF(mFpFpF)|" +
                "tF(tFzFzF)|(tFzF){2}|(mFzF){2}|zF(zFtFtF)|" +
                "tF(tFmFmF)|(tFmF){2}|(mFtF){2}|mF(mFtFtF)|" +
                "mF(mFzFzF)|"+
                "zF(zFmFmF)|"+
                "(zFtF){2}|"+
                "(zFpFtFmF)|"+
                "(pFtFzFmF)|"+
                "(pKtKmKmK)|"+
                "(papa){2}|(tata){2}|pa(patata)|(pata){2}|(tapa){2}|ta(tapapa)|" +
                "(zaza){2}|pa(pazaza)|(paza){2}|(zapa){2}|za(zapapa)|" +
                "(mama){2}|pa(pamama)|(pama){2}|(mapa){2}|ma(mapapa)|" +
                "ta(tazaza)|(taza){2}|(maza){2}|za(zatata)|" +
                "ta(tamama)|(tama){2}|(mata){2}|ma(matata)|" +
                "ma(mazaza)|"+
                "za(zamama)|"+
                "(zata){2}|"+
                "(zapatama)|" +
                "(patazama)|"+
                "(patamama)|"+
                "(pTpT){2}|(tTtT){2}|pT(pTtTtT)|(pTtT){2}|(tTpT){2}|tT(tTpTpT)|" +
                "(zTzT){2}|pT(pTzTzT)|(pTzT){2}|(zTpT){2}|zT(zTpTpT)|" +
                "(mTmT){2}|pT(pTmTmT)|(pTmT){2}|(mTpT){2}|mT(mTpTpT)|" +
                "tT(tTzTzT)|(tTzT){2}|(mTzT){2}|zT(zTtTtT)|" +
                "tT(tTmTmT)|(tTmT){2}|(mTtT){2}|mT(mTtTtT)|" +
                "mT(mTzTzT)|"+
                "zT(zTmTmT)|"+
                "(zTtT){2}|"+
                "(zTpTtTmT)|"+
                "(pTtTzTmT)|"+
                "(pTtTmTmT)|"+
                "(p9p9){2}|(t9t9){2}|p9(p9t9t9)|(p9t9){2}|(t9p9){2}|t9(t9p9p9)|" +
                "(z9z9){2}|p9(p9z9z9)|(p9z9){2}|(z9p9){2}|z9(z9p9p9)|" +
                "(m9m9){2}|p9(p9m9m9)|(p9m9){2}|(m9p9){2}|m9(m9p9p9)|" +
                "t9(t9z9z9)|(t9z9){2}|(m9z9){2}|z9(z9t9t9)|" +
                "t9(t9m9m9)|(t9m9){2}|(m9t9){2}|m9(m9t9t9)|" +
                "m9(m9z9z9)|"+
                "z9(z9m9m9)|"+
                "(z9t9){2}|"+
                "(z9p9t9m9)|"+
                "(p9t9z9m9)|"+
                "(p9t9m9m9)|"+
                "(p8p8){2}|(t8t8){2}|p8(p8t8t8)|(p8t8){2}|(t8p8){2}|t8(t8p8p8)|" +
                "(z8z8){2}|p8(p8z8z8)|(p8z8){2}|(z8p8){2}|z8(z8p8p8)|" +
                "(m8m8){2}|p8(p8m8m8)|(p8m8){2}|(m8p8){2}|m8(m8p8p8)|" +
                "(t8t8z8z8)|(t8z8){2}|(m8z8){2}|z8(z8t8t8)|" +
                "t8(t8m8m8)|(t8m8){2}|(m8t8){2}|m8(m8t8t8)|" +
                "m8(m8z8z8)|"+
                "z8(z8m8m8)|"+
                "(z8t8){2}|"+
                "(z8p8t8m8)|"+
                "(p8t8z8m8)|"+
                "(p8t8m8m8)|"+
                "(p7p7){2}|(t7t7){2}|p7(p7t7t7)|(p7t7){2}|(t7p7){2}|t7(t7p7p7)|" +
                "(z7z7){2}|p7(p7z7z7)|(p7z7){2}|(z7p7){2}|z7(z7p7p7)|" +
                "(m7m7){2}|p7(p7m7m7)|(p7m7){2}|(m7p7){2}|m7(m7p7p7)|" +
                "(t7t7z7z7)|(t7z7){2}|(m7z7){2}|z7(z7t7t7)|" +
                "t7(t7m7m7)|(t7m7){2}|(m7t7){2}|m7(m7t7t7)|" +
                "m7(m7z7z7)|"+
                "z7(z7m7m7)|"+
                "(z7t7){2}|"+
                "(z7p7t7m7)|"+
                "(p7t7z7m7)|"+
                "(p7t7m7m7)\\b.*");

        for(String line: lines){
            if (line.matches(regex.pattern())) {
                bl = true;
                break;
            }
        }
        return bl;
    }

    private static boolean bandaE(List<String> lines){
        boolean bl = false;

        Pattern regex = Pattern.compile(".*\\bp(\\w)p(\\w)p(\\w)p(\\w)|p(\\w)p(\\w)p(\\w)p(\\w)[\\r\\n]|"+
                "t(\\w)t(\\w)t(\\w)t(\\w)|t(\\w)t(\\w)t(\\w)t(\\w)[\\r\\n]|"+
                "m(\\w)m(\\w)m(\\w)m(\\w)|m(\\w)m(\\w)m(\\w)m(\\w)[\\r\\n]|"+
                "z(\\w)z(\\w)z(\\w)z(\\w)|z(\\w)z(\\w)z(\\w)z(\\w)[\\r\\n]\\b.*");

        for(String line: lines){
            if (line.matches(regex.pattern())) {
                bl = true;
                break;
            }
        }
        return bl;
    }

    private static boolean aranyharmasE(List<String> lines){
        boolean bl = false;

        Pattern regex = Pattern.compile(".*\\b(\\w{3})A(\\w)A(\\w)A|(\\w{3})A(\\w)A(\\w)A[\\r\\n]+|"+
                "(\\w)A(\\w{3})A(\\w)A|(\\w)A(\\w{3})A(\\w)A[\\r\\n]+|"+
                "(\\w)A(\\w)A(\\w{3})A|(\\w)A(\\w)A(\\w{3})A[\\r\\n]+|"+
                "(\\w)A(\\w)A(\\w)A(\\w{2})|(\\w)A(\\w)A(\\w)A(\\w{2})[\\r\\n]+|"+
                "(\\w{3})K(\\w)K(\\w)K|(\\w{3})K(\\w)K(\\w)K[\\r\\n]+|"+
                "(\\w)K(\\w{3})K(\\w)K|(\\w)K(\\w{3})K(\\w)K[\\r\\n]+|"+
                "(\\w)K(\\w)K(\\w{3})K|(\\w)K(\\w)K(\\w{3})K[\\r\\n]+|"+
                "(\\w)K(\\w)K(\\w)K(\\w{2})|(\\w)K(\\w)K(\\w)K(\\w{2})[\\r\\n]+|"+
                "(\\w{3})F(\\w)F(\\w)F|(\\w{3})F(\\w)F(\\w)F[\\r\\n]+|"+
                "(\\w)F(\\w{3})F(\\w)F|(\\w)F(\\w{3})F(\\w)F[\\r\\n]+|"+
                "(\\w)F(\\w)F(\\w{3})F|(\\w)F(\\w)F(\\w{3})F[\\r\\n]+|"+
                "(\\w)F(\\w)F(\\w)F(\\w{2})|(\\w)F(\\w)F(\\w)F(\\w{2})[\\r\\n]+|"+
                "(\\w{3})a(\\w)a(\\w)a|(\\w{3})a(\\w)a(\\w)a[\\r\\n]+|"+
                "(\\w)a(\\w{3})a(\\w)a|(\\w)a(\\w{3})a(\\w)a[\\r\\n]+|"+
                "(\\w)a(\\w)a(\\w{3})a|(\\w)a(\\w)a(\\w{3})a[\\r\\n]+|"+
                "(\\w)a(\\w)a(\\w)a(\\w{2})|(\\w)a(\\w)a(\\w)a(\\w{2})[\\r\\n]+|"+
                "(\\w{3})T(\\w)T(\\w)T|(\\w{3})T(\\w)T(\\w)T[\\r\\n]+|"+
                "(\\w)T(\\w{3})T(\\w)T|(\\w)T(\\w{3})T(\\w)T[\\r\\n]+|"+
                "(\\w)T(\\w)T(\\w{3})T|(\\w)T(\\w)T(\\w{3})T[\\r\\n]+|"+
                "(\\w)T(\\w)T(\\w)T(\\w{2})|(\\w)T(\\w)T(\\w)T(\\w{2})[\\r\\n]+|"+
                "(\\w{3})9(\\w)9(\\w)9|(\\w{3})9(\\w)9(\\w)9[\\r\\n]+|"+
                "(\\w)9(\\w{3})9(\\w)9|(\\w)9(\\w{3})9(\\w)9[\\r\\n]+|"+
                "(\\w)9(\\w)9(\\w{3})9|(\\w)9(\\w)9(\\w{3})9[\\r\\n]+|"+
                "(\\w)9(\\w)9(\\w)9(\\w{2})|(\\w)9(\\w)9(\\w)9(\\w{2})[\\r\\n]+|"+
                "(\\w{3})8(\\w)8(\\w)8|(\\w{3})8(\\w)8(\\w)8[\\r\\n]+|"+
                "(\\w)8(\\w{3})8(\\w)8|(\\w)8(\\w{3})8(\\w)8[\\r\\n]+|"+
                "(\\w)8(\\w)8(\\w{3})8|(\\w)8(\\w)8(\\w{3})8[\\r\\n]+|"+
                "(\\w)8(\\w)8(\\w)8(\\w{2})|(\\w)8(\\w)8(\\w)8(\\w{2})[\\r\\n]+|"+
                "(\\w{3})7(\\w)7(\\w)7|(\\w{3})7(\\w)7(\\w)7[\\r\\n]+|"+
                "(\\w)7(\\w{3})7(\\w)7|(\\w)7(\\w{3})7(\\w)7[\\r\\n]+|"+
                "(\\w)7(\\w)7(\\w{3})7|(\\w)7(\\w)7(\\w{3})7[\\r\\n]+|"+
                "(\\w)7(\\w)7(\\w)7(\\w{2})|(\\w)7(\\w)7(\\w)7(\\w{2})[\\r\\n]+\\b.*");

        for(String line: lines){
            if (line.matches(regex.pattern())) {
                bl = true;
                break;
            }
        }
        return bl;
    }

    private static boolean haromszorosE(List<String> lines){
        boolean bl = false;

        Pattern regex = Pattern.compile(".*\\b(\\w{2})p(\\w)p(\\w)p(\\w)|(\\w{2})p(\\w)p(\\w)p(\\w)[\\r\\n]+|" +
                "(\\w{2})t(\\w)t(\\w)t(\\w)|(\\w{2})t(\\w)t(\\w)t(\\w)[\\r\\n]+|" +
                "(\\w{2})m(\\w)m(\\w)m(\\w)|(\\w{2})m(\\w)m(\\w)m(\\w)[\\r\\n]+|" +
                "(\\w{2})z(\\w)z(\\w)z(\\w)|(\\w{2})z(\\w)z(\\w)z(\\w)[\\r\\n]+|" +
                "p(\\w)p(\\w)p(\\w{3})|p(\\w)p(\\w)p(\\w{3})[\\r\\n]+|" +
                "t(\\w)t(\\w)t(\\w{3})|t(\\w)t(\\w)t(\\w{3})[\\r\\n]+|" +
                "m(\\w)m(\\w)m(\\w{3})|m(\\w)m(\\w)m(\\w{3})[\\r\\n]+|" +
                "z(\\w)z(\\w)z(\\w{3})|z(\\w)z(\\w)z(\\w{3})[\\r\\n]+\\b.*");

        for(String line: lines){
            if (line.matches(regex.pattern())) {
                bl = true;
                break;
            }
        }
        return bl;
    }

    private static boolean ketaszE(List<String> lines){
        boolean bl = false;

        Pattern regex = Pattern.compile(".*\\b((\\w)A){2}(\\w{4})|((\\w)A){2}(\\w{4})[\\r\\n]+|"+
                "(\\w{4})((\\w)A){2}|(\\w{4})((\\w)A){2}[\\r\\n]+|"+
                "(\\w{2})((\\w)A){2}(\\w{2})|(\\w{2})((\\w)A){2}(\\w{2})[\\r\\n]+|"+
                "(\\w)A(\\w{4})(\\w)A|(\\w)A(\\w{4})(\\w)A[\\r\\n]+|"+
                "(\\w)A(\\w{2})(\\w)A(\\w{2})|(\\w)A(\\w{2})(\\w)A(\\w{2})[\\r\\n]+|"+
                "(\\w{2})(\\w)A(\\w{2})(\\w)A|(\\w{2})(\\w)A(\\w{2})(\\w)A[\\r\\n]+\\b.*");

        for(String line: lines){
            if (line.matches(regex.pattern())) {
                bl = true;
                break;
            }
        }
        return bl;
    }

    private static boolean ferbliE(List<String> lines){
        boolean bl = false;

        Pattern regex = Pattern.compile(".*\\bp(\\w)p(\\w{5})|p(\\w)p(\\w{5})[\\r\\n]+|"+
                "t(\\w)t(\\w{5})|t(\\w)t(\\w{5})[\\r\\n]+|"+
                "m(\\w)m(\\w{5})|m(\\w)m(\\w{5})[\\r\\n]+|"+
                "z(\\w)z(\\w{5})|z(\\w)z(\\w{5})[\\r\\n]+\\b.*");

        for(String line: lines){
            if (line.matches(regex.pattern())) {
                bl = true;
                break;
            }
        }
        return bl;
    }

    private static boolean szingliE(List<String> lines){
        boolean bl = false;

        Pattern regex = Pattern.compile(".*\\bp(\\w)t(\\w)m(\\w)z(\\w)|p(\\w)t(\\w)m(\\w)z(\\w)[\\r\\n]+|" +
                "t(\\w)p(\\w)m(\\w)z(\\w)|t(\\w)p(\\w)m(\\w)z(\\w)[\\r\\n]+\\b.*");

        for(String line: lines){
            if (line.matches(regex.pattern())) {
                bl = true;
                break;
            }
        }
        return bl;
    }

    //</editor-fold>


    public static void main(String[] args){
        long startIdo = System.currentTimeMillis();

        List<String> lapok = Olvassdbe.Fajl("src\\ferbli\\lapok.txt");

        //<editor-fold desc="Kiiratasok">
        if(kicsiGirigareE(lapok)){
            Irdki.Fajl("Kicsi girigáré: "+ IGAZ+"\n"+
                    "Nagy girigáré: "+ HAMIS+"\n"+
                    "Vannak: "+ HAMIS+"\n"+
                    "Banda: "+ HAMIS+"\n"+
                    "Aranyhármas: "+ HAMIS+"\n"+
                    "Haromszoros: "+ HAMIS+"\n"+
                    "Két ász: "+ HAMIS+"\n"+
                    "Ferbli: "+ HAMIS+"\n"+
                    "Szingli: "+ HAMIS);;

            System.out.println("Kicsi girigáré: "+ IGAZ);
            System.out.println("Nagy girigáré: "+HAMIS);
            System.out.println("Vannak: "+HAMIS);
            System.out.println("Banda: "+HAMIS);
            System.out.println("Aranyhármas: "+ HAMIS);
            System.out.println("Háromszoros: "+ HAMIS);
            System.out.println("Két ász: "+ HAMIS);
            System.out.println("Ferbli: "+ HAMIS);
            System.out.println("Szingli: "+ HAMIS);
        }
        else if(nagyGirigareE(lapok)){
            Irdki.Fajl("Kicsi girigáré: "+ HAMIS+"\n"+
                    "Nagy girigáré: "+ IGAZ+"\n"+
                    "Vannak: "+ HAMIS+"\n"+
                    "Banda: "+ HAMIS+"\n"+
                    "Aranyhármas: "+ HAMIS+"\n"+
                    "Haromszoros: "+ HAMIS+"\n"+
                    "Két ász: "+ HAMIS+"\n"+
                    "Ferbli: "+ HAMIS+"\n"+
                    "Szingli: "+ HAMIS);;

            System.out.println("Kicsi girigáré: "+HAMIS);
            System.out.println("Nagy girigáré: "+IGAZ);
            System.out.println("Vannak: "+HAMIS);
            System.out.println("Banda: "+HAMIS);
            System.out.println("Aranyhármas: "+ HAMIS);
            System.out.println("Háromszoros: "+ HAMIS);
            System.out.println("Két ász: "+ HAMIS);
            System.out.println("Ferbli: "+ HAMIS);
            System.out.println("Szingli: "+ HAMIS);
        }
        else if(vannakE(lapok)){
            Irdki.Fajl("Kicsi girigáré: "+ HAMIS+"\n"+
                    "Nagy girigáré: "+ HAMIS+"\n"+
                    "Vannak: "+ IGAZ+"\n"+
                    "Banda: "+ HAMIS+"\n"+
                    "Aranyhármas: "+ HAMIS+"\n"+
                    "Haromszoros: "+ HAMIS+"\n"+
                    "Két ász: "+ HAMIS+"\n"+
                    "Ferbli: "+ HAMIS+"\n"+
                    "Szingli: "+ HAMIS);;

            System.out.println("Kicsi girigáré: "+HAMIS);
            System.out.println("Nagy girigáré: "+HAMIS);
            System.out.println("Vannak: "+IGAZ);
            System.out.println("Banda: "+HAMIS);
            System.out.println("Aranyhármas: "+ HAMIS);
            System.out.println("Háromszoros: "+ HAMIS);
            System.out.println("Két ász: "+ HAMIS);
            System.out.println("Ferbli: "+ HAMIS);
            System.out.println("Szingli: "+ HAMIS);
        }
        else if(bandaE(lapok)){
            Irdki.Fajl("Kicsi girigáré: "+ HAMIS+"\n"+
                    "Nagy girigáré: "+ HAMIS+"\n"+
                    "Vannak: "+ HAMIS+"\n"+
                    "Banda: "+ IGAZ+"\n"+
                    "Aranyhármas: "+ HAMIS+"\n"+
                    "Haromszoros: "+ HAMIS+"\n"+
                    "Két ász: "+ HAMIS+"\n"+
                    "Ferbli: "+ HAMIS+"\n"+
                    "Szingli: "+ HAMIS);;

            System.out.println("Kicsi girigáré: "+HAMIS);
            System.out.println("Nagy girigáré: "+HAMIS);
            System.out.println("Vannak: "+HAMIS);
            System.out.println("Banda: "+IGAZ);
            System.out.println("Aranyhármas: "+ HAMIS);
            System.out.println("Háromszoros: "+ HAMIS);
            System.out.println("Két ász: "+ HAMIS);
            System.out.println("Ferbli: "+ HAMIS);
            System.out.println("Szingli: "+ HAMIS);
        }
        else if(aranyharmasE(lapok)){
            Irdki.Fajl("Kicsi girigáré: "+ HAMIS+"\n"+
                    "Nagy girigáré: "+ HAMIS+"\n"+
                    "Vannak: "+ HAMIS+"\n"+
                    "Banda: "+ HAMIS+"\n"+
                    "Aranyhármas: "+ IGAZ+"\n"+
                    "Haromszoros: "+ HAMIS+"\n"+
                    "Két ász: "+ HAMIS+"\n"+
                    "Ferbli: "+ HAMIS+"\n"+
                    "Szingli: "+ HAMIS);;

            System.out.println("Kicsi girigáré: "+HAMIS);
            System.out.println("Nagy girigáré: "+HAMIS);
            System.out.println("Vannak: "+HAMIS);
            System.out.println("Banda: "+HAMIS);
            System.out.println("Aranyhármas: "+ IGAZ);
            System.out.println("Háromszoros: "+ HAMIS);
            System.out.println("Két ász: "+ HAMIS);
            System.out.println("Ferbli: "+ HAMIS);
            System.out.println("Szingli: "+ HAMIS);
        }
        else if(haromszorosE(lapok)){
            Irdki.Fajl("Kicsi girigáré: "+ HAMIS+"\n"+
                    "Nagy girigáré: "+ HAMIS+"\n"+
                    "Vannak: "+ HAMIS+"\n"+
                    "Banda: "+ HAMIS+"\n"+
                    "Aranyhármas: "+ HAMIS+"\n"+
                    "Haromszoros: "+ IGAZ+"\n"+
                    "Két ász: "+ HAMIS+"\n"+
                    "Ferbli: "+ HAMIS+"\n"+
                    "Szingli: "+ HAMIS);;

            System.out.println("Kicsi girigáré: "+HAMIS);
            System.out.println("Nagy girigáré: "+HAMIS);
            System.out.println("Vannak: "+HAMIS);
            System.out.println("Banda: "+HAMIS);
            System.out.println("Aranyhármas: "+ HAMIS);
            System.out.println("Háromszoros: "+ IGAZ);
            System.out.println("Két ász: "+ HAMIS);
            System.out.println("Ferbli: "+ HAMIS);
            System.out.println("Szingli: "+ HAMIS);
        }
        else if(ketaszE(lapok)){
            Irdki.Fajl("Kicsi girigáré: "+ HAMIS+"\n"+
                    "Nagy girigáré: "+ HAMIS+"\n"+
                    "Vannak: "+ HAMIS+"\n"+
                    "Banda: "+ HAMIS+"\n"+
                    "Aranyhármas: "+ HAMIS+"\n"+
                    "Haromszoros: "+ HAMIS+"\n"+
                    "Két ász: "+ IGAZ+"\n"+
                    "Ferbli: "+ HAMIS+"\n"+
                    "Szingli: "+ HAMIS);;

            System.out.println("Kicsi girigáré: "+HAMIS);
            System.out.println("Nagy girigáré: "+HAMIS);
            System.out.println("Vannak: "+HAMIS);
            System.out.println("Banda: "+HAMIS);
            System.out.println("Aranyhármas: "+ HAMIS);
            System.out.println("Háromszoros: "+ HAMIS);
            System.out.println("Két ász: "+ IGAZ);
            System.out.println("Ferbli: "+ HAMIS);
            System.out.println("Szingli: "+ HAMIS);
        }
        else if(ferbliE(lapok)){
            Irdki.Fajl("Kicsi girigáré: "+ HAMIS+"\n"+
                    "Nagy girigáré: "+ HAMIS+"\n"+
                    "Vannak: "+ HAMIS+"\n"+
                    "Banda: "+ HAMIS+"\n"+
                    "Aranyhármas: "+ HAMIS+"\n"+
                    "Haromszoros: "+ HAMIS+"\n"+
                    "Két ász: "+ HAMIS+"\n"+
                    "Ferbli: "+ IGAZ+"\n"+
                    "Szingli: "+ HAMIS);;

            System.out.println("Kicsi girigáré: "+HAMIS);
            System.out.println("Nagy girigáré: "+HAMIS);
            System.out.println("Vannak: "+HAMIS);
            System.out.println("Banda: "+HAMIS);
            System.out.println("Aranyhármas: "+ HAMIS);
            System.out.println("Háromszoros: "+ HAMIS);
            System.out.println("Két ász: "+ HAMIS);
            System.out.println("Ferbli: "+ IGAZ);
            System.out.println("Szingli: "+ HAMIS);
        }
        else if(szingliE(lapok)){
            Irdki.Fajl("Kicsi girigáré: "+ HAMIS+"\n"+
                    "Nagy girigáré: "+ HAMIS+"\n"+
                    "Vannak: "+ HAMIS+"\n"+
                    "Banda: "+ HAMIS+"\n"+
                    "Aranyhármas: "+ HAMIS+"\n"+
                    "Haromszoros: "+ HAMIS+"\n"+
                    "Két ász: "+ HAMIS+"\n"+
                    "Ferbli: "+ HAMIS+"\n"+
                    "Szingli: "+ IGAZ);;

            System.out.println("Kicsi girigáré: "+HAMIS);
            System.out.println("Nagy girigáré: "+HAMIS);
            System.out.println("Vannak: "+HAMIS);
            System.out.println("Banda: "+HAMIS);
            System.out.println("Aranyhármas: "+ HAMIS);
            System.out.println("Háromszoros: "+ HAMIS);
            System.out.println("Két ász: "+ HAMIS);
            System.out.println("Ferbli: "+ HAMIS);
            System.out.println("Szingli: "+ IGAZ);
        }
        else{
            Irdki.Fajl("Kicsi girigáré: "+ HAMIS+"\n"+
                    "Nagy girigáré: "+ HAMIS+"\n"+
                    "Vannak: "+ HAMIS+"\n"+
                    "Banda: "+ HAMIS+"\n"+
                    "Aranyhármas: "+ HAMIS+"\n"+
                    "Haromszoros: "+ HAMIS+"\n"+
                    "Két ász: "+ HAMIS+"\n"+
                    "Ferbli: "+ HAMIS+"\n"+
                    "Szingli: "+ HAMIS);;

            System.out.println("Kicsi girigáré: "+HAMIS);
            System.out.println("Nagy girigáré: "+HAMIS);
            System.out.println("Vannak: "+HAMIS);
            System.out.println("Banda: "+HAMIS);
            System.out.println("Aranyhármas: "+ HAMIS);
            System.out.println("Háromszoros: "+ HAMIS);
            System.out.println("Két ász: "+ HAMIS);
            System.out.println("Ferbli: "+ HAMIS);
            System.out.println("Szingli: "+ HAMIS);
        }
        //</editor-fold>

        long becsultIdo = System.currentTimeMillis() - startIdo;
        System.out.print("\nBecsült futási idő: "+becsultIdo+"ms");
    }
}
