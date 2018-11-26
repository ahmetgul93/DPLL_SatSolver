package myPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SAT {
    private int _numberOfVar = 0;
    private int _numberOfClauses = 0;
    private List<Clause> _listOfClauses = new ArrayList<Clause>(_numberOfClauses);
    private HashMap<Integer, Boolean> _literalsSituation = new HashMap<Integer, Boolean>();
    private String file = "";

    public SAT (int i) {
        file = "src/test/problem"+i+".cnf";
        initializeSAT();
    }

    public HashMap<Integer, Boolean> GetSituation()
    {
        return _literalsSituation;
    }

    public List<Clause> GetClauses()
    {
        return _listOfClauses;
    }

    private void initializeSAT() {
        ReadFromFile();
    }

    private void ReadFromFile() {
        List<String> list = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(file))) {
            // c ile başlamayan değerleri aldım !
            list = stream.filter(line -> !line.startsWith("c")).collect(Collectors.toList());
            System.out.println(list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // p ile başlayan satırı aldım !
        var initialRow = list.get(0);

        if (initialRow.startsWith("p")) {
            String[] words = initialRow.split(" ");
            int count = 0;
            for (String word : words) {
                if (word != null && !word.isEmpty() && !word.equals("p") && !word.equals("cnf")) {
                    if (count == 1) {
                        _numberOfClauses = Integer.parseInt(word);
                    } else if (count == 0) {
                        _numberOfVar = Integer.parseInt(word);
                        count++;
                    }
                }
            }

            var variableList = list.subList(1, list.size());
            for (String row : variableList) {
                Clause c = new Clause();
                String[] rowWords = row.split(" ");
                for (String s : rowWords) {
                    if (s != null && !s.isEmpty() && !s.equals("0")) {
                        int name = 0;
                        boolean isNegetion = false;
                        if (s.startsWith("-")) {
                            s = s.substring(1, s.length());
                            isNegetion = true;
                        }
                        name = Integer.parseInt(s);
                        Literal lit = new Literal(name, isNegetion);
                        c.AddLiteral(lit);
                    }
                }
                _listOfClauses.add(c);
            }
        } else {
            // Hatalı satır okundu demek
        }
    }

    /**
     * Check clause list if it contains only empty clauses
     */
    private boolean isOnlyEmptyClauses(List<Clause> ListOfClauses) {
        try {
            for (Clause clause : ListOfClauses) {
                if (clause.isContainLiterals()) {
                    return false;
                }
            }
            return true;
        }
        catch (Exception ex)
        {
            System.out.println("My Exception: -> " + ex);
            return false;
        }
    }

    /**
     *
     * @param lit
     */
    protected boolean isContainLiteral(List<Clause> ListOfClauses, Literal lit) {
        Boolean result = false;
        for(Clause clause: ListOfClauses) {
            if (clause.isContainLiteral(lit))
            {
                return true;
            }
        }
        return false;
    }

    /**
     *
     */
    public Result Solve(List<Clause> ListOfClauses, HashMap<Integer, Boolean> LiteralsSituation )
    {
        if(ListOfClauses.size() == 0) {
            return new Result(LiteralsSituation);
        }

        if(isOnlyEmptyClauses(ListOfClauses)) {
            return null;
        }

        int myCase = 0;
        Literal lit = null;
        for(Clause clause : ListOfClauses) {
            if(clause.getNumberOfLiteral() == 1) {
                lit = clause.getCoreClause().get(0);
                if(lit.IsNegation()) {
                    myCase = 2;
                    // Burada yeni bir obje oluşturdum referans kaynaklı bir sıkıntı olabilir diye. Set ile mi true set edilecek düşün !
                    lit = new Literal(lit.GetLiteralName(),false);
                    //_literalsSituation.put(lit.GetLiteralName(),false);
                } else {
                    myCase = 1;
                }
                break;
            }
        }

        if(myCase == 0) {
            for(Clause clause : ListOfClauses) {
                for(Literal literal : clause.getCoreClause()) {
                    Literal inverseLiteral;
                    if (literal.IsNegation()) {
                        inverseLiteral = new Literal(literal.GetLiteralName(),false);
                    }
                    else {
                        inverseLiteral = new Literal(literal.GetLiteralName(),true);
                    }
                    if(!isContainLiteral(ListOfClauses,inverseLiteral))
                    {
                        if(literal.IsNegation()) {
                            // Burada yeni bir obje oluşturdum referans kaynaklı bir sıkıntı olabilir. Set ile mi true set edilecek düşün !
                            lit = new Literal(literal.GetLiteralName(),false);
                            myCase = 2;
                        }
                        else {
                            //lit = literal;
                            lit = new Literal(literal.GetLiteralName(),literal.IsNegation());
                            myCase = 1;
                        }
                        break;
                    }
                }
            }
        }

        if(lit == null) { // null check !
            //lit = Utility.getLiteralList(ListOfClauses).get(new Random().nextInt(ListOfClauses.size()));
            lit = Utility.getLiteralList(ListOfClauses).get(0);
            //  Burayı bir düşün ! Random mu seçsem acaba ?
        }

        if(myCase != 2) {
            List<Clause> newListOfClauses =  Utility.copy(ListOfClauses);
            HashMap<Integer, Boolean> newSituationList = (HashMap<Integer, Boolean>) LiteralsSituation.clone();

            newSituationList.put(lit.GetLiteralName(), true);
            newListOfClauses = Utility.handleClauses(newListOfClauses, lit,true);

            Result tempResult = Solve(newListOfClauses, newSituationList);

            if (tempResult != null) {
                return tempResult;
            }
        }

        if(myCase != 1) {
            List<Clause> newListOfClausesOther =  Utility.copy(ListOfClauses);
            HashMap<Integer, Boolean> newSituationListOther = (HashMap<Integer, Boolean>) LiteralsSituation.clone();

            newSituationListOther.put(lit.GetLiteralName(),false);
            newListOfClausesOther = Utility.handleClauses(newListOfClausesOther, lit,false);

            Result tempResultOther = Solve(newListOfClausesOther, newSituationListOther);
            if (tempResultOther != null) {
                return tempResultOther;
            }
        }

        return null;
    }
}