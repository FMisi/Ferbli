/* Ferbli
 * 2022.10.26. CC0LRB
 * Szerintem ez: pontos
 * A kovetkezo szempontok miatt:
 *
 */
package ferbli;

import java.util.*;
import java.util.regex.Pattern;

/*
- ferbli handeket elemzünk részlegesen
- a file első sorában jön 2 lap a standard elkódolásban egy stringben,
pl "pTt9" jelenti, hogy az asztalon a piros X és tök IX van lent
- a többi sorában mindben 2-2 lap ugyanígy, pl. "patA" jelenti, hogy ebben a kézben a piros alsó és a tök ász van
- a program pedig kiírja, hogy hány főnél van Girigáré, Négyes (Vannak-nak is szokták hívni),
Banda, Aranyhármas, Háromszoros, Két ász, Ferbli, és Szingli,
és hogy kinél ezek közül mi (mindenkinek csak a legerősebbet mondva ezek közül).



Girigáré
Kicsi (ez az erősebb): piros X, tök IX, zöld VIII, makk VII., pl.: ("pTt9z8m7")
Nagy: piros ász, tök király, zöld felső, makk alsó, (ász: "A", alsó: "a"), pl.: ("pAtKzFma")

Vannak
négy ász; négy király; négy felső; négy alsó; négy X-es; négy IX-es; négy VIII-es; négy VII-es

Banda
Négy lap egy színből, erősségi sorrendjüket a lapok pontértékének összege dönti el (41-34).
Azonos pontösszegű és különböző színű bandák esetén a szín dönt.

Aranyhármas
három ász; három király; három felső; három alsó; három X-es; három IX-es; három VIII-es; három VII-es

Háromszoros
Három lap egy színből, erősségi sorrendjüket a lapok pontértékének összege dönti el (31-24).
Azonos pontösszegű, de különböző színű háromszorosok esetén a szín dönt.
Azonos pontösszegű és azonos színű "háromszorosok" esetén a legrangosabb lap határozza meg az erősorrendet.

Két ász
Két ász (más pár nem értékes kombináció).
Két darab "két ász" közül az a rangosabb, amelyikben a piros ász van.

Ferbli
Két lap egy színből, erősségi sorrendjüket a lapok pontértékének összege dönti el (21-15).
Azonos pontösszegű, de különböző színű ferblik esetén a szín dönt.
Azonos pontösszegű és azonos színű ferblik esetén a legrangosabb lap határozza meg az erősorrendet.

Szingli
Négy különböző színű lap egy kézben (nem girigáré),
csak a legmagasabb pontértékű lap vesz részt az összehasonlításban.
A szinglik erősségi sorrendjét a pontérték dönti el (11-7).
Azonos pontérték, de különböző színű szingli esetén a szín dönt.
Azonos pontértékű és azonos színű szingli esetén a lap rangja határozza meg az erősorrendet.
 */

public class Main {

