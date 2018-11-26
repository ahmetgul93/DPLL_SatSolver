package myPackage;

import java.util.HashMap;

public class Result {

    private HashMap<Integer, Boolean> situationTable;

    public Result(HashMap<Integer, Boolean> hm) {
        situationTable = hm;
    }

    public HashMap<Integer, Boolean> getResult() {
        return situationTable;
    }

}
