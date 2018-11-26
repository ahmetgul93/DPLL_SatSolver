package myPackage;

public class Literal implements Cloneable {
    private Integer _literalName;
    private Boolean _negetion;

    public Literal(Integer literalName, Boolean negetion)
    {
        _literalName = literalName;
        _negetion = negetion;
    }

    public void setNegation(Boolean newValue) {
        _negetion = newValue;
    }

    public Boolean IsNegation () {
        return _negetion;
    }

    public Integer GetLiteralName() {
        return _literalName;
    }
}