    //<editor-fold desc="Checkerek">
    private static boolean kicsiGirigareE(List<String> lines){
        boolean bl = false;
        Pattern regex = Pattern.compile(".*\\bpT+(t9z8m7|t9m7z8|z8t9m7|z8m7t9|m7z8t9|m7t9z8)|" +
                "t9+(pTz8m7|pTm7z8|m7pTz8|m7z8pT|z8pTm7|z8m7pT)|" +
                "z8+(pTt9m7|pTm7t9|m7pTt9|m7t9pT|t9pTm7|t9m7pT)|" +
                "m7+(pTt9z8|pTz8t9|z8pTt9|z8t9pT|t9pTz8|t9z8pT)\\b.*");

        //trash(ebb) megvalositas:
        Pattern regex2 = Pattern.compile(".*\\bpTt9z8m7|pTt9m7z8|pTz8t9m7|pTz8m7t9|pTm7z8t9|pTm7t9z8|" +
                "t9pTz8m7|pTm7z8|m7pTz8|m7z8pT|z8pTm7|z8m7pT|" +
                "z8pTt9m7|z8pTm7t9|z8m7pTt9|z8m7t9pT|z8t9pTm7|z8t9m7pT|" +
                "m7pTt9z8|m7pTz8t9|m7z8pTt9|m7z8t9pT|m7t9pTz8|m7t9z8pT\\b.*");

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
        Pattern regex = Pattern.compile(".*\\bpA+(tKzFma|tKmazF|zFtKma|zFmatK|mazFtK|matKzF)|"+
                "tK+(pAzFma|pAmazF|zFpAma|zFmapA|mazFpA|mapAzF)|"+
                "zF+(pAtKma|pAmatK|tKpAma|tKmapA|matKpA|mapAtK)|"+
                "ma+(pAtKzF|pAzFtK|tKpAzF|tKzFpA|zFtKpA|zFpAtK)\\b.*");

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
                "(pKpK){2}|(tKtK){2}|pK(pKtKtK)|(pKtK){2}|(tKpK){2}|tK+(tKpKpK)|" +
                "(zKzK){2}|pK(pKzKzK)|(pKzK){2}|(zKpK){2}|zK(zKpKpK)|" +
                "(mKmK){2}|pK+(pKmKmK)|(pKmK){2}|(mKpK){2}|mK(mKpKpK)|" +
                "tK(tKzKzK)|(tKzK){2}|(mKzK){2}|zK(zKtKtK)|" +
                "tK(tKmKmK)|(tKmK){2}|(mKtK){2}|mK(mKtKtK)|" +
                "mK(mKzKzK)|" +
                "zK(zKmKmK)|" +
                "(zKtK){2}|" +
                "(zKpKtKmK)|"+
                "(pFpF){2}|(tFtF){2}|pF(pFtFtF)|(pFtF){2}|(tFpF){2}|tF+(tFpFpF)|" +
                "(zFzF){2}|pF(pFzFzF)|(pFzF){2}|(zFpF){2}|zF(zFpFpF)|" +
                "(mFmF){2}|pF+(pFmFmF)|(pFmF){2}|(mFpF){2}|mF(mFpFpF)|" +
                "tF(tFzFzF)|(tFzF){2}|(mFzF){2}|zF(zFtFtF)|" +
                "tF(tFmFmF)|(tFmF){2}|(mFtF){2}|mF(mFtFtF)|" +
                "mF(mFzFzF)|"+
                "zF(zFmFmF)|"+
                "(zFtF){2}|"+
                "(zFpFtFmF)|"+
                "(papa){2}|(tata){2}|pa(patata)|(pata){2}|(tapa){2}|ta+(tapapa)|" +
                "(zaza){2}|pa(pazaza)|(paza){2}|(zapa){2}|za(zapapa)|" +
                "(mama){2}|pa+(pamama)|(pama){2}|(mapa){2}|ma(mapapa)|" +
                "ta(tazaza)|(taza){2}|(maza){2}|za(zatata)|" +
                "ta(tamama)|(tama){2}|(mata){2}|ma(matata)|" +
                "ma(mazaza)|"+
                "za(zamama)|"+
                "(zata){2}|"+
                "(zapatama)|" +
                "(pTpT){2}|(tTtT){2}|pT(pTtTtT)|(pTtT){2}|(tTpT){2}|tT+(tTpTpT)|" +
                "(zTzT){2}|pT(pTzTzT)|(pTzT){2}|(zTpT){2}|zT(zTpTpT)|" +
                "(mTmT){2}|pT+(pTmTmT)|(pTmT){2}|(mTpT){2}|mT(mTpTpT)|" +
                "tT(tTzTzT)|(tTzT){2}|(mTzT){2}|zT(zTtTtT)|" +
                "tT(tTmTmT)|(tTmT){2}|(mTtT){2}|mT(mTtTtT)|" +
                "mT(mTzTzT)|"+
                "zT(zTmTmT)|"+
                "(zTtT){2}|"+
                "(zTpTtTmT)\\b.*");

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

        Irdki.Fajl("Kicsi girigáré: "+ kicsiGirigareE(lapok)+"\n"+
                        "Nagy girigáré: "+ nagyGirigareE(lapok)+"\n"+
                        "Vannak: "+ vannakE(lapok));

        System.out.println("Kicsi girigáré: "+kicsiGirigareE(lapok));
        System.out.println("Nagy girigáré: "+nagyGirigareE(lapok));
        System.out.println("Vannak: "+vannakE(lapok));

        long becsultIdo = System.currentTimeMillis() - startIdo;
        System.out.print("\nBecsült futási idő: "+becsultIdo+"ms");
    }
}
