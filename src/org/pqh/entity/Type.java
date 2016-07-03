package org.pqh.entity;

/**
 * Created by 10295 on 2016/5/22.
 */
public enum Type {
    L("int"), S("varchar(255)"), I("int"), D("datetime"),
    B("boolean"),F("float"),O("int");
    public final String value;

    Type(String value) {
        this.value = value;
    }

    public static Type getValue(String value){
        String[]types=value.split("\\.");
        char type=types[types.length-1].charAt(0);
        switch (type){
            case 'L':return L;
            case 'S':return S;
            case 'I':return I;
            case 'D':return D;
            case 'B':return B;
            case 'F':return F;
            case 'O':return O;
            default:return S;
            }
        }

}
