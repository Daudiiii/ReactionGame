package tuke.daudi.reactiongame;


public class Player {
    private int id;
    private String psc;
    private String nick;
    private String age;
    private String gender;
    private String glasses;
    private Long score;
    private String edu;

    public Player() {
    }

    public Player(String nick, String age, String psc, String gender, String glasses,  String edu, Long score) {
        this.psc = psc;
        this.nick = nick;
        this.age = age;
        this.gender = gender;
        this.glasses = glasses;
        this.score = score;
        this.edu = edu;
    }



    public String getEdu() {
        return edu;
    }


    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPsc() {
        return psc;
    }

    public void setPsc(String psc) {
        this.psc = psc;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAge() {
        return age;
    }


    public String getGender() {
        return gender;
    }


    public String getGlasses() {
        return glasses;
    }

}
