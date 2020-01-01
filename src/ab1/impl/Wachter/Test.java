package ab1.impl.Wachter;

import ab1.Movement;
import ab1.TM;

import java.util.Arrays;
import java.util.HashSet;

public class Test {

   public static void main(String[] args){
       Ab1Impl sol = new Ab1Impl();
       TM machine = sol.getTMImpl();

       machine.reset();

       machine.setSymbols(new HashSet<Character>(Arrays.asList('a', '#')));
       machine.addTransition(3, '#', 1, '#', Movement.LEFT);
       machine.addTransition(1, 'a', 2, '#', Movement.STAY);
       machine.addTransition(2, '#', 1, '#', Movement.LEFT);
       machine.addTransition(1, '#', 0, '#', Movement.STAY);
       machine.setInitialState(3);
       machine.setInitialTapeContent("#aaaaa".toCharArray());

       while (!machine.isHalt())
           machine.doNextStep();

       machine.reset();
       machine.setSymbols(new HashSet<Character>(Arrays.asList('a', 'b', 'c', '#')));
       machine.setInitialState(1);
       try {
           machine.doNextStep();
       } catch (IllegalStateException ex) {
       }
   }


}
