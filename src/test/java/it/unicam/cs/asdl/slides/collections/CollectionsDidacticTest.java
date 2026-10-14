package it.unicam.cs.asdl.slides.collections;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;
/** Test didattici per generics, List, Set, ordinamento e iteratori. */
public class CollectionsDidacticTest {
    private SavingsAccount account(String iban, String nome, double saldo) {
        return new SavingsAccount(saldo, nome, iban);
    }
    @Test void genericsMantengonoIlTipo() {
        // Il compilatore sa che get restituisce SavingsAccount: nessun cast.
        SavingsAccount a=account("IT01","Anna",10);
        GenericBox<SavingsAccount> box=new GenericBox<SavingsAccount>(a);
        assertSame(a,box.get());
        assertEquals("IT01",new AccountBox<SavingsAccount>(a).getIban());
        GenericPair<String,Integer> pair=new GenericPair<String,Integer>("conto",2);
        assertEquals("conto",pair.getKey());
        assertEquals(Integer.valueOf(2),pair.getValue());
    }
    @Test void metodiGenericiEWildcard() {
        // Una List<SavingsAccount> è leggibile come List<? extends BankAccount>.
        List<SavingsAccount> a=Arrays.asList(account("A","A",10),account("B","B",20));
        assertEquals(30,GenericExamples.totalBalance(a),0.00001);
        assertSame(a.get(0),GenericExamples.first(a.toArray(new SavingsAccount[0])));
        List<BankAccount> b=new ArrayList<BankAccount>();
        GenericExamples.appendAccount(b,a.get(0));
        assertEquals(1,b.size());
    }
    @Test void uguaglianzaLogicaInListESet() {
        // Due conti diversi nello heap con lo stesso IBAN sono uguali.
        BankAccount a=account("IT01","Anna",10), b=account("IT01","Mario",20);
        List<BankAccount> list=new ArrayList<BankAccount>();
        list.add(a);list.add(b);
        assertEquals(2,list.size()); // List ammette ripetizioni
        assertEquals(0,list.indexOf(b));
        Set<BankAccount> set=new HashSet<BankAccount>(list);
        assertEquals(1,set.size()); // HashSet usa equals e hashCode
    }
    @Test void comparatoriEOrdineNaturale() {
        BankAccount a=account("B","Anna",20),b=account("A","Zoe",10);
        List<BankAccount> list=new ArrayList<BankAccount>(Arrays.asList(a,b));
        Collections.sort(list);assertSame(b,list.get(0)); // IBAN
        Collections.sort(list,BankAccount.ORDINA_PER_INTESTATARIO);
        assertSame(a,list.get(0)); // nome
        Collections.sort(list,BankAccount.ORDINA_PER_SALDO);
        assertSame(b,list.get(0)); // saldo
    }
    @Test void iteratoreRimuoveDuranteScansione() {
        List<Integer> list=new ArrayList<Integer>(Arrays.asList(1,2,3,4));
        Iterator<Integer> it=list.iterator();
        while(it.hasNext()) if(it.next()%2==0) it.remove();
        assertEquals(Arrays.asList(1,3),list);
    }
    @Test void arrayListContieneNullEConsenteDuplicati() {
        List<String> list=new ArrayList<String>();
        list.add(null);list.add("A");list.add("A");
        assertEquals(3,list.size());
        assertTrue(list.contains(null));
        assertEquals(1,list.indexOf("A"));
    }
    @Test void treeSetUsaLOrdinamento() {
        SortedSet<BankAccount> set=new TreeSet<BankAccount>();
        set.add(account("B","X",10));set.add(account("A","Y",20));
        assertEquals("A",set.first().getIban());
        assertEquals("B",set.last().getIban());
    }
    @Test void bancaConListRifiutaDuplicati() {
        Bank bank=new Bank("SWIFT","Banca","Via");
        assertTrue(bank.insert(account("A","X",10)));
        assertFalse(bank.insert(account("A","Y",20)));
        assertEquals(0,bank.search(account("A","Z",30)));
        assertTrue(bank.delete(account("A","K",0)));
        assertEquals(-1,bank.search(account("A","Z",30)));
    }
    @Test void bancaRimuoveUsandoIteratore() {
        Bank bank=new Bank("SWIFT","Banca","Via");
        bank.insert(account("A","X",10));bank.insert(account("B","Y",20));
        bank.removeAccountsWithBalanceLessThanOrEqualTo(10);
        assertEquals(-1,bank.search(account("A","X",10)));
        assertEquals(0,bank.search(account("B","Y",20)));
    }
    @Test void bancaConverteContiConSaldoBasso() {
        Bank bank=new Bank("SWIFT","Banca","Via");
        bank.insert(account("A","X",10));bank.insert(account("B","Y",20));
        bank.changeAccountsWithBalanceLessThan(15);
        // Il conto sostitutivo conserva IBAN e saldo, ma cambia sottoclasse.
        assertEquals(1,bank.search(account("A","X",10)));
        assertTrue(bank.getAccountByName("X") instanceof CheckingAccount);
    }
}
