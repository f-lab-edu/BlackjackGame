public class Card {

    String value;
    String type;

    Card(String value, String type){
        this.value = value;     // 숫자
        this.type = type;       // 종류
    }
    public  String toString(){
        return value + "-" + type;
    }
    public int getValue(){

        // J,Q,K는 10으로 계산, A는 11로 계산
        if("AJQK".contains(value)){
            if(value == "A"){
                return 11;
            }
            return 10;
        }
        return Integer.parseInt(value);
    }

    public boolean isAce() {
        return value == "A";
    }
    public String getImagePath(){
        return "./cards/" + toString() + ".png";
    }
}
