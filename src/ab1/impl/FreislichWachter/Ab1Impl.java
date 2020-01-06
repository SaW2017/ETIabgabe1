package ab1.impl.FreislichWachter;

import ab1.Ab1;
import ab1.RegEx;
import ab1.TM;

public class Ab1Impl implements Ab1 {

    @Override
    public TM getTMImpl() {
        return new TMImpl();
    }

    @Override
    public RegEx getRegExImpl() {
        return new RegExImpl();
    }

}
