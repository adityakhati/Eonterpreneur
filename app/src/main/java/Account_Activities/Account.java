package Account_Activities;

public class Account {


    String num;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Account(String num) {
        this.num = num;
    }

    String amt;

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public Account(String num, String amt) {
        this.num = num;
        this.amt = amt;
    }

    String date;
}
