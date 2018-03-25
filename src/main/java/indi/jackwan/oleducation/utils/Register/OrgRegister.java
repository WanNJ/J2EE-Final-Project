package indi.jackwan.oleducation.utils.Register;

public class OrgRegister {
    public static String generateOrgCodeFromId(int integer) throws Exception {
        if(integer > 9999999)
            throw new Exception("Organization Number Overflow");

        StringBuilder raw = new StringBuilder(String.valueOf(integer));
        for (int i =  raw.toString().length(); i < 7; i++) {
            raw.insert(0, "0");
        }
        return raw.toString();
    }
}