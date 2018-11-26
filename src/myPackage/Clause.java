package myPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Clause implements Cloneable{
   private List<Literal> _coreClause = new ArrayList<Literal>();
   boolean result = false;

    public  Clause () {};

    public Clause (List<Literal> _literalList)
    {
        _coreClause = _literalList;
    }


    public List<Literal> getCoreClause() {
        return _coreClause;
    }

    public int getNumberOfLiteral(){
        return _coreClause.size();
    }

    protected void AddLiteral(Literal literal) {
        _coreClause.add(literal);
    }

    protected void removeLiteral(Literal lit) {
        _coreClause.removeIf((Literal l) -> l.GetLiteralName() == lit.GetLiteralName() && l.IsNegation() == lit.IsNegation());
    }

    protected boolean isContainLiterals() {
        if(_coreClause.size() == 0){
            return false;
        }
        else {
            return true;
        }
    }

    protected boolean isContainLiteral(Literal lit) {
        Boolean result = false;
        for(Literal l : _coreClause) {
            if (l.GetLiteralName() == lit.GetLiteralName())
            {
                if (l.IsNegation() == lit.IsNegation())
                {
                    return true;
                }
            }
        }
        return false;
    }


}
