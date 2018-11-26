package myPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Utility {

    public static List<Clause> copy(List<Clause> list)
    {
        List<Clause> newList = new ArrayList<Clause>();
        for(Clause clause : list) {
            List<Literal> literals = new ArrayList<Literal>();
            for(Literal lit : clause.getCoreClause()) {
                literals.add(new Literal(lit.GetLiteralName(),lit.IsNegation()));
            }
            Clause newClause = new Clause(literals);
            newList.add(newClause);
        }
        return newList;
    }

    /**
     * Removes duplicate clause in list !
     */
    public static void removeDuplicateClauses(List<Clause> _list) {
        List<Clause> reducedList = new ArrayList<>();
        for (int i = 0; i < _list.size(); i++) {
            Clause c = _list.get(i);
            //if(c.isContainLiterals()) {
                if (!reducedList.contains(c)) {
                    reducedList.add(c);
                }
            //}
        }
        _list.clear();
        _list.addAll(reducedList);
    }

    /**
     * Removes all clauses that contain a given literal !
     * @param lit
     */
    public static void removeClausesByLiteral(List<Clause> _list, Literal lit) {
        _list.removeIf((Clause c) -> c.isContainLiteral(lit));
    }

    /**
     * Removes the given literal in clauses
     * @param lit
     */
    public static void removeLiteralFromClauses(List<Clause> _list, Literal lit) {
        for(Clause clause : _list) {
            if(clause.isContainLiteral(lit)) {
                clause.removeLiteral(lit);
            }
        }
    }

    public static List<Clause> handleClauses(List<Clause> _list, Literal lit, Boolean value)
    {
        if(value) {
            removeClausesByLiteral(_list, lit);
            removeLiteralFromClauses(_list, new Literal(lit.GetLiteralName(),true));
        } else {
            removeClausesByLiteral(_list, new Literal(lit.GetLiteralName(),true));
            removeLiteralFromClauses(_list, lit);
        }
        removeDuplicateClauses(_list);

        return _list;
    }

    public static List<Literal> getLiteralList(List<Clause> _list)
    {
        List<Literal> _allLiterals = new ArrayList<Literal>();
        for(Clause clause : _list) {
            for(Literal lit: clause.getCoreClause()) {
                if(lit.IsNegation()) {
                    Literal tempLit = new Literal(lit.GetLiteralName(), false);
                   _allLiterals.add(tempLit);
                }
                else
                {
                    _allLiterals.add(lit);
                }
            }
        }
       _allLiterals = _allLiterals.stream().filter(distinctByKey(b -> b.GetLiteralName())).collect(Collectors.toList());
        return _allLiterals;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
