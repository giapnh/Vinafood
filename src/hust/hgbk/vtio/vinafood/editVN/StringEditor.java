/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hust.hgbk.vtio.vinafood.editVN;

import java.util.HashMap;

/**
 *
 * @author congnh
 */
public class StringEditor {
    
    protected static char[] a_CHARACTER={'a', 'à', 'á', 'ả', 'ã' ,'ạ', 'â','ầ'
            , 'ấ', 'ẩ', 'ẫ', 'ậ', 'ă', 'ằ', 'ắ', 'ẳ', 'ẵ' ,'ặ'};
    protected static char[] A_CHARACTER={'A', 'À', 'Á', 'Ả', 'Ã' ,'Ạ', 'Â','Ầ'
            , 'Ấ', 'Ẩ', 'Ẫ', 'Ậ', 'Ă', 'Ằ', 'Ắ', 'Ẳ', 'Ẵ' ,'Ặ'};
    protected static char[] d_CHARACTER={'d', 'đ'};
    protected static char[] D_CHARACTER={'D', 'Đ'};
    protected static char[] e_CHARACTER={'e', 'è', 'é', 'ẻ', 'ẽ' ,'ẹ', 'ê','ề'
            , 'ế', 'ể', 'ễ', 'ệ'};
    protected static char[] i_CHARACTER={'i', 'ì', 'í', 'ỉ', 'ĩ' ,'ị'};
    protected static char[] I_CHARACTER={'I', 'Ì', 'Í', 'Ỉ', 'Ĩ' ,'Ị'};
    protected static char[] E_CHARACTER={'E', 'È', 'É', 'Ẻ', 'Ẽ' ,'Ẹ', 'Ê','Ề'
            , 'Ế', 'Ể', 'Ễ', 'Ệ'};
    protected static char[] o_CHARACTER={'o', 'ò', 'ó', 'ỏ', 'õ' ,'ọ', 'ô','ồ'
            , 'ố', 'ổ', 'ỗ', 'ộ', 'ơ', 'ờ', 'ớ', 'ở', 'ỡ', 'ợ'};
    protected static char[] O_CHARACTER={'O', 'Ò', 'Ó', 'Ỏ', 'Õ' ,'Ọ', 'Ô','Ồ'
            , 'Ố', 'Ổ', 'Ỗ', 'Ộ', 'Ơ', 'Ờ', 'Ớ', 'Ở', 'Ỡ', 'Ợ'};
    protected static char[] u_CHARACTER={'u','ù','ú','ủ','ũ','ụ','ư', 'ừ', 'ứ'
            , 'ử', 'ữ' ,'ự'};
    protected static char[] U_CHARACTER={'U','Ù','Ú','Ủ','Ũ','Ụ','Ư', 'Ừ', 'Ứ'
            , 'Ử', 'Ữ' ,'Ự'};
    protected static char[] y_CHARACTER={'y', 'ỳ', 'ý', 'ỷ', 'ỹ' ,'ỵ'};
    protected static char[] Y_CHARACTER={'Y', 'Ỳ', 'Ý', 'Ỷ', 'Ỹ' ,'Ỵ'};
    protected static HashMap charHashMap;
    
    
    public static String filterAll(String string){
        return filterLiteral(filterLanguage(string));
    }
    
    public static String filterLiteral(String string){
        String filterString=null;
        if(string!=null&&string.length()!=0){
            int index=string.lastIndexOf("^^http://");
            if(index>=0)
                filterString=string.substring(0, index);
            else
                filterString=string;
        }else{
            filterString=string;
        }
        return filterString;
    }
    
    public static String filterLanguage(String string){
        String filterString=null;
        if(string!=null&&string.length()!=0){
            //Take the index of @+language_code
            int index=string.lastIndexOf("@");
            //Checking whether string has language. 
            if(index>=0&&index+3==string.length()){//Length of language_code is 2
               filterString=string.substring(0, index);
            }else{
                filterString=string;
            }
        }else{
            filterString=string;//string is null or empty
        }
        return filterString;
    }
    
    public static String getLanguage(String string){
        String language=null;
        if(string==null)return null;
        int index=string.lastIndexOf("@");
        if(index>=0&&index==string.length()-3){
            language=string.substring(index+1);
        }
        return language;
    }
    
    public static String convertLabelToURI(String string){
        String returnString=string;
        if(returnString!=null)
            if(returnString.length()!=0){
                returnString=returnString.toLowerCase();
                returnString=returnString.replace(' ', '-');
                int i=0;
                while(i<returnString.length()&&returnString.charAt(i)!='('){
                    i++;
                }
                returnString=returnString.substring(0, i);
            }
        return returnString;
    }
    
    public static String convertVnToNonVn(String string){
        String returnString=string;
        if(returnString!=null)
            if(returnString.length()!=0){
                for(int i=0;i<returnString.length();i++){
                    char c=returnString.charAt(i);
                    returnString=returnString.replace(c, convertVnCharToNonVnChar(c));
                }
            }
        return returnString;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Convert Vn to Non Vn">
    private static char convertVnCharToNonVnChar(char c){
        if(charHashMap==null){
            charHashMap=new HashMap();
            putVnCharsToHashMap(charHashMap, a_CHARACTER);
            putVnCharsToHashMap(charHashMap, A_CHARACTER);
            putVnCharsToHashMap(charHashMap, d_CHARACTER);
            putVnCharsToHashMap(charHashMap, D_CHARACTER);
            putVnCharsToHashMap(charHashMap, e_CHARACTER);
            putVnCharsToHashMap(charHashMap, E_CHARACTER);
            putVnCharsToHashMap(charHashMap, i_CHARACTER);
            putVnCharsToHashMap(charHashMap, I_CHARACTER);
            putVnCharsToHashMap(charHashMap, o_CHARACTER);
            putVnCharsToHashMap(charHashMap, O_CHARACTER);
            putVnCharsToHashMap(charHashMap, u_CHARACTER);
            putVnCharsToHashMap(charHashMap, U_CHARACTER);
            putVnCharsToHashMap(charHashMap, y_CHARACTER);
            putVnCharsToHashMap(charHashMap, Y_CHARACTER);
        }
        Character returnChar=(Character)charHashMap.get(c);
        if(returnChar!=null)
            return returnChar;
        else
            return c;
    }
    
    private static void putVnCharsToHashMap(HashMap hashMap, char[] chars){
        if(hashMap==null||chars==null||chars.length<1)
            return;
        for(int i=0;i<chars.length;i++){
            hashMap.put(chars[i], chars[0]);
        }
    }
    //</editor-fold>
    
}
